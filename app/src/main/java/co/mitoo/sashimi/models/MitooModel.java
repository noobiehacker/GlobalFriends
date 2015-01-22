package co.mitoo.sashimi.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.network.ServiceBuilder;
import co.mitoo.sashimi.network.SteakApi;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.StaticString;
import co.mitoo.sashimi.utils.events.LeagueQueryResponseEvent;

/**
 * Created by david on 14-11-12.
 */
public abstract class MitooModel
{

    protected Activity activity;
    protected Runnable serializeRunnable;
    protected Runnable getResultsRunnable;
    protected Handler handler;
    private String sharedPreferenceKey;

    public void setResources(Activity activity) {
        setActivity(activity);
    }

    private SteakApi steakApiService;

    public MitooModel(Activity activity) {
        setActivity(activity);
        BusProvider.register(this);
    }
    
    public SteakApi getSteakApiService() {
        if(steakApiService==null)
            steakApiService = new ServiceBuilder().setEndPoint(StaticString.steakStagingEndPoint)
                                                  .create(SteakApi.class);
        return steakApiService;
    }

    public void setSteakApiService(SteakApi steakApiService) {
        this.steakApiService = steakApiService;
    }

    protected void removeReferences(){
        BusProvider.unregister(this);
    }

    public boolean isPersistanceStorage() {
        return MitooConstants.persistenceStorage;
    }

    protected void obtainResults(){

    }

    protected Runnable createGetResultsRunnable(){
        return new Runnable() {
            @Override
            public void run() {

                try {
                    MitooModel.this.obtainResults();
                }
                catch(Exception e){
                }
            }
        };
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    protected String getSavedObjectData(String key ,String defaultValue){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultValue);
    }

    protected void saveStringToPreference(String key, String value){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    protected void deleteStringFromPreference(String key){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.commit();
    }

    protected void clearPreference() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().commit();
    }
    
    protected String getSharedPreferenceErrorValue(){
        return getActivity().getString(R.string.shared_preference_error);
    }

    public String getSharedPreferenceKey() {
        return sharedPreferenceKey;
    }

    public void setSharedPreferenceKey(String sharedPreferenceKey) {
        this.sharedPreferenceKey = sharedPreferenceKey;
    }
}
