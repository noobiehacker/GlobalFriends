package co.mitoo.sashimi.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.view.View;
import android.view.ViewGroup;

import co.mitoo.sashimi.R;
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
}
