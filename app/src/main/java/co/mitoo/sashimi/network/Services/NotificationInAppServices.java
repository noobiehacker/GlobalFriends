package co.mitoo.sashimi.network.Services;
import android.os.Bundle;
import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.recieve.NotificationReceive;
import co.mitoo.sashimi.services.EventTrackingService;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.CompetitionNotificationRequestEvent;
import co.mitoo.sashimi.utils.events.CompetitionNotificationResponseEvent;
import co.mitoo.sashimi.utils.events.CompetitionNotificationUpdateResponseEvent;
import co.mitoo.sashimi.utils.events.ConsumeNotificationEvent;
import co.mitoo.sashimi.utils.events.FixtureNotificaitonRequestEvent;
import co.mitoo.sashimi.utils.events.FixtureNotificationResponseEvent;
import co.mitoo.sashimi.utils.events.FixtureNotificationUpdateResponseEvent;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.NotificationEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import co.mitoo.sashimi.views.application.MitooApplication;
import co.mitoo.sashimi.views.fragments.CompetitionSeasonFragment;
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
    private List<NotificationReceive> notificationInQueue = new ArrayList<NotificationReceive>();

    @Subscribe
    public void onNotificationRecieve(NotificationEvent event) {

        NotificationReceive notification = event.getNotificationReceive();
        EventTrackingService.userOpenedNotification(this.getUserID(), "", notification.getObj_type(), notification.getObj_id(), notification.getMitoo_action());
        MitooEnum.RoutingDestination destination = getRoutingDestination(event.getNotificationReceive());
        this.notificationInQueue.add(notification);

        if(destination == MitooEnum.RoutingDestination.FIXTURE){
            String fixtureObjectID = event.getNotificationReceive().getObj_id();
            routeToFixture(fixtureObjectID);
        }else if(destination == MitooEnum.RoutingDestination.COMPETITIONSEASON){
            String competitionObjectID= event.getNotificationReceive().getObj_id();
            routeToCompetitionSeason(competitionObjectID);
        }

    }

    private void routeToFixture(String fixtureObjectID){

        int fixtureID = Integer.parseInt(fixtureObjectID);
        enableHomeScreenLoading();
        BusProvider.post(new FixtureNotificaitonRequestEvent(fixtureID));
    }

    private void routeToCompetitionSeason(String competitionObjectID){
        int competitionSeasonID = Integer.parseInt(competitionObjectID);
        enableHomeScreenLoading();
        BusProvider.post(new CompetitionNotificationRequestEvent(competitionSeasonID));

    }

    private MitooEnum.RoutingDestination getRoutingDestination(NotificationReceive notificationReceive){
        if(notificationReceive.getObj_type().equalsIgnoreCase(getActivity().getString(R.string.mitoo_notification_fixture)))
            return MitooEnum.RoutingDestination.FIXTURE;
        else if(notificationReceive.getObj_type().equalsIgnoreCase(getActivity().getString(R.string.mitoo_notification_competition)))
            return MitooEnum.RoutingDestination.COMPETITIONSEASON;
        else
            return MitooEnum.RoutingDestination.FIXTURE;
    }

    public void consumeEventsInQueue() {

        BusProvider.post(new ConsumeNotificationEvent());

    }

    @Subscribe
    public void onCompetitionNotificationResponseEvent(CompetitionNotificationResponseEvent event) {

        disableHomeScreenLoading();

        //IF WER ARE ON Competition SCREEN
        if (getActivity().topFragmentType() == CompetitionSeasonFragment.class && !event.hasError()){

            String mitooAction = getMitooActionFromID(event.getCompetition().getId());
            if(mitooAction!=null)
                BusProvider.post(new CompetitionNotificationUpdateResponseEvent(event.getCompetition(), mitooAction));
            else
                BusProvider.post(new CompetitionNotificationUpdateResponseEvent(event.getCompetition()));

        }
        else {

            //IF WER ARE ON OTHER SCREEN
            queueHomeFragment();

            if (!event.hasError()) {
                //COMPETITION SEASON SCREEN PUSH
                queueCompetitionFragment(event.getCompetition().getId());
            }
            if (!getFragmentStack().isEmpty()){
                consumeEventsInQueue();

            }

        }

    }

    @Subscribe
    public void onFixtureNotificationResponseEvent(FixtureNotificationResponseEvent event) {

        disableHomeScreenLoading();

        //IF WER ARE ON FIXTURE SCREEN
        if (getActivity().topFragmentType() == FixtureFragment.class && !event.hasError())
            BusProvider.post(new FixtureNotificationUpdateResponseEvent(event.getFixture()));
        else {

            //IF WER ARE ON OTHER SCREEN
            queueHomeFragment();

            if (!event.hasError()) {
                //COMPETITION SEASON SCREEN PUSH
                queueCompetitionFragment(event.getFixture().getFixture().getCompetition_season_id());

                //FIXTURE SCREEN PUSH
                queueFixtureFragment(event.getFixture().getFixture().getId());

            }

            if (!getFragmentStack().isEmpty())
                consumeEventsInQueue();

        }
    }

    private void disableHomeScreenLoading(){

        if(getFragmentStack().size()>0){
            MitooFragment fragmemt = getFragmentStack().peek();
            if(fragmemt instanceof HomeFragment)
                fragmemt.setLoading(false);
        }
    }

    private void enableHomeScreenLoading(){

        if(getFragmentStack().size()>0){
            MitooFragment fragmemt = getFragmentStack().peek();
            if(fragmemt instanceof HomeFragment)
                fragmemt.setLoading(true);
        }
    }

    private void queueHomeFragment(){

        Bundle homeBundle = new Bundle();
        homeBundle.putInt(getUserIDKey(), getUserID());

        //HOME SCREEN PUSH
        FragmentChangeEvent firstEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_home)
                .setTransition(MitooEnum.FragmentTransition.CHANGE)
                .setBundle(homeBundle)
                .build();


        getEventQueue().offer(firstEvent);

    }

    private void queueCompetitionFragment(int competitionSeasonID){

        Bundle competitionBundle = new Bundle();
        competitionBundle.putInt(getCompetitionSeasonIdKey(), competitionSeasonID);
        competitionBundle.putString(getTeamColorKey(), getActivity().getString(R.string.place_holder_color_one));
        if(getMitooActionFromID(competitionSeasonID)!=null)
            competitionBundle.putString(getMitooActionKey(), getMitooActionFromID(competitionSeasonID));
        FragmentChangeEvent seconndEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_competition)
                .setTransition(MitooEnum.FragmentTransition.PUSH)
                .setBundle(competitionBundle)
                .build();
        getEventQueue().offer(seconndEvent);

    }

    private String getMitooActionFromID(int id){
        NotificationReceive notificationReceive = findNotificationInQueue(id);
        if(notificationReceive!=null){
            return notificationReceive.getMitoo_action();
        }else{
            return null;
        }
    }

    private NotificationReceive findNotificationInQueue(int id){

        NotificationReceive result = null;
        if(this.notificationInQueue!=null && !this.notificationInQueue.isEmpty()){

            loop:
            for(NotificationReceive item : this.notificationInQueue){
                String objectID= item.getObj_id();
                if(objectID!=null){
                    int notificationID = Integer.parseInt(objectID);
                    if(notificationID == id){
                        result = item;
                        break loop;
                    }
                }
            }
        }

        return result;
    }

    private void queueFixtureFragment(int fixtureID){

        Bundle fixtureBundle = new Bundle();
        fixtureBundle.putInt(getFixtureIdKey(), fixtureID);
        FragmentChangeEvent thirdEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_fixture)
                .setTransition(MitooEnum.FragmentTransition.PUSH)
                .setBundle(fixtureBundle)
                .build();
        getEventQueue().offer(thirdEvent);

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

    protected String getMitooActionKey() {
        return getActivity().getString(R.string.bundle_key_mitoo_action);
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
