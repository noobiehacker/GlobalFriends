package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.SessionModel;
import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.LeagueModelEnquireRequestEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquiresResponseEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.ModelPersistedDataDeletedEvent;
import co.mitoo.sashimi.utils.events.ModelPersistedDataLoadedEvent;

/**
 * Created by david on 14-11-05.
 */
public class SplashScreenFragment extends MitooFragment {
    @Override
    public void onClick(View v) {

    }

    public static SplashScreenFragment newInstance() {
        SplashScreenFragment fragment = new SplashScreenFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  getActivity().getLayoutInflater().inflate(R.layout.fragment_splash,
                container, false);
        return view;
    }

    @Subscribe
    public void onModelPersistedDataLoaded(ModelPersistedDataLoadedEvent event) {
        
        loadFirstFragment();

    }

    @Subscribe
    public void onModelPersistedDataDeleted(ModelPersistedDataDeletedEvent event) {

        loadFirstFragment();

    }
    
    @Subscribe
    public void onLeagueEnquireResponse(LeagueModelEnquiresResponseEvent event) {

        fireFragmentChangeAction(R.id.fragment_home, MitooEnum.fragmentTransition.CHANGE);

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        
        super.onError(error);
    }

    private void loadFirstFragment(){
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                SessionRecieve session = getSessionModel().getSession();
                if (session != null) {
                    getMitooActivity().updateAuthToken(session);
                    getLeagueModel().requestLeagueEnquire(new LeagueModelEnquireRequestEvent(session.id, MitooEnum.crud.READ));

                } else {
                    fireFragmentChangeAction(R.id.fragment_landing, MitooEnum.fragmentTransition.CHANGE);

                }
            }
        }, 1000);
        
    }


}
