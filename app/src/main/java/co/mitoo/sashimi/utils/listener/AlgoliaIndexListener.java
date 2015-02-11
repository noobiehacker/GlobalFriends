package co.mitoo.sashimi.utils.listener;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.IndexListener;
import com.algolia.search.saas.Query;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.algoliaResponseEvent;

/**
 * Created by david on 15-01-15.
 */
public class AlgoliaIndexListener implements IndexListener {
    
    public AlgoliaIndexListener(){
    }

    @Override
    public void searchResult(Index index, Query query, JSONObject jsonObject) {
        if(jsonObject!=null) {
            BusProvider.post(new algoliaResponseEvent(jsonObject));
        }
    }
    
    @Override
    public void addObjectResult(Index index, JSONObject jsonObject, JSONObject jsonObject2) {
        
    }

    @Override
    public void addObjectError(Index index, JSONObject jsonObject, AlgoliaException e) {

    }

    @Override
    public void addObjectsResult(Index index, List<JSONObject> jsonObjects, JSONObject jsonObject) {

    }

    @Override
    public void addObjectsError(Index index, List<JSONObject> jsonObjects, AlgoliaException e) {

    }

    @Override
    public void addObjectsResult(Index index, JSONArray jsonArray, JSONObject jsonObject) {

    }

    @Override
    public void addObjectsError(Index index, JSONArray jsonArray, AlgoliaException e) {

    }

    @Override
    public void searchError(Index index, Query query, AlgoliaException e) {

    }

    @Override
    public void deleteObjectResult(Index index, String s, JSONObject jsonObject) {

    }

    @Override
    public void deleteObjectError(Index index, String s, AlgoliaException e) {

    }

    @Override
    public void deleteObjectsResult(Index index, JSONArray jsonArray, JSONObject jsonObject) {

    }

    @Override
    public void deleteByQueryError(Index index, Query query, AlgoliaException e) {

    }

    @Override
    public void deleteByQueryResult(Index index) {

    }

    @Override
    public void deleteObjectsError(Index index, List<JSONObject> jsonObjects, AlgoliaException e) {

    }

    @Override
    public void saveObjectResult(Index index, JSONObject jsonObject, String s, JSONObject jsonObject2) {

    }

    @Override
    public void saveObjectError(Index index, JSONObject jsonObject, String s, AlgoliaException e) {

    }

    @Override
    public void saveObjectsResult(Index index, List<JSONObject> jsonObjects, JSONObject jsonObject) {

    }

    @Override
    public void saveObjectsError(Index index, List<JSONObject> jsonObjects, AlgoliaException e) {

    }

    @Override
    public void saveObjectsResult(Index index, JSONArray jsonArray, JSONObject jsonObject) {

    }

    @Override
    public void saveObjectsError(Index index, JSONArray jsonArray, AlgoliaException e) {

    }

    @Override
    public void partialUpdateResult(Index index, JSONObject jsonObject, String s, JSONObject jsonObject2) {

    }

    @Override
    public void partialUpdateError(Index index, JSONObject jsonObject, String s, AlgoliaException e) {

    }

    @Override
    public void partialUpdateObjectsResult(Index index, List<JSONObject> jsonObjects, JSONObject jsonObject) {

    }

    @Override
    public void partialUpdateObjectsError(Index index, List<JSONObject> jsonObjects, AlgoliaException e) {

    }

    @Override
    public void partialUpdateObjectsResult(Index index, JSONArray jsonArray, JSONObject jsonObject) {

    }

    @Override
    public void partialUpdateObjectsError(Index index, JSONArray jsonArray, AlgoliaException e) {

    }

    @Override
    public void getObjectResult(Index index, String s, JSONObject jsonObject) {

    }

    @Override
    public void getObjectError(Index index, String s, AlgoliaException e) {

    }

    @Override
    public void getObjectsResult(Index index, List<String> strings, JSONObject jsonObject) {

    }

    @Override
    public void getObjectsError(Index index, List<String> strings, AlgoliaException e) {

    }

    @Override
    public void waitTaskResult(Index index, String s) {

    }

    @Override
    public void waitTaskError(Index index, String s, AlgoliaException e) {

    }

    @Override
    public void getSettingsResult(Index index, JSONObject jsonObject) {

    }

    @Override
    public void getSettingsError(Index index, AlgoliaException e) {

    }

    @Override
    public void setSettingsResult(Index index, JSONObject jsonObject, JSONObject jsonObject2) {

    }

    @Override
    public void setSettingsError(Index index, JSONObject jsonObject, AlgoliaException e) {

    }
}
