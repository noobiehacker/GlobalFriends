package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.squareup.otto.Subscribe;
import java.util.TimeZone;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.recieve.UserInfoRecieve;
import co.mitoo.sashimi.models.jsonPojo.send.JsonSignUpSend;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;

/**
 * Created by david on 15-03-23.
 */
public class ConfirmSetPasswordFragment extends MitooFragment {

    private EditText passwordTextField;

    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable()){
            switch (v.getId()) {
                case R.id.setPasswordButton:
                    setPasswordButtonAction();
                    break;
            }
        }
    }

    public static ConfirmAccountFragment newInstance() {
        ConfirmAccountFragment fragment = new ConfirmAccountFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_confirm_set_password,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        view.findViewById(R.id.setPasswordButton).setOnClickListener(this);

    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){

        super.onError(error);
    }

    private String getPassword(){
        return this.getTextFromTextField(R.id.passwordInput);
    }

    private void setPasswordButtonAction(){

        if(getPassword().equals("")){
            displayText(getString(R.string.toast_password_required));
        }
        else if(!getFormHelper().validPassword(getPassword())){
            getFormHelper().handleInvalidEmail(getPassword());
        }
        else{
            JsonSignUpSend confirmJson = createConfirmJsonFrom();
            String token = getSessionModel().getInvitation_token().getToken();
            getUserInfoModel().requestToConfirmUser(token , confirmJson);
            setLoading(true);

        }
    }

    private String getTimeZone() {
        return TimeZone.getDefault().getDisplayName();
    }

    private JsonSignUpSend createConfirmJsonFrom() {

        UserInfoRecieve userInfo = getUserInfoModel().getUserInfoRecieve();
        return new JsonSignUpSend(userInfo.email, getPassword(), userInfo.name, userInfo.phone, getTimeZone());
    }
}
