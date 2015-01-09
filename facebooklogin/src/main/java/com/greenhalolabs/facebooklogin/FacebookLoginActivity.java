package com.greenhalolabs.facebooklogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by dannyroa on 9/1/14.
 */
public class FacebookLoginActivity extends Activity {

    private static final String TAG = FacebookLoginActivity.class.getSimpleName();

    public static final int FACEBOOK_LOGIN_REQUEST_CODE = 1001;

    public static final String EXTRA_FACEBOOK_ACCESS_TOKEN = "extra_facebook_access_token";
    public static final String EXTRA_ERROR_MESSAGE = "extra_error_message";
    public static final String EXTRA_PERMISSIONS = "extra_permissions";
    private static final String EXTRA_FACEBOOK_APPLICATION_ID = "extra_facebook_application_id";

    private static final String KATANA_PACKAGE = "com.facebook.katana";
    private static final String KATANA_PROXY_AUTH_ACTIVITY = "com.facebook.katana.ProxyAuth";
    private static final String KATANA_PROXY_AUTH_PERMISSIONS_KEY = "scope";
    private static final String KATANA_PROXY_AUTH_APP_ID_KEY = "client_id";
    private static final int AUTHORIZE_FACEBOOK = 11111;

    List<String> permissions;

    public static void launch(Fragment fragment, String applicationId, ArrayList<String> permissions) {
        final Intent intent = new Intent(fragment.getActivity(), FacebookLoginActivity.class);
        intent.putExtra(EXTRA_FACEBOOK_APPLICATION_ID, applicationId);
        intent.putStringArrayListExtra(EXTRA_PERMISSIONS, permissions);
        fragment.startActivityForResult(intent, FacebookLoginActivity.FACEBOOK_LOGIN_REQUEST_CODE);
    }

    public static void launch(Activity activity, String applicationId, ArrayList<String> permissions) {
        final Intent intent = new Intent(activity, FacebookLoginActivity.class);
        intent.putExtra(EXTRA_FACEBOOK_APPLICATION_ID, applicationId);
        intent.putStringArrayListExtra(EXTRA_PERMISSIONS, permissions);
        activity.startActivityForResult(intent, FacebookLoginActivity.FACEBOOK_LOGIN_REQUEST_CODE);
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissions = getIntent().getStringArrayListExtra(EXTRA_PERMISSIONS);

        String applicationId = getIntent().getStringExtra(EXTRA_FACEBOOK_APPLICATION_ID);

        if (Utility.isPackageExists(getApplicationContext(), KATANA_PACKAGE)) {

            Intent intent =
                createProxyAuthIntent(applicationId,
                                      permissions);

            startActivityForResult(intent, AUTHORIZE_FACEBOOK);
        }
        else {
            Log.d(TAG, "use web view");

            Bundle parameters = new Bundle();

            parameters.putString(ServerProtocol.DIALOG_PARAM_SCOPE, TextUtils.join(",", permissions));

            clearFacebookCookies(getApplicationContext());

            WebDialog.OnCompleteListener listener = new WebDialog.OnCompleteListener() {
                @Override
                public void onComplete(Bundle values, FacebookException error) {
                    onWebDialogComplete(values, error);
                }
            };

            WebDialog.Builder builder =
                new AuthDialogBuilder(this, applicationId, parameters)
                    .setOnCompleteListener(listener);
            WebDialog loginDialog = builder.build();
            loginDialog.show();
        }
    }

    static Intent createProxyAuthIntent(String applicationId, List<String> permissions) {

        Intent intent = new Intent().setClassName(KATANA_PACKAGE, KATANA_PROXY_AUTH_ACTIVITY)
                                    .putExtra(KATANA_PROXY_AUTH_APP_ID_KEY, applicationId);

        if (permissions != null && permissions.size() > 0) {
            intent.putExtra(KATANA_PROXY_AUTH_PERMISSIONS_KEY, TextUtils.join(",", permissions));
        }

        return intent;
    }

    void onWebDialogComplete(Bundle values,
                             FacebookException error) {
        if (values != null) {
            AccessToken token = AccessToken
                .createFromWebBundle(permissions, values, AccessTokenSource.WEB_VIEW);

            returnToCallingActivity(token.getToken());

        } else {
            if (error instanceof FacebookOperationCanceledException) {
                returnToCallingActivityWithError("User canceled log in.");
            } else if (error != null) {
                returnToCallingActivityWithError(error.getMessage());
            }
            else {
                returnToCallingActivityWithError("Unexpected Error.");
            }
        }

    }

    static class AuthDialogBuilder extends WebDialog.Builder {
        private static final String OAUTH_DIALOG = "oauth";
        static final String REDIRECT_URI = "fbconnect://success";

        public AuthDialogBuilder(Context context, String applicationId, Bundle parameters) {
            super(context, applicationId, OAUTH_DIALOG, parameters);
        }

        @Override
        public WebDialog build() {
            Bundle parameters = getParameters();
            parameters.putString(ServerProtocol.DIALOG_PARAM_REDIRECT_URI, REDIRECT_URI);
            parameters.putString(ServerProtocol.DIALOG_PARAM_CLIENT_ID, getApplicationId());

            return new WebDialog(getContext(), OAUTH_DIALOG, parameters, getTheme(), getListener());
        }
    }

    public static void clearFacebookCookies(Context context) {
        // setCookie acts differently when trying to expire cookies between builds of Android that are using
        // Chromium HTTP stack and those that are not. Using both of these domains to ensure it works on both.
        clearCookiesForDomain(context, "facebook.com");
        clearCookiesForDomain(context, ".facebook.com");
        clearCookiesForDomain(context, "https://facebook.com");
        clearCookiesForDomain(context, "https://.facebook.com");
    }

    private static void clearCookiesForDomain(Context context, String domain) {
        // This is to work around a bug where CookieManager may fail to instantiate if CookieSyncManager
        // has never been created.
        CookieSyncManager syncManager = CookieSyncManager.createInstance(context);
        syncManager.sync();

        CookieManager cookieManager = CookieManager.getInstance();

        String cookies = cookieManager.getCookie(domain);
        if (cookies == null) {
            return;
        }

        String[] splitCookies = cookies.split(";");
        for (String cookie : splitCookies) {
            String[] cookieParts = cookie.split("=");
            if (cookieParts.length > 0) {
                String newCookie = cookieParts[0].trim() + "=;expires=Sat, 1 Jan 2000 00:00:01 UTC;";
                cookieManager.setCookie(domain, newCookie);
            }
        }
        cookieManager.removeExpiredCookie();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)  {

        if (requestCode == AUTHORIZE_FACEBOOK) {

            if (data == null) {
                // This happens if the user presses 'Back'.
                returnToCallingActivityWithError("Operation Canceled.");
            } else if (resultCode == Activity.RESULT_CANCELED) {
                returnToCallingActivityWithError(data.getStringExtra("error"));
            } else if (resultCode != Activity.RESULT_OK) {
                returnToCallingActivityWithError("Unexpected resultCode from authorization.");
            } else {
                handleResultOk(data);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleResultOk(Intent data) {
        Bundle extras = data.getExtras();
        String error = extras.getString("error");
        if (error == null) {
            error = extras.getString("error_type");
        }

        if (error == null) {
            AccessToken token = AccessToken.createFromWebBundle(permissions, extras,
                                                                AccessTokenSource.FACEBOOK_APPLICATION_WEB);
            returnToCallingActivity(token.getToken());
        } else if (errorsProxyAuthDisabled.contains(error)) {
            returnToCallingActivityWithError(error);
        } else if (errorsUserCanceled.contains(error)) {
            returnToCallingActivityWithError(error);
        } else {
            returnToCallingActivityWithError(extras.getString("error_description"));
        }
    }

    void returnToCallingActivity(String token) {

        Intent returnIntent = new Intent();
        returnIntent.putExtra(EXTRA_FACEBOOK_ACCESS_TOKEN, token);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    void returnToCallingActivityWithError(String errorMessage) {

        Intent returnIntent = new Intent();
        returnIntent.putExtra(EXTRA_ERROR_MESSAGE, errorMessage);
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();

    }


    public static final Collection<String> errorsProxyAuthDisabled =
        Utility.unmodifiableCollection("service_disabled", "AndroidAuthKillSwitchException");
    public static final Collection<String> errorsUserCanceled =
        Utility.unmodifiableCollection("access_denied", "OAuthAccessDeniedException");


}

