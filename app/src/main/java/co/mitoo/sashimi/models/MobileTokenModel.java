package co.mitoo.sashimi.models;
import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.models.jsonPojo.recieve.DeviceInfoRecieve;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.MobileTokenEventResponse;
import co.mitoo.sashimi.views.activities.MitooActivity;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by david on 15-03-29.
 */

public class MobileTokenModel  extends MitooModel{

    public MobileTokenModel(MitooActivity activity) {
        super(activity);
    }

    private DeviceInfoRecieve deviceInfoRecieve;

    public DeviceInfoRecieve getDeviceInfoRecieve() {
        return deviceInfoRecieve;
    }

    public void setDeviceInfoRecieve(DeviceInfoRecieve deviceInfoRecieve) {
        this.deviceInfoRecieve = deviceInfoRecieve;
    }

    public void requestDeviceTokenAssociation(int userID , boolean refresh) {

        if(getDeviceInfoRecieve() == null || refresh){
            handleObservable(getSteakApiService().createDeviceAssociation(userID) , DeviceInfoRecieve.class);
        }
        else{
            BusProvider.post(new MobileTokenEventResponse());
        }
    }

    public void requestDeviceTokenDisassociation(String token ) {

        handleObservable(getSteakApiService().deleteDeviceAssociation(token), Response.class);

    }

    @Subscribe
    public void onApiFailEvent(RetrofitError event) {

        BusProvider.post(new MitooActivitiesErrorEvent(event));
    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve)  {

        if(objectRecieve instanceof Response)
            handleDeleteAssociationResponse((Response) objectRecieve);
        else if (objectRecieve instanceof DeviceInfoRecieve)
            handleCreateAssociationResponse((DeviceInfoRecieve)deviceInfoRecieve);

    }

    private void handleDeleteAssociationResponse(Response response){
        int status = response.getStatus();
        if(status == 204){

        }
    }

    private void handleCreateAssociationResponse(DeviceInfoRecieve deviceInfoRecieve){
        setDeviceInfoRecieve(deviceInfoRecieve);

    }

}