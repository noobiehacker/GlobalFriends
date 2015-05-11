package co.mitoo.sashimi.utils;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.Picasso;
import java.util.Arrays;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.views.widgets.MitooImageTarget;
import co.mitoo.sashimi.views.activities.MitooActivity;
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
    private Picasso picasso;
    private LeagueViewHelper leagueViewHelper;
    private FixtureViewHelper fixtureViewHelper;

    public MitooActivity getActivity() {
        return activity;
    }
    
    public void setActivity(MitooActivity activity) {
        this.activity = activity;
    }
   

    private void setUpStaticLeagueBackground(final View leagueItemHolder, League league) {

        ImageView leagueBackgroundImageView = (ImageView) leagueItemHolder.findViewById(R.id.leagueBackGround);
        MitooImageTarget target = new MitooImageTarget(leagueBackgroundImageView);
        league.setLeagueCover(target);
        String leagueBackgroundUrl = getCoverTall(league);
        if (leagueBackgroundImageView != null && leagueBackgroundImageView != null && leagueBackgroundUrl!=null) {
            getPicasso().with(getActivity())
                    .load(getCoverTall(league))
                    .placeholder(R.color.over_lay_black)
                    .fit()
                    .centerCrop()
                    .into(leagueBackgroundImageView);
        }

    }

    public void setUpConfirmAccountView(View fragmentView, Competition competition){

        League league = competition.getLeague();
        setUpStaticLeagueBackground(fragmentView, league);
        String leagueIconUrl = getLogo(league);
        ImageView iconImage = (ImageView) fragmentView.findViewById(R.id.leagueLogo);
        getPicasso().with(getActivity())
                .load(leagueIconUrl)
                .into(iconImage);
        setUpLeagueNameText(fragmentView, competition.getLeague());
    }

    public void setUpConfirmPasswordView(View fragmentView, Competition competition){

        League league = competition.getLeague();
        setUpStaticLeagueBackground(fragmentView, league);

    }

    public void setUpConfirmDoneView(View fragmentView, Competition competition){

        League league = competition.getLeague();
        setUpStaticLeagueBackground(fragmentView, league);
        String leagueIconUrl = getLogo(league);
        ImageView iconImage = (ImageView) fragmentView.findViewById(R.id.leagueLogo);
        getPicasso().with(getActivity())
                .load(leagueIconUrl)
                .into(iconImage);
        setUpLeagueNameText(fragmentView, competition.getLeague());

    }

    public void setUpLeagueBackgroundView(View fragmentView, League league){
        setUpStaticLeagueBackground(fragmentView, league);

    }
    
    public void setUpLeagueNameText(View view, League league){

        TextView leagueNameTextView =  (TextView) view.findViewById(R.id.league_name);
        leagueNameTextView.setText(league.getName());

    }

    public void setTextViewTextColor(TextView view, String color){
        int colorID = getColor(color);
        if(colorID!=MitooConstants.invalidConstant){
            view.setTextColor(colorID);
        }
    }

    public void setViewBackgroundDrawableColor(View view, String color){
        int colorID = getColor(color);
        if(colorID!=MitooConstants.invalidConstant){
            Drawable drawable =view.getBackground();
            drawable.setColorFilter(colorID, PorterDuff.Mode.SRC);
        }
    }

    public Drawable createRoundLeftCorners(String color){
        GradientDrawable drawable = new GradientDrawable();
        int colorID = getColor(color);
        drawable.setColor(colorID);
        drawable.setCornerRadii(createLeftCornerRadii());
        return drawable;
    }

    private float[] createLeftCornerRadii(){
        
        float[] radii = new float[8];
        Arrays.fill(radii, 0);
        radii[0] = getPixelFromDimenID(R.dimen.corner_radius_small);
        radii[1] = getPixelFromDimenID(R.dimen.corner_radius_small);
        radii[6] = getPixelFromDimenID(R.dimen.corner_radius_small);
        radii[7] = getPixelFromDimenID(R.dimen.corner_radius_small);
        return radii;
    }
    
    public int getColor(String leagueColorInput){

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

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setOnTouchCloseKeyboard(innerView);
            }
        }
        else{
            
            if(!(view instanceof EditText || view instanceof ListView)) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        getActivity().hideSoftKeyboard(v);
                        return false;
                    }
                });
            }
        }

    }
    
    public void recursivelyCenterVertically(View view) {

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                recursivelyCenterVertically(innerView);
            }
        }

    }

    public int getPixelFromDimenID(int id){

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
            setUpMap(league.getLatLng(), map, league.getCity());
        }
        
    }

    public void setUpMap(LatLng latLng , GoogleMap map , String markerTitle) {

        setUpMapRemaining(latLng,map, markerTitle);

    }

    public void setUpMap(LatLng latLng , GoogleMap map ) {

        setUpMapRemaining(latLng,map, null);

    }

    private void setUpMapRemaining(LatLng latLng , GoogleMap map , String markerTitle) {

        MarkerOptions option;
        if(markerTitle!=null && markerTitle!="")
            option = createMarkerOption(latLng, markerTitle);
        else
            option = new MarkerOptions().position(latLng);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
        Marker marker = map.addMarker(option);
        disableMapGestures(map);

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
                getPixelFromDimenID(R.dimen.spacing_map_text_width),
                getPixelFromDimenID(R.dimen.spacing_map_text_height),
                getPixelFromDimenID(R.dimen.spacing_map_text_width),
                getPixelFromDimenID(R.dimen.spacing_map_text_height)
        );
        Bitmap markerIcon = generator.makeIcon(cityName);
        MarkerOptions option = new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(markerIcon));
        return option;
        
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

    public Picasso getPicasso() {
        if (picasso == null) {
            picasso=getActivity().getPicasso();
        }
        return picasso;
    }

    public void customizeMainSearch(SearchView searchView){
        MitooSearchViewStyle.on(searchView)
                .setSearchHintDrawable(getActivity().getString(R.string.search_page_text_3))
                .setSearchPlateColor(getActivity().getResources().getColor(R.color.white))
                .setAutoCompleteHintColor(getActivity().getResources().getColor(R.color.gray_light_six))
                .setAutoCompleteTextColor(getActivity().getResources().getColor(R.color.gray_dark_four))
                .setUpMainRemaining()
                .setCursorColor(getActivity().getResources().getColor(R.color.blue_sky_light));
    }

    public void customizeLocationSearch(SearchView searchView){

        MitooSearchViewStyle.on(searchView)
                .setSearchHintDrawable(getActivity().getString(R.string.location_search_page_text_1))
                .setSearchPlateColor(getActivity().getResources().getColor(R.color.gray_dark_five))
                .setAutoCompleteHintColor(getActivity().getResources().getColor(R.color.gray_light_five))
                .setAutoCompleteTextColor(getActivity().getResources().getColor(R.color.white))
                .setUpLocationRemaining()
                .setCursorColor(getActivity().getResources().getColor(R.color.blue_sky_light));
                
    }
    
    public String getRetinaUrl(String url){

        return getActivity().getDataHelper().getRetinaURL(url);

    }

    public String getCover(League league){
        
        String result = "";
        if(league!=null){
            result = getRetinaUrl(league.getCover_mobile());
        }
        return result;

    }

    public String getCoverTall(League league){

        String result = null;
        if(league!=null){
            result = getRetinaUrl(league.getCover_mobile_tall());
        }
        return result;

    }

    public String getLogo(League league) {

        String result = "";
        if (league != null) {
            result = getRetinaUrl(league.getLogo_large());
        }
        return result;
    }

    public RelativeLayout.LayoutParams createCenterInVerticalParam(){

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        return params;

    }
    
    public View createViewFromInflator(int layoutID){
        LayoutInflater inflater =  getActivity().getLayoutInflater();
        RelativeLayout enquiredText = (RelativeLayout)inflater.inflate(layoutID, null);
        return enquiredText;
    }

    public View createHeaderORFooterView(int layoutID, String text){

        int textViewID = getActivity().getDataHelper().getTextViewIDFromLayout(layoutID);
        View holder = createViewFromInflator(layoutID);
        TextView headerTextView = (TextView)holder.findViewById(textViewID);
        headerTextView.setText(text);
        return holder;
    }

    public void setUpListHeader(ListView listView , int layoutID , String headerText){

        View holder = createHeaderORFooterView(layoutID, headerText);
        listView.addHeaderView(holder);
    }

    public View setUpListFooter(ListView listView , int layoutID , String footerText) {

        View holder = createHeaderORFooterView(layoutID, footerText);
        if(listView.getFooterViewsCount() ==0 ){
            listView.addFooterView(holder);
        }
        return holder;
    }

    public <T> void setUpListView(ListView listView, ArrayAdapter<T> adapter ,String headerText
            ,AdapterView.OnItemClickListener listener){
        int headerLayoutID =  R.layout.view_list_header;
        //FIX
        setUpListHeader(listView, headerLayoutID, headerText);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(listener);
    }

    public <T> void setUpListView(ListView listView, ArrayAdapter<T> adapter ,String headerText){
        int headerLayoutID =  R.layout.view_list_header;
        setUpListHeader(listView, headerLayoutID, headerText);
        listView.setAdapter(adapter);
    }

    private DataHelper getDataHelper(){
        return getActivity().getDataHelper();
    }

    public LeagueViewHelper getLeagueViewHelper() {
        if(leagueViewHelper==null)
            leagueViewHelper = new LeagueViewHelper(this);
        return leagueViewHelper;
    }

    public FixtureViewHelper getFixtureViewHelper() {
        if(fixtureViewHelper==null)
            fixtureViewHelper = new FixtureViewHelper(this);
        return fixtureViewHelper;
    }

}
