package co.mitoo.sashimi.network.Services;
import com.algolia.search.saas.APIClient;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.otto.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.send.JsonLeagueEnquireSend;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.AlgoliaLeagueSearchEvent;
import co.mitoo.sashimi.utils.events.LeagueRequestFromIDEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquireRequestEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquiresResponseEvent;
import co.mitoo.sashimi.utils.events.AlgoliaResponseEvent;
import co.mitoo.sashimi.utils.events.LeagueQueryResponseEvent;
import co.mitoo.sashimi.utils.events.LeagueResponseFromIDEvent;
import co.mitoo.sashimi.utils.events.LeaguesAlreadyEnquiredRequest;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.listener.AlgoliaIndexListener;
import co.mitoo.sashimi.views.activities.MitooActivity;
import retrofit.client.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import org.apache.commons.lang.SerializationUtils;
/**
 * Created by david on 14-12-08.
 */
public class LeagueService extends MitooService {

    private APIClient algoliaClient;
    private Index index;
    private AlgoliaIndexListener aiListener;

    private List<League> searchLeagueList;
    private List<League> enquiredLeagueList;

    private JSONObject results;
    private League selectedLeague;
    private boolean requestingAlgolia;

    public LeagueService(MitooActivity activity) {
        super(activity);
        setUpAlgolia();
    }

    private void setUpAlgolia(){

        this.algoliaClient = new APIClient(getActivity().getString(R.string.App_Id_algolia) , getActivity().getString(R.string.API_key_algolia)) ;
        setIndex(this.algoliaClient.initIndex(getActivity().getDataHelper().getAlgoliaIndex()));
        this.aiListener = new AlgoliaIndexListener();

    }

    @Subscribe
    public void requestLeagueFromID(LeagueRequestFromIDEvent event){

        League league = getLeagueByID(event.getLeagueID());
        if(league==null){
            Observable<League> observable = getSteakApiService().getLeagueFromLeagueID(event.getLeagueID());
            observable.subscribe(new Subscriber<League>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    BusProvider.post(new LeagueResponseFromIDEvent(e));
                }

                @Override
                public void onNext(League league) {
                    BusProvider.post(new LeagueResponseFromIDEvent(new LeagueModel(league, leagueIsJoinable(league))));
                }
            });
        }else{
            BusProvider.post(new LeagueResponseFromIDEvent(new LeagueModel(league, leagueIsJoinable(league))));
        }

    }

    @Subscribe
    public void requestAlgoLiaSearch(AlgoliaLeagueSearchEvent event){

        Query algoliaQuery = new Query(event.getQuery());
        if(event.getLatLng()!=null){
            LatLng center = event.getLatLng();
            algoliaQuery.aroundLatitudeLongitude((float)center.latitude, (float)center.longitude, MitooConstants.searchRadius);

        }
        setRequestingAlgolia(true);
        getIndex().searchASync(algoliaQuery, this.aiListener);
        
    }

    @Subscribe
    public void requestEnquiredLeagues(LeaguesAlreadyEnquiredRequest event) {

        if(this.enquiredLeagueList != null && !this.enquiredLeagueList.isEmpty())
            BusProvider.post(new LeagueModelEnquiresResponseEvent(this.enquiredLeagueList));
        else
            handleObservable(getSteakApiService().getLeagueEnquiries(getEnquriesConstant(), event.getUserID()), League[].class);
    }

    @Subscribe
    public void requestToEnquireLeague(LeagueModelEnquireRequestEvent event) {

        JsonLeagueEnquireSend sendData = new JsonLeagueEnquireSend(event.getUserID(), event.getLeagueModel().getLeague().getFirstSports());
        Observable<Response> obserable = getSteakApiService().createLeagueEnquiries(event.getLeagueModel().getLeague().getId(), sendData);
        final League leagueToPassIn = event.getLeagueModel().getLeague();
        obserable.subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response response) {
                if(LeagueService.this.enquiredLeagueList!=null){
                    LeagueService.this.enquiredLeagueList.add(leagueToPassIn);
                }
                BusProvider.post(new LeagueModelEnquiresResponseEvent(LeagueService.this.enquiredLeagueList));

            }
        });
    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {

        if (objectRecieve instanceof League[]) {
            clearLeaguesEnquired();
            addLeagueEnquired((League[]) objectRecieve);
            setSelectedLeague(getFirstLeague());
            BusProvider.post(new LeagueModelEnquiresResponseEvent(getLeaguesEnquired()));
        }
        
    }

    @Subscribe
    public void algoliaResponse(AlgoliaResponseEvent event){

        if(isRequestingAlgolia())
            parseLeagueResult(event.getResult());
    }

    
    private void parseLeagueResult(JSONObject results){
        
        setResults(results);
        this.backgroundRunnable =new Runnable() {
            @Override
            public void run() {

                try {
                    JSONArray hits = LeagueService.this.results.getJSONArray(getActivity().getString(R.string.algolia_result_param));
                    ObjectMapper objectMapper = new ObjectMapper();
                    LeagueService.this.searchLeagueList = objectMapper.readValue(hits.toString(), new TypeReference<List<League>>(){});
                    List<LeagueModel> leagueModelList = leagueModelTransform(LeagueService.this.searchLeagueList);
                    BusProvider.post(new LeagueQueryResponseEvent(leagueModelList));
                }
                catch(Exception e){
                    BusProvider.post(new MitooActivitiesErrorEvent(MitooEnum.ErrorType.APP , e.toString()));
                }
                
                setRequestingAlgolia(false);
            }
        };

        Thread t = new Thread(this.backgroundRunnable);
        t.start();
        
    }

    private List<LeagueModel> leagueModelTransform(List<League> leagues){
        List<LeagueModel> leagueModelsList = new ArrayList<LeagueModel>();
        for(League item : leagues){
            leagueModelsList.add(new LeagueModel(item ,leagueIsJoinable(item)));
        }
        return leagueModelsList;
    }

    private List<League> deepCopy(List<League> league){
        List<League> results = new ArrayList<League>();
        for(League item: league){
            results.add((League)SerializationUtils.clone(item));
        }
        return results;
    }
    
    public League getLeagueByID(int ObjectID){

        League result = null;
        forloop:
        for(League item : this.searchLeagueList){
            if(result!=null)
                break forloop;
            else if(item.getId() == ObjectID)
            {
                result = item;
            }
        }
        return result;

    }

    public League getSelectedLeague() {
        return selectedLeague;
    }

    public void setSelectedLeague(League selectedLeague) {
        this.selectedLeague = selectedLeague;
    }

    private String getEnquriesConstant(){
        return getActivity().getString(R.string.steak_api_param_filter_enquiries);
    }

    public List<League> getLeaguesEnquired() {
        if(this.enquiredLeagueList ==null)
            this.enquiredLeagueList = new ArrayList<League>();
        return this.enquiredLeagueList;
    }

    public void addLeagueEnquired(League[] newleaguesEnquired) {

        if (this.enquiredLeagueList == null) {
            this.enquiredLeagueList = new ArrayList<League>();
        }
        DataHelper helper = getActivity().getDataHelper();
        for (League item : newleaguesEnquired) {
            this.enquiredLeagueList.add(item);
        }
    }


    private League getFirstLeague(){
        if(getLeaguesEnquired() !=null && getLeaguesEnquired().size()>0)
            return getLeaguesEnquired().get(0);
        return null;
    }
    
    public boolean leagueIsJoinable(League league){

        if(league ==null)
            return true;
        else{
            return !enquiredLeagueContains(league);
        }

    }
    
    private boolean enquiredLeagueContains(League league){
        
        boolean containsLeague = false;
        if(league!=null && this.enquiredLeagueList!=null){
            loop:
            for(League item : this.enquiredLeagueList){
                if(item!=null && item.equals(league))
                    containsLeague= true;
                if(containsLeague)
                    break loop;
            }
        }
        return containsLeague;
        
    }

    public List<League> getLeagueSearchResults() {
        return this.searchLeagueList;
    }

    public boolean isRequestingAlgolia() {
        return requestingAlgolia;
    }

    public void setRequestingAlgolia(boolean requestingAlgolia) {
        this.requestingAlgolia = requestingAlgolia;
    }
    
    private void setEnquiredDateAsNow(League league){
        
        Date date = new Date();
        DataHelper helper = getActivity().getDataHelper();
        league.setCreated_at(helper.getDisplayableTimeString(date));
        
    }

    @Override
    public void resetFields(){
        this.searchLeagueList =null;
        this.selectedLeague = null;
        
    }

    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public void setEnquiredLeagues(List<League> enquiredLeagues) {
        this.searchLeagueList = enquiredLeagues;
    }

    private void clearLeaguesEnquired(){
        setEnquiredLeagues(new ArrayList<League>());
    }

    public void setResults(JSONObject results) {
        this.results = results;
    }

}