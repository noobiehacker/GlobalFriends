package co.mitoo.sashimi.views.fragments;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
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
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.LocationModel;
import co.mitoo.sashimi.models.MitooModel;
import co.mitoo.sashimi.models.SessionModel;
import co.mitoo.sashimi.models.UserInfoModel;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.managers.ModelManager;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.listener.LocationServicesPromptOnclickListener;
import co.mitoo.sashimi.views.activities.MitooActivity;
import retrofit.RetrofitError;

/**
 * Created by david on 14-11-13.
 */
public abstract class MitooFragment extends Fragment implements View.OnClickListener {

    private Toast currentToast;
    private Handler handler;
    private Runnable runnable;
    private boolean busRegistered= false;
    protected Toolbar toolbar;
    protected String fragmentTitle = "";
    private boolean allowBackPressed= true;
    private boolean loading = false;
    private ProgressDialog progressDialog;
    private DataHelper dataHelper;

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
    }

    @Override
    public void onPause() {
        super.onPause();
        tearDownReferences();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tearDownReferences();
    }

    @Override
    public void onStop () {
        super.onStop();
        tearDownReferences();
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error) {

        setLoading(false);
        handleAndDisplayError(error);

    }
    
    public void registerBus(){
        if(!busRegistered){
            BusProvider.register(this);
            busRegistered = true;
        }
    }
    
    public void unregisterBus(){
        if(busRegistered)
        {
            BusProvider.unregister(this);
            busRegistered =false;
        }
    }
    
    protected void initializeFields(){
        
        setFragmentTitle(getString(R.string.toolbar_placeholder));
    }

    protected void initializeViews(View view){

       setUpToolBar(view);
        
    }

    protected MitooActivity getMitooActivity() {
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
                displayText(getString(R.string.error_401));
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
            displayText(getString(R.string.toast_network_error));

    }

    protected void handleLocationServices() {
        if (!getMitooActivity().LocationServicesIsOn()) {
            buildLocationServicePrompt();
        }
    }



    protected void fireFragmentChangeAction(int fragmentId) {
        
        MitooEnum.fragmentTransition transition = MitooEnum.fragmentTransition.PUSH;
        if(fragmentId == R.id.fragment_home){
            transition = MitooEnum.fragmentTransition.CHANGE;
        }
        FragmentChangeEvent event = new FragmentChangeEvent(this, transition , fragmentId );
        BusProvider.post(event);
    }

    protected void fireFragmentChangeAction(int fragmentId , Bundle bundle) {
        
        MitooEnum.fragmentTransition transition = MitooEnum.fragmentTransition.PUSH;
        if(fragmentId == R.id.fragment_home){
            transition = MitooEnum.fragmentTransition.CHANGE;
        }
        FragmentChangeEvent event = new FragmentChangeEvent(this, transition, fragmentId , bundle);
        BusProvider.post(event);
    }


    protected void fireFragmentChangeAction(MitooEnum.fragmentTransition transition) {

        FragmentChangeEvent event = new FragmentChangeEvent(this,transition);
        BusProvider.post(event);
    }
    //Buggy don't use
    protected void popFragmentAction(){

        getMitooActivity().popFragment();
        
    }

    protected void displayText(String text) {

        removeToast();
        View toastLayout = createToastView();
        createTextForToast(toastLayout,text);
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastLayout);
        toast.show();
        currentToast=toast;
    }
    
    private View createToastView(){

        View layout = getActivity().getLayoutInflater().inflate(R.layout.view_toast,
                (ViewGroup) getActivity().findViewById(R.id.toast_layout_root));
        return layout;
    }
    
    private void createTextForToast(View layout, String text){

        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(text);
        
    }

    protected void buildLocationServicePrompt() {

        buildPrompt(getString(R.string.prompt_location_services_title),
                getString(R.string.prompt_location_services_message),
                getString(R.string.prompt_yes),
                getString(R.string.prompt_no),
                new LocationServicesPromptOnclickListener(true, getActivity()),
                new LocationServicesPromptOnclickListener(false,getActivity()));

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

    private void handleCallBacks(){
        if(handler!=null && runnable != null){
            handler.removeCallbacks(runnable);
        }
        
    }

    public List<String> buiildStringList(int arrayID){

        List<String> returnList = new ArrayList<String>();
        String[] arrayOfString = getResources().getStringArray(arrayID);
        for(String item : arrayOfString){
            returnList.add(item);
        }
        return returnList;

    }
    
    protected <T> void setUpListView(ArrayAdapter<T> adapter, ListView listView,AdapterView.OnItemClickListener listener){
        
        listView.setOnItemClickListener(listener);
        listView.setAdapter(adapter);
        
    }
    
    public void removeToast() {

        if(currentToast!=null)
        {
            currentToast.cancel();
        }
        currentToast=null;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    public void tearDownReferences(){

        handleCallBacks();
        removeToast();
        unregisterBus();
        setLoading(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    protected void setUpToolBar(View view) {

        toolbar = (Toolbar)view.findViewById(R.id.app_bar);
        if(toolbar!=null){

            toolbar.setNavigationIcon(R.drawable.header_back_icon);
            toolbar.setTitle(getFragmentTitle());
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MitooFragment.this.getMitooActivity().onBackPressed();
                }
            });

        }

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
    
    protected MitooModel getMitooModel(Class<?> classType){
        
        if(classType == UserInfoModel.class)
            return getMitooActivity().getModelManager().getUserInfoModel();
        else if(classType == LeagueModel.class)
            return getMitooActivity().getModelManager().getLeagueModel();
        else if(classType == SessionModel.class)
            return getMitooActivity().getModelManager().getSessionModel();
        else if(classType == LocationModel.class)
            return getMitooActivity().getModelManager().getLocationModel();
        else
            return null;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        if(this.loading){
            displayProgressDialog();
        }else{
            cancelProgressDialog();
        }
    }
    
    private void displayProgressDialog(){
        
        ProgressDialog dialog = getProgressDialog();
        if(!dialog.isShowing())
            dialog.show();
    
    }

    private void cancelProgressDialog(){

        ProgressDialog dialog = getProgressDialog();
        if(dialog.isShowing())
            dialog.dismiss();
        
    }
    
    private void iniializeDialog(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getString(R.string.dialog_loading_title));
        progressDialog.setMessage(getString(R.string.dialog_loading_message));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
    }

    public ProgressDialog getProgressDialog() {
        if(this.progressDialog== null)
            iniializeDialog();
        return progressDialog;
    }


    public DataHelper getDataHelper() {
        if(dataHelper ==null)
            setDataHelper(new DataHelper(getActivity()));
        return dataHelper;
    }

    public void setDataHelper(DataHelper dataHelper) {
        this.dataHelper = dataHelper;
    }

    protected SessionModel getSessionModel(){

        return (SessionModel) getMitooModel(SessionModel.class);
    }

    protected LeagueModel getLeagueModel(){

        return (LeagueModel) getMitooModel(LeagueModel.class);
    }
}



