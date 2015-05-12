package co.mitoo.sashimi.services;

import com.google.common.collect.ImmutableMap;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import co.mitoo.sashimi.views.adapters.StringListAdapter;
import io.keen.client.java.KeenClient;

/**
 * Created by prollinson on 11/05/15.
 */
public class EventTrackingService {


    public static void userViewedHomeScreen(int userId) {
        final Map<String, Object> event = ImmutableMap.<String, Object>builder().
                put("user", ImmutableMap.<String, Object>builder().
                        put("id", userId).
                        build()).
                build();

        KeenClient.client().queueEvent("user_viewed_home", event);

        EventTrackingService.userEngagement(userId);
    }

    public static void userViewedCompetitionScheduleScreen(int userId, int competitionSeasonId, int leagueId) {
        final Map<String, Object> event = ImmutableMap.<String, Object>builder().
                put("user", ImmutableMap.<String, Object>builder().
                        put("id", userId).
                        build()).
                put("competition_season", ImmutableMap.<String, Object>builder().
                        put("id", competitionSeasonId).
                        build()).
                put("league", ImmutableMap.<String, Object>builder().
                        put("id", leagueId).
                        build()).
                build();

        KeenClient.client().queueEvent("user_viewed_competition_schedule", event);

        EventTrackingService.userEngagement(userId, competitionSeasonId);
    }

    public static void userViewedCompetitionResultsScreen(int userId, int competitionSeasonId, int leagueId) {
        final Map<String, Object> event = ImmutableMap.<String, Object>builder().
                put("user", ImmutableMap.<String, Object>builder().
                        put("id", userId).
                        build()).
                put("competition_season", ImmutableMap.<String, Object>builder().
                        put("id", competitionSeasonId).
                        build()).
                put("league", ImmutableMap.<String, Object>builder().
                        put("id", leagueId).
                        build()).
                build();

        KeenClient.client().queueEvent("user_viewed_competition_results", event);

        EventTrackingService.userEngagement(userId, competitionSeasonId);
    }

    public static void userViewedFixtureDetailsScreen(int userId, int fixtureId, int competitionSeasonId, int leagueId) {
        final Map<String, Object> event = ImmutableMap.<String, Object>builder().
                put("user", ImmutableMap.<String, Object>builder().
                        put("id", userId).
                        build()).
                put("fixture", ImmutableMap.<String, Object>builder().
                        put("id", fixtureId).
                        build()).
                put("competition_season", ImmutableMap.<String, Object>builder().
                        put("id", competitionSeasonId).
                        build()).
                put("league", ImmutableMap.<String, Object>builder().
                        put("id", leagueId).
                        build()).
                build();

        KeenClient.client().queueEvent("user_viewed_fixture_details", event);

        EventTrackingService.userEngagement(userId, competitionSeasonId);
    }

    public static void userViewedNotificationPreferencesScreen(int userId, int competitionSeasonId) {
        final Map<String, Object> event = ImmutableMap.<String, Object>builder().
                put("user", ImmutableMap.<String, Object>builder().
                        put("id", userId).
                        build()).
                put("competition_season", ImmutableMap.<String, Object>builder().
                        put("id", competitionSeasonId).
                        build()).
                build();

        KeenClient.client().queueEvent("user_viewed_notification_preferences", event);

        EventTrackingService.userEngagement(userId, competitionSeasonId);
    }

    public static void userEngagement(int userId) {
        EventTrackingService.userEngagement(userId, 0);
    }

    public static void userEngagement(int userId, int competitionSeasonId) {

        Date now = new Date();

        // to make analysis easier through Keen we send two additional timestamps with zeroed values
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00'Z'");
        format.setTimeZone(TimeZone.getTimeZone("Zulu"));
        String timestampZeroedHour = format.format(now);

        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:00:00'Z'");
        format.setTimeZone(TimeZone.getTimeZone("Zulu"));
        String timestampZeroedMinuteSec = format.format(now);

        // day of week
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        String dayOfWeek = Integer.toString(calendar.get(Calendar.DAY_OF_WEEK));

        format = new SimpleDateFormat("MM");
        format.setTimeZone(TimeZone.getTimeZone("Zulu"));
        String monthOfYear = format.format(now);

        // construct the map for this event
        ImmutableMap.Builder map = ImmutableMap.<String, Object>builder().
                put("user", ImmutableMap.<String, Object>builder().
                        put("id", userId).
                        build()).
                put("day", timestampZeroedHour).
                put("hour", timestampZeroedMinuteSec).
                put("day_of_week", dayOfWeek).
                put("month_of_year", monthOfYear);

        // Competition Season might not be available
        if (competitionSeasonId != 0) {
            map.put("competition_season", ImmutableMap.<String, Object>builder().
                    put("id", competitionSeasonId).
                    build());
        }

        // build the map
        final Map<String, Object> event = map.build();

        KeenClient.client().queueEvent("user_engagement", event);
    }

    public static void userViewedProfileScreen(int userId) {
        final Map<String, Object> event = ImmutableMap.<String, Object>builder().
                put("user", ImmutableMap.<String, Object>builder().
                        put("id", userId).
                        build()).
                build();

        KeenClient.client().queueEvent("user_viewed_profile", event);

        EventTrackingService.userEngagement(userId);
    }

    // this should be called whenever a user actions a notification
    public static void userOpenedNotification(int userId, String type, String mitooObjectType, String objectId, String mitooAction) {
        final Map<String, Object> event = ImmutableMap.<String, Object>builder().
                put("user", ImmutableMap.<String, Object>builder().
                        put("id", userId).
                        build()).
                put("medium", "push").
                put("type", type).
                put("action", ImmutableMap.<String, Object>builder().
                        put("object_type", mitooObjectType).
                        put("object_id", objectId).
                        put("mitoo_action", mitooAction).
                        build()).
                build();

        KeenClient.client().queueEvent("user_opened_notification", event);

        EventTrackingService.userEngagement(userId);

    }
}
