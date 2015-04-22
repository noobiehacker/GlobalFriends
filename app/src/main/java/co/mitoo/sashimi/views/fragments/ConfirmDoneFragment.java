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
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.MobileTokenEventResponse;

/**
 * Created by david on 15-03-23.
 */
public class ConfirmDoneFragment extends MitooFragment {

    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable(v.getId())){
            switch (v.getId()) {
                case R.id.viewMyLeaguesButton:
                    viewMyLeagueButtonAction();
                    break;
            }
        }
    }

    public static ConfirmDoneFragment newInstance() {
        ConfirmDoneFragment fragment = new ConfirmDoneFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_confirm_done,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        Competition competition = getCompetitionModel().getSelectedCompetition();
        getViewHelper().setUpConfirmDoneView(view, competition);
        setUpPasswordAdviceText(view);
        setUpButtonColor(view);

    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
        setAllowBackPressed(false);
        setFragmentTitle(getString(R.string.tool_bar_confirmation));
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){

        super.onError(error);
    }

    @Override
    protected void initializeOnClickListeners(View view) {

        view.findViewById(R.id.viewMyLeaguesButton).setOnClickListener(this);

    }

    public void setUpPasswordAdviceText(View view){

        TextView greetingTextView = (TextView) view.findViewById(R.id.confirmDoneBottomText);
        greetingTextView.setText(createPasswordText());

    }

    private void viewMyLeagueButtonAction(){

        getMobileTokenModel().requestDeviceTokenAssociation(getUserID(), true);

    }

    @Subscribe
    public void onMobileTokenModelResponse(MobileTokenEventResponse event){

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.bundle_key_from_confirm), getString(R.string.bundle_value_true));
        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_home)
                .setTransition(MitooEnum.FragmentTransition.CHANGE)
                .setAnimation(MitooEnum.FragmentAnimation.VERTICAL)
                .setBundle(bundle)
                .build();
        postFragmentChangeEvent(fragmentChangeEvent);

    }

    private String createPasswordText(){

        String identifierUsed = getConfirmInfoModel().getConfirmInfo().getIdentifier_used();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.confirmation_page_text_six));
        stringBuilder.append(identifierUsed);
        stringBuilder.append(getString(R.string.confirmation_page_text_seven));
        return stringBuilder.toString();

    }

    private void setUpButtonColor(View view){
        Button button = (Button) view.findViewById(R.id.viewMyLeaguesButton);
        Competition selectedCompetition = getCompetitionModel().getSelectedCompetition();
        String leagueColor = selectedCompetition.getLeague().getColor_1();
        getViewHelper().setViewBackgroundDrawableColor(button, leagueColor);
    }

}