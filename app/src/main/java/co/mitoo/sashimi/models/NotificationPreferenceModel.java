package co.mitoo.sashimi.models;
import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.models.appObject.MitooNotification;
import co.mitoo.sashimi.models.jsonPojo.recieve.NotificationPreferenceRecieved;
import co.mitoo.sashimi.models.jsonPojo.recieve.notification.group_settings;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.NotificationModelEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import retrofit.RetrofitError;
import rx.Observable;

/**
 * Created by david on 15-04-07.
 */
public class NotificationPreferenceModel extends MitooModel{

    private NotificationPreferenceRecieved notificationPrefReceive;

    public NotificationPreferenceModel(MitooActivity activity) {
        super(activity);
    }

    public void requestNotificationPreference(){

        Observable<NotificationPreferenceRecieved> observable = getSteakApiService()
                .getNotificationPreference(getUserID(), getCompetitionID());
        handleObservable(observable , NotificationPreferenceRecieved.class);

    }

    public void requestNotificationUpdate(NotificationPreferenceRecieved prefReceive){

        Observable<NotificationPreferenceRecieved> observable = getSteakApiService()
                .updateNotificationPreference(getUserID(), getCompetitionID(), prefReceive);
        handleObservable(observable , NotificationPreferenceRecieved.class);

    }

    @Override
    public void resetFields() {
        setNotificationPrefReceive(null);
    }

    @Subscribe
    public void onApiFailEvent(RetrofitError event) {

        BusProvider.post(new MitooActivitiesErrorEvent(event));
    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve)  {

        if(objectRecieve instanceof NotificationPreferenceRecieved){
            setNotificationPrefReceive((NotificationPreferenceRecieved)objectRecieve);
            BusProvider.post(new NotificationModelEvent());
        }

    }

    public NotificationPreferenceRecieved getNotificationPrefReceive() {
        return notificationPrefReceive;
    }

    public void setNotificationPrefReceive(NotificationPreferenceRecieved notificationPrefReceive) {
        this.notificationPrefReceive = notificationPrefReceive;
    }

    public void requestNotificationPrefUpdate(boolean checked,
                                              MitooNotification mitooNotificationObject) {

        if(mitooNotificationObject.getNotificationType() == MitooEnum.NotificationType.EMAIL)
            requestEmailPrefUpdate(checked , mitooNotificationObject);
        else if(mitooNotificationObject.getNotificationType() == MitooEnum.NotificationType.PUSH)
            requestPushPrefUpdate(checked , mitooNotificationObject);

    }

    public void requestEmailPrefUpdate(boolean checked,
                                              MitooNotification mitooNotificationObject) {

        group_settings group_settings = getNotificationPrefReceive().getGroup_settings();

        switch (mitooNotificationObject.getNotificationCategory()) {
            case TeamGames:
                group_settings.getGroup_team_games().setEmail(checked);
                break;
            case TeamResults:
                group_settings.getGroup_team_results().setEmail(checked);
                break;
            case LeagueResults:
                group_settings.getGroup_league_results().setEmail(checked);
                break;
        }
        requestNotificationUpdate(getNotificationPrefReceive());
    }

    public void requestPushPrefUpdate(boolean checked,
                                       MitooNotification mitooNotificationObject) {

        group_settings group_settings = getNotificationPrefReceive().getGroup_settings();

        switch (mitooNotificationObject.getNotificationCategory()) {
            case TeamGames:
                group_settings.getGroup_team_games().setPush(checked);
                break;
            case TeamResults:
                group_settings.getGroup_team_results().setPush(checked);
                break;
            case LeagueResults:
                group_settings.getGroup_league_results().setPush(checked);
                break;
        }
        requestNotificationUpdate(getNotificationPrefReceive());
    }

    private int getUserID(){
        return getActivity().getModelManager().getSessionModel().getSession().id;

    }

    private int getCompetitionID(){
        return getActivity().getModelManager().getCompetitionModel().getSelectedCompetition().getId();

    }


}
