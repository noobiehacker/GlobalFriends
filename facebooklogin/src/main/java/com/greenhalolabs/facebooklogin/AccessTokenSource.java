package com.greenhalolabs.facebooklogin;

/**
 * Created by dannyroa on 8/20/14.
 */

/**
 * Indicates where a Facebook access token was obtained from.
 */
public enum AccessTokenSource {
    /**
     * Indicates an access token has not been obtained, or is otherwise invalid.
     */
    NONE(false),
    /**
     * Indicates an access token was obtained by the user logging in through the
     * Facebook app for Android using the web login dialog.
     */
    FACEBOOK_APPLICATION_WEB(true),
    /**
     * Indicates an access token was obtained by the user logging in through the
     * Facebook app for Android using the native login dialog.
     */
    FACEBOOK_APPLICATION_NATIVE(true),
    /**
     * Indicates an access token was obtained by asking the Facebook app for the
     * current token based on permissions the user has already granted to the app.
     * No dialog was shown to the user in this case.
     */
    FACEBOOK_APPLICATION_SERVICE(true),
    /**
     * Indicates an access token was obtained by the user logging in through the
     * Web-based dialog.
     */
    WEB_VIEW(false),
    /**
     * Indicates an access token is for a test user rather than an actual
     * Facebook user.
     */
    TEST_USER(true),
    /**
     * Indicates an access token constructed with a Client Token.
     */
    CLIENT_TOKEN(true);

    private final boolean canExtendToken;

    AccessTokenSource(boolean canExtendToken) {
        this.canExtendToken = canExtendToken;
    }

    boolean canExtendToken() {
        return canExtendToken;
    }
}
