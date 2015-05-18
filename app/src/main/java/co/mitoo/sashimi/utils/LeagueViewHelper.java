package co.mitoo.sashimi.utils;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;

import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.events.BackGroundTaskCompleteEvent;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import co.mitoo.sashimi.views.fragments.MitooFragment;
import co.mitoo.sashimi.views.widgets.MitooImageTarget;

/**
 * Created by david on 15-03-13.
 */
public class LeagueViewHelper {

    private ViewHelper viewHelper;
    private int itemLoaded = 0;
    private int iconBackGroundTasks= 0;

    public LeagueViewHelper(ViewHelper viewHelper) {
        this.viewHelper = viewHelper;
    }

    public ViewHelper getViewHelper() {
        return viewHelper;
    }

    public RelativeLayout createLeagueResult(LeagueModel league , View leagueListHolder){

        RelativeLayout leagueItemContainer = (RelativeLayout)getViewHelper().createRelativeLayoutFromInflator(R.layout.view_league_dynamic_header);
        this.setUpFullLeagueText(leagueItemContainer, league);
        this.setUpCheckBox(leagueItemContainer, league);
        this.setLineColor(leagueItemContainer, league);
        this.setUpIconImageWithCallBack(leagueItemContainer, league.getLeague(), leagueListHolder);
        return leagueItemContainer;
    }

    private MitooActivity getActivity(){
        return getViewHelper().getActivity();
    }

    public void setUpIconImageWithCallBack(final View leagueItemContainer, final League league, View leagueListHolder){
        int iconDimenID = R.dimen.league_listview_icon_height;
        ImageView leagueIconImageView = (ImageView) leagueItemContainer.findViewById(R.id.leagueImage);
        MitooImageTarget target = new MitooImageTarget(leagueIconImageView);
        league.setIconTarget(target);
        target.setCallBack(createResultIconCallBack(leagueIconImageView, league, leagueItemContainer, leagueListHolder));
        getViewHelper().getPicasso().with(getActivity())
                .load(getViewHelper().getLogo(league))
                .transform(new LogoTransform( getViewHelper().getPixelFromDimenID(iconDimenID)))
                .into(target);
    }

    public void setUpFullLeagueText(View view, LeagueModel league){

        TextView leagueSportsTextView =  (TextView) view.findViewById(R.id.leagueInfo);
        leagueSportsTextView.setText(league.getLeague().getLeagueSports());
        getViewHelper().setUpLeagueNameText(view, league.getLeague());
        setUpCityNameText(view, league.getLeague());

    }

    public void setUpCheckBox(View view , LeagueModel league){

        ImageView checkBoxImageView = (ImageView) view.findViewById(R.id.checkBoxImage);
        if(league.isLeagueIsJoinable())
            checkBoxImageView.setVisibility(View.GONE);
        else{
            checkBoxImageView.setVisibility(View.VISIBLE);
        }

    }

    public void setLineColor(View container, LeagueModel league){

        View bottomLine = (View) container.findViewById(R.id.bottomLine);
        getViewHelper().setViewBackgroundDrawableColor(bottomLine, league.getLeague().getColor_1());

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

    public void setUpCityNameText(View view, League league){

        TextView cityNameTextView =  (TextView) view.findViewById(R.id.city_name);
        View cityContainer = view .findViewById(R.id.city_name_container);
        cityNameTextView.setText(league.getCity());
        cityContainer.setBackgroundDrawable(getViewHelper().createRoundLeftCorners(league.getColor_1()));

    }

    public void addLeagueDataToList(final MitooFragment fragment, final int leagueLayout, final LinearLayout holder, List<LeagueModel> leagues) {
        getViewHelper().setHandler(fragment.getHandler());
        recursiveAddLeagueDataToList(fragment, leagueLayout, holder, leagues);
    }

    private void recursiveAddLeagueDataToList(final MitooFragment fragment, final int leagueLayout,final LinearLayout holder, List<LeagueModel> leagues){

        int indexToStop= leagues.size();

        if(leagues.size()>indexToStop){

            final List<LeagueModel> leagueToLoadLater = leagues.subList(indexToStop , leagues.size());
            fragment.setRunnable(createRecursiveLoadList(fragment, leagueLayout, holder, leagueToLoadLater));
            getViewHelper().setRunnable(fragment.getRunnable());
            leagues = leagues.subList(0,indexToStop);
            getHandler().post(getRunnable());

        }

        for(LeagueModel item : leagues){

            incrementIconBackgroundTasks();
            RelativeLayout layout = createLeagueResult(item, holder);
            layout.setOnClickListener(createLeagueItemClickedListner(fragment, item));
            holder.addView(layout);
        }

    }

    private Runnable createRecursiveLoadList(final MitooFragment fragment,final int leagueLayout,final LinearLayout holder,final List<LeagueModel> leagueToLoadLater ){
        return new Runnable() {
            @Override
            public void run() {
                recursiveAddLeagueDataToList(fragment,leagueLayout, holder, leagueToLoadLater);
            };

        };
    }

    private View.OnClickListener createLeagueItemClickedListner(final MitooFragment fragment,final LeagueModel itemClicked){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment.getDataHelper().isClickable(v.getId()))
                    leagueListItemAction(fragment,itemClicked);
            }
        };
    }

    private void leagueListItemAction(MitooFragment fragment,LeagueModel league){

        Bundle bundle = new Bundle();
        bundle.putInt(fragment.getString(R.string.bundle_key_league_id_key) , league.getLeague().getId());
        bundle.putString(fragment.getString(R.string.bundle_key_tool_bar_title) , league.getLeague().getName());
        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_league)
                .setTransition(MitooEnum.FragmentTransition.PUSH)
                .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                .setBundle(bundle)
                .build();
        BusProvider.post(fragmentChangeEvent);

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
            getViewHelper().getPicasso().with(getActivity())
                    .load(getViewHelper().getCover(league))
                    .placeholder(R.color.over_lay_black)
                    .into(target);
        }
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

    private void leagueBackgroundLoadCompleteAction(){

        itemLoaded++;
        if(itemLoaded==3){
            itemLoaded = 0;
            getHandler().post(getRunnable());

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

    private void setUpEnquireListIconContainer(View containerView , int visibility) {
        View iconContainer = containerView.findViewById(R.id.icon_container);
        if (iconContainer != null) {

            iconContainer.setVisibility(visibility);
        }
    }

    private Callback createListIconCallBack(final View container){

        return new Callback() {
            @Override
            public void onSuccess() {
                setUpEnquireListIconContainer(container, View.VISIBLE);
            }

            @Override
            public void onError() {

            }
        };

    }

    /*
     *
      Use for the two list view on home screen
    *
    */

    public void setUpLeagueListIcon(final View view, League league){

        String leagueIconUrl = getViewHelper().getLogo(league);
        setUpLeagueIcon(view , leagueIconUrl);

    }

    public void setUpLeagueIcon(final View view , String leagueIconUrl){
        ImageView iconImage = (ImageView) view.findViewById(R.id.enquired_list_icon);
        int iconDimenID = R.dimen.enquired_list_icon_length;
        float ratio = getActivity().getDataHelper().getFloatValue(R.dimen.width_to_height_ratio);
        if(leagueIconUrl != null && !leagueIconUrl.isEmpty()){
            getViewHelper().getPicasso().with(getActivity())
                    .load(leagueIconUrl)
                    .transform(new LogoTransform(getViewHelper().getPixelFromDimenID(iconDimenID), ratio))
                    .into(iconImage, createListIconCallBack(view));
        }

        getViewHelper().getPicasso().with(getActivity())
                .load(leagueIconUrl)
                .placeholder(R.color.over_lay_black)
                .transform(new LogoTransform(getViewHelper().getPixelFromDimenID(iconDimenID), ratio))
                .into(iconImage, createListIconCallBack(view));
    }

        /*
     *
      Use for the two list view on home screen
    *
    */

    public Runnable getRunnable() {
        return getViewHelper().getRunnable();
    }

    public Handler getHandler() {
        return getViewHelper().getHandler();
    }

}
