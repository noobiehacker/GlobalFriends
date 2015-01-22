package co.mitoo.sashimi.models;

import android.app.Activity;
import android.content.res.Resources;

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
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.LeagueQueryRequestEvent;
import co.mitoo.sashimi.utils.events.AlgoliaResponseEvent;
import co.mitoo.sashimi.utils.events.LeagueQueryResponseEvent;
import co.mitoo.sashimi.utils.events.LeagueResultRequestEvent;
import co.mitoo.sashimi.utils.events.LeagueResultResponseEvent;
import co.mitoo.sashimi.utils.listener.AlgoliaIndexListener;

import android.os.Handler;
import org.apache.commons.lang.SerializationUtils;
/**
 * Created by david on 14-12-08.
 */
public class LeagueModel extends MitooModel {

    private APIClient algoliaClient;
    private Index index;
    private AlgoliaIndexListener aiListener;
    private List<League> leagues;
    private JSONObject results;
    private League selectedLeague;

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

    private void algoliaSearch(String query){
        index.searchASync(new Query(query), this.aiListener);
    }

    @Subscribe
    public void algoLiaResponse(AlgoliaResponseEvent event){

        parseLeagueResult(event.getResult());
    }
    
    @Subscribe
    public void onLeagueResultRequest(LeagueResultRequestEvent event){
        
        if(this.leagues!=null)
            BusProvider.post(new LeagueResultResponseEvent(this.leagues));
        
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
                    LeagueModel.this.leagues = objectMapper.readValue(hits.toString(), new TypeReference<List<League>>(){});
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
        
        if(this.leagues!=null){
            BusProvider.post(new LeagueQueryResponseEvent(this.leagues));
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
    
    public League getLeagueByObjectID(int ObjectID){

        League result = null;
        forloop:
        for(League item : this.leagues){
            if(result!=null)
                break forloop;
            else if(item.getObjectID() == ObjectID)
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
}
