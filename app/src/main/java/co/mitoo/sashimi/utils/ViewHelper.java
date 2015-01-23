package co.mitoo.sashimi.utils;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
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
        
        TextView leagueNameTextView =  (TextView) view.findViewById(R.id.user_name);
        TextView leagueSportsTextView =  (TextView) view.findViewById(R.id.leagueInfo);
        leagueNameTextView.setText(league.getName());
        leagueSportsTextView.setText(league.getLeagueSports());

    }

    public void setLineColor(View view , League league){
        View bottomLine = (View) view.findViewById(R.id.bottomLine);
        int colorID = getColor(league.getColor_1());
        if(colorID!=MitooConstants.invalidConstant)
            bottomLine.setBackgroundColor(colorID);
    }

    public void setJoinBottonColor(View view , String leagueColor){
        Button joinButton = (Button) view.findViewById(R.id.interestedButton);
        int colorID = getColor(leagueColor);
        if(colorID!=MitooConstants.invalidConstant){
            Drawable drawable =joinButton.getBackground();
            drawable.setColorFilter(colorID, PorterDuff.Mode.ADD);
        }
    }

    public void setJoinBottonColor(View view , int colorID){
        Button joinButton = (Button) view.findViewById(R.id.interestedButton);
        if(colorID!=MitooConstants.invalidConstant){
            Drawable drawable =joinButton.getBackground();
            drawable.setColorFilter(colorID, PorterDuff.Mode.ADD);
        }
    }

    private int getCornerRadius(){
        return getContext().getResources().getDimensionPixelSize(R.dimen.corner_radius_small);
    }

    private int getBorder(){
        return getContext().getResources().getDimensionPixelSize(R.dimen.image_border);
    }
    
    private int getColor(String leagueColorInput){

        int colorID = MitooConstants.invalidConstant;
        if(validColorInput(leagueColorInput)){
            String colorWithHash = "#"+ leagueColorInput;
            colorID= Color.parseColor(colorWithHash);
        }
        return colorID;
        
    }
    
    private boolean validColorInput(String input){
        boolean result = true;
        if(input .length()==6){
            loop:
            for(int i = 0 ; i<input.length() ; i++){
                if(!validHex(input.charAt(i)))
                    result=false;
                if(!result)
                    break loop;
            }
        }
        return result;
        
    }
    
    private boolean validHex(Character c){
        if(( c>='0' && c<='9' ) || ( c>= 'a' && c<= 'f') || (c>= 'A' && c<='F')){
            return true;
        }
        return false;
    }
}
