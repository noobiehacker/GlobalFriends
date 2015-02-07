package co.mitoo.sashimi.utils;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
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

    public void setUpLeagueImage(View view, League league , MitooEnum.ViewType viewType){
        
        setUpIconImage(view, league, viewType);
    }
    
    private int getIconDimen(MitooEnum.ViewType viewType){

        int iconDimen = MitooConstants.invalidConstant;
        if(viewType == MitooEnum.ViewType.FRAGMENT)
            iconDimen = R.dimen.league_page_icon_height;
        else
            iconDimen = R.dimen.league_listview_icon_height;
        return iconDimen;
    }

    private String getLogoUrl(MitooEnum.ViewType viewType , League league){

        String logo = "";
        if(viewType == MitooEnum.ViewType.FRAGMENT)
            logo =  league.getLogo_medium();
        else
            logo =  league.getLogo_medium();
        return logo;
    }

    private void setUpLeagueBackground(View view, League league){
       
        if(view.getHeight()!=0){
            String cover = league.getCover();
            cover = "https://thepublicblogger.files.wordpress.com/2014/05/colors.jpg";
            RelativeLayout viewLayout =(RelativeLayout)view.findViewById(R.id.league_image_holder);
            RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(viewLayout.getWidth(),viewLayout.getHeight());

            ImageView leagueOverLayImageView = (ImageView) viewLayout.findViewById(R.id.blackOverLay);
            leagueOverLayImageView.setLayoutParams(layoutParam);

            ImageView leagueBackgroundImageView = (ImageView) viewLayout.findViewById(R.id.leagueBackGround);
            leagueBackgroundImageView.setLayoutParams(layoutParam);
            Picasso.with(getActivity())
                    .load(cover)
                    .fit()
                    .centerCrop()
                    .into(leagueBackgroundImageView);
        }

    }

    private void setUpIconImage(final View view, final League league, MitooEnum.ViewType viewType){
        int iconDimenID = getIconDimen(viewType);
        String logo = getLogoUrl(viewType, league);
        logo = "http://upload.wikimedia.org/wikipedia/en/thumb/0/01/Golden_State_Warriors_logo.svg/838px-Golden_State_Warriors_logo.svg.png";

        ImageView leagueIconImageView = (ImageView) view.findViewById(R.id.leagueImage);
        Picasso.with(getActivity())
                .load(logo)
                .transform(new LogoTransform( getPixelFromDimenID(iconDimenID)))
                .transform(new RoundedTransformation(getPixelFromDimenID(R.dimen.image_border)
                        , getPixelFromDimenID(R.dimen.corner_radius_small)))
                .into(leagueIconImageView , new Callback() {
                    @Override
                    public void onSuccess() {
                        
                        setUpHolderViewCallBack( view ,league, view);

                        }

                    @Override
                    public void onError() {
                       // setUpHolderViewCallBack( view ,league, view);

                    }
                });
    }

    public void setUpFullLeagueText(View view, League league, MitooEnum.ViewType viewType){
        
        TextView leagueSportsTextView =  (TextView) view.findViewById(R.id.leagueInfo);
        TextView cityNameTextView =  (TextView) view.findViewById(R.id.city_name);
        
        leagueSportsTextView.setText(league.getLeagueSports());
        cityNameTextView.setText(league.getCity());

        setUpLeagueNameText(view, league, viewType);

    }

    public void setUpLeagueNameText(View view, League league, MitooEnum.ViewType viewType){

        TextView leagueNameTextView =  (TextView) view.findViewById(R.id.league_name);
        leagueNameTextView.setText(league.getName());
        setUpHolderViewCallBack(leagueNameTextView,league, view);
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

    public void setTextViewColor(TextView view, int colorID){
        if(colorID!=MitooConstants.invalidConstant){
            view.setTextColor(colorID);
        }
    }

    public void setTextViewColor(TextView view, String color){
        int colorID = getColor(color);
        setTextViewColor(view, colorID);
    }

    public void setViewColor(View view, int colorID){
        if(colorID!=MitooConstants.invalidConstant){
            Drawable drawable =view.getBackground();
            drawable.setColorFilter(colorID, PorterDuff.Mode.ADD);
        }
    }

    public void setViewColor(View view, String color){
        int colorID = getColor(color);
        setViewColor(view, colorID);
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
    
    public View createListViewPadding(){
        
        View view = new View(getActivity());
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,
                0);
        view.setLayoutParams(params);
        return view;
    }



    public void setUpMap(League league , GoogleMap map){
        if(league !=null & map !=null){
            LatLng latLng = league.getLatLng();
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            MarkerOptions option = createMarkerOption(latLng, league.getCity());
            Marker marker = map.addMarker(option);
            disableMapGestures(map);
        }
        
    }
    
    private void disableMapGestures(GoogleMap map){

        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.getUiSettings().setAllGesturesEnabled(false);
        
    }
    
    private MarkerOptions createMarkerOption(LatLng latLng, String cityName){

        IconGenerator generator = new IconGenerator(getActivity());
        generator.setTextAppearance(R.style.grayCityMapText);
        generator.setContentPadding(
                getPixelFromDimenID(R.dimen.spacing_map_text_width) ,
                getPixelFromDimenID(R.dimen.spacing_map_text_height) ,
                getPixelFromDimenID(R.dimen.spacing_map_text_width) ,
                getPixelFromDimenID(R.dimen.spacing_map_text_height)
                );
        Bitmap markerIcon = generator.makeIcon(cityName);
        MarkerOptions option = new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(markerIcon));
        return option;
        
    }

    public void setUpHolderViewCallBack(final View view ,final League league, final View holder){

        if(view!=null){
            view.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            // Ensure you call it only once :
                            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            setUpLeagueBackground(holder, league);

                        }
                    });
        }

    }
    
    public RelativeLayout createLeagueResult(League league){
        
        return null;
    }
}
