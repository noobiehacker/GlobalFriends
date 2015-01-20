package co.mitoo.sashimi.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.listener.ListViewOnClickLIstener;
import co.mitoo.sashimi.views.adapters.UserProfileInfoAdapter;

/**
 * Created by david on 15-01-12.
 */
public class UserDetailsFragment extends MitooFragment {

    //Top List variables
    ListView detailsList;
    UserProfileInfoAdapter detailsListAdapter;
    List<String> detailsListData;
    ListViewOnClickLIstener detailsListOnItemClickListner;

    public static UserDetailsFragment newInstance() {
        UserDetailsFragment fragment = new UserDetailsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  getActivity().getLayoutInflater().inflate(R.layout.fragment_user_details,
                container, false);
        initializeFields();
        initializeViews(view);
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initializeFields(){

        detailsListData = buiildStringList(R.array.user_profile_top_list);

    }

    @Override
    protected void initializeViews(View view){

        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        detailsList = (ListView)view.findViewById(R.id.user_profile_details);
        detailsListOnItemClickListner = new ListViewOnClickLIstener(detailsList.getId());
        detailsListAdapter = new UserProfileInfoAdapter(getActivity(),R.id.user_profile_details ,detailsListData);
        setUpListView(detailsListAdapter, detailsList, detailsListOnItemClickListner );

    }
}
