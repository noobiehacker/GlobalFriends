package com.greenhalolabs.facebooklogin;

/**
 * Created by dannyroa on 8/20/14.
 */

import java.util.Collection;

/**
 * com.facebook.internal is solely for the use of other packages within the Facebook SDK for Android. Use of
 * any of the classes in this package is unsupported, and they may be modified or removed without warning at
 * any time.
 */
public final class ServerProtocol {
    static final String FACEBOOK_COM = "facebook.com";
    public static final String DIALOG_AUTHORITY = "m." + FACEBOOK_COM;
    public static final String DIALOG_PATH = "dialog/";
    public static final String DIALOG_PARAM_SCOPE = "scope";
    public static final String DIALOG_PARAM_CLIENT_ID = "client_id";
    public static final String DIALOG_PARAM_DISPLAY = "display";
    public static final String DIALOG_PARAM_REDIRECT_URI = "redirect_uri";
    public static final String DIALOG_PARAM_TYPE = "type";
    public static final String DIALOG_PARAM_ACCESS_TOKEN = "access_token";
    public static final String DIALOG_PARAM_APP_ID = "app_id";

    // URL components
    public static final String GRAPH_URL = "https://graph." + FACEBOOK_COM;
    public static final String GRAPH_URL_BASE = "https://graph." + FACEBOOK_COM + "/";
    public static final String REST_URL_BASE = "https://api." + FACEBOOK_COM + "/method/";
    public static final String BATCHED_REST_METHOD_URL_BASE = "method/";

    public static final Collection<String> errorsProxyAuthDisabled =
        Utility.unmodifiableCollection("service_disabled", "AndroidAuthKillSwitchException");
    public static final Collection<String> errorsUserCanceled =
        Utility.unmodifiableCollection("access_denied", "OAuthAccessDeniedException");
}
