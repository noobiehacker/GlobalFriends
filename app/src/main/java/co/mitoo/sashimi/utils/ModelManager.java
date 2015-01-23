package co.mitoo.sashimi.utils;
import java.util.ArrayList;
import java.util.List;

import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.MitooModel;
import co.mitoo.sashimi.models.SessionModel;
import co.mitoo.sashimi.models.UserInfoModel;
import co.mitoo.sashimi.views.activities.MitooActivity;

/**
 * Created by david on 15-01-21.
 */
public class ModelManager {

    private MitooActivity activity;
    private List<MitooModel> mitooModelList;
    private List<IsPersistable> persistableList;

    public ModelManager(MitooActivity activity) {
        setActivity(activity);
        setMitooModelList(new ArrayList<MitooModel>());
        setPersistableList(new ArrayList<IsPersistable>());
        inializeOnStartModels();
    }

    public MitooActivity getMitooActivity() {
        return activity;
    }

    public void setActivity(MitooActivity activity) {
        this.activity = activity;
    }

    public LeagueModel getLeagueModel() {

        LeagueModel leagueModel = null;
        MitooModel model = getModel(LeagueModel.class);
        if (model != null) {
            leagueModel = (LeagueModel) model;
        } else{
            leagueModel = new LeagueModel(getMitooActivity());
            addModel(leagueModel);
        }
        return leagueModel;
    }

    public SessionModel getSessionModel() {

        SessionModel sessionModel = null;
        MitooModel model = getModel(SessionModel.class);
        if (model != null) {
            sessionModel = (SessionModel) model;
        } else {
            sessionModel = new SessionModel(getMitooActivity());
            addModel(sessionModel);
        }
        return sessionModel;
    }

    public UserInfoModel getUserInfoModel() {

        UserInfoModel userInfoModel = null;
        MitooModel model = getModel(UserInfoModel.class);
        if (model != null) {
            userInfoModel = (UserInfoModel) model;
        } else {
            userInfoModel = new UserInfoModel(getMitooActivity());
            addModel(userInfoModel);
        }
        return userInfoModel;
    }

    public List<IsPersistable> getPersistableList() {
        return persistableList;
    }

    public void setPersistableList(List<IsPersistable> persistableList) {
        this.persistableList = persistableList;
    }

    public List<MitooModel> getMitooModelList() {
        return mitooModelList;
    }

    public void setMitooModelList(List<MitooModel> mitooModelList) {
        this.mitooModelList = mitooModelList;
    }

    public void addModel(MitooModel modelToAdd) {

        getMitooModelList().add(modelToAdd);

        if (modelToAdd instanceof IsPersistable) {
            addModelToPersistableList((IsPersistable) modelToAdd);

        }
    }

    public MitooModel getModel(Class<?> modelClass) {

        MitooModel result = null;
        forloop:
        for(MitooModel item : this.mitooModelList){
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
        for(MitooModel item : this.mitooModelList){
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
        
    }

    public void readAllPersistedData(){

        for(IsPersistable item  : getPersistableList()){
            item.readData();
        }
    }

    public void deleteAllPersistedData(){

        for(IsPersistable item  : getPersistableList()){
            item.deleteData();
        }
    }
}
