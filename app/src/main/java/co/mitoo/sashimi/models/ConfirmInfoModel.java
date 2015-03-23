package co.mitoo.sashimi.models;
import co.mitoo.sashimi.models.jsonPojo.ConfirmInfo;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.ConfirmInfoModelResponseEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;

/**
 * Created by david on 15-03-20.
 */
public class ConfirmInfoModel extends MitooModel{

    private ConfirmInfo confirmInfo;

    public ConfirmInfoModel(MitooActivity activity) {
        super(activity);
    }

    public void requestConfirmationInformation(String token){

        if(getConfirmInfo()==null){
            handleObservable(getSteakApiService().getConfirmationInfo(token), ConfirmInfo.class) ;
        }
        else{
            BusProvider.post(new ConfirmInfoModelResponseEvent());
        }

    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {

        if (objectRecieve instanceof ConfirmInfo) {
            BusProvider.post(new ConfirmInfoModelResponseEvent());
        }
    }

    @Override
    protected void resetFields() {
        setConfirmInfo(null);
    }

    public ConfirmInfo getConfirmInfo() {
        return confirmInfo;
    }

    public void setConfirmInfo(ConfirmInfo confirmInfo) {
        this.confirmInfo = confirmInfo;
    }
}