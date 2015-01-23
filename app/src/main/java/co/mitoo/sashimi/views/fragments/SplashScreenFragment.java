package co.mitoo.sashimi.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquireRequestEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquiresResponseEvent;
import co.mitoo.sashimi.utils.events.SessionModelResponseEvent;
import co.mitoo.sashimi.utils.listener.FragmentChangeListener;

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
    public void sessionModeldResponse(SessionModelResponseEvent event) {

        final SessionModelResponseEvent eventToPassIn = event;
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                if (eventToPassIn.getSession() != null) {
                    getMitooActivity().updateAuthToken(eventToPassIn.getSession());
                    BusProvider.post(new LeagueModelEnquireRequestEvent(eventToPassIn.getSession().id, MitooEnum.crud.READ));

                } else {
                    fireFragmentChangeAction(R.id.fragment_landing);

                }
            }
        }, 1000);

    }

    @Subscribe
    public void onLeagueEnquireResponse(LeagueModelEnquiresResponseEvent event) {

        fireFragmentChangeAction(R.id.fragment_home);

    }

}
