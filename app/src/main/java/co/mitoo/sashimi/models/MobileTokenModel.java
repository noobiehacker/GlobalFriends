package co.mitoo.sashimi.models;
import com.squareup.otto.Subscribe;
import com.urbanairship.UAirship;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.recieve.JsonDeviceInfo;
import co.mitoo.sashimi.network.DataPersistanceService;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.IsPersistable;
import co.mitoo.sashimi.utils.events.LogOutNetworkCompleteEevent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.MobileTokenAssociateRequestEvent;
import co.mitoo.sashimi.utils.events.MobileTokenEventResponse;
import co.mitoo.sashimi.views.activities.MitooActivity;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;

/**
 * Created by david on 15-03-29.
 */

public class MobileTokenModel  extends MitooModel  implements IsPersistable {

    public MobileTokenModel(MitooActivity activity) {
        super(activity);
    }

    private JsonDeviceInfo jsonDeviceInfo;

    public JsonDeviceInfo getJsonDeviceInfo() {
        return jsonDeviceInfo;
    }

    public Boolean channelIDSent;
    public boolean fireLogOutEvent=false;

    public void setJsonDeviceInfo(JsonDeviceInfo jsonDeviceInfo) {
        this.jsonDeviceInfo = jsonDeviceInfo;
    }

    @Subscribe
    public void onRequestDeviceTokenAssociation(MobileTokenAssociateRequestEvent event) {

        if(getJsonDeviceInfo() == null ){
            Observable<Response> observable = getSteakApiService()
                    .createDeviceAssociation(event.getUserID(), createDeviceInfo(getChannelID()));
            handleObservable(observable , Response.class);
        }
        else{
            BusProvider.post(new MobileTokenEventResponse());
        }
    }

    public void requestDeviceTokenDisassociation() {

        if(this.channelIDSent==true)
            handleObservable(getSteakApiService().deleteDeviceAssociation(getChannelID())
                , Response.class);
        else
            fireLogOutEvent();

    }

    @Subscribe
    public void onApiFailEvent(RetrofitError event) {

        boolean urlIsTokenEndPoint = urlIsTokenEndPoint(event.getUrl());
        if(event.getResponse() !=null && event.getResponse().getStatus() == 404){
            if(urlIsTokenEndPoint)
                fireLogOutEvent();
        }
        else
            BusProvider.post(new MitooActivitiesErrorEvent(event));
    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve)  {

        if(objectRecieve instanceof Response){
            int status = ((Response)objectRecieve).getStatus();
            if(status == 201){
                setChannelIDSent(true);
                BusProvider.post(new MobileTokenEventResponse());
            }else if (status ==204){
                fireLogOutEvent();
            }
        }

    }

    private JsonDeviceInfo createDeviceInfo(String token){
        DataHelper dataHelper = getActivity().getDataHelper();
        JsonDeviceInfo deviceInfo = new JsonDeviceInfo();
        deviceInfo.app_version = dataHelper.getAppVersion();
        deviceInfo.os_version = dataHelper.getOSVersion();
        deviceInfo.model = dataHelper.getDeviceName();
        deviceInfo.platform = dataHelper.getPlatformName();
        deviceInfo.token = token;
        return deviceInfo;
    }

    private String getChannelID(){
        return UAirship.shared().getPushManager().getChannelId();
    }

    public boolean isChannelIDSent() {
        return channelIDSent;
    }

    public void setChannelIDSent(boolean channelIDSent) {
        this.channelIDSent = channelIDSent;
        saveData();
    }


    public void fireLogOutEvent(){
        BusProvider.post(new LogOutNetworkCompleteEevent());
/*
        if(getFireLogOutEvent())
        setFireLogOutEvent(true); */

    }
    @Override
    public void readData() {

        DataPersistanceService service = getPersistanceService();
        Object value = service.readFromPreference(getPreferenceKey(), Boolean.class);
        if(value!=null)
            setChannelIDSent((Boolean)value);

    }

    @Override
    public void saveData() {

        getPersistanceService().saveToPreference(getPreferenceKey() ,  isChannelIDSent());

    }

    @Override
    public void deleteData() {

        getPersistanceService().deleteFromPreference(getPreferenceKey());
        setChannelIDSent(false);

    }

    @Override
    public String getPreferenceKey() {
        return getActivity().getString(R.string.shared_preference_mobile_token_key);
    }

    @Override
    public void resetFields() {

        setChannelIDSent(false);
        setJsonDeviceInfo(null);
    }

    public Boolean getFireLogOutEvent() {
        return fireLogOutEvent;
    }

    public void setFireLogOutEvent(Boolean fireLogOutEvent) {
        this.fireLogOutEvent = fireLogOutEvent;
    }

    private boolean urlIsTokenEndPoint(String url){
        String endPointSuffix = "/users/v1/mobile_devices/";
        return url.toLowerCase().contains((endPointSuffix));
    }
}