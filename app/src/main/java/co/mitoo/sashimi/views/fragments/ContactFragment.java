package co.mitoo.sashimi.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;

/**
 * Created by david on 15-05-30.
 */
public class ContactFragment extends  MitooFragment {

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_contact,
                container, false);
        initializeViews(view);
        initializeFields();
        return view;
    }

    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable(v.getId())){
            switch (v.getId()) {
                case R.id.messngerImage:
                    Intent intent = new Intent("android.intent.category.LAUNCHER");
                    intent.setPackage("com.facebook.orca");
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    protected void initializeViews(View view){
        initializeOnClickListeners(view);

    }

    @Override
    protected void initializeOnClickListeners(View view){
        view.findViewById(R.id.messngerImage).setOnClickListener(this);
        //      view.findViewById(R.id.searchButton).setOnClickListener(this);

    }

}
