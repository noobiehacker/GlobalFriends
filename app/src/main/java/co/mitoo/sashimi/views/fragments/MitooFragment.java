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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.AppSettingsModel;
import co.mitoo.sashimi.models.CompetitionModel;
import co.mitoo.sashimi.models.ConfirmInfoModel;
import co.mitoo.sashimi.models.FixtureModel;
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.LocationModel;
import co.mitoo.sashimi.models.SessionModel;
import co.mitoo.sashimi.models.TeamModel;
import co.mitoo.sashimi.models.UserInfoModel;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.FormHelper;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.managers.ModelManager;
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
    private FormHelper formHelper;
    private boolean pageFirstLoad = true;
    private ProgressLayout progressLayout;
    private boolean popActionRequiresDelay = false;

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
        handleNetwork();
        registerBus();
        setPageFirstLoad(false);
    }

    @Override
    public void onPause() {
        tearDownReferences();
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
        setUpToolBar(view);
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
            displayText(error.getErrorMessage());
        }

    }

    protected void handleNetworkError() {

        displayText(getString(R.string.error_no_internet));
    }

    protected void handleHttpErrors(int statusCode) {
        switch (statusCode) {
            case 401:
                displayText(getString(R.string.error_401_incorrect_cred));
                break;
            case 404:
                displayText(getString(R.string.error_404));
                break;
            case 409:
                displayText(getString(R.string.error_409));
                break;
            case 422:
                displayText(getString(R.string.error_422));
                break;
            case 500:
                displayText(getString(R.string.error_500));
            default:
        }
    }

    private void handleNetwork() {
        if (!getMitooActivity().NetWorkConnectionIsOn())
            displayText(getString(R.string.error_no_internet));

    }

    protected void handleLocationServices() {
        if (!getMitooActivity().LocationServicesIsOn()) {
            buildLocationServicePrompt();
        }
    }

    public void fireFragmentChangeAction(int fragmentId) {

        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingleTonInstance()
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

    public void displayText(String text) {

        removeToast();
        View toastLayout = createToastView();
        createTextForToast(toastLayout, text);
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastLayout);
        toast.show();
        currentToast = toast;
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
        setLoading(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Activity a = getActivity();
            if (a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    protected Toolbar setUpToolBar(View view) {

        setToolbar((Toolbar) view.findViewById(R.id.app_bar));
        if (getToolbar() != null) {

            getToolbar().setNavigationIcon(R.drawable.header_back_icon);
            getToolbar().setTitle(getDataHelper().removeSpaceAtEnd(getFragmentTitle()));
            getToolbar().setTitleTextColor(getResources().getColor(R.color.white));
            setUpBackButtonClickListner();

        }
        return toolbar;

    }

    public String getFragmentTitle() {
        return fragmentTitle;
    }

    public void setFragmentTitle(String fragmentTitle) {
        this.fragmentTitle = fragmentTitle;
    }


    public ModelManager getRetriever() {
        return getMitooActivity().getModelManager();
    }

    public boolean isAllowBackPressed() {
        return allowBackPressed;
    }

    public void setAllowBackPressed(boolean allowBackPressed) {
        this.allowBackPressed = allowBackPressed;
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


    protected SessionModel getSessionModel() {

        return getMitooActivity().getModelManager().getSessionModel();
    }

    protected LeagueModel getLeagueModel() {
        return getMitooActivity().getModelManager().getLeagueModel();
    }

    protected AppSettingsModel getAppSettingsModel() {
        return getMitooActivity().getModelManager().getAppSettingsModel();
    }

    protected CompetitionModel getCompetitionModel() {
        return getMitooActivity().getModelManager().getCompetitionModel();
    }

    protected TeamModel getTeamModel() {
        return getMitooActivity().getModelManager().getTeamModel();
    }

    protected FixtureModel getFixtureModel() {
        return getMitooActivity().getModelManager().getFixtureModel();
    }

    protected UserInfoModel getUserInfoModel() {
        return getMitooActivity().getModelManager().getUserInfoModel();
    }

    protected LocationModel getLocationModel() {
        return getMitooActivity().getModelManager().getLocationModel();
    }

    protected ConfirmInfoModel getConfirmInfoModel() {
        return getMitooActivity().getModelManager().getConfirmInfoModel();
    }

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

    protected void requestFocusForTopInput(final EditText editText) {

        Runnable requestFocusRunnable = new Runnable() {
            @Override
            public void run() {
                if (editText != null)
                    editText.requestFocusFromTouch();
                getMitooActivity().showKeyboard();
            }
        };
        setRunnable(requestFocusRunnable);
        getHandler().postDelayed(getRunnable(), 250);

    }

    public FormHelper getFormHelper() {
        if (formHelper == null)
            formHelper = new FormHelper(this);
        return formHelper;
    }

    public boolean isPageFirstLoad() {
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
            window.setStatusBarColor(getResources().getColor(R.color.gray_dark_six));
        }

    }

    protected void routeToHome(){

        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingleTonInstance()
                .setFragmentID(R.id.fragment_home)
                .setTransition(MitooEnum.FragmentTransition.CHANGE)
                .setAnimation(MitooEnum.FragmentAnimation.VERTICAL)
                .build();
        postFragmentChangeEvent(fragmentChangeEvent);

    }

    protected void routeToLanding(){
        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingleTonInstance()
                .setFragmentID(R.id.fragment_landing)
                .setTransition(MitooEnum.FragmentTransition.CHANGE)
                .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                .build();
        postFragmentChangeEvent(fragmentChangeEvent);
    }

    protected void requestData() {
    }

}