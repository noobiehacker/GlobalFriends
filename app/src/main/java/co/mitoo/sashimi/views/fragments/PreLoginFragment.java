package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;

/**
 * Created by david on 15-05-04.
 */
public class PreLoginFragment extends MitooFragment{

    private EditText identifierText;

    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable(v.getId())) {
            switch (v.getId()) {
                case R.id.loginButton:
                    loginButtonAction();
                    break;
            }
        }
    }

    public static PreLoginFragment newInstance() {
        return new PreLoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_pre_login,
                container, false);
        initializeFields();
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeOnClickListeners(View view){
        super.initializeOnClickListeners(view);
        view.findViewById(R.id.loginButton).setOnClickListener(this);
    }

    @Override
    public void onResume(){

        super.onResume();
        requestFocusForTopInput(this.identifierText);

    }

    @Override
    public void initializeViews(View view){
        super.initializeViews(view);
        this.identifierText = (EditText) view.findViewById(R.id.identifierInput);

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){

        super.onError(error);
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();
        setFragmentTitle(getString(R.string.tool_bar_login));
    }

    protected void handleHttpErrors(int statusCode) {

        /*
        if(statusCode == 404 ){
            String errorMessage = getDataHelper().getResetPageBadEmailMessage(getEmail());
            displayTextWithToast(errorMessage);
        }else{
            super.handleHttpErrors(statusCode);
        }*/

    }

    private void loginButtonAction(){

        boolean confirm = true;
        if(confirm)
            routeToPreConfirm();
        else
            routeToLogin();
    }

    private void routeToLogin(){

        FragmentChangeEvent event = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_login)
                .build();
        postFragmentChangeEvent(event);

    }

    private void routeToPreConfirm(){
        FragmentChangeEvent event = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_pre_confirm)
                .build();
        postFragmentChangeEvent(event);

    }

    private String getIdentifier(){
        return this.identifierText.getText().toString();
    }


}
