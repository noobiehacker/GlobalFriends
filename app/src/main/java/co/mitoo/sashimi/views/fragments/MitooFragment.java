package co.mitoo.sashimi.views.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.otto.Bus;

import java.util.ArrayList;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.listener.LocationServicesPromptOnclickListener;
import co.mitoo.sashimi.views.activities.MitooActivity;
import retrofit.RetrofitError;

/**
 * Created by david on 14-11-13.
 */
public abstract class MitooFragment extends Fragment implements View.OnClickListener {

    protected Bus bus;
    private ArrayList<Toast> toasts;
    private Toast currentToast;
    private Handler handler;
    private Runnable runnable;

    protected String getTextFromTextField(int textFieldId) {
        EditText textField = (EditText) getActivity().findViewById(textFieldId);
        return textField.getText().toString();
    }

    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        BusProvider.register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        handleNetwork();
        BusProvider.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        handleCallBacks();
        BusProvider.unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handleCallBacks();
        removeToast();
    }

    @Override
    public void onStop () {
        super.onStop();
        handleCallBacks();
        removeToast();
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

    //Animation
    protected void slideUpView(int id) {
        View view = getActivity().findViewById(id);
        view.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.SlideInUp)
                .duration(700)
                .playOn(view);
    }

    protected void slidDownView(int id) {
        View view = getActivity().findViewById(id);
        YoYo.with(Techniques.SlideOutDown)
                .duration(700)
                .playOn(view);
    }

    protected void fadeOutView(int id) {
        View view = getActivity().findViewById(id);
        view.setVisibility(View.GONE);
        YoYo.with(Techniques.FadeOut)
                .duration(700)
                .playOn(view);
    }

    protected void fadeInView(int id) {
        View view = getActivity().findViewById(id);
        view.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn)
                .duration(700)
                .playOn(view);
        view.setVisibility(View.VISIBLE);
    }

    protected void fireFragmentChangeAction(int fragmentId) {
        FragmentChangeEvent event = new FragmentChangeEvent(this, fragmentId);
        event.setPush(true);
        BusProvider.post(event);
    }
    
    protected void popFragmentAction(){

        FragmentManager fm = getActivity().getFragmentManager();
        fm.popBackStack();
        /*
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

            }
        }, 1000);*/
        
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
                new LocationServicesPromptOnclickListener(true),
                new LocationServicesPromptOnclickListener(false));

    }

    private void buildPrompt(String title, String message, String positiveMessage,
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
    
    public void removeToast() {

        if(currentToast!=null)
        {
            currentToast.cancel();
        }
        currentToast=null;

    }
}



