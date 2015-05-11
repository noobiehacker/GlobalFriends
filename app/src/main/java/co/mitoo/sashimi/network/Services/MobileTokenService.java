package co.mitoo.sashimi.network.Services;
import com.squareup.otto.Subscribe;
import com.urbanairship.UAirship;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.recieve.JsonDeviceInfo;
import co.mitoo.sashimi.network.DataPersistanceService;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.IsPersistable;
import co.mitoo.sashimi.utils.events.LogOutNetworkCompleteEevent;
import co.mitoo.sashimi.utils.events.MobileTokenAssociateRequestEvent;
import co.mitoo.sashimi.utils.events.MobileTokenDisassociateRequestEvent;
import co.mitoo.sashimi.utils.events.MobileTokenEventResponse;
import co.mitoo.sashimi.views.activities.MitooActivity;
import retrofit.client.Response;
import rx.Observable;

/**
 * Created by david on 15-03-29.
 */

public class MobileTokenService extends MitooService implements IsPersistable {

    public MobileTokenService(MitooActivity activity) {
        super(activity);
    }

    private JsonDeviceInfo jsonDeviceInfo;

    public JsonDeviceInfo getJsonDeviceInfo() {
        return jsonDeviceInfo;
    }

    public boolean channelIDSent;
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

    @Subscribe
    public void requestDeviceTokenDisassociation(MobileTokenDisassociateRequestEvent event) {

        if(this.channelIDSent==true)
            handleObservable(getSteakApiService().deleteDeviceAssociation(getChannelID())
                , Response.class);
        else
            fireLogOutEvent();

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
        if(url!=null){
            String endPointSuffix = "/users/v1/mobile_devices/";
            return url.toLowerCase().contains((endPointSuffix));
        }else{
            return  false;

        }
    }
}