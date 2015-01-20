package co.mitoo.sashimi.models;

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
    private Runnable parseRunnable;
    private Runnable getResultsRunnable;
    private Handler handler;
    private JSONObject results;
    
    public LeagueModel(Resources resources){
        super(resources);
        setUpAlgolia();
    }

    private void setUpAlgolia(){

        algoliaClient = new APIClient(getResources().getString(R.string.App_Id_algolia) , getResources().getString(R.string.API_key_algolia)) ;
        index = algoliaClient.initIndex(getResources().getString(R.string.algolia_index));
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
        this.parseRunnable =new Runnable() {
            @Override
            public void run() {

                try {
                    JSONArray hits = LeagueModel.this.results.getJSONArray(getResources().getString(R.string.algolia_result_param));
                    ObjectMapper objectMapper = new ObjectMapper();
                    LeagueModel.this.leagues = objectMapper.readValue(hits.toString(), new TypeReference<List<League>>(){});
                }
                catch(Exception e){
                }
            }
        };

        Thread t = new Thread(this.parseRunnable);
        t.start();
        
        this.getResultsRunnable = createGetResultsRunnable();
        this.handler.postDelayed(this.getResultsRunnable, 1000);

    }
    
    public void removeReferences(){
        super.removeReferences();
    }
    
    private void obtainResults(){
        
        if(this.leagues!=null){
            BusProvider.post(new LeagueQueryResponseEvent(this.leagues));
        }else
        {
            this.handler.postDelayed(this.getResultsRunnable,1000);
        }
        
    }
    
    private Runnable createGetResultsRunnable(){
        return new Runnable() {
            @Override
            public void run() {

                try {
                    LeagueModel.this.obtainResults();
                }
                catch(Exception e){
                }
            }
        };
    }
    
    private List<League> deepCopy(List<League> league){
        List<League> results = new ArrayList<League>();
        for(League item: league){
            results.add((League)SerializationUtils.clone(item));
        }
        return results;
    }
}
