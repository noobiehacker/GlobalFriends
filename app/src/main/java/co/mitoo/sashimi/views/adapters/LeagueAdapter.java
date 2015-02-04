package co.mitoo.sashimi.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.RoundedTransformation;
import co.mitoo.sashimi.utils.ViewHelper;
import co.mitoo.sashimi.views.activities.MitooActivity;
import co.mitoo.sashimi.views.fragments.MitooFragment;

/**
 * Created by david on 14-11-21.
 */
public class LeagueAdapter extends ArrayAdapter<League> implements AdapterView.OnItemClickListener {
    
    private MitooFragment fragment;
    private boolean headerLayout;

    public LeagueAdapter(Context context, int resourceId, List<League> objects , MitooFragment fragment, boolean hasHeader) {
        super(context, resourceId, objects);
        setFragment(fragment);
        setHeaderLayout(hasHeader);
    }
    
    public LeagueAdapter(Context context, int resourceId, List<League> objects) {
        super(context, resourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(getContext(), R.layout.list_view_item_league, null);
        League league = this.getItem(position);
        ViewHelper helper = new ViewHelper(getFragment().getMitooActivity());
        helper.setUpLeagueImage(convertView, league , getViewType());
        helper.setUpLeageText(convertView, league, getViewType());
        helper.setUpCheckBox(convertView , league);
        helper.setLineColor(convertView, league);
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position != 0 || !isHeaderLayout()) {
            League item = (League)parent.getItemAtPosition(position);
            view.setSelected(true);
            leagueListItemAction(item);
            getFragment().getMitooActivity().hideSoftKeyboard(view);
        }
       
    }

    private void leagueListItemAction(League league){

        getFragment().setLoading(true);
        MitooActivity activity = getFragment().getMitooActivity();
        LeagueModel model = activity.getModelManager().getLeagueModel();
        model.setSelectedLeague(league);
        getFragment().fireFragmentChangeAction(R.id.fragment_league);
    }

    public MitooFragment getFragment() {
        return fragment;
    }
    


    public void setFragment(MitooFragment fragment) {
        this.fragment = fragment;
    }

    public boolean isHeaderLayout() {
        return headerLayout;
    }

    public void setHeaderLayout(boolean headerLayout) {
        this.headerLayout = headerLayout;
    }
    
    private MitooEnum.ViewType getViewType(){

        return MitooEnum.ViewType.LIST;

    }
}
