package co.mitoo.sashimi.network.Services;
import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.models.appObject.MitooNotification;
import co.mitoo.sashimi.models.jsonPojo.recieve.NotificationPreferenceRecieved;
import co.mitoo.sashimi.models.jsonPojo.recieve.notification.GroupSettings;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.NotificationModelResponseEvent;
import co.mitoo.sashimi.utils.events.NotificationModelUpdateResponse;
import co.mitoo.sashimi.utils.events.NotificationRequestEvent;
import co.mitoo.sashimi.utils.events.NotificationUpdateEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by david on 15-04-07.
 */
public class NotificationPreferenceService extends MitooService {

    private NotificationPreferenceRecieved preference;

    public NotificationPreferenceService(MitooActivity activity) {
        super(activity);
    }

    @Subscribe
    public void requestNotificationPreference(NotificationRequestEvent event){

        Observable<NotificationPreferenceRecieved> observable = getSteakApiService()
                .getNotificationPreference(event.getUserID(), event.getCompetitionSeasonID());
        handleObservable(observable , NotificationPreferenceRecieved.class);

    }

    @Subscribe
    public void onRequestNotificationUpdate(NotificationUpdateEvent event){

        updateNotificationModelObject(event.isChecked(), event.getNotification());
        requestNotificationUpdate(event);

    }

    public void updateNotificationModelObject(boolean checked,
                                              MitooNotification mitooNotificationObject){

        if(mitooNotificationObject.getNotificationType() == MitooEnum.NotificationType.EMAIL)
            requestEmailPrefUpdate(checked , mitooNotificationObject);
        else if(mitooNotificationObject.getNotificationType() == MitooEnum.NotificationType.PUSH)
            requestPushPrefUpdate(checked , mitooNotificationObject);

    }

    public void requestEmailPrefUpdate(boolean checked,
                                       MitooNotification mitooNotificationObject){

        GroupSettings group_settings = preference.getGroup_settings();

        switch (mitooNotificationObject.getNotificationCategory()) {
            case TeamGames:
                group_settings.getGroupTeamGames().setEmail(checked);
                break;
            case TeamResults:
                group_settings.getGroupTeamResults().setEmail(checked);
                break;
            case LeagueResults:
                group_settings.getGroupLeagueResults().setEmail(checked);
                break;
            case RainOut:
                group_settings.getGroupLeagueAlerts().setEmail(checked);
                break;
        }
    }

    public void requestPushPrefUpdate(boolean checked,
                                      MitooNotification mitooNotificationObject){

        GroupSettings group_settings = preference.getGroup_settings();

        switch (mitooNotificationObject.getNotificationCategory()) {
            case TeamGames:
                group_settings.getGroupTeamGames().setPush(checked);
                break;
            case TeamResults:
                group_settings.getGroupTeamResults().setPush(checked);
                break;
            case LeagueResults:
                group_settings.getGroupLeagueResults().setPush(checked);
                break;
            case RainOut:
                group_settings.getGroupLeagueAlerts().setPush(checked);
                break;
        }
    }

    public void requestNotificationUpdate(NotificationUpdateEvent event) {

        Observable<NotificationPreferenceRecieved> observable = getSteakApiService()
                .updateNotificationPreference(event.getUserID(), event.getCompetitionSeasonID(), this.preference);
        observable.subscribe(new Subscriber<NotificationPreferenceRecieved>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(NotificationPreferenceRecieved notificationPreferenceRecieved) {
                NotificationPreferenceService.this.preference = (NotificationPreferenceRecieved)notificationPreferenceRecieved;
                BusProvider.post(new NotificationModelUpdateResponse((NotificationPreferenceRecieved)notificationPreferenceRecieved));
            }
        });

    }

    @Override
    public void resetFields() {
    }

    @Subscribe
    public void onApiFailEvent(RetrofitError event) {

        BusProvider.post(new MitooActivitiesErrorEvent(event));
    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve)  {

        if(objectRecieve instanceof NotificationPreferenceRecieved){
            this.preference = (NotificationPreferenceRecieved)objectRecieve;
            BusProvider.post(new NotificationModelResponseEvent((NotificationPreferenceRecieved)objectRecieve));
        }

    }

}