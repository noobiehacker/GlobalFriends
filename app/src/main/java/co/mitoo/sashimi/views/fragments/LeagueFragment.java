package co.mitoo.sashimi.views.fragments;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.SessionModel;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.ViewHelper;
import co.mitoo.sashimi.utils.events.LeagueModelEnquireRequestEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquiresResponseEvent;
/**
 * Created by david on 14-12-19.
 */

public class LeagueFragment extends MitooFragment {

    private String leagueTitle;
    private League selectedLeague;
    private TextView leagueDetailsTextView;
    private TextView readMoreTextView;
    private Button interestedButton;
    private View disabledButtonView;
    
    public static LeagueFragment newInstance() {
        LeagueFragment fragment = new LeagueFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_league,
                        container, false);
        initializeFields();
        initializeViews(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.googleMapFragment);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
    }

    @Subscribe
    public void onLeagueEnquireResponse(LeagueModelEnquiresResponseEvent event) {

        getInterestedButton().setVisibility(View.GONE);
        getDisabledButtonView().setVisibility(View.VISIBLE);
        setLoading(false);

    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
        setFragmentTitle(getSelectedLeague().getName());
    }

    @Override
    protected void initializeViews(View view){
        
        super.initializeViews(view);
        initializeOnClickListeners(view);
        setUpMap();
        setUpLeagueView(view);
        setUpLeagueDetailsText(view, getSelectedLeague());
    }
    
    private void setUpLeagueView(View view){
        
        getViewHelper().setUpFullLeagueText(view, getSelectedLeague(), getViewType());
        getViewHelper().setLineColor(view, getSelectedLeague());
        getViewHelper().setUpCheckBox(view, getSelectedLeague());
        getViewHelper().setUpLeagueImage(view,getSelectedLeague(),getViewType());
        setUpInterestedButton(view , getViewHelper());

    }
    
    @Override
    protected void initializeOnClickListeners(View view){
        view.findViewById(R.id.interestedButton).setOnClickListener(this);
        view.findViewById(R.id.read_more_text_view).setOnClickListener(this);
        super.initializeOnClickListeners(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.interestedButton:
                joinButtonAction();
                break;
            case R.id.read_more_text_view:
                readMoreAction();
                break;
        }
    }
    
    private void setUpMap(){

        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.googleMapFragment)).getMap();
        getViewHelper().setUpMap(getSelectedLeague(), map);

    }
    
    private void setUpInterestedButton(View view , ViewHelper viewHelper){
        
        setInterestedButton((Button) view.findViewById(R.id.interestedButton));
        setDisabledButtonView(view.findViewById(R.id.disabledInterestedView));
        if(getLeagueModel().selectedLeagueIsJoinable()){
            viewHelper.setViewColor(getInterestedButton(), getSelectedLeague().getColor_1());
        }else{
            
            getInterestedButton().setVisibility(View.GONE);
            getDisabledButtonView().setVisibility(View.VISIBLE);
        }

    }

    public String getLeagueTitle() {
        return leagueTitle;
    }

    public void setLeagueTitle(String leagueTitle) {
        this.leagueTitle = leagueTitle;
    }

    private void setUpLeagueDetailsText(View view, League league){

        setReadMoreTextView((TextView) view.findViewById(R.id.read_more_text_view));
        getViewHelper().setTextViewColor(getReadMoreTextView(), league.getColor_1());
        setLeagueDetailsTextView ((TextView)view.findViewById(R.id.league_join_details));
        getLeagueDetailsTextView().setText(getTruncatedAbout(league));

    }
    
    public League getSelectedLeague() {
        if(selectedLeague==null){
            setSelectedLeague(getRetriever().getLeagueModel().getSelectedLeague());
        }
        return selectedLeague;
    }

    public void setSelectedLeague(League selectedLeague) {
        this.selectedLeague = selectedLeague;
    }

    private MitooEnum.ViewType getViewType(){

        return MitooEnum.ViewType.FRAGMENT;

    }


    private void joinButtonAction(){

        setLoading(true);
        SessionModel sessionModel =getSessionModel();
        if(sessionModel.userIsLoggedIn()){
            int UserID = sessionModel.getSession().id;
            LeagueModelEnquireRequestEvent requestEvent = new LeagueModelEnquireRequestEvent(UserID, MitooEnum.crud.CREATE);
            getLeagueModel().requestLeagueEnquire(requestEvent);
        }
        else{
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.bundle_key_league_object_id),String.valueOf(getSelectedLeague().getId()));
            fireFragmentChangeAction(R.id.fragment_sign_up, bundle);
        }
    }

    private void readMoreAction(){
        
        expandLeagueDetailsTextView();
        hideReadMoreTextView();
        
    }

    private void expandLeagueDetailsTextView(){
        
        if(getLeagueDetailsTextView()!=null){
            getLeagueDetailsTextView().setText(getSelectedLeague().getAbout());
          //  getViewHelper().updateLeagueDetailsPadding(getLeagueDetailsTextView());
        }
        getViewHelper().setViewVisibility(getReadMoreTextView(),false);
        
    }

    private void hideReadMoreTextView(){
        if(getReadMoreTextView()!=null)
            getReadMoreTextView().setVisibility(View.GONE);

    }
    public TextView getLeagueDetailsTextView() {
        return leagueDetailsTextView;
    }

    public void setLeagueDetailsTextView(TextView leagueDetailsTextView) {
        this.leagueDetailsTextView = leagueDetailsTextView;
    }

    public TextView getReadMoreTextView() {
        return readMoreTextView;
    }

    public void setReadMoreTextView(TextView readMoreTextView) {
        this.readMoreTextView = readMoreTextView;
    }

    private String getTruncatedAbout(League league){
        if(league!=null && league.getAbout().length()> 100)
            return league.getAbout().substring(0, 99) + "...";
        else
            return league.getAbout();

    }

    public View getDisabledButtonView() {
        return disabledButtonView;
    }

    public void setDisabledButtonView(View disabledButtonView) {
        this.disabledButtonView = disabledButtonView;
    }

    public Button getInterestedButton() {
        return interestedButton;
    }

    public void setInterestedButton(Button interestedButton) {
        this.interestedButton = interestedButton;
    }
}
