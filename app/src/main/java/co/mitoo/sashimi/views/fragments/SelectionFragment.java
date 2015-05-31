package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.mitoo.sashimi.R;

/**
 * Created by david on 15-05-30.
 */
public class SelectionFragment extends  MitooFragment {

    public static SelectionFragment newInstance() {
        SelectionFragment fragment = new SelectionFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_selection,
                container, false);
        initializeViews(view);
        initializeFields();
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initializeViews(View view){
        initializeOnClickListeners(view);

    }
}
