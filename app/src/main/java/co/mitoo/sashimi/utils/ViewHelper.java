package co.mitoo.sashimi.utils;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.views.activities.MitooActivity;

/**
 * Created by david on 15-01-20.
 */
public class ViewHelper {
    
    private MitooActivity activity;

    public ViewHelper(MitooActivity activity) {
        this.activity = activity;
    }

    public MitooActivity getActivity() {
        return activity;
    }

    public void setActivity(MitooActivity activity) {
        this.activity = activity;
    }

    public void setUpLeagueImage(View view, League league){

        final ImageView leagueIconImageView = (ImageView) view.findViewById(R.id.leagueImage);
        ImageView leagueBackgroundImageView = (ImageView) view.findViewById(R.id.leagueBackGround);
        Picasso.with(getActivity())
                .load(league.getLogo_large())
                .transform(new LogoTransform(0,getPixelFromDimenID(R.dimen.league_icon_height)))
                .transform(new RoundedTransformation(getPixelFromDimenID(R.dimen.image_border) , getPixelFromDimenID(R.dimen.corner_radius_small)))
                .into(leagueIconImageView);
        Picasso.with(getActivity())
                .load(league.getCover())
                .into(leagueBackgroundImageView);

    }

    public void setUpLeageText(View view , League league){
        
        TextView leagueNameTextView =  (TextView) view.findViewById(R.id.league_name);
        TextView leagueSportsTextView =  (TextView) view.findViewById(R.id.leagueInfo);
        TextView cityNameTextView =  (TextView) view.findViewById(R.id.city_name);
        leagueNameTextView.setText(league.getName());
        leagueSportsTextView.setText(league.getLeagueSports());
        cityNameTextView.setText(league.getCity());

    }
    
    public void setUpCheckBox(View view , League league){
        
        ImageView checkBoxImageView = (ImageView) view.findViewById(R.id.checkBoxImage);
        Boolean joinedLeague =  ! (getActivity().getModelManager().getLeagueModel().leagueIsJoinable(league));
        if(joinedLeague)
            checkBoxImageView.setVisibility(View.VISIBLE);
        
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
        return getActivity().getResources().getDimensionPixelSize(R.dimen.corner_radius_small);
    }

    private int getBorder(){
        return getPixelFromDimenID(R.dimen.image_border);
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
    

    public void setOnTouchCloseKeyboard(View view) {

        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    getActivity().hideSoftKeyboard(v);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setOnTouchCloseKeyboard(innerView);
            }
        }
    }
    
    private int getPixelFromDimenID(int id){

        return getActivity().getResources().getDimensionPixelSize(id);
    }

    public void setViewVisibility(View view, boolean visible){

        if(view!=null)
        {
            if(visible)
                view.setVisibility(View.VISIBLE);
            else
                view.setVisibility(View.GONE);
        }

    }
    

}
