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
        PRODUCTION, STAGING ,APIARY , LOCALHOST
    }

    public enum MenuItemSelected {
        FEEDBACK, SETTINGS ,NONE
    }

    public enum LeagueListType {
        COMPETITION, ENQUIRED
    }

    public enum FixtureRowType {
        TIME, SCORE, ABANDONED, VOID, POSTPONED , CANCELED, TBC ,RESCHEDULE
    }

    public enum FixtureTabType {
        FIXTURE_SCHEDULE, FIXTURE_RESULT
    }

    public enum TimeFrame {
        PAST , FUTURE
    }

    public enum NotificationType {
        NextGame, TeamResults, RivalResults
    }

    public enum ModelType {
        TEAM , FIXTURE
    }

    public enum ConfirmFlow {
        SIGNUP, INVITE
    }
}
