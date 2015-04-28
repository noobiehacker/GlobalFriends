package co.mitoo.sashimi.utils;

/**
 * Created by david on 15-01-13.
 */
public class MitooEnum {

    public enum FragmentTransition {
        PUSH, POP, CHANGE,NONE
    }

    public enum FeedBackOption {
        HAPPY, CONFUSED, UNHAPPY
    }

    public enum AboutMitooOption {
        TERMS, PRIVACYPOLICY ,FAQ
    }

    public enum Crud {
        CREATE, READ, UPDATE,DELETE
    }

    public enum SessionRequestType{
        LOGIN , SIGNUP
    }
    
    public enum ModelResponse{
        PREFERENCE, API
    }

    public enum ViewType{
        LIST, FRAGMENT
    }

    public enum ErrorType{
        APP ,API
    }

    public enum FragmentAnimation {
        HORIZONTAL, VERTICAL , DOWNLEFT ,NONE
    }

    public enum APIRequest {
        REQUEST, UPDATE
    }

    public enum AppEnvironment {
        PRODUCTION, STAGING , LOCALHOST
    }

    public enum SteakEndPoint {
        PRODUCTION, STAGING ,APIARY , LOCALHOST
    }

    public enum MenuItemSelected {
        FEEDBACK, SETTINGS ,NONE
    }

    public enum LeagueListType {
        COMPETITION, ENQUIRED
    }

    public enum FixtureStatus {
        TIME, SCORE, ABANDONED, VOID, POSTPONED, CANCELED, RESCHEDULED, DELETED
    }

    public enum FixtureTabType {
        FIXTURE_SCHEDULE, FIXTURE_RESULT , TEAM_STANDINGS
    }

    public enum TimeFrame {
        PAST , FUTURE
    }

    public enum NotificationCategory {
        TeamGames, TeamResults, LeagueResults , RainOut
    }

    public enum NotificationType {
        EMAIL, PUSH
    }

    public enum ModelType {
        TEAM , FIXTURE
    }

    public enum ConfirmFlow {
        SIGNUP, INVITE
    }

    public enum RoutingDestination {
        FIXTURE, COMPETITIONSEASON
    }
}
