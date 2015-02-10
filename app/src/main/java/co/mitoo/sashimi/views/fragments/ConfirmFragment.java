package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.ViewHelper;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.views.Dialog.FeedBackDialogBuilder;
import android.os.Handler;
/**
 * Created by david on 15-01-19.
 */
public class ConfirmFragment extends MitooFragment {

    private League selectedLeague;
    
    public static ConfirmFragment newInstance() {

        return new ConfirmFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setAllowBackPressed(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceStaste) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_confirm,
                container, false);
        initializeOnClickListeners(view);
        initializeViews(view);
        return view;
    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
        setFragmentTitle(getString(R.string.tool_bar_confirmation));
    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        ViewHelper viewHelper = new ViewHelper(getMitooActivity());
        viewHelper.setUpConfirmView(view, getSelectedLeague());
        setUpPopUpTask();
    }

    @Override
    protected void initializeOnClickListeners(View view){
        view.findViewById(R.id.viewMyLeaguesButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewMyLeaguesButton:
                viewMyLeagueButtonAction();
            break;
        }
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        super.onError(error);
    }

    private void viewMyLeagueButtonAction(){
        
        fireFragmentChangeAction(R.id.fragment_home);
        
    }
    
    @Override
    protected void setUpToolBar(View view) {

        toolbar = (Toolbar)view.findViewById(R.id.app_bar);
        if(toolbar!=null){

            toolbar.setTitle(getFragmentTitle());
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        }
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

    
    private void setUpPopUpTask(){
        
        if(firstTimeUser()){
            Handler handler = getHandler();
            setRunnable( new Runnable() {
                @Override
                public void run() {
                    FeedBackDialogBuilder dialog = new FeedBackDialogBuilder(getActivity());
                    dialog.buildPrompt().show();
                }
            });
            handler.postDelayed(getRunnable(), 3000);

        }
        
    }
    
    private boolean firstTimeUser(){

        Boolean result= false;
        Bundle arguments = getArguments();
        if (arguments != null) {
            String value = (String) arguments.get(getString(R.string.bundle_key_first_time));
            if(value.equals(getString(R.string.bundle_value_true)))
                result=true;
            else
                result =false;
        }
        return result;
    }

    private MitooEnum.ViewType getViewType(){

        return MitooEnum.ViewType.FRAGMENT;

    }
}
