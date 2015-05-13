package co.mitoo.sashimi.managers;
import java.util.ArrayList;
import java.util.List;

import co.mitoo.sashimi.network.Services.AppSettingsService;
import co.mitoo.sashimi.network.Services.CompetitionService;
import co.mitoo.sashimi.network.Services.ConfirmInfoService;
import co.mitoo.sashimi.network.Services.FixtureService;
import co.mitoo.sashimi.network.Services.LeagueService;
import co.mitoo.sashimi.network.Services.LocationService;
import co.mitoo.sashimi.network.Services.MitooService;
import co.mitoo.sashimi.network.Services.MobileTokenService;
import co.mitoo.sashimi.network.Services.NotificationPreferenceService;
import co.mitoo.sashimi.network.Services.SessionService;
import co.mitoo.sashimi.network.Services.TeamService;
import co.mitoo.sashimi.network.Services.UserInfoService;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.IsPersistable;
import co.mitoo.sashimi.utils.events.ModelPersistedDataDeletedEvent;
import co.mitoo.sashimi.utils.events.ModelPersistedDataLoadedEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;

/**
 * Created by david on 15-01-21.
 */
public class ModelManager {

    private MitooActivity activity;
    private List<MitooService> mitooServiceList;
    private List<IsPersistable> persistableList;
    protected Runnable currentRunnable;

    public ModelManager(MitooActivity activity) {
        setActivity(activity);
        initializeServices();
    }

    private void initializeServices(){
        setMitooModelList(new ArrayList<MitooService>());
        setPersistableList(new ArrayList<IsPersistable>());
        inializeOnStartModels();
    }

    //USED FOR LOGING OUT
    public void clearAllUserServices(){
        for(MitooService service : this.mitooServiceList){
            BusProvider.unregister(service);
        }
        initializeServices();
    }

    public MitooActivity getMitooActivity() {
        return activity;
    }

    public void setActivity(MitooActivity activity) {
        this.activity = activity;
    }

    public LeagueService getLeagueModel() {

        return (LeagueService) getModel(LeagueService.class);

    }

    public SessionService getSessionModel() {

        return (SessionService) getModel(SessionService.class);

    }

    public UserInfoService getUserInfoModel() {

        return (UserInfoService) getModel(UserInfoService.class);

    }

    public LocationService getLocationModel() {

        return (LocationService) getModel(LocationService.class);

    }

    public AppSettingsService getAppSettingsModel() {

        return (AppSettingsService) getModel(AppSettingsService.class);
    }

    public CompetitionService getCompetitionModel() {

        return (CompetitionService) getModel(CompetitionService.class);
    }

    public TeamService getTeamModel() {

        return (TeamService) getModel(TeamService.class);
    }

    public FixtureService getFixtureModel() {

        return (FixtureService)getModel(FixtureService.class);
    }

    public ConfirmInfoService getConfirmInfoModel() {

        return (ConfirmInfoService)getModel(ConfirmInfoService.class);
    }

    public NotificationPreferenceService getNotificationPreferenceModel() {

        return (NotificationPreferenceService)getModel(NotificationPreferenceService.class);
    }

    private <T> MitooService getModel(Class<T> classType) {

        T classModel = null;
        Object model = getModelFromList(classType);
        if (model != null) {
            classModel = (T) model;
        } else {

            try {
                model = classType.getConstructor(MitooActivity.class)
                        .newInstance(getMitooActivity());
            } catch (Exception e) {

                String tremp = e.toString();

            }
            addModel((MitooService) model);
        }
        return (MitooService) model;
    }

    public MobileTokenService getMobileTokenModel() {

        MobileTokenService mobileTokenModel = null;
        MitooService model = getModel(MobileTokenService.class);
        if (model != null) {
            mobileTokenModel  = (MobileTokenService) model;
        } else {
            mobileTokenModel  = new MobileTokenService(getMitooActivity());
            addModel(mobileTokenModel );
        }
        return mobileTokenModel;
    }

    public List<IsPersistable> getPersistableList() {
        return persistableList;
    }

    public void setPersistableList(List<IsPersistable> persistableList) {
        this.persistableList = persistableList;
    }

    public List<MitooService> getMitooModelList() {
        return mitooServiceList;
    }

    public void setMitooModelList(List<MitooService> mitooServiceList) {
        this.mitooServiceList = mitooServiceList;
    }

    public void addModel(MitooService modelToAdd) {

        getMitooModelList().add(modelToAdd);

        if (modelToAdd instanceof IsPersistable) {
            addModelToPersistableList((IsPersistable) modelToAdd);

        }
    }

    public MitooService getModelFromList(Class<?> modelClass) {

        MitooService result = null;
        forloop:
        for(MitooService item : this.mitooServiceList){
            if(modelClass.isInstance(item)){
                result = item;
            }
            if(result!=null)
                break forloop;
        }
        return result;
    }
    
    private void addModelToPersistableList(IsPersistable persistable){

        getPersistableList().add(persistable);

    }
    private boolean containsModelType(Class<?> modelClass){

        boolean result = false;
        forloop:
        for(MitooService item : this.mitooServiceList){
            if(modelClass.isInstance(item)){
                result = true;
            }
            if(result)
                break forloop;
        }
        return result;

    }
    
    private void inializeOnStartModels(){
        
        getSessionModel();
        getUserInfoModel();
        getLeagueModel();
        getAppSettingsModel();
        getLocationModel();
        getMobileTokenModel();
    }

    public void readAllPersistedData(){

        Runnable runnable =new Runnable() {
            @Override
            public void run() {

                try {
                    for(IsPersistable item  : getPersistableList()){
                        item.readData();
                    }
                    BusProvider.post(new ModelPersistedDataLoadedEvent());
                }
                catch(Exception e){
                    String temp = e.toString();
                }
            }
        };
        runRunnableInBackground(runnable);

    }

    public void deleteAllPersistedData(){

        Runnable runnable =new Runnable() {
            @Override
            public void run() {

                try {
                    for(IsPersistable item  : getPersistableList()){
                        item.deleteData();
                    }
                    removeModelReferences();
                    BusProvider.post(new ModelPersistedDataDeletedEvent());
                }
                catch(Exception e){
                }
            }
        };

        runRunnableInBackground(runnable);
    }
    
    private void runRunnableInBackground(Runnable runnable){
        
        this.currentRunnable= runnable;
        Thread thread = new Thread(this.currentRunnable);
        thread.start();

    }
    
    private void removeModelReferences(){

        DataHelper dataHelper = new DataHelper(getMitooActivity());

        for(MitooService item  : getMitooModelList()){
            item.resetFields();
        }
        System.gc();
    }
}
