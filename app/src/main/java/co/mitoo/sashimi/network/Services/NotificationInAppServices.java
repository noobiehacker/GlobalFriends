package co.mitoo.sashimi.network.Services;
import android.os.Bundle;
import com.squareup.otto.Subscribe;
import java.util.Queue;
import java.util.Stack;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.recieve.NotificationReceive;
import co.mitoo.sashimi.services.EventTrackingService;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.ConsumeNotificationEvent;
import co.mitoo.sashimi.utils.events.FixtureNotificaitonRequestEvent;
import co.mitoo.sashimi.utils.events.FixtureNotificationResponseEvent;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.NotificationEvent;
import co.mitoo.sashimi.utils.events.NotificationUpdateResponseEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import co.mitoo.sashimi.views.application.MitooApplication;
import co.mitoo.sashimi.views.fragments.FixtureFragment;
import co.mitoo.sashimi.views.fragments.HomeFragment;
import co.mitoo.sashimi.views.fragments.MitooFragment;

/**
 * Created by david on 15-05-19.
 */
public class NotificationInAppServices extends MitooService {

    public NotificationInAppServices(MitooActivity activity) {
        super(activity);
    }

    @Subscribe
    public void onConsumeAllEvents(ConsumeNotificationEvent event){
        consumeEventsInQueue();
    }

    @Subscribe
    public void onNotificationRecieve(NotificationEvent event) {

        NotificationReceive notification = event.getNotificationReceive();
        EventTrackingService.userOpenedNotification(this.getUserID(), "", notification.getObj_type(), notification.getObj_id(), notification.getMitoo_action());
        String fixtureObjectID = event.getNotificationReceive().getObj_id();
        boolean notificationIsFixture = true;
        if(notificationIsFixture){
            int fixtureID = Integer.parseInt(fixtureObjectID);
            if(getFragmentStack().size()>0){
                MitooFragment fragmemt = getFragmentStack().peek();
                if(fragmemt instanceof HomeFragment)
                    fragmemt.setLoading(true);
            }
            BusProvider.post(new FixtureNotificaitonRequestEvent(fixtureID));
        }

    }

    public void consumeEventsInQueue() {

        if (getActivity().getModelManager().getSessionModel().userIsLoggedIn()) {
            if (!getEventQueue().isEmpty())
                getActivity().popAllFragments();
            while (!getEventQueue().isEmpty()) {
                BusProvider.post(getEventQueue().poll());
            }
        }

    }

    @Subscribe
    public void onFixtureNotificationResponseEvent(FixtureNotificationResponseEvent event) {

        if(getFragmentStack().size()>0){
            MitooFragment fragmemt = getFragmentStack().peek();
            if(fragmemt instanceof HomeFragment)
                fragmemt.setLoading(false);
        }

        //IF WER ARE ON FIXTURE SCREEN
        if (getActivity().topFragmentType() == FixtureFragment.class && !event.hasError())
            BusProvider.post(new NotificationUpdateResponseEvent(event.getFixture()));
        else {

            //IF WER ARE ON OTHER SCREEN

            Bundle homeBundle = new Bundle();
            homeBundle.putInt(getUserIDKey(), getUserID());

            //HOME SCREEN PUSH
            FragmentChangeEvent firstEvent = FragmentChangeEventBuilder.getSingletonInstance()
                    .setFragmentID(R.id.fragment_home)
                    .setTransition(MitooEnum.FragmentTransition.CHANGE)
                    .setBundle(homeBundle)
                    .build();


            getEventQueue().offer(firstEvent);

            if (!event.hasError()) {
                //COMPETITION SEASON SCREEN PUSH

                Bundle competitionBundle = new Bundle();
                competitionBundle.putInt(getCompetitionSeasonIdKey(), event.getFixture().getFixture().getCompetition_season_id());
                competitionBundle.putString(getTeamColorKey(), getActivity().getString(R.string.place_holder_color));
                FragmentChangeEvent seconndEvent = FragmentChangeEventBuilder.getSingletonInstance()
                        .setFragmentID(R.id.fragment_competition)
                        .setTransition(MitooEnum.FragmentTransition.PUSH)
                        .setBundle(competitionBundle)
                        .build();
                getEventQueue().offer(seconndEvent);

                //FIXTURE SCREEN PUSH

                Bundle fixtureBundle = new Bundle();
                fixtureBundle.putInt(getFixtureIdKey(), event.getFixture().getFixture().getId());
                FragmentChangeEvent thirdEvent = FragmentChangeEventBuilder.getSingletonInstance()
                        .setFragmentID(R.id.fragment_fixture)
                        .setTransition(MitooEnum.FragmentTransition.PUSH)
                        .setBundle(fixtureBundle)
                        .build();
                getEventQueue().offer(thirdEvent);
            }

            if (!getFragmentStack().isEmpty())
                consumeEventsInQueue();

        }

    }

    protected String getUserIDKey() {
        return getActivity().getString(R.string.bundle_key_user_id_key);
    }

    protected String getCompetitionSeasonIdKey() {
        return getActivity().getString(R.string.bundle_key_competition_id_key);
    }

    protected String getFixtureIdKey() {
        return getActivity().getString(R.string.bundle_key_fixture_id_key);
    }

    protected String getTeamColorKey() {
        return getActivity().getString(R.string.bundle_key_team_color_key);
    }


    private Stack<MitooFragment> getFragmentStack(){
        MitooApplication application = (MitooApplication) getActivity().getApplication();
        return application.getFragmentStack();
    }

    private Queue<Object> getEventQueue(){
        MitooApplication application = (MitooApplication) getActivity().getApplication();
        return application.getEventQueue();
    }

}
