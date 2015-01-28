package co.mitoo.sashimi.network;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.SessionPersistanceResponseEvent;

/**
 * Created by david on 15-01-22.
 */
public class DataPersistanceService {

    protected Runnable serializeRunnable;
    private Activity activity;

    public DataPersistanceService(Activity activity){
        setActivity(activity);
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

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void readFromPreference(String key , final Class<?> classType) {

        String savedUserSerialized = getSavedObjectData(key, getSharedPreferenceErrorValue());
        BusProvider.post(new SessionPersistanceResponseEvent(deserializeObject(savedUserSerialized, classType)));

    }

    public void saveToPreference(String key, Object session){

        final Object objectToPassIn= session;
        final String keyToPassIn  = key;
        this.serializeRunnable =new Runnable() {
            @Override
            public void run() {

                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String serializedValue = objectMapper.writeValueAsString(objectToPassIn);
                    saveStringToPreference(keyToPassIn , serializedValue);
                }
                catch(Exception e){
                }
            }
        };

        runRunnableOnNewThread(this.serializeRunnable);

    }

    public void deleteFromPreference(String key){

        final String keyToPassIn  = key;
        this.serializeRunnable = new Runnable() {
            @Override
            public void run() {

                try {
                    deleteStringFromPreference(keyToPassIn);
                }
                catch(Exception e){
                }
            }
        };

        runRunnableOnNewThread(this.serializeRunnable);

    }

    private void runRunnableOnNewThread(Runnable runnable){

        Thread t = new Thread(runnable);
        t.start();

    }
    
    private SessionRecieve deserializeObject(String savedUserSerialized  , Class<?> classType) {
        SessionRecieve deserializedObject = null;
        if (classType == SessionRecieve.class) {

            try {
                if (savedUserSerialized != getSharedPreferenceErrorValue()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    deserializedObject = objectMapper.readValue(savedUserSerialized, SessionRecieve.class);
                }
            } catch (Exception e) {

            }
        }
        return deserializedObject;

    }

}
