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
            setUpImage(convertView, league);
            setUpText(convertView, league);
        }
        return convertView;
    }
    
    private void setUpImage(View view, League league){

        ImageView leagueIconImageView = (ImageView) view.findViewById(R.id.leagueImage);
        ImageView leagueBackgroundImageView = (ImageView) view.findViewById(R.id.leagueBackGround);
        Picasso.with(getContext())
                .load(league.getLogo())
                .transform(new RoundedTransformation(getCornerRadius(),getBorder()))
                .into(leagueIconImageView);
        Picasso.with(getContext())
                .load(league.getCover())
                .into(leagueBackgroundImageView);
    }
    
    private void setUpText(View view , League league){
        TextView leagueNameTextView =  (TextView) view.findViewById(R.id.leagueName);
        TextView leagueInfoTextView =  (TextView) view.findViewById(R.id.leagueInfo);
        leagueNameTextView.setText(league.getName());
        //leagueInfoTextView.setText(league.getAbout());
        
    }

    private int getCornerRadius(){
        return getContext().getResources().getDimensionPixelSize(R.dimen.corner_radius_small);
    }

    private int getBorder(){
        return getContext().getResources().getDimensionPixelSize(R.dimen.image_border);
    }

}
