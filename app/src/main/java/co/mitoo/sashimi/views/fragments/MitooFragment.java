package co.mitoo.sashimi.views.fragments;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.network.Services.AppSettingsService;
import co.mitoo.sashimi.network.Services.CompetitionService;
import co.mitoo.sashimi.network.Services.ConfirmInfoService;
import co.mitoo.sashimi.network.Services.FixtureService;
import co.mitoo.sashimi.network.Services.LeagueService;
import co.mitoo.sashimi.network.Services.LocationService;
import co.mitoo.sashimi.network.Services.MitooService;
import co.mitoo.sashimi.network.Services.MobileTokenService;
import co.mitoo.sashimi.network.Services.NotificationPreferenceService;
import co.mitoo.sashimi.network.Services.SessionService;
import co.mitoo.sashimi.network.Services.TeamService;
import co.mitoo.sashimi.network.Services.UserInfoService;
import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;
import co.mitoo.sashimi.network.DataPersistanceService;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.ViewHelper;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.listener.LocationServicesPromptOnclickListener;
import co.mitoo.sashimi.views.activities.MitooActivity;
import retrofit.RetrofitError;
import com.github.androidprogresslayout.ProgressLayout;

/**
 * Created by david on 14-11-13.
 */
public abstract class MitooFragment extends Fragment implements View.OnClickListener {

    private Toast currentToast;
    private Handler handler;
    private Runnable runnable;
    private boolean busRegistered = false;
    protected Toolbar toolbar;
    protected String fragmentTitle = "";
    private boolean allowBackPressed = true;
    protected boolean loading = false;
    private ProgressDialog progressDialog;
    private ViewHelper viewHelper;
    private ViewGroup rootView;
//    private FormHelper formHelper;
    private boolean pageFirstLoad = true;
    private ProgressLayout progressLayout;
    private boolean popActionRequiresDelay = false;
    private boolean backClicked =false;
    private boolean foreGround;
    protected boolean isResumed = false;

    protected String getTextFromTextField(int textFieldId) {
        EditText textField = (EditText) getActivity().findViewById(textFieldId);
        return textField.getText().toString();
    }

    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        initializeFields();
        registerBus();
    }

    @Override
    public void onResume() {
        super.onResume();
        setForeGround(true);
        handleNetwork();
        registerBus();
        setPageFirstLoad(false);
        this.isResumed= true;

    }

    @Override
    public void onPause() {
        tearDownReferences();
        if(getArguments()!=null)
            onSaveInstanceState(getArguments());
        else
            onSaveInstanceState(new Bundle());
        super.onPause();
    }

    @Override
    public void onDestroy() {
        tearDownReferences();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        tearDownReferences();
        super.onStop();
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error) {

        setLoading(false);
        setPreDataLoading(false);
        handleAndDisplayError(error);

    }

    public void registerBus() {
        if (!busRegistered) {
            BusProvider.register(this);
            busRegistered = true;
        }
    }

    public void unregisterBus() {
        if (busRegistered) {
            BusProvider.unregister(this);
            busRegistered = false;
        }
    }

    protected void initializeFields() {

        setFragmentTitle(getString(R.string.toolbar_placeholder));
    }

    protected void initializeViews(View view) {

        initializeStatusBarColor();
//        setUpToolBar(view);
        setRootView((ViewGroup) view);

    }

    protected void initializeOnClickListeners(View view) {

        getViewHelper().setOnTouchCloseKeyboard(view);

    }

    public MitooActivity getMitooActivity() {
        MitooActivity returnActivity = null;
        Activity activity = getActivity();
        if (activity instanceof MitooActivity) {
            returnActivity = (MitooActivity) activity;
        }
        return returnActivity;
    }

    protected void handleAndDisplayError(MitooActivitiesErrorEvent error) {

        if (error.getRetrofitError() != null) {
            RetrofitError retrofitError = error.getRetrofitError();
            if (retrofitError.getKind() == RetrofitError.Kind.NETWORK) {
                handleNetworkError();
            } else {
                handleHttpErrors(retrofitError.getResponse().getStatus());
            }
        } else {
            displayTextWithToast(error.getErrorMessage());
        }

    }

    protected void handleNetworkError() {

        if (getProgressLayout() != null) {

            centerProgressLayout();
            getProgressLayout().removeAllViews();
//            getProgressLayout().addView(createNetworkFailureView());
            displayTextWithToast(getString(R.string.error_no_internet));

        }
    }

    protected void handleHttpErrors(int statusCode) {
        switch (statusCode) {
            case 401:
                displayTextWithToast(getString(R.string.error_401_incorrect_cred));
                break;
            case 404:
                displayTextWithToast(getString(R.string.error_404));
                break;
            case 409:
                displayTextWithToast(getString(R.string.error_409));
                break;
            case 422:
                displayTextWithToast(getString(R.string.error_422));
                break;
            case 500:
            case 504:
                displayTextWithToast(getString(R.string.error_500));
            default:
        }
    }

    protected void handleNetwork(){
        //if (getMitooActivity()!=null && !getMitooActivity().NetWorkConnectionIsOn())
        //displayTextWithToast(getString(R.string.error_no_internet));

    }

    protected void handleLocationServices() {
        if (!getMitooActivity().LocationServicesIsOn()) {
            buildLocationServicePrompt();
        }
    }

    public void fireFragmentChangeAction(int fragmentId) {

        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(fragmentId)
                .build();
        postFragmentChangeEvent(fragmentChangeEvent);

    }

    public void postFragmentChangeEvent(final FragmentChangeEvent event) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                BusProvider.post(event);

            }
        };
        getHandler().postDelayed(runnable, 150);

    }

    protected void popFragmentAction() {

        getMitooActivity().popFragment();

    }

    public void displayTextWithToast(String text) {

        if(isForeGround()){
            removeToast();
            View toastLayout = createToastView();
            createTextForToast(toastLayout, text);
            Toast toast = new Toast(getActivity().getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(toastLayout);
            toast.show();
            currentToast = toast;
        }

    }

    public void displayTextWithDialog(String title, String message , DialogInterface.OnClickListener listenner){

        //TODO: REFACTORED TO USE PRO AND FIGURE OUT THEMEING OPTIONS
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.prompt_ok),listenner)
                .setCancelable(false);

        builder.create().show();
    }

    private View createToastView() {

        View layout = getActivity().getLayoutInflater().inflate(R.layout.view_toast,
                (ViewGroup) getActivity().findViewById(R.id.toast_layout_root));
        return layout;
    }

    private void createTextForToast(View layout, String text) {

        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(text);

    }

    protected void buildLocationServicePrompt() {

        buildPrompt(getString(R.string.prompt_location_services_title),
                getString(R.string.prompt_location_services_message),
                getString(R.string.prompt_yes),
                getString(R.string.prompt_no),
                new LocationServicesPromptOnclickListener(true, getActivity()),
                new LocationServicesPromptOnclickListener(false, getActivity()));

    }

    protected void buildPrompt(String title, String message, String positiveMessage,
                               String negativeMessage, DialogInterface.OnClickListener positiveListener,
                               DialogInterface.OnClickListener negativeListener) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getActivity(),
                android.R.style.Theme_Holo_Dialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(contextThemeWrapper);
        builder.setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton(positiveMessage, positiveListener)
                .setNegativeButton(negativeMessage, negativeListener);
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void handleCallBacks() {

        if (getHandler() != null) {
            getHandler().removeCallbacksAndMessages(null);
        }

    }

    public List<String> buiildStringList(int arrayID) {

        List<String> returnList = new ArrayList<String>();
        String[] arrayOfString = getResources().getStringArray(arrayID);
        for (String item : arrayOfString) {
            returnList.add(item);
        }
        return returnList;

    }

    protected <T> void setUpListView(ArrayAdapter<T> adapter, ListView listView, AdapterView.OnItemClickListener listener) {

        listView.setOnItemClickListener(listener);
        listView.setAdapter(adapter);

    }

    public void removeToast() {

        if (currentToast != null) {
            currentToast.cancel();
        }
        currentToast = null;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void tearDownReferences() {

        handleCallBacks();
        removeToast();
        unregisterBus();
        getMitooActivity().hideSoftKeyboard();
        setForeGround(false);
        setLoading(false);
        this.isResumed =false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Activity a = getActivity();
            if (a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

/*    protected Toolbar setUpToolBar(View view) {

        setToolbar((Toolbar) view.findViewById(R.id.app_bar));
        if (getToolbar() != null) {

            getToolbar().setTitle(getDataHelper().removeSpaceAtEnd(getFragmentTitle()));
            getToolbar().setTitleTextColor(getResources().getColor(R.color.white));
            if(backPressedAllowed()) {

                getToolbar().setNavigationIcon(R.drawable.header_back_icon);
                setUpBackButtonClickListner();

            }
        }
        return toolbar;

    }*/

    public String getFragmentTitle() {
        return fragmentTitle;
    }

    public void setFragmentTitle(String fragmentTitle) {
        this.fragmentTitle = fragmentTitle;
    }

    public boolean backPressedAllowed() {
        return allowBackPressed;
    }

    public void setAllowBackPressed(boolean allowBackPressed) {
        this.allowBackPressed = allowBackPressed;
    }

    protected MitooService getMitooModel(Class<?> classType) {

        if (classType == UserInfoService.class)
            return getMitooActivity().getModelManager().getUserInfoModel();
        else if (classType == LeagueService.class)
            return getMitooActivity().getModelManager().getLeagueModel();
        else if (classType == SessionService.class)
            return getMitooActivity().getModelManager().getSessionModel();
        else if (classType == LocationService.class)
            return getMitooActivity().getModelManager().getLocationModel();
        else if (classType == AppSettingsService.class)
            return getMitooActivity().getModelManager().getAppSettingsModel();
        else if (classType == MobileTokenService.class)
            return getMitooActivity().getModelManager().getMobileTokenModel();
        else
            return null;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        if (this.loading) {
            displayProgressDialog();
        } else {
            cancelProgressDialog();
        }
    }

    public void setPreDataLoading(boolean loading) {

        this.loading = loading;
        if (getProgressLayout() != null) {
            if (this.loading) {
                getProgressLayout().showProgress();
            } else {
                getProgressLayout().showContent();
            }
        }

    }

    private void displayProgressDialog() {

        ProgressDialog dialog = getProgressDialog();
        if (!dialog.isShowing())
            dialog.show();

    }

    private void cancelProgressDialog() {

        ProgressDialog dialog = getProgressDialog();
        if (dialog.isShowing())
            dialog.dismiss();

    }

    private void iniializeDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getString(R.string.dialog_loading_title));
        progressDialog.setMessage(getString(R.string.dialog_loading_message));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
    }

    public ProgressDialog getProgressDialog() {
        if (this.progressDialog == null)
            iniializeDialog();
        return progressDialog;
    }

    public DataHelper getDataHelper() {
        return getMitooActivity().getDataHelper();
    }


    protected SessionService getSessionModel() {

        return getMitooActivity().getModelManager().getSessionModel();
    }

    protected LeagueService getLeagueModel() {
        return getMitooActivity().getModelManager().getLeagueModel();
    }

    protected AppSettingsService getAppSettingsModel() {
        return getMitooActivity().getModelManager().getAppSettingsModel();
    }

    protected CompetitionService getCompetitionModel() {
        return getMitooActivity().getModelManager().getCompetitionModel();
    }

    protected TeamService getTeamModel() {
        return getMitooActivity().getModelManager().getTeamModel();
    }

    protected FixtureService getFixtureModel() {
        return getMitooActivity().getModelManager().getFixtureModel();
    }

    protected UserInfoService getUserInfoModel() {
        return getMitooActivity().getModelManager().getUserInfoModel();
    }

    protected LocationService getLocationModel() {
        return getMitooActivity().getModelManager().getLocationModel();
    }

    protected ConfirmInfoService getConfirmInfoModel() {
        return getMitooActivity().getModelManager().getConfirmInfoModel();
    }

    protected MobileTokenService getMobileTokenModel() {
        return getMitooActivity().getModelManager().getMobileTokenModel();
    }

    protected NotificationPreferenceService getNotificationPreferenceModel() {
        return getMitooActivity().getModelManager().getNotificationPreferenceModel();
    }

    //TODO
    /*
    protected StandingsService getStandingsModel() {
        return getMitooActivity().getModelManager().getStandingsModel();
    }*/

    public ViewHelper getViewHelper() {
        if (viewHelper == null)
            viewHelper = new ViewHelper(getMitooActivity());
        return viewHelper;
    }

    public void setViewHelper(ViewHelper viewHelper) {
        this.viewHelper = viewHelper;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public Handler getHandler() {
        if (handler == null)
            handler = new Handler();
        return handler;
    }

    protected Bundle createBundleForNextFragment(int keyStringID, int valueStringID) {

        Bundle bundle = new Bundle();
        String key = getMitooActivity().getString(keyStringID);
        String value = getMitooActivity().getString(valueStringID);
        bundle.putString(key, value);
        return bundle;
    }

    protected void handleViewVisibility(View view, boolean show) {
        getViewHelper().setViewVisibility(view, show);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public ViewGroup getRootView() {
        return rootView;
    }

    public void setRootView(ViewGroup rootView) {
        this.rootView = rootView;
    }

    protected void removeDynamicViews() {

    }

    protected void requestFocusForInput(final EditText editText) {

        Runnable requestFocusRunnable = new Runnable() {
            @Override
            public void run() {
                if (editText != null)
                    editText.requestFocusFromTouch();
                getMitooActivity().showKeyboard();
            }
        };
        setRunnable(requestFocusRunnable);
        getHandler().postDelayed(getRunnable(), MitooConstants.durationShort);

    }

/*    public FormHelper getFormHelper() {
        if (formHelper == null)
            formHelper = new FormHelper(this);
        return formHelper;
    }*/

    public boolean pageLoaded() {
        return pageFirstLoad;
    }

    public void setPageFirstLoad(boolean pageFirstLoad) {
        this.pageFirstLoad = pageFirstLoad;
    }

    public ProgressLayout getProgressLayout() {
        return progressLayout;
    }

    public void setProgressLayout(ProgressLayout progressLayout) {
        this.progressLayout = progressLayout;
    }


    public boolean popActionRequiresDelay() {
        return popActionRequiresDelay;
    }

    public void setPopActionRequiresDelay(boolean popActionRequiresDelay) {
        this.popActionRequiresDelay = popActionRequiresDelay;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    protected void setUpBackButtonClickListner() {

        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getMitooActivity().getDataHelper().isClickable(v.getId()))
                    MitooFragment.this.getMitooActivity().onBackPressed();
            }
        });
    }

    protected Object getBundleArgumentFromKey(String key) {

        Object results = null;
        Bundle arguments = getArguments();
        if (arguments != null) {
            results = arguments.get(key);
        }
        return results;

    }

    protected void initializeStatusBarColor() {

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_dark_seven));
        }

    }

    protected void routeToHome(){

        //USE THIS PART TO SHORT CIRCUIT APP TO WHERE YOU WANT

        //TODO:REFACTOR


        Bundle bundle = new Bundle();
        bundle.putInt(getUserIDKey(), getUserID());
        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_home)
                .setTransition(MitooEnum.FragmentTransition.CHANGE)
                .setAnimation(MitooEnum.FragmentAnimation.VERTICAL)
                .setBundle(bundle)
                .build();
        postFragmentChangeEvent(fragmentChangeEvent);

                /*

        Bundle bundle = new Bundle();
        bundle.putInt(getUserIDKey(), getUserID());
        bundle.putInt(getCompetitionSeasonIdKey(), 46791);
        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_standings)
                .setTransition(MitooEnum.FragmentTransition.CHANGE)
                .setAnimation(MitooEnum.FragmentAnimation.VERTICAL)
                .setBundle(bundle)
                .build();
        postFragmentChangeEvent(fragmentChangeEvent);

                                */




    }

    protected void routeToLanding(){

        int startingFragmentId = R.id.fragment_landing;
        //int startingFragmentId = R.id.fragment_interest;
        //int startingFragmentId = R.id.fragment_contact;

        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(startingFragmentId)
                .setTransition(MitooEnum.FragmentTransition.PUSH)
                .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                .build();
        postFragmentChangeEvent(fragmentChangeEvent);
    }

    protected void startRegularFlow(){

        if (getSessionModel()!=null && getSessionModel().getSession() != null) {
            getMitooActivity().updateAuthToken(getSessionModel().getSession().auth_token);
            routeToHome();
        }
        else{
            routeToLanding();
        }
    }

    protected void centerProgressLayout(){

        if(getProgressLayout()!=null){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)getProgressLayout().getLayoutParams();
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            getProgressLayout().setLayoutParams(params);
            getProgressLayout().setGravity(Gravity.CENTER);
        }

    }

    protected void requestData() {
    }

    public void resetFields() {
    }

    public boolean isBackClicked() {
        return backClicked;
    }

    public void setBackClicked(boolean backClicked) {
        this.backClicked = backClicked;
    }

    protected void onNetWorkFailure(){
    }

/*    protected View createNetworkFailureView(){
        RelativeLayout layout = (RelativeLayout) getViewHelper().createRelativeLayoutFromInflator(R.layout.view_networkfailure_text_view);
        return layout;
    }*/

    public boolean isForeGround() {
        return foreGround;
    }

    public void setForeGround(boolean foreGround) {
        this.foreGround = foreGround;
    }

    protected int getUserID(){
        if(getUserInfoModel().getUserInfoRecieve() != null)
            return getUserInfoModel().getUserInfoRecieve().id;
        else if(getSessionModel().getSession()!=null)
            return getSessionModel().getSession().id;
        else{

            DataPersistanceService service = getMitooActivity().getPersistanceService();
            String key = getActivity().getString(R.string.shared_preference_session_key);

            Object object =  service.readFromPreference(key , SessionRecieve.class);

            if(object instanceof  SessionRecieve){
                SessionRecieve session = (SessionRecieve)object;
                return session.id;
            }else{
                return MitooConstants.invalidConstant;
            }
        }
    }

/*    protected boolean isDuringConfirmFlow(){
        boolean result = false;
        if(getMitooActivity()!=null && getMitooActivity().topFragmentType()!= null ){

            if(getMitooActivity().topFragmentType() == ConfirmAccountFragment.class ||
                    getMitooActivity().topFragmentType() == ConfirmSetPasswordFragment.class ||
                    getMitooActivity().topFragmentType() == ConfirmDoneFragment.class)
                result =true;
        }
        return result;
    }*/

/*    protected void handleAuth401Error(){
        if(!isDuringConfirmFlow())
            BusProvider.post(new LogOutNetworkCompleteEevent());
    }*/

    protected DialogInterface.OnClickListener createRegularFlowDialogListner(){

        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startRegularFlow();
            }
        };
    }

    protected String getUserIDKey(){
        return getString(R.string.bundle_key_user_id_key);
    }

    protected String getCompetitionSeasonIdKey() {
        return getString(R.string.bundle_key_competition_id_key);
    }

    protected String getTeamColorKey() {
        return getString(R.string.bundle_key_team_color_key);
    }

    protected String getConfirmInfoKey() {
        return getString(R.string.bundle_key_confirm_token_key);
    }

    protected String getIdentifierKey() {
        return getString(R.string.bundle_key_identifier_key);
    }

    protected String getLeagueIDKey() {
        return getString(R.string.bundle_key_league_id_key);
    }

    protected String getToolBarTitle(){
        return getString(R.string.bundle_key_tool_bar_title);
    }

    protected String getRainOutKey(){
        return getString(R.string.bundle_key_rain_out_key);
    }

    protected String getFirstRainOutColorKey(){
        return getString(R.string.bundle_key_first_color_key);
    }

    protected String getSecondRainOutColorKey(){
        return getString(R.string.bundle_key_second_color_key);
    }

    protected String getMitooActionKey() {
        return getActivity().getString(R.string.bundle_key_mitoo_action);
    }

    protected String getTabIndexSelectedKey() {
        return getActivity().getString(R.string.bundle_key_tab_index_key);
    }


}
