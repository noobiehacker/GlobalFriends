package co.mitoo.sashimi.views.adapters;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.appObject.MitooStandings;
import co.mitoo.sashimi.models.jsonPojo.Team;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.TeamViewHelper;
import co.mitoo.sashimi.utils.ViewHelper;
import co.mitoo.sashimi.views.fragments.MitooFragment;

/**
 * Created by david on 15-04-28.
 */
public class StandingsGridAdapter extends ArrayAdapter<MitooStandings> implements AdapterView.OnItemClickListener , ListAdapter{

    private ViewHelper viewHelper;
    private TeamViewHelper teamViewHelper;
    private MitooFragment fragment;

    public StandingsGridAdapter(Context context, int resourceId, List<MitooStandings> objects , MitooFragment fragment) {
        super(context, resourceId, objects);
        this.viewHelper = fragment.getViewHelper();
        this.fragment=fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.view_standings_row, null);
            setUpStaticViews(position, convertView);
            setUpDynamicViews(position, convertView);

        }
        return convertView;

    }

    //Set up ther ranking, logo , and team name
    private void setUpStaticViews(int position, View convertView){

        //Get all the Static Views
        TextView rankingsText = (TextView) convertView.findViewById(R.id.rankingsText);
        RelativeLayout logoAndTextContainer = (RelativeLayout) convertView.findViewById(R.id.logoAndNameContainer);
        ImageView teamIcon = (ImageView) convertView.findViewById(R.id.teamIcon);
        TextView teamName = (TextView) convertView.findViewById(R.id.teamName);

        //Set up all static Views
        MitooStandings standing = (MitooStandings) getItem(position);
        Team team = getDataHelper().getTeam(standing.getId());

        if(team!=null){

            rankingsText.setText( Integer.toString(position));
            getTeamViewHelper().setUpTeamName(team , teamName);
            getTeamViewHelper().loadTeamIcon(teamIcon , team);

        }

    }

    //Set up dynamic view
    private void setUpDynamicViews(int position, View convertView){

    }

    public TeamViewHelper getTeamViewHelper() {
        if(teamViewHelper == null){
            teamViewHelper = new TeamViewHelper(this.viewHelper);
        }
        return teamViewHelper;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private DataHelper getDataHelper(){
        return this.fragment.getDataHelper();
    }

}
