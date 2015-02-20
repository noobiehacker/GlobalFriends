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
        HORIZONTAL, VERTICAL , DOWNLEFT
    }

    public enum APIRequest {
        REQUEST, UPDATE
    }
}
