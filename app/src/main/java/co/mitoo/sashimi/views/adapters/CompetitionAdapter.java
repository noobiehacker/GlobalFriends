package co.mitoo.sashimi.views.adapters;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.CompetitionModel;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.utils.DataHelper;
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
        setUpLeagueIcon(convertView, competition);
        setUpLeagueText(convertView, competition);
        return convertView;
    }

    private void setUpLeagueIcon(View view, Competition competition) {

        getFragment().getViewHelper().setUpMyLeagueListIcon(view , competition);

    }
    private void setUpLeagueText(View view, Competition competition){
        TextView leagueNameText = (TextView) view.findViewById(R.id.leagueTitleText);
        TextView leagueDateText = (TextView) view.findViewById(R.id.leagueDateText);
        leagueNameText.setText(competition.getSeason_name());
        leagueDateText.setText(competition.getName());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(getFragment().getDataHelper().isClickable() && id!= -1) {
            Competition item = (Competition) parent.getItemAtPosition(position);
            setSelectedModelItem(item);
            getFragment().fireFragmentChangeAction(R.id.fragment_fixture);
        }

    }

    public MitooFragment getFragment() {
        return fragment;
    }

    public void setFragment(MitooFragment fragment) {
        this.fragment = fragment;
    }

    private String getLeagueFormatedDate(String date){
        DataHelper helper= getFragment().getMitooActivity().getDataHelper();
        return helper.parseDate(date);
    }

    private void setSelectedModelItem(Competition competition){
        MitooActivity activity = getFragment().getMitooActivity();
        CompetitionModel model = activity.getModelManager().getCompetitionModel();
        model.setSelectedCompetition(competition);
    }
}
