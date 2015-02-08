package co.mitoo.sashimi.utils;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.views.activities.MitooActivity;
import co.mitoo.sashimi.views.fragments.MitooFragment;
import android.os.Handler;

/**
 * Created by david on 15-01-20.
 */
public class ViewHelper {
    
    private MitooActivity activity;
    public ViewHelper(MitooActivity activity) {
        this.activity = activity;
    }
    private Handler handler;
    private Runnable runnable;
    public MitooActivity getActivity() {
        return activity;
    }
    private int itemLoaded = 0;

    public void setActivity(MitooActivity activity) {
        this.activity = activity;
    }
    
    private int getIconDimen(int layoutID){

        int iconDimen = MitooConstants.invalidConstant;
        if(layoutID == R.layout.partial_league_page_header)
            iconDimen = R.dimen.league_page_icon_height;
        else
            iconDimen = R.dimen.league_listview_icon_height;
        return iconDimen;
    }

    private String getLogoUrl(int layoutID , League league){

        String logo = "";
        if(layoutID == R.layout.partial_league_page_header)
            logo =  league.getLogo_medium();
        else
            logo =  league.getLogo_medium();
        return logo;
    }

    private void setUpDynamicLeagueBackground(final View leagueItemHolder, League league) {

        ImageView leagueOverLayImageView = (ImageView) leagueItemHolder.findViewById(R.id.blackOverLay);
        ImageView leagueBackgroundImageView = (ImageView) leagueItemHolder.findViewById(R.id.leagueBackGround);
        if (leagueBackgroundImageView != null && leagueBackgroundImageView != null) {
            String cover = league.getCover_mobile();
            RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(leagueItemHolder.getWidth(), leagueItemHolder.getHeight());

            leagueOverLayImageView.setLayoutParams(layoutParam);

            leagueBackgroundImageView.setLayoutParams(layoutParam);
            Picasso.with(getActivity())
                    .load(cover)
                    .fit()
                    .centerCrop()
                    .placeholder(R.color.over_lay_black)
                    .into(leagueBackgroundImageView, createBackgroundCallBack());
        }
    }


    private void setUpLeagueBackground(final View leagueItemHolder, League league) {

        ImageView leagueOverLayImageView = (ImageView) leagueItemHolder.findViewById(R.id.blackOverLay);
        ImageView leagueBackgroundImageView = (ImageView) leagueItemHolder.findViewById(R.id.leagueBackGround);
        if (leagueBackgroundImageView != null && leagueBackgroundImageView != null) {
            String cover = league.getCover_mobile();
            RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,500);
            leagueOverLayImageView.setLayoutParams(layoutParam);
            leagueBackgroundImageView.setLayoutParams(layoutParam);
            Picasso.with(getActivity())
                    .load(cover)
                    .fit()
                    .centerCrop()
                    .into(leagueBackgroundImageView);
        }

    }
    
    
    private void leagueBackgroundLoadCompleteAction(){

        itemLoaded++;
        if(itemLoaded==3){
            itemLoaded = 0;
            getHandler().post(getRunnable());

        }
    }
    
    public void setUpSignUpView(View fragmentView, League league){
        
        setUpIconImage(fragmentView, league, R.layout.list_view_item_league);
        setUpLeagueBackground(fragmentView,league);
        setUpLeagueNameText(fragmentView,league);
    }

    public void setUpIconImageWithCallBack(final View leagueItemContainer, final League league, int layoutID, View leagueListHolder){
        int iconDimenID = getIconDimen(layoutID);
        String logo = getLogoUrl(layoutID, league);
        ImageView leagueIconImageView = (ImageView) leagueItemContainer.findViewById(R.id.leagueImage);
        Picasso.with(getActivity())
                .load(logo)
                .transform(new LogoTransform( getPixelFromDimenID(iconDimenID)))
                .transform(new RoundedTransformation(getPixelFromDimenID(R.dimen.image_border)
                        , getPixelFromDimenID(R.dimen.corner_radius_small)))
                .into(leagueIconImageView , createIconCallBack(leagueIconImageView, league, leagueItemContainer, leagueListHolder));
    }

    public void setUpIconImage(final View leagueItemContainer, final League league, int layoutID){
        int iconDimenID = getIconDimen(layoutID);
        String logo = getLogoUrl(layoutID, league);
        ImageView leagueIconImageView = (ImageView) leagueItemContainer.findViewById(R.id.leagueImage);
        Picasso.with(getActivity())
                .load(logo)
                .transform(new LogoTransform( getPixelFromDimenID(iconDimenID)))
                .transform(new RoundedTransformation(getPixelFromDimenID(R.dimen.image_border)
                        , getPixelFromDimenID(R.dimen.corner_radius_small)))
                .into(leagueIconImageView );
    }

    public void setUpFullLeagueText(View view, League league){
        
        TextView leagueSportsTextView =  (TextView) view.findViewById(R.id.leagueInfo);
        TextView cityNameTextView =  (TextView) view.findViewById(R.id.city_name);
        leagueSportsTextView.setText(league.getLeagueSports());
        cityNameTextView.setText(league.getCity());
        setUpLeagueNameText(view, league);

    }

    public void setUpLeagueNameText(View view, League league){

        TextView leagueNameTextView =  (TextView) view.findViewById(R.id.league_name);
        leagueNameTextView.setText(league.getName());

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

    public void addLeagueDataToList(final MitooFragment fragment, final int leagueLayout, final LinearLayout holder, List<League> leagues) {
        setHandler(fragment.getHandler());
        recursiveAddLeagueDataToList(fragment, leagueLayout,holder,leagues);
    }

    private void recursiveAddLeagueDataToList(final MitooFragment fragment, final int leagueLayout,final LinearLayout holder, List<League> leagues){

        int indexToStop= 3;

        if(leagues.size()>indexToStop){

            final List<League> leagueToLoadLater = leagues.subList(indexToStop , leagues.size());
            setRunnable(createRecursiveLoadList(fragment, leagueLayout, holder, leagueToLoadLater));
            leagues = leagues.subList(0,indexToStop);

        }

        for(League item : leagues){

            RelativeLayout layout = createLeagueResult(item, leagueLayout, holder);
            layout.setOnClickListener(createLeagueItemClickedListner(fragment, item));
            holder.addView(layout);
        }

    }

    private Runnable createRecursiveLoadList(final MitooFragment fragment,final int leagueLayout,final LinearLayout holder,final List<League> leagueToLoadLater ){
        return new Runnable() {
            @Override
            public void run() {
                recursiveAddLeagueDataToList(fragment,leagueLayout, holder, leagueToLoadLater);
            };

        };
    }

    private View.OnClickListener createLeagueItemClickedListner(final MitooFragment fragment,final League itemClicked){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leagueListItemAction(fragment,itemClicked);
            }
        };
    }

    private void leagueListItemAction(MitooFragment fragment,League league){

        fragment.setLoading(true);
        LeagueModel model =getActivity().getModelManager().getLeagueModel();
        model.setSelectedLeague(league);
        fragment.fireFragmentChangeAction(R.id.fragment_league);
    }


    public void setUpViewCallBack(final View loadedView, final League league, final View leagueItemHolder, final View leagueListHolder){

        if(loadedView!= null && leagueItemHolder!=null){
            loadedView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            // Ensure you call it only once :
                            loadedView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            setUpDynamicLeagueBackground(leagueItemHolder, league);
                        }
                    });
        }
    }
    
    public RelativeLayout createLeagueResult(League league , int layOutID, View leagueListHolder){

        RelativeLayout leagueItemContainer = (RelativeLayout) getActivity().getLayoutInflater().inflate(layOutID, null);
        this.setUpFullLeagueText(leagueItemContainer, league);
        this.setUpCheckBox(leagueItemContainer, league);
        this.setLineColor(leagueItemContainer, league);
        this.setUpIconImageWithCallBack(leagueItemContainer, league, layOutID, leagueListHolder);
        return leagueItemContainer;
    }
    
    private Callback createIconCallBack(final View iconView, final League league, final View leagueItemHolder, final View leagueListHolder){
        
        return new Callback() {
            @Override
            public void onSuccess() {
                setUpViewCallBack(iconView, league, leagueItemHolder, leagueListHolder);
            }

            @Override
            public void onError() {
                setUpDynamicLeagueBackground(leagueItemHolder, league);
            }
        };
        
    }

    private Callback createBackgroundCallBack(){

        return new Callback() {
            @Override
            public void onSuccess() {
                leagueBackgroundLoadCompleteAction();
            }

            @Override
            public void onError() {
                leagueBackgroundLoadCompleteAction();
            }
        };

    }

    public void unbindDrawables(View view) {

        Thread thread= new Thread(createRemoveViewRunnable(view));
        thread.start();

    }
    
    private Runnable createRemoveViewRunnable(final View view){
        return new Runnable() {
            @Override
            public void run() {
                if (view.getBackground() != null) {
                    view.getBackground().setCallback(null);
                }
                if (view instanceof ViewGroup) {
                    for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                        unbindDrawables(((ViewGroup) view).getChildAt(i));
                    }
                    ((ViewGroup) view).removeAllViews();
                }
                System.gc();
            }
        };
    }

    public Handler getHandler() {
        if(handler==null)
            handler = new Handler();
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }
}
