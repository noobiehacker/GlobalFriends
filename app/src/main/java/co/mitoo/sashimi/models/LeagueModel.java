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
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.send.JsonLeagueEnquireSend;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.AlgoliaLeagueSearchEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquireRequestEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquiresResponseEvent;
import co.mitoo.sashimi.utils.events.AlgoliaResponseEvent;
import co.mitoo.sashimi.utils.events.LeagueQueryResponseEvent;
import co.mitoo.sashimi.utils.events.LeagueResultRequestEvent;
import co.mitoo.sashimi.utils.events.LeagueResultResponseEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.listener.AlgoliaIndexListener;
import co.mitoo.sashimi.views.activities.MitooActivity;
import retrofit.client.Response;
import android.os.Handler;
import org.apache.commons.lang.SerializationUtils;
/**
 * Created by david on 14-12-08.
 */
public class LeagueModel extends MitooModel{

    private APIClient algoliaClient;
    private Index index;
    private AlgoliaIndexListener aiListener;
    private List<League> leagueResults;
    private League[] leagueEnquired;
    private JSONObject results;
    private League selectedLeague;

    public LeagueModel(MitooActivity activity) {
        super(activity);
        setUpAlgolia();
    }

    private void setUpAlgolia(){

        algoliaClient = new APIClient(getActivity().getString(R.string.App_Id_algolia) , getActivity().getString(R.string.API_key_algolia)) ;
        index = algoliaClient.initIndex(getActivity().getString(R.string.algolia_staging_index));
        aiListener = new AlgoliaIndexListener();

    }

    @Subscribe
    public void requestAlgoLiaSearch(AlgoliaLeagueSearchEvent event){

        Query algoliaQuery = new Query(event.getQuery());
        if(event.getLatLng()!=null){
            LatLng center = event.getLatLng();
            algoliaQuery.aroundLatitudeLongitude((float)center.latitude, (float)center.longitude, 25);

        }
        index.searchASync(algoliaQuery, this.aiListener);
        
    }

    public void requestLeagueEnquire(LeagueModelEnquireRequestEvent event){

        if(event.getRequestType()== MitooEnum.crud.CREATE) {
            if (getSelectedLeague() != null) {
                JsonLeagueEnquireSend sendData = new JsonLeagueEnquireSend(event.getUserID(), getSelectedLeague().getFirstSports());
                handleObservable(getSteakApiService().createLeagueEnquiries(getSelectedLeague().getId(), sendData), Response.class);
            }
        } else if (event.getRequestType() == MitooEnum.crud.READ) {
            handleObservable(getSteakApiService().getLeagueEnquiries(getEnquriesConstant() ,event.getUserID()), League[].class);
        }
    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {

        if (objectRecieve instanceof League[]) {
            addLeagueEnquired((League[]) objectRecieve);
            setSelectedLeague(getFirstLeague());
            BusProvider.post(new LeagueModelEnquiresResponseEvent(getLeagueEnquired()));

        } else if (objectRecieve instanceof Response) {
            League[] enquired = new League[1];
            enquired[0] = getSelectedLeague();
            addLeagueEnquired(enquired);
            BusProvider.post(new LeagueModelEnquiresResponseEvent((Response)objectRecieve));
        }
    }
   

    @Subscribe
    public void algoLiaResponse(AlgoliaResponseEvent event){

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
                    LeagueModel.this.leagueResults = objectMapper.readValue(hits.toString(), new TypeReference<List<League>>(){});
                    BusProvider.post(new LeagueQueryResponseEvent(LeagueModel.this.leagueResults));
                }
                catch(Exception e){
                    BusProvider.post(new MitooActivitiesErrorEvent(MitooEnum.ErrorType.APP , e.toString()));
                }
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
        for(League item : this.leagueResults){
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

    public League[] getLeagueEnquired() {
        return leagueEnquired;
    }

    public void addLeagueEnquired(League[] leagueEnquired) {
        if(this.leagueEnquired==null){
            this.leagueEnquired = leagueEnquired;
        }
        else{
            //HORRABLE ARRAY, REFRACTOR LATER
            League[] newLeagueArray =  new League[this.leagueEnquired.length+leagueEnquired.length];
            for(int i = 0 ; i< leagueEnquired.length ; i++){
                newLeagueArray[i] = leagueEnquired[i];
            }
            for(int i = newLeagueArray.length-1 ; i>= newLeagueArray.length- this.leagueEnquired.length ; i--){
                newLeagueArray[i] = this.leagueEnquired[i];
            }
            this.leagueEnquired=newLeagueArray;
        }
            
    }

    private League getFirstLeague(){
        if(getLeagueEnquired() !=null && getLeagueEnquired().length>0)
            return getLeagueEnquired()[0];
        return null;
    }
    
    public boolean selectedLeagueIsJoinable(){
        
        //Case 1:User has not logged in thus no enquired leagues
        if(getLeagueEnquired() ==null)
            return true;
        else{
        //Case 2:User has logged in, return false if the current selected league
        //       is in our enquired leagues
            return !enquiredLeagueContains(getSelectedLeague());
        }
        
    }
    
    private boolean enquiredLeagueContains(League league){
        
        boolean containsLeague = false;
        if(league!=null){
            loop:
            for(League item : getLeagueEnquired()){
                if(item.equals(league))
                    containsLeague= true;
                if(containsLeague)
                    break loop;
            }
        }
        return containsLeague;
        
    }

    public List<League> getLeagueResults() {
        return leagueResults;
    }

    public void setLeagueResults(List<League> leagueResults) {
        this.leagueResults = leagueResults;
    }
}
