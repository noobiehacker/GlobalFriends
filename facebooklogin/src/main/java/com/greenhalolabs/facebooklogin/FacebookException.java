package com.greenhalolabs.facebooklogin;

/**
 * Created by dannyroa on 8/20/14.
 */
/**
 * Represents an error condition specific to the Facebook SDK for Android.
 */
public class FacebookException extends RuntimeException {
    static final long serialVersionUID = 1;

    /**
     * Constructs a new FacebookException.
     */
    public FacebookException() {
        super();
    }

    /**
     * Constructs a new FacebookException.
     *
     * @param message
     *            the detail message of this exception
     */
    public FacebookException(String message) {
        super(message);
    }

    /**
     * Constructs a new FacebookException.
     *
     * @param message
     *            the detail message of this exception
     * @param throwable
     *            the cause of this exception
     */
    public FacebookException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Constructs a new FacebookException.
     *
     * @param throwable
     *            the cause of this exception
     */
    public FacebookException(Throwable throwable) {
        super(throwable);
    }
}