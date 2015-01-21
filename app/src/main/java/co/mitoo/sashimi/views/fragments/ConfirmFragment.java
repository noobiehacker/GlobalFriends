package co.mitoo.sashimi.views.fragments;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
/**
 * Created by david on 15-01-19.
 */
public class ConfirmFragment extends MitooFragment {


    public static ConfirmFragment newInstance() {

        return new ConfirmFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceStaste) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_confirm,
                container, false);
        initializeOnClickListeners(view);
        initializeFields(savedInstanceStaste);
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    private void initializeFields(Bundle savedInstanceState){

        super.initializeFields();
        setFragmentTitle(getString(R.string.tool_bar_confirmation));
    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
    }

    private void initializeOnClickListeners(View view){
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
        handleAndDisplayError(error);
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

}
