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
import co.mitoo.sashimi.views.adapters.StringListAdapter;

/**
 * Created by david on 15-01-12.
 */
public class SettingsFragment extends MitooFragment {

    //Top List variables
    ListView topList;
    StringListAdapter topListAdapter;
    List<String> topListData;
    ListViewOnClickLIstener toplistOnItemClickListner;

    //Bottom List variables
    ListView bottomList;
    List<String> bottomListData;
    StringListAdapter bottomListAdapter;
    ListViewOnClickLIstener bottomlistOnItemClickListner;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  getActivity().getLayoutInflater().inflate(R.layout.fragment_settings,
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

        setFragmentTitle(getString(R.string.tool_bar_settings));
        topListData = buiildStringList(R.array.settings_top_menu_list);
        bottomListData = buiildStringList(R.array.settings_bottom_menu_list);

    }
    
    @Override
    protected void initializeViews(View view){

        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   
        topList = (ListView) view.findViewById( R.id.userProfileTopList);
        topListAdapter = new StringListAdapter(getActivity(),R.id.userProfileTopList, topListData);
        toplistOnItemClickListner = new ListViewOnClickLIstener(topList.getId());
        setUpListView(topListAdapter, topList, toplistOnItemClickListner );

        bottomList = (ListView) view.findViewById( R.id.userProfileBottomList);
        bottomListAdapter = new StringListAdapter(getActivity(),R.id.userProfileBottomList, bottomListData);
        bottomlistOnItemClickListner = new ListViewOnClickLIstener(topList.getId());
        setUpListView(bottomListAdapter, bottomList, bottomlistOnItemClickListner);
        bottomListAdapter.notifyDataSetChanged();
        setUpToolBar(view);
    }
}
