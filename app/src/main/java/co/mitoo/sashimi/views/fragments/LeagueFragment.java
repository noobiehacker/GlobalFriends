package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.mitoo.sashimi.R;

/**
 * Created by david on 14-12-19.
 */
public class LeagueFragment extends MitooFragment {

    public static LeagueFragment newInstance() {
        LeagueFragment fragment = new LeagueFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_league,
                container, false);
        initializeViewElements(view);
        initializeFields();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private void joinAsTeamAction(){
        fireFragmentChangeAction(R.id.fragment_join);
    }

    private void joinAsPlayerAction(){
        fireFragmentChangeAction(R.id.fragment_join);
    }

    private void initializeFields(){
    }

    private void initializeViewElements(View view){
        //Work around for the animation to display a gray background during load
        initializeOnClickListeners(view);
    }

    private void initializeOnClickListeners(View view){
        view.findViewById(R.id.joinAsPlayerButton).setOnClickListener(this);
        view.findViewById(R.id.joinAsTeamButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.joinAsTeamButton:
                 joinAsTeamAction();
                break;
            case R.id.joinAsPlayerButton:
                joinAsPlayerAction();
                break;
        }
    }
}
