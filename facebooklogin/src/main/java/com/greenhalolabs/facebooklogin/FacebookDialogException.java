package com.greenhalolabs.facebooklogin;

/**
 * Created by dannyroa on 8/20/14.
 */

/**
 * Represents an error condition relating to displaying a Facebook Web dialog.
 */
public class FacebookDialogException extends FacebookException {
    static final long serialVersionUID = 1;
    private int errorCode;
    private String failingUrl;

    /**
     * Constructs a new FacebookException.
     */
    public FacebookDialogException(String message, int errorCode, String failingUrl) {
        super(message);
        this.errorCode = errorCode;
        this.failingUrl = failingUrl;
    }

    /**
     * Gets the error code received by the WebView. See:
     * http://developer.android.com/reference/android/webkit/WebViewClient.html
     *
     * @return the error code
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Gets the URL that the dialog was trying to load.
     * @return the URL
     */
    public String getFailingUrl() {
        return failingUrl;
    }
}

