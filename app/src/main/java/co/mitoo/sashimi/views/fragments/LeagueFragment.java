package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.network.Services.SessionService;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.LeagueViewHelper;
import co.mitoo.sashimi.utils.ViewHelper;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.LeagueRequestFromIDEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquireRequestEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquiresResponseEvent;
import co.mitoo.sashimi.utils.events.LeagueResponseFromIDEvent;

/**
 * Created by david on 14-12-19.
 */

public class LeagueFragment extends MitooFragment {

    private LeagueModel leagueModel;
    private TextView leagueDetailsTextView;
    private TextView readMoreTextView;
    private Button interestedButton;
    private View disabledButtonView;
    private int leagueID;
    private boolean viewLoaded;
    private RelativeLayout holder;
    private GoogleMap map ;

    public static LeagueFragment newInstance() {
        LeagueFragment fragment = new LeagueFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            this.leagueID = savedInstanceState.getInt(getLeagueIDKey());
        }else{
            this.leagueID =  getArguments().getInt(getLeagueIDKey());
        }

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(getLeagueIDKey(), this.leagueID);
        super.onSaveInstanceState(bundle);

    }

    @Override
    public void onResume(){
        super.onResume();
        BusProvider.post(new LeagueRequestFromIDEvent(this.leagueID));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_league,
                        container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    public void onPause(){
        MapFragment f = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.googleMapFragment);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
    
    @Subscribe
    public void onLeagueResponse(LeagueResponseFromIDEvent event) {

        if(event.getLeagueModel()!=null){
            this.leagueModel =event.getLeagueModel();
            updateView();
        }

    }

    @Subscribe
    public void onLeagueEnquireResponse(LeagueModelEnquiresResponseEvent event) {

        this.interestedButton.setVisibility(View.GONE);
        this.disabledButtonView.setVisibility(View.VISIBLE);
        setLoading(false);

    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
        if(this.leagueModel!=null)
            setFragmentTitle(this.leagueModel.getLeague().getName());
    }

    @Override
    protected void initializeViews(View view){
        
        super.initializeViews(view);
        this.holder = (RelativeLayout)view.findViewById(R.id.league_image_holder);
        this.interestedButton = (Button)view.findViewById(R.id.interestedButton);
        this.disabledButtonView = view.findViewById(R.id.disabledInterestedView);
        this.readMoreTextView= (TextView) view.findViewById(R.id.read_more_text_view);
        this.leagueDetailsTextView =(TextView)view.findViewById(R.id.league_join_details);
        this.map=((MapFragment) getFragmentManager().findFragmentById(R.id.googleMapFragment)).getMap();
        this.viewLoaded= true;
        updateView();

    }

    private void updateView(){

        if(this.leagueModel!=null && this.viewLoaded){

            League league = this.leagueModel.getLeague();
            setUpLeagueHeaderView();
            setUpInterestedButton(getViewHelper());
            setUpLeagueDetailsText(league);
            setFragmentTitle(league.getName());
            getToolbar().setTitle(getFragmentTitle());
            getViewHelper().setUpMap(league, this.map);

        }

    }
    
    private void setUpLeagueHeaderView(){

        LeagueViewHelper leagueViewHelper = getViewHelper().getLeagueViewHelper();
        RelativeLayout leagueView = leagueViewHelper.createLeagueResult(this.leagueModel, holder);
        this.holder.addView(leagueView);
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

    private void setUpInterestedButton(ViewHelper viewHelper){

        if(this.leagueModel.isLeagueIsJoinable()){
            viewHelper.setViewBackgroundDrawableColor(getInterestedButton(), this.leagueModel.getLeague().getColor_1());
        }else{
            getInterestedButton().setVisibility(View.GONE);
            getDisabledButtonView().setVisibility(View.VISIBLE);
        }

    }

    private void setUpLeagueDetailsText(League league){

        getViewHelper().setTextViewTextColor(getReadMoreTextView(), league.getColor_1());
        getLeagueDetailsTextView().setText(getTruncatedAbout(league));

    }

    private void joinButtonAction(){

        SessionService sessionModel =getSessionModel();
        if(sessionModel.userIsLoggedIn()){
            setLoading(true);
            BusProvider.post(new LeagueModelEnquireRequestEvent(getUserID()));
        }
        else{
            Bundle bundle = new Bundle();
            bundle.putInt(getLeagueIDKey(), this.leagueID);
            FragmentChangeEvent event = FragmentChangeEventBuilder.getSingletonInstance()
                    .setFragmentID(R.id.fragment_sign_up)
                    .setBundle(bundle)
                    .build();
            BusProvider.post(event);
        }
    }

    private void readMoreAction(){
        
        expandLeagueDetailsTextView();
        hideReadMoreTextView();
        
    }

    private void expandLeagueDetailsTextView(){
        
        if(getLeagueDetailsTextView()!=null){
            getLeagueDetailsTextView().setText(this.leagueModel.getLeague().getAbout());
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

    public TextView getReadMoreTextView() {
        return readMoreTextView;
    }

    private String getTruncatedAbout(League league){

        if(league!=null && league.getAbout()!=null&& league.getAbout().length()> 100)
            return league.getAbout().substring(0, 99) + "...";
        else
            return league.getAbout();

    }

    public View getDisabledButtonView() {
        return disabledButtonView;
    }

    public Button getInterestedButton() {
        return interestedButton;
    }

}