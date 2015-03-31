package co.mitoo.sashimi.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.views.activities.MitooActivity;
import co.mitoo.sashimi.views.fragments.MitooFragment;

/**
 * Created by david on 14-11-21.
 */
public class LeagueAdapter extends ArrayAdapter<League> implements AdapterView.OnItemClickListener {
    
    private MitooFragment fragment;

    public LeagueAdapter(Context context, int resourceId, List<League> objects , MitooFragment fragment){
        super(context, resourceId, objects);
        setFragment(fragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(getContext(), R.layout.list_view_enquired_league ,null);
        League league = this.getItem(position);
        setUpLeagueIcon(convertView, league);
        setUpLeagueText(convertView, league);
        return convertView;
    }

    private void setUpLeagueIcon(View view, League league) {
     
        getFragment().getViewHelper().setUpEnquireListIcon(view , league);

    }
    private void setUpLeagueText(View view, League league){
        TextView leagueNameText = (TextView) view.findViewById(R.id.topText);
        TextView leagueDateText = (TextView) view.findViewById(R.id.bottomText);
        leagueNameText.setText(league.getName());
        String date = league.getCreated_at();
        leagueDateText.setText(getContext().getResources().getString(R.string.home_page_enquired_date_prefix) + " " +  getLeagueFormatedDate(date));
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(fragment.getDataHelper().isClickable() && id!= -1) {

            League item = (League) parent.getItemAtPosition(position);
            view.setSelected(true);
            enquiredLeagueListItemAction(item);
            getFragment().getMitooActivity().hideSoftKeyboard(view);
        }

    }

    private void enquiredLeagueListItemAction(League league){

        setSelectedModelItem(league);
        getFragment().fireFragmentChangeAction(R.id.fragment_league);
    }

    public MitooFragment getFragment() {
        return fragment;
    }

    public void setFragment(MitooFragment fragment) {
        this.fragment = fragment;
    }

    private String getLeagueFormatedDate(String date){
        DataHelper helper= getFragment().getMitooActivity().getDataHelper();
        return helper.parseDateToDisplayFormat(date);
    }

    private void setSelectedModelItem(League league){
        MitooActivity activity = getFragment().getMitooActivity();
        LeagueModel model = activity.getModelManager().getLeagueModel();
        model.setSelectedLeague(league);
    }

}
