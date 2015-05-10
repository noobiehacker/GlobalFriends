package co.mitoo.sashimi.utils;
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
import co.mitoo.sashimi.models.LeagueService;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.events.BackGroundTaskCompleteEvent;
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

    public RelativeLayout createLeagueResult(League league , View leagueListHolder){

        RelativeLayout leagueItemContainer = (RelativeLayout)getViewHelper().createViewFromInflator(R.layout.view_league_dynamic_header);
        this.setUpFullLeagueText(leagueItemContainer, league);
        this.setUpCheckBox(leagueItemContainer, league);
        this.setLineColor(leagueItemContainer, league);
        this.setUpIconImageWithCallBack(leagueItemContainer, league, leagueListHolder);
        return leagueItemContainer;
    }

    private MitooActivity getActivity(){
        return getViewHelper().getActivity();
    }

    private DataHelper getDataHelper(){
        return getViewHelper().getActivity().getDataHelper();
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

    public void setUpFullLeagueText(View view, League league){

        TextView leagueSportsTextView =  (TextView) view.findViewById(R.id.leagueInfo);
        leagueSportsTextView.setText(league.getLeagueSports());
        getViewHelper().setUpLeagueNameText(view, league);
        setUpCityNameText(view, league);

    }


    public void setUpCheckBox(View view , League league){

        ImageView checkBoxImageView = (ImageView) view.findViewById(R.id.checkBoxImage);
        Boolean joinedLeague =  ! (getActivity().getModelManager().getLeagueModel().leagueIsJoinable(league));
        if(joinedLeague)
            checkBoxImageView.setVisibility(View.VISIBLE);

    }

    public void setLineColor(View container, League league){

        View bottomLine = (View) container.findViewById(R.id.bottomLine);
        getViewHelper().setViewBackgroundDrawableColor(bottomLine, league.getColor_1());

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
        View cityContainer = (View) view .findViewById(R.id.city_name_container);
        cityNameTextView.setText(league.getCity());
        cityContainer.setBackgroundDrawable(getViewHelper().createRoundLeftCorners(league.getColor_1()));

    }

    public void addLeagueDataToList(final MitooFragment fragment, final int leagueLayout, final LinearLayout holder, List<League> leagues) {
        getViewHelper().setHandler(fragment.getHandler());
        recursiveAddLeagueDataToList(fragment, leagueLayout, holder, leagues);
    }

    private void recursiveAddLeagueDataToList(final MitooFragment fragment, final int leagueLayout,final LinearLayout holder, List<League> leagues){

        int indexToStop= leagues.size();

        if(leagues.size()>indexToStop){

            final List<League> leagueToLoadLater = leagues.subList(indexToStop , leagues.size());
            fragment.setRunnable(createRecursiveLoadList(fragment, leagueLayout, holder, leagueToLoadLater));
            getViewHelper().setRunnable(fragment.getRunnable());
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
                if(fragment.getDataHelper().isClickable(v.getId()))
                    leagueListItemAction(fragment,itemClicked);
            }
        };
    }

    private void leagueListItemAction(MitooFragment fragment,League league){

        LeagueService model =getActivity().getModelManager().getLeagueModel();
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
        getViewHelper().getPicasso().with(getActivity())
                .load(leagueIconUrl)
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
