package co.mitoo.sashimi.views.fragments;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.RetriggerEmailResponseEvent;
import co.mitoo.sashimi.utils.events.RetriggerEmailSmsEvent;
import retrofit.RetrofitError;

/**
 * Created by david on 15-05-04.
 */
public class PreConfirmFragment extends MitooFragment {

    private TextView topText;
    private TextView bottomText;
    private int userID = MitooConstants.invalidConstant;
    private boolean dialogDisplayed =false;
    public static PreConfirmFragment newInstance() {
        return new PreConfirmFragment();
    }

    @Override
    public void onClick(View v) {
        if (getDataHelper().isClickable(v.getId())) {
            switch (v.getId()) {
                case R.id.preConfirmButton:
                    resendButtonAction();
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_pre_confirm,
                container, false);
        initializeFields();
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeOnClickListeners(View view) {
        super.initializeOnClickListeners(view);
        view.findViewById(R.id.preConfirmButton).setOnClickListener(this);

    }

    @Override
    public void onResume() {

        super.onResume();

    }

    @Override
    public void initializeViews(View view) {

        super.initializeViews(view);
        this.topText = (TextView) view.findViewById(R.id.preConfirmText1);
        this.topText.setText(createTopText());
        this.bottomText = (TextView) view.findViewById(R.id.preConfirmText2);
        this.bottomText.setText(createBottomText());

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error) {

        super.onError(error);

    }

    //TODO:REFACTOR
    @Override
    protected void handleHttpErrors(int statusCode) {

        if (statusCode == 409) {
            BusProvider.post(new RetriggerEmailResponseEvent(false));
        } else {
            super.handleHttpErrors(statusCode);

        }
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();
        setFragmentTitle(getString(R.string.tool_bar_pre_confirm));
    }

    private String getIdentifierType(){

        Bundle bundle = getArguments();
        String key = getString(R.string.bundle_key_identifier_type);
        String identifier = bundle.getString(key);
        return identifier;

    }

    private String createTopText(){

        String identifier = getIdentifierType();
        return getString(R.string.pre_confirm_page_text1) + identifier;
    }

    private String createBottomText(){

        String result = getString(R.string.pre_confirm_page_text2)+ getIdentifierType()+ getString(R.string.pre_confirm_page_text3);
        return result;

    }

    @Subscribe
    public void onRetriggerEventResponse(RetriggerEmailResponseEvent event){

        if(this.dialogDisplayed==false){

            if(event.isResponseSent()){
                displayTextWithDialog(
                        getString(R.string.pre_confirm_alert_success_title),
                        getString(R.string.pre_confirm_alert_success_message),
                        createAlertListener());
            }else{
                displayTextWithDialog(
                        getString(R.string.pre_confirm_alert_failure_title),
                        getString(R.string.pre_confirm_alert_failure_message),
                        createAlertListener());

            }
            this.dialogDisplayed=true;
        }

    }

    private void resendButtonAction(){
        BusProvider.post(new RetriggerEmailSmsEvent(Integer.toString(getUserID())));
    }

    @Override
    public int getUserID() {

        if(this.userID == MitooConstants.invalidConstant){
            Bundle bundle = getArguments();
            String key = getString(R.string.bundle_key_user_id);
            String value = bundle.getString(key);
            if(value!=null)
                this.userID = Integer.parseInt(bundle.getString(key));
        }
        return userID;
    }

    protected DialogInterface.OnClickListener createAlertListener(){

        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                PreConfirmFragment.this.dialogDisplayed =false;
                dialog.dismiss();
            }
        };
    }
}