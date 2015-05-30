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
import co.mitoo.sashimi.models.appObject.StandingsRow;
import co.mitoo.sashimi.models.jsonPojo.Team;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.TeamViewModel;
import co.mitoo.sashimi.utils.ViewHelper;
import co.mitoo.sashimi.views.fragments.MitooFragment;

/**
 * Created by david on 15-04-28.
 */
public class StandingsTeamAdapter extends ArrayAdapter<StandingsRow> implements AdapterView.OnItemClickListener , ListAdapter {


    private ViewHelper viewHelper;
    private TeamViewModel teamViewModel;
    private MitooFragment fragment;

    class HeaderViewHolder {
        TextView text;
    }

    public StandingsTeamAdapter(Context context, int resourceId, List<StandingsRow> objects , MitooFragment fragment) {
        super(context, resourceId, objects);
        this.viewHelper = fragment.getViewHelper();
        this.fragment=fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.view_standings_row, null);
            setUpStaticViews(position, convertView);

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
        StandingsRow standing = (StandingsRow) getItem(position);
        Team team = getDataHelper().getTeam(standing.getId());

        if(team!=null){

            rankingsText.setText( Integer.toString(position));
            getTeamViewModel().setUpTeamName(team , teamName);
            getTeamViewModel().loadTeamIcon(teamIcon , team);

        }

    }

    public TeamViewModel getTeamViewModel() {
        if(teamViewModel == null){
            teamViewModel = new TeamViewModel(this.viewHelper);
        }
        return teamViewModel;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private DataHelper getDataHelper(){
        return this.fragment.getDataHelper();
    }

}