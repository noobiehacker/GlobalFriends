package co.mitoo.sashimi.models;
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
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.send.JsonLeagueEnquireSend;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.AlgoliaLeagueSearchEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquireRequestEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquiresResponseEvent;
import co.mitoo.sashimi.utils.events.algoliaResponseEvent;
import co.mitoo.sashimi.utils.events.LeagueQueryResponseEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.listener.AlgoliaIndexListener;
import co.mitoo.sashimi.views.activities.MitooActivity;
import retrofit.client.Response;

import org.apache.commons.lang.SerializationUtils;
/**
 * Created by david on 14-12-08.
 */
public class LeagueModel extends MitooModel{

    private APIClient algoliaClient;
    private Index index;
    private AlgoliaIndexListener aiListener;
    private List<League> leagueSearchResults;
    private List<League> leagueEnquired;
    private JSONObject results;
    private League selectedLeague;
    private boolean requestingAlgolia;

    public LeagueModel(MitooActivity activity) {
        super(activity);
        setUpAlgolia();
    }

    private void setUpAlgolia(){

        algoliaClient = new APIClient(getActivity().getString(R.string.App_Id_algolia) , getActivity().getString(R.string.API_key_algolia)) ;
        index = algoliaClient.initIndex(getActivity().getString(R.string.algolia_staging_index));
        aiListener = new AlgoliaIndexListener();

    }

    public void requestAlgoLiaSearch(AlgoliaLeagueSearchEvent event){

        Query algoliaQuery = new Query(event.getQuery());
        if(event.getLatLng()!=null){
            LatLng center = event.getLatLng();
            algoliaQuery.aroundLatitudeLongitude((float)center.latitude, (float)center.longitude, MitooConstants.searchRadius);

        }
        setRequestingAlgolia(true);
        index.searchASync(algoliaQuery, this.aiListener);
        
    }

    public void requestEnquiredLeagues(LeagueModelEnquireRequestEvent event) {

        if(event.getApiRequestType()== MitooEnum.APIRequest.REQUEST && getLeaguesEnquired().size()!=0)
            BusProvider.post(new LeagueModelEnquiresResponseEvent(getLeaguesEnquired()));
        else
            handleObservable(getSteakApiService().getLeagueEnquiries(getEnquriesConstant(), event.getUserID()), League[].class);
    }

    public void requestToEnquireLeague(LeagueModelEnquireRequestEvent event) {

        if (getSelectedLeague() != null) {
            JsonLeagueEnquireSend sendData = new JsonLeagueEnquireSend(event.getUserID(), getSelectedLeague().getFirstSports());
            handleObservable(getSteakApiService().createLeagueEnquiries(getSelectedLeague().getId(), sendData), Response.class);
        }

    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {

        if (objectRecieve instanceof League[]) {
            addLeagueEnquired((League[]) objectRecieve);
            setSelectedLeague(getFirstLeague());
            BusProvider.post(new LeagueModelEnquiresResponseEvent(getLeaguesEnquired()));

        } else if (objectRecieve instanceof Response) {
            League[] enquired = new League[1];
            setEnquiredDateAsNow(getSelectedLeague());
            enquired[0] = getSelectedLeague();
            addLeagueEnquired(enquired);
            BusProvider.post(new LeagueModelEnquiresResponseEvent((Response)objectRecieve));
        }
    }
   

    @Subscribe
    public void algoliaResponse(algoliaResponseEvent event){

        if(isRequestingAlgolia())
            parseLeagueResult(event.getResult());
    }

    
    private void parseLeagueResult(JSONObject results){
        
        this.results=results;
        this.backgroundRunnable =new Runnable() {
            @Override
            public void run() {

                try {
                    JSONArray hits = LeagueModel.this.results.getJSONArray(getActivity().getString(R.string.algolia_result_param));
                    ObjectMapper objectMapper = new ObjectMapper();
                    LeagueModel.this.leagueSearchResults = objectMapper.readValue(hits.toString(), new TypeReference<List<League>>(){});
                    BusProvider.post(new LeagueQueryResponseEvent(LeagueModel.this.leagueSearchResults));
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
        for(League item : this.leagueSearchResults){
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
        return getActivity().getString(R.string.steak_api_const_filter_enquiries);
    }

    public List<League> getLeaguesEnquired() {
        if(leagueEnquired==null)
            leagueEnquired = new ArrayList<League>();
        return leagueEnquired;
    }

    public void addLeagueEnquired(League[] newleaguesEnquired) {

        if (this.leagueEnquired == null) {
            this.leagueEnquired = new ArrayList<League>();
        }
        for (League item : newleaguesEnquired) {
            this.leagueEnquired.add(item);
        }
    }

    private League[] createCombinedEnquiredArray(League[] oldArray, League[] inputArray){
        
        League[] combinedLeagueArray =  new League[oldArray.length+inputArray.length];
        for(int i = 0 ; i< combinedLeagueArray.length ; i++){
            if(i<oldArray.length)
                combinedLeagueArray[i] = oldArray[i];
            else
                combinedLeagueArray[i] = inputArray[i-oldArray.length];
        }
        return combinedLeagueArray;
        
    }

    private League getFirstLeague(){
        if(getLeaguesEnquired() !=null && getLeaguesEnquired().size()>0)
            return getLeaguesEnquired().get(0);
        return null;
    }
    
    public boolean selectedLeagueIsJoinable(){
        
        return leagueIsJoinable(getSelectedLeague());
        
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
        if(league!=null){
            loop:
            for(League item : getLeaguesEnquired()){
                if(item.equals(league))
                    containsLeague= true;
                if(containsLeague)
                    break loop;
            }
        }
        return containsLeague;
        
    }

    public List<League> getLeagueSearchResults() {
        return leagueSearchResults;
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
        league.setCreated_at(helper.getDateString(date));
        
    }
    
    public void resetFields(){
        this.leagueEnquired=null;
        this.leagueSearchResults = null;
        this.selectedLeague = null;
        
    }

}

