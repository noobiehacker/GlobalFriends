package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.listener.FragmentChangeListener;

/**
 * Created by david on 14-11-05.
 */

public class SearchFragment extends MitooProgessFragment{

    private FragmentChangeListener listner;

    public static SearchFragment newInstance(FragmentChangeListener listner) {
        SearchFragment fragment = new SearchFragment();
        fragment.listner=listner;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_search,
                container, false);
        contentView = view;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}