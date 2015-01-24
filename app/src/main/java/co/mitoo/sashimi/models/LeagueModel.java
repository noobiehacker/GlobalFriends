package co.mitoo.sashimi.models;
import android.app.Activity;
import com.algolia.search.saas.APIClient;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import co.mitoo.sashimi.utils.events.LeagueModelEnquireRequestEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquiresResponseEvent;
import co.mitoo.sashimi.utils.events.LeagueQueryRequestEvent;
import co.mitoo.sashimi.utils.events.AlgoliaResponseEvent;
import co.mitoo.sashimi.utils.events.LeagueQueryResponseEvent;
import co.mitoo.sashimi.utils.events.LeagueResultRequestEvent;
import co.mitoo.sashimi.utils.events.LeagueResultResponseEvent;
import co.mitoo.sashimi.utils.listener.AlgoliaIndexListener;
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
    private Handler handler;
    private Runnable serializeRunnable;
    private Runnable getResultsRunnable;

    public LeagueModel(Activity activity) {
        super(activity);
        setUpAlgolia();
    }

    private void setUpAlgolia(){

        algoliaClient = new APIClient(getActivity().getString(R.string.App_Id_algolia) , getActivity().getString(R.string.API_key_algolia)) ;
        index = algoliaClient.initIndex(getActivity().getString(R.string.algolia_index));
        aiListener = new AlgoliaIndexListener();

    }

    @Subscribe
    public void algoLiaSearchRequest(LeagueQueryRequestEvent event){

        algoliaSearch(event.getQuery());
        
    }

    @Subscribe
    public void onLeagueEnquireRequest(LeagueModelEnquireRequestEvent event){

        if(event.getRequestType()== MitooEnum.crud.CREATE) {
            if (getSelectedLeague() != null) {
                JsonLeagueEnquireSend sendData = new JsonLeagueEnquireSend(event.getUserID(), getSelectedLeague().getSports()[0]);
                handleObservable(getSteakApiService().createLeagueEnquiries(event.getUserID(), sendData), Response.class);
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
    
    private void algoliaSearch(String query){
        index.searchASync(new Query(query), this.aiListener);
    }

    @Subscribe
    public void algoLiaResponse(AlgoliaResponseEvent event){

        parseLeagueResult(event.getResult());
    }
    
    @Subscribe
    public void onLeagueResultRequest(LeagueResultRequestEvent event){
        
        if(this.leagueResults !=null)
            BusProvider.post(new LeagueResultResponseEvent(this.leagueResults));
        
    }
    
    private void parseLeagueResult(JSONObject results){
        
        this.results=results;
        this.handler= new Handler();
        this.serializeRunnable =new Runnable() {
            @Override
            public void run() {

                try {
                    JSONArray hits = LeagueModel.this.results.getJSONArray(getActivity().getString(R.string.algolia_result_param));
                    ObjectMapper objectMapper = new ObjectMapper();
                    LeagueModel.this.leagueResults = objectMapper.readValue(hits.toString(), new TypeReference<List<League>>(){});
                }
                catch(Exception e){
                }
            }
        };

        Thread t = new Thread(this.serializeRunnable);
        t.start();
        
        this.getResultsRunnable = createGetResultsRunnable();
        this.handler.postDelayed(this.getResultsRunnable, 1000);

    }
    
    public void removeReferences(){
        super.removeReferences();
    }
    
    @Override
    protected void obtainResults(){
        
        if(this.leagueResults !=null){
            BusProvider.post(new LeagueQueryResponseEvent(this.leagueResults));
        }else
        {
            this.handler.postDelayed(this.getResultsRunnable,1000);
        }
        
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
        
        if(getLeagueEnquired() ==null)
            return true;
        else{
            //can only join we our enquired leagues not have this league
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
}
