package co.mitoo.sashimi.models;
import android.os.Handler;

import java.util.Stack;

import co.mitoo.sashimi.managers.ModelManager;
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

public abstract class MitooModel{

    protected MitooActivity activity;
    protected DataPersistanceService persistanceService;
    protected Handler handler;
    protected Runnable backgroundRunnable;
    private Stack<Object> events;


    private SteakApi steakApiService;

    public MitooModel(MitooActivity activity) {
        setActivity(activity);
        BusProvider.register(this);
    }
    
    public SteakApi getSteakApiService() {
        if(steakApiService == null)
            steakApiService = ServiceBuilder.getSingleTonInstance().getSteakApiService();
        return steakApiService;
    }

    public void setSteakApiService(SteakApi steakApiService) {
        this.steakApiService = steakApiService;
    }

    protected void removeReferences(){
        BusProvider.unregister(this);
    }

    public boolean isPersistanceStorage() {
        return MitooConstants.getPersistenceStorage();
    }

    public MitooActivity getActivity() {
        return activity;
    }

    public void setActivity(MitooActivity activity) {
        this.activity = activity;
    }

    public DataPersistanceService getPersistanceService() {
        if(persistanceService==null)
            persistanceService=getActivity().getPersistanceService();
        return persistanceService;
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

                String error = e.getMessage();

            }

            @Override
            public void onNext(T objectRecieve) {

                handleSubscriberResponse(objectRecieve);
            }
        };
    }

    protected void handleSubscriberResponse(Object objectRecieve) {
    }

    protected void obtainResults() {
    }

    public void resetFields() {}

    protected int getUserID(){
        ModelManager manager = this.activity.getModelManager();
        if(manager.getUserInfoModel().getUserInfoRecieve() != null)
            return manager.getUserInfoModel().getUserInfoRecieve().id;
        else if(manager.getSessionModel().getSession()!=null)
            return manager.getSessionModel().getSession().id;
        else
            return MitooConstants.invalidConstant;
    }

    protected Stack<Object> getEventsStack(){
        if(this.events == null)
            this.events = new Stack<Object>();
        return this.events;

    }
}
