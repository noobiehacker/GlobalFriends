package co.mitoo.sashimi.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.views.Dialog.FeedBackDialogBuilder;

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
        FeedBackDialogBuilder dialog = new FeedBackDialogBuilder(getActivity());
        dialog.buildPrompt().show();
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
        ImageView checkBoxImage = (ImageView)view.findViewById(R.id.checkBoxImage);
        checkBoxImage.setVisibility(View.VISIBLE);
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
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    switch (menuItem.getItemId()){
                        case R.id.menu_feedback:

                            break;
                        case R.id.menu_settings:
                            fireFragmentChangeAction(R.id.fragment_settings);
                            break;
                    }
                    return false;
                }
            });
        }
        /*
        ActionBarActivity activity = (ActionBarActivity)getContext();
        activity.setSupportActionBar(toolbar);
        */

    }
}




