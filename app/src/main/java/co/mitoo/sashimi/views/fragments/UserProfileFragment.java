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
public class UserProfileFragment extends MitooFragment {

    //Top List variables
    ListView topList;
    UserProfileInfoAdapter topListAdapter;
    List<String> topListData;
    ListViewOnClickLIstener toplistOnItemClickListner;

    //Bottom List variables
    ListView bottomList;
    List<String> bottomListData;
    UserProfileInfoAdapter bottomListAdapter;
    ListViewOnClickLIstener bottomlistOnItemClickListner;

    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  getActivity().getLayoutInflater().inflate(R.layout.fragment_user_profile,
                container, false);
        initializeFields();
        initializeViews(view);
        return view;
    }

    
    @Override
    public void onClick(View v) {

    }

    private void initializeFields(){

        topListData = buiildStringList(R.array.user_profile_top_list);
        bottomListData = buiildStringList(R.array.user_profile_bottom_list);

    }
    
    private void initializeViews(View view){

        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   
        topList = (ListView) view.findViewById( R.id.userProfileTopList);
        topListAdapter = new UserProfileInfoAdapter(getActivity(),R.id.userProfileTopList, topListData);
        toplistOnItemClickListner = new ListViewOnClickLIstener(topList.getId());
        setUpListView(topListAdapter, topList, toplistOnItemClickListner );

        bottomList = (ListView) view.findViewById( R.id.userProfileBottomList);
        bottomListAdapter = new UserProfileInfoAdapter(getActivity(),R.id.userProfileBottomList, bottomListData);
        bottomlistOnItemClickListner = new ListViewOnClickLIstener(topList.getId());
        setUpListView(bottomListAdapter, bottomList, bottomlistOnItemClickListner);

    }
}
