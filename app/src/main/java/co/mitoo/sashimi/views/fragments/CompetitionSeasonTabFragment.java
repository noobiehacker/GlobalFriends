package co.mitoo.sashimi.views.fragments;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.FixtureModel;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.FixtureWrapper;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import co.mitoo.sashimi.views.adapters.FixtureListAdapter;

/**
 * Created by david on 15-03-08.
 */

public class CompetitionSeasonTabFragment extends MitooFragment {

    private LinearLayout fixtureTabContainerView;
    private MitooEnum.FixtureTabType tabType;
    private TextView noResultsTextView;
    private MitooActivity mitooActivity;

    private ListView fixtureListView;
    private FixtureListAdapter fixtureListAdapter;
    private List<FixtureWrapper> fixtureList;

    @Override
    public void onClick(View v) {
    }

    public static CompetitionSeasonTabFragment newInstance() {
        CompetitionSeasonTabFragment fragment = new CompetitionSeasonTabFragment();
        return fragment;
    }

    public static CompetitionSeasonTabFragment newInstance(MitooEnum.FixtureTabType tabType) {
        CompetitionSeasonTabFragment fragment = new CompetitionSeasonTabFragment();
        fragment.setTabType(tabType);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof  MitooActivity)
            setMitooActivity((MitooActivity)activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_competition_tab,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        setNoResultsTextView((TextView) view.findViewById(R.id.noFixturesTextView));
        setUpNoResultsView();
        setFixtureListView((ListView) view.findViewById(R.id.fixture_list_view));
        getFixtureListView().setAdapter(getFixtureListAdapter());
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();
        updateFixtureData();
    }

    @Override
    public void onResume(){
        super.onResume();
        getFixtureListAdapter().notifyDataSetChanged();

    }

    public TextView getNoResultsTextView() {
        return noResultsTextView;
    }

    public void setNoResultsTextView(TextView noResultsTextView) {
        this.noResultsTextView = noResultsTextView;
    }

    private void setUpNoResultsView(){

        if(getFixtureList().isEmpty()){
            getNoResultsTextView().setVisibility(View.VISIBLE);
            if(getTabType()==MitooEnum.FixtureTabType.FIXTURE_RESULT)
                getNoResultsTextView().setText(getString(R.string.fixture_page_no_results));
            else
                getNoResultsTextView().setText(getString(R.string.fixture_page_no_up_coming_games));
        }

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){

        super.onError(error);
    }

    public LinearLayout getFixtureTabContainerView() {
        return fixtureTabContainerView;
    }

    public void setFixtureTabContainerView(LinearLayout fixtureTabContainerView) {
        this.fixtureTabContainerView = fixtureTabContainerView;
    }

    public MitooEnum.FixtureTabType getTabType() {
        if (tabType == null)
            setTabType(MitooEnum.FixtureTabType.FIXTURE_SCHEDULE);
        return tabType;
    }

    public void setTabType(MitooEnum.FixtureTabType tabType) {
        this.tabType = tabType;
    }

    @Override
    public MitooActivity getMitooActivity() {
        if(mitooActivity!=null)
            return mitooActivity;
        else
            return super.getMitooActivity();
    }

    @Override
    protected FixtureModel getFixtureModel() {
        return getMitooActivity().getModelManager().getFixtureModel();
    }

    public void setMitooActivity(MitooActivity mitooActivity) {
        this.mitooActivity = mitooActivity;
    }

    public ListView getFixtureListView() {
        return fixtureListView;
    }

    public void setFixtureListView(ListView fixtureListView) {
        this.fixtureListView = fixtureListView;
    }

    public FixtureListAdapter getFixtureListAdapter() {
        if(fixtureListAdapter == null)
            fixtureListAdapter = new FixtureListAdapter(getActivity(), R.id.fixture_list_view,
                    getFixtureList() , this);
        return fixtureListAdapter;
    }

    public List<FixtureWrapper> getFixtureList() {
        if(fixtureList==null)
            fixtureList = new ArrayList<FixtureWrapper>();
        return fixtureList;
    }

    public void updateFixtureData() {

        List<FixtureWrapper> listOfFixtureToAdd;
        switch(getTabType()){
            case FIXTURE_RESULT:
                listOfFixtureToAdd = getFixtureModel().getResult();
                break;
            case FIXTURE_SCHEDULE:
                listOfFixtureToAdd = getFixtureModel().getSchedule();
                break;
            default:
                listOfFixtureToAdd = getFixtureModel().getSchedule();
        }

        if (listOfFixtureToAdd != null) {
            DataHelper dataHelper = getDataHelper();
            dataHelper.clearList(getFixtureList());
            dataHelper.addToListList(getFixtureList(), listOfFixtureToAdd);
        }
    }

}
