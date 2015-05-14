package co.mitoo.sashimi.views.adapters;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.network.Services.CompetitionService;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.CompetitionDataClearEvent;
import co.mitoo.sashimi.utils.events.FixtureDataClearEvent;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.TeamServiceDataClearEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import co.mitoo.sashimi.views.fragments.MitooFragment;
/**
 * Created by david on 15-03-06.
 */
public class CompetitionAdapter extends ArrayAdapter<Competition> implements AdapterView.OnItemClickListener {

    private MitooFragment fragment;

    public CompetitionAdapter(Context context, int resourceId, List<Competition> objects , MitooFragment fragment) {
        super(context, resourceId, objects);
        setFragment(fragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(getContext(), R.layout.list_view_enquired_league ,null);
        Competition competition = this.getItem(position);

        setUpCompetitionText(convertView, competition);
        League league = competition.getLeague();
        if(league!=null){
            setUpLeagueIcon(convertView, league);
            setUpLeagueText(convertView, league);
        }
        return convertView;
    }

    private void setUpCompetitionText(View view, Competition competition){

        TextView competitionNameText = (TextView) view.findViewById(R.id.topText);
        competitionNameText.setText(competition.getName());

    }

    private void setUpLeagueIcon(View view, League league) {

        getFragment().getViewHelper().getLeagueViewHelper().setUpLeagueListIcon(view, league);

    }

    private void setUpLeagueText(View view, League league){

        TextView leagueNameText = (TextView) view.findViewById(R.id.bottomText);
        leagueNameText.setText(league.getName());

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(getFragment().getDataHelper().isClickable(view.getId()) && id!= -1) {
            Competition item = (Competition) parent.getItemAtPosition(position);
            setSelectedModelItem(item);
            BusProvider.post(new FixtureDataClearEvent());
            BusProvider.post(new TeamServiceDataClearEvent());
            FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                    .setFragmentID(R.id.fragment_competition)
                    .setTransition(MitooEnum.FragmentTransition.PUSH)
                    .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                    .setBundle(createBundle(item.getId() , item.getLeague().getColor_1() , item.getName()))
                    .build();
            BusProvider.post(fragmentChangeEvent);
        }

    }

    public MitooFragment getFragment() {
        return fragment;
    }

    public void setFragment(MitooFragment fragment) {
        this.fragment = fragment;
    }

    private void setSelectedModelItem(Competition competition){
        MitooActivity activity = getFragment().getMitooActivity();
        CompetitionService model = activity.getModelManager().getCompetitionModel();
        model.setSelectedCompetition(competition);
    }

    private Bundle createBundle(int competitionSeasonID , String color , String toolBarName){
        String competitionKey = getFragment().getString(R.string.bundle_key_competition_id_key);
        String teamColorKey = getFragment().getString(R.string.bundle_key_team_color_key);
        String toolBarStringKey = getFragment().getString(R.string.bundle_key_tool_bar_title);
        Bundle bundle = new Bundle();
        bundle.putInt(competitionKey, competitionSeasonID);
        bundle.putString(teamColorKey, color);
        bundle.putString(toolBarStringKey, toolBarName);
        return bundle;
    }

}
