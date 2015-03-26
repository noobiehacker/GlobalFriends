package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;

/**
 * Created by david on 15-03-17.
 */
public class ConfirmAccountFragment extends MitooFragment {

    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable()){
            switch (v.getId()) {
                case R.id.confirmJoinButton:
                    confirmButtonAction();
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
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_confirm_account,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        Competition competition = getCompetitionModel().getSelectedCompetition();
        getViewHelper().setUpConfirmAccountView(view, competition);
        setUpConfirmLeagueText(view);
        setUpGreetingText(view);
        setUpButtonColor(view);
    }


    public void setUpConfirmLeagueText(View view){

        if(getCompetitionModel().getMyCompetition().size()==1){
            Competition selectedCompetition = getCompetitionModel().getSelectedCompetition();
            TextView competitionTextView = (TextView) view.findViewById(R.id.confirmAccountCompetitionName);
            competitionTextView.setText(selectedCompetition.getName());
        }

    }

    public void setUpGreetingText(View view){

        TextView greetingTextView = (TextView) view.findViewById(R.id.confirmAccountTopText);
        greetingTextView.setText(createGreetingText());

    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
        setAllowBackPressed(false);
        setFragmentTitle(getString(R.string.tool_bar_confirm_your_account));

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){

        super.onError(error);
    }

    @Override
    protected void initializeOnClickListeners(View view) {

        view.findViewById(R.id.confirmJoinButton).setOnClickListener(this);

    }

    private void confirmButtonAction(){

        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingleTonInstance()
                .setFragmentID(R.id.fragment_confirm_set_password)
                .setTransition(MitooEnum.FragmentTransition.PUSH)
                .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                .build();
        postFragmentChangeEvent(fragmentChangeEvent);

    }

    private String createGreetingText(){
        String userName = getUserInfoModel().getUserInfoRecieve().name;
        return userName + getString(R.string.confirmation_page_text_one);
    }

    private void setUpButtonColor(View view){
        Button button = (Button) view.findViewById(R.id.confirmJoinButton);
        Competition selectedCompetition = getCompetitionModel().getSelectedCompetition();
        String leagueColor = selectedCompetition.getLeague().getColor_1();
        getViewHelper().setViewBackgroundDrawableColor(button, leagueColor);
    }

}
