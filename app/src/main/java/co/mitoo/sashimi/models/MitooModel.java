package co.mitoo.sashimi.models;

import android.os.Handler;

import co.mitoo.sashimi.network.DataPersistanceService;
import co.mitoo.sashimi.network.ServiceBuilder;
import co.mitoo.sashimi.network.SteakApi;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.views.activities.MitooActivity;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by david on 14-11-12.
 */
public abstract class MitooModel
{

    protected MitooActivity activity;
    protected DataPersistanceService persistanceService;
    protected Handler handler;
    protected Runnable backgroundRunnable;
    protected Runnable getResultsRunnable;


    private SteakApi steakApiService;

    public MitooModel(MitooActivity activity) {
        setActivity(activity);
        setUpPersistanceService();
        BusProvider.register(this);
    }
    
    public SteakApi getSteakApiService() {
        return ServiceBuilder.getSingleTonInstance().getSteakApiService();
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


    public MitooActivity getActivity() {
        return activity;
    }

    public void setActivity(MitooActivity activity) {
        this.activity = activity;
    }

    public DataPersistanceService getPersistanceService() {
        return persistanceService;
    }

    public void setPersistanceService(DataPersistanceService persistanceService) {
        this.persistanceService = persistanceService;
    }
    
    private void setUpPersistanceService(){
        setPersistanceService(new DataPersistanceService(getActivity()));
        
    }

    protected <T> void handleObservable(Observable<T> observable, Class<T> classType) {
        observable.subscribe(createSubscriber(classType));
    }

    protected <T> Subscriber<T> createSubscriber(Class<T> objectRecieve) {
        return new Subscriber<T>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(T objectRecieve) {

                handleSubscriberResponse(objectRecieve);
            }
        };
    }

    protected void handleSubscriberResponse(Object objectRecieve) {

    }
    protected Runnable createGetResultsRunnable(){
        return new Runnable() {
            @Override
            public void run() {

                try {
                    obtainResults();
                }
                catch(Exception e){
                }
            }
        };
    }

    protected void obtainResults() {
    }
}
