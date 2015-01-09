package com.greenhalolabs.facebooklogin;

/**
 * Created by dannyroa on 8/20/14.
 */

/**
 * Represents an error returned from the Facebook service in response to a request.
 */
public class FacebookServiceException extends FacebookException {

    private final FacebookRequestError error;

    private static final long serialVersionUID = 1;

    /**
     * Constructs a new FacebookServiceException.
     *
     * @param error the error from the request
     */
    public FacebookServiceException(FacebookRequestError error, String errorMessage) {
        super(errorMessage);
        this.error = error;
    }

    /**
     * Returns an object that encapsulates complete information representing the error returned by Facebook.
     *
     * @return complete information representing the error.
     */
    public final FacebookRequestError getRequestError() {
        return error;
    }

    @Override
    public final String toString() {
        return new StringBuilder()
            .append("{FacebookServiceException: ")
            .append("httpResponseCode: ")
            .append(error.getRequestStatusCode())
            .append(", facebookErrorCode: ")
            .append(error.getErrorCode())
            .append(", facebookErrorType: ")
            .append(error.getErrorType())
            .append(", message: ")
            .append(error.getErrorMessage())
            .append("}")
            .toString();
    }

}
