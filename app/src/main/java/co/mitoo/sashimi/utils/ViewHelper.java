package co.mitoo.sashimi.utils;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.League;

/**
 * Created by david on 15-01-20.
 */
public class ViewHelper {
    
    private Context context;

    public ViewHelper(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public void setUpLeagueImage(View view, League league){

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

    public void setUpLeageText(View view , League league){
        
        TextView leagueNameTextView =  (TextView) view.findViewById(R.id.leagueName);
        TextView leagueSportsTextView =  (TextView) view.findViewById(R.id.leagueInfo);
        leagueNameTextView.setText(league.getName());
        leagueSportsTextView.setText(league.getLeagueSports());

    }

    private int getCornerRadius(){
        return getContext().getResources().getDimensionPixelSize(R.dimen.corner_radius_small);
    }

    private int getBorder(){
        return getContext().getResources().getDimensionPixelSize(R.dimen.image_border);
    }
}
