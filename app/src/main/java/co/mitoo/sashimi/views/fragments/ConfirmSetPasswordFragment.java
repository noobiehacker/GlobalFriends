package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.squareup.otto.Subscribe;
import java.util.TimeZone;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.models.jsonPojo.recieve.UserInfoRecieve;
import co.mitoo.sashimi.models.jsonPojo.send.JsonSignUpSend;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.UserInfoModelResponseEvent;
import co.mitoo.sashimi.views.Dialog.FeedBackDialogBuilder;

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

    public static ConfirmSetPasswordFragment newInstance() {
        ConfirmSetPasswordFragment fragment = new ConfirmSetPasswordFragment();
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
        Competition competition = getCompetitionModel().getSelectedCompetition();
        getViewHelper().setUpConfirmPasswordView(view, competition);

    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
        setFragmentTitle(getString(R.string.tool_bar_confirm_your_account));
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
            getFormHelper().handleInvalidPassword(getPassword());
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

    @Subscribe
    public void onUserInfoReceieve(UserInfoModelResponseEvent event) {

        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingleTonInstance()
                .setFragmentID(R.id.fragment_confirm_done)
                .setTransition(MitooEnum.FragmentTransition.PUSH)
                .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                .build();
        postFragmentChangeEvent(fragmentChangeEvent);

    }
}
