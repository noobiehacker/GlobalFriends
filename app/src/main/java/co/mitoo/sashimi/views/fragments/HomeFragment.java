package co.mitoo.sashimi.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;

/**
 * Created by david on 15-01-12.
 */
public class HomeFragment extends MitooFragment {
    @Override
    public void onClick(View v) {

    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  getActivity().getLayoutInflater().inflate(R.layout.fragment_home,
                container, false);
        initializeOnClickListeners(view);
        initializeFields();
        initializeViews(view);
        return view;
    }

    @Override
    protected void initializeFields(){

        super.initializeFields();

    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    private void initializeOnClickListeners(View view){

    }

    @Override
    protected void setUpToolBar(View view) {

        toolbar = (Toolbar)view.findViewById(R.id.app_bar);
        if(toolbar!=null){

            toolbar.setLogo(R.drawable.header_mitoo_logo);
            toolbar.setTitle("");
            toolbar.inflateMenu(R.menu.menu_main);

        }
        /*
        ActionBarActivity activity = (ActionBarActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        */

    }
}




