package co.mitoo.sashimi.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.RoundedTransformation;
import co.mitoo.sashimi.utils.ViewHelper;

/**
 * Created by david on 14-11-21.
 */
public class LeagueAdapter extends ArrayAdapter<League> {
    public LeagueAdapter(Context context, int resourceId, List<League> objects) {
        super(context, resourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        this.getItem(position);
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.list_view_item_league, null);
            League league = this.getItem(position);
            ViewHelper helper = new ViewHelper(getContext());
            helper.setUpLeagueImage(convertView, league);
            helper.setUpLeageText(convertView, league);
            helper.setLineColor(convertView, league);
        }
        return convertView;
    }

}
