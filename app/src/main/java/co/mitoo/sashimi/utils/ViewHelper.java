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
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.Team;
import co.mitoo.sashimi.utils.events.BackGroundTaskCompleteEvent;
import co.mitoo.sashimi.views.MitooImageTarget;
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
    private int itemLoaded = 0;
    private Picasso picasso;
    private int iconBackGroundTasks= 0;

    public MitooActivity getActivity() {
        return activity;
    }
    
    public void setActivity(MitooActivity activity) {
        this.activity = activity;
    }

    private void setUpDynamicLeagueBackground(final View leagueItemHolder, League league) {

        ImageView leagueOverLayImageView = (ImageView) leagueItemHolder.findViewById(R.id.blackOverLay);
        ImageView leagueBackgroundImageView = (ImageView) leagueItemHolder.findViewById(R.id.leagueBackGround);
        MitooImageTarget target = new MitooImageTarget(leagueBackgroundImageView);
        league.setLeagueMobileCover(target);
        target.setCallBack(createBackgroundCallBack());
        
        if (leagueBackgroundImageView != null && leagueBackgroundImageView != null) {
            RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(leagueItemHolder.getMeasuredWidth(), leagueItemHolder.getHeight());
            leagueOverLayImageView.setLayoutParams(layoutParam);
            leagueBackgroundImageView.setLayoutParams(layoutParam);
            getPicasso().with(getActivity())
                    .load(getCover(league))
                    .placeholder(R.color.over_lay_black)
                    .into(target);
        }
    }
   

    private void setUpStaticLeagueBackground(final View leagueItemHolder, League league) {

        ImageView leagueBackgroundImageView = (ImageView) leagueItemHolder.findViewById(R.id.leagueBackGround);
        MitooImageTarget target = new MitooImageTarget(leagueBackgroundImageView);
        league.setLeagueCover(target);
        if (leagueBackgroundImageView != null && leagueBackgroundImageView != null) {
            getPicasso().with(getActivity())
                    .load(getCoverTall(league))
                    .placeholder(R.color.over_lay_black)
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
        setUpStaticLeagueBackground(fragmentView, league);

    }

    public void setUpConfirmView(View fragmentView, League league){
        setUpStaticLeagueBackground(fragmentView, league);

    }
    
    public void setUpEnquireListIcon(final View view , League league){

        String leagueIconUrl = getLogo(league);
        setUpLeagueIcon(view , leagueIconUrl);

    }

    public void setUpMyLeagueListIcon(final View view , Competition competition){

        String leagueIconUrl = getLogo(competition);
        setUpLeagueIcon(view , leagueIconUrl);
    }

    public void setUpLeagueIcon(final View view , String leagueIconUrl){
        ImageView iconImage = (ImageView) view.findViewById(R.id.enquired_list_icon);
        int iconDimenID = R.dimen.enquired_list_icon_length;
        getPicasso().with(getActivity())
                .load(leagueIconUrl)
                .transform(new LogoTransform(getPixelFromDimenID(iconDimenID), 1.0))
                .into(iconImage, createListIconCallBack(view));
    }

    public void setUpIconImageWithCallBack(final View leagueItemContainer, final League league, View leagueListHolder){
        int iconDimenID = R.dimen.league_listview_icon_height;
        ImageView leagueIconImageView = (ImageView) leagueItemContainer.findViewById(R.id.leagueImage);
        MitooImageTarget target = new MitooImageTarget(leagueIconImageView);
        league.setIconTarget(target);
        target.setCallBack(createResultIconCallBack(leagueIconImageView, league, leagueItemContainer, leagueListHolder));
        getPicasso().with(getActivity())
                .load(getLogo(league))
                .transform(new LogoTransform( getPixelFromDimenID(iconDimenID)))
                .into(target);
    }

    public void setUpFullLeagueText(View view, League league){
        
        TextView leagueSportsTextView =  (TextView) view.findViewById(R.id.leagueInfo);
        leagueSportsTextView.setText(league.getLeagueSports());
        setUpLeagueNameText(view, league);
        setUpCityNameText(view, league);
    }

    public void setUpCityNameText(View view, League league){

        TextView cityNameTextView =  (TextView) view.findViewById(R.id.city_name);
        View cityContainer = (View ) view .findViewById(R.id.city_name_container);
        cityNameTextView.setText(league.getCity());
        cityContainer.setBackgroundDrawable(createRoundLeftCorners(league.getColor_1()));

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

    private Drawable createRoundLeftCorners(String color){
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
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
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

        int indexToStop= leagues.size();

        if(leagues.size()>indexToStop){

            final List<League> leagueToLoadLater = leagues.subList(indexToStop , leagues.size());
            fragment.setRunnable(createRecursiveLoadList(fragment, leagueLayout, holder, leagueToLoadLater));
            setRunnable(fragment.getRunnable());
            leagues = leagues.subList(0,indexToStop);
            getHandler().post(getRunnable());

        }

        for(League item : leagues){

            incrementIconBackgroundTasks();
            RelativeLayout layout = createLeagueResult(item, holder);
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
                if(fragment.getDataHelper().isClickable())
                    leagueListItemAction(fragment,itemClicked);
            }
        };
    }

    private void leagueListItemAction(MitooFragment fragment,League league){

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
    
    public RelativeLayout createLeagueResult(League league , View leagueListHolder){

        RelativeLayout leagueItemContainer = (RelativeLayout)createViewFromInflator(R.layout.view_league_dynamic_header);
        this.setUpFullLeagueText(leagueItemContainer, league);
        this.setUpCheckBox(leagueItemContainer, league);
        this.setLineColor(leagueItemContainer, league);
        this.setUpIconImageWithCallBack(leagueItemContainer, league, leagueListHolder);
        return leagueItemContainer;
    }

    public void setUpFixtureForTab(List<FixtureWrapper> fixtureList, LinearLayout tabLayOutContainer) {

        //1)Break the bigger list of Fixture into smaller list of all having the same date
        //2)Than call helper method to create these grouped fixtures for one date, one by one
        Date dateForCurrentList= null;
        List<FixtureWrapper> listOfFixtureForOneDate = null;
        for (FixtureWrapper item : fixtureList) {
            Date dateForItem = item.getFixtureDate();
            if(dateForCurrentList ==null || !getDataHelper().isSameDate(dateForItem, dateForCurrentList)){
                listOfFixtureForOneDate = new ArrayList<FixtureWrapper>();
                listOfFixtureForOneDate.add(item);
            }
            else{
                tabLayOutContainer.addView(createFixtureForOneDate(listOfFixtureForOneDate));
            }
        }
        //3 Add in fixtures for last loop
        tabLayOutContainer.addView(createFixtureForOneDate(listOfFixtureForOneDate));

    }

    public RelativeLayout createFixtureForOneDate(List<FixtureWrapper> fixtureGroup){

        RelativeLayout fixtureGroupContainer = (RelativeLayout)createViewFromInflator(R.layout.view_fixture_grouped);
        LinearLayout fixtureHolder = (LinearLayout) fixtureGroupContainer.findViewById(R.id.fixtureRowContainer);
        for(FixtureWrapper item : fixtureGroup){
            fixtureHolder.addView(createFixtureRow(item));
        }
        setDateForFixtureGroup(fixtureGroupContainer, fixtureGroup);
        return fixtureGroupContainer;
    }

    private void setDateForFixtureGroup(RelativeLayout fixtureGroupContainer , List<FixtureWrapper> fixtureGroup){

        if(fixtureGroup.size()>0){
            TextView dateTextView = (TextView) fixtureGroupContainer.findViewById(R.id.dateTextField);
            String fixtureDate = fixtureGroup.get(0).getDisplayableDate();
            dateTextView.setText(fixtureDate);
        }
    }

    public RelativeLayout createFixtureRow(FixtureWrapper fixture){

        RelativeLayout fixtureRow = (RelativeLayout)createViewFromInflator(R.layout.view_fixture_row);
        customizeFixtureRow(fixtureRow,fixture);
        return fixtureRow;
    }

    private void setUpFixturRowTeamTextView(RelativeLayout row , FixtureWrapper fixture ,MitooEnum.FixtureRowType fixtureType){

        TextView leftTeamName = (TextView)row.findViewById(R.id.leftTeamName);
        TextView rightTeamName = (TextView)row.findViewById(R.id.rightTeamName);
        Team homeTeam = getDataHelper().getTeam(fixture.getFixture().getHome_team_id());
        Team awayTeam = getDataHelper().getTeam(fixture.getFixture().getAway_team_id());
        if(fixtureType == MitooEnum.FixtureRowType.TBC){
            leftTeamName.setText(getActivity().getString(R.string.fixture_page_tbc));
            rightTeamName.setText(getActivity().getString(R.string.fixture_page_tbc));
            leftTeamName.setTextAppearance(getActivity(), R.style.schedulePageTBCText);
            rightTeamName.setTextAppearance(getActivity(), R.style.schedulePageTBCText);
        }else{
            leftTeamName.setText(homeTeam.getName());
            rightTeamName.setText(awayTeam.getName());
        }

    }

    private void setUpFixtureStamp(RelativeLayout row, FixtureWrapper fitxture, MitooEnum.FixtureRowType fixtureType){

        RelativeLayout alphaContainer = (RelativeLayout) row.findViewById(R.id.alphaContainer);
        ImageView stampView= (ImageView) row.findViewById(R.id.stampIcon);
        float alphaValue = getActivity().getDataHelper().getFloatValue(R.dimen.low_alpha);
        switch(fixtureType){
            case TIME:
            case TBC:
            case SCORE:
                break;
            case ABANDONED:
                alphaContainer.setAlpha(alphaValue);
                stampView.setImageResource(R.drawable.abandonned_stamp);
                break;
            case VOID:
                alphaContainer.setAlpha(alphaValue);
                stampView.setImageResource(R.drawable.void_stamp);
                break;
            case POSTPONED:
                alphaContainer.setAlpha(alphaValue);
                stampView.setImageResource(R.drawable.postponed_stamp);
                break;
            case CANCEL:
                alphaContainer.setAlpha(alphaValue);
                stampView.setImageResource(R.drawable.cancelled_stamp);
                break;
            case RESCHEDULE:
                alphaContainer.setAlpha(alphaValue);
                stampView.setImageResource(R.drawable.cancelled_stamp);
                break;
            default:
                break;

        }
    }

    private void setUpFixtureTeamIcons(RelativeLayout row , FixtureWrapper fitxture ,MitooEnum.FixtureRowType fixtureType){

        ImageView leftTeamIcon = (ImageView) row.findViewById(R.id.leftTeamIcon);
        ImageView rightTeamIcon = (ImageView) row.findViewById(R.id.rightTeamIcon);
        rightTeamIcon.setImageResource(R.drawable.team_3);
        leftTeamIcon.setImageResource(R.drawable.team_2);
    }

    private void setUpFixtureCenterText(RelativeLayout row , FixtureWrapper fitxture ,MitooEnum.FixtureRowType fixtureType){

        TextView centerText = (TextView)row.findViewById(R.id.middleTextField);
        switch(fixtureType){
            case TIME:
                centerText.setTextAppearance(getActivity(), R.style.schedulePageTimeText);
                centerText.setText(fitxture.getDisplayableTime());
                break;
            case SCORE:
                centerText.setTextAppearance(getActivity(), R.style.schedulePageScoreText);
                centerText.setText(fitxture.getDisplayableScore());
                break;
            case TBC:
                centerText.setText(fitxture.getDisplayableTime());
                centerText.setTextAppearance(getActivity(), R.style.schedulePageTimeText);
                break;
            default:
                break;
        }
    }

    private void customizeFixtureRow(RelativeLayout row , FixtureWrapper fixture){

        MitooEnum.FixtureRowType fixtureType = getActivity().getDataHelper().getFixtureRowTypeFixture(fixture);
        setUpFixturRowTeamTextView(row, fixture, fixtureType);
        setUpFixtureStamp(row, fixture, fixtureType);
        setUpFixtureCenterText(row, fixture, fixtureType);
        setUpFixtureTeamIcons(row, fixture, fixtureType);

    }

    public void setLineColor(View container, League league){
        
        View bottomLine = (View) container.findViewById(R.id.bottomLine);
        setViewBackgroundDrawableColor(bottomLine, league.getColor_1());
        
    }
    
    private Callback createResultIconCallBack(final View iconView, final League league, final View leagueItemHolder, final View leagueListHolder){
        
        return new Callback() {
            @Override
            public void onSuccess() {
                setUpViewCallBack(iconView, league, leagueItemHolder, leagueListHolder);
                decrementIconBackgroundTasks();

            }

            @Override
            public void onError() {
                
                setUpDynamicLeagueBackground(leagueItemHolder, league);
                setUpEnquireListIconContainer(leagueListHolder, View.GONE);
                decrementIconBackgroundTasks();

            }
        };
    }

    private Callback createListIconCallBack(final View container){

        return new Callback() {
            @Override
            public void onSuccess() {
                setUpEnquireListIconContainer(container , View.VISIBLE);
            }

            @Override
            public void onError() {

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

    private String getCover(League league){
        
        String result = "";
        if(league!=null){
            result = getRetinaUrl(league.getCover_mobile());
        }
        return result;

    }

    private String getCoverTall(League league){

        String result = "";
        if(league!=null){
            result = getRetinaUrl(league.getCover_mobile_tall());
        }
        return result;

    }
    private String getLogo(League league) {

        String result = "";
        if (league != null) {
            result = getRetinaUrl(league.getLogo_large());
        }
        return result;
    }

    private String getLogo(Competition competition) {

        String result = "";
        result = "http://www.portlandsoccerplex.com/wp-content/uploads/2014/03/soccerplex-logo.png";
        if (competition != null) {
            result = getRetinaUrl(result);
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


    private void setUpEnquireListIconContainer(View containerView , int visibility) {
        View iconContainer = containerView.findViewById(R.id.icon_container);
        if (iconContainer != null) {

           iconContainer.setVisibility(visibility);
        }
    }

    private void incrementIconBackgroundTasks(){
        this.iconBackGroundTasks++;
    }

    private void decrementIconBackgroundTasks(){
        this.iconBackGroundTasks--;
        if(this.iconBackGroundTasks==0)
            BusProvider.post(new BackGroundTaskCompleteEvent());
    }


    public void setUpListHeader(ListView listView , int layoutID , String headerText){

        View holder = createHeaderORFooterView(layoutID, headerText);
        listView.addHeaderView(holder);
    }

    public void setUpListFooter(ListView listView , int layoutID , String footerText) {

        if(listView.getFooterViewsCount() ==0 ){
            View holder = createHeaderORFooterView(layoutID, footerText);
            listView.addFooterView(holder);
        }
    }

    public <T> void setUpListView(ListView listView, ArrayAdapter<T> adapter ,String headerText
            ,AdapterView.OnItemClickListener listener){
        int headerLayoutID =  R.layout.view_list_header;
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(listener);
        setUpListHeader(listView, headerLayoutID, headerText);
    }

    public <T> void setUpListView(ListView listView, ArrayAdapter<T> adapter ,String headerText){
        int headerLayoutID =  R.layout.view_list_header;
        listView.setAdapter(adapter);
        setUpListHeader(listView, headerLayoutID, headerText);
    }

    private DataHelper getDataHelper(){
        return getActivity().getDataHelper();
    }

}
