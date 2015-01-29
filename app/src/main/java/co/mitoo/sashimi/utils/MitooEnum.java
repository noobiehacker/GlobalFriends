package co.mitoo.sashimi.utils;

/**
 * Created by david on 15-01-13.
 */
public class MitooEnum {


    public enum fragmentTransition{
        PUSH, POP, CHANGE,NONE
    }

    public enum feedBackOption{
        HAPPY, CONFUSED, UNHAPPY
    }

    public enum aboutMitooOption{
        TERMS, PRIVACYPOLICY
    }

    public enum crud{
        CREATE, READ, UPDATE,DELETE
    }

    public enum SessionRequestType{
        LOGIN , SIGNUP
    }
    
    public enum ModelResponse{
        PREFERENCE, API
    }

    public enum ErrorType{
        APP ,API
    }
}
