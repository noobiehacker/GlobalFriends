package co.mitoo.sashimi.utils;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.joda.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.FixtureService;
import co.mitoo.sashimi.models.jsonPojo.Team;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;

/**
 * Created by david on 15-03-13.
 */
public class FixtureViewHelper {

    public FixtureViewHelper(ViewHelper viewHelper) {
        this.viewHelper = viewHelper;
    }

    public ViewHelper getViewHelper() {
        return viewHelper;
    }

    private ViewHelper viewHelper;

    private View.OnClickListener createFixtureItemClickedListener(final FixtureWrapper itemClicked){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity().getDataHelper().isClickable(v.getId()))
                    fixtureItemClickAction(itemClicked);
            }
        };
    }

    public void setUpFixtureForTabRefrac(List<FixtureWrapper> fixtureList, LinearLayout tabLayOutContainer) {

        //1)Run one loop to count up how many fixtures does a particular date have
        Map<LocalDate, Integer> map = new HashMap<LocalDate, Integer>();
        //1b)Use a hash map to count up an store the value
        for (FixtureWrapper item : fixtureList) {

            if (map.containsKey(item.getJodafixtureDate())) {
                map.put(item.getJodafixtureDate(), map.get(item.getJodafixtureDate()) + 1);
            } else {
                map.put(item.getJodafixtureDate(), 1);
            }

        }
        //2)Second loop, for every date, get from the map on how many fixtures of a particular date has
        Iterator<FixtureWrapper> iterator = fixtureList.iterator();
        List<FixtureWrapper> fixtureListForOneDate = null;
        int itemsRemainingInList = 0;
        while (iterator.hasNext()) {

            FixtureWrapper item = iterator.next();
            if (fixtureListForOneDate == null) {
                fixtureListForOneDate = new ArrayList<FixtureWrapper>();
                itemsRemainingInList = map.get(item.getJodafixtureDate());
            }
            //2a)Group the List of fixture by iterating the value count and add them all to an ArrayList
            fixtureListForOneDate.add(item);

            if (itemsRemainingInList == 1) {
                //3)Create a view for this list of fixtures for one date
                tabLayOutContainer.addView(createFixtureForOneDate(fixtureListForOneDate));
                fixtureListForOneDate = null;
            }

            itemsRemainingInList--;
        }

    }

    public RelativeLayout createFixtureForOneDate(List<FixtureWrapper> fixtureGroup){

        RelativeLayout fixtureGroupContainer = (RelativeLayout)getViewHelper().createViewFromInflator(R.layout.view_fixture_grouped);
        LinearLayout fixtureHolder = (LinearLayout) fixtureGroupContainer.findViewById(R.id.fixtureRowContainer);

        for(FixtureWrapper item : fixtureGroup){
            MitooEnum.FixtureStatus fixtureType = item.getFixtureType();
            if(fixtureType != MitooEnum.FixtureStatus.DELETED){
                fixtureHolder.addView(createFixtureRow(item));
            }
        }
        setDateForFixtureGroup(fixtureGroupContainer, fixtureGroup);
        return fixtureGroupContainer;
    }

    public void setDateForFixtureGroup(RelativeLayout fixtureGroupContainer , List<FixtureWrapper> fixtureGroup){

        if(fixtureGroup.size()>0){
            TextView dateTextView = (TextView) fixtureGroupContainer.findViewById(R.id.dateTextField);
            String fixtureDate = fixtureGroup.get(0).getLongDisplayableDate();
            dateTextView.setText(fixtureDate);
        }
    }

    public RelativeLayout createFixtureRow(FixtureWrapper fixture){

        RelativeLayout fixtureRow = (RelativeLayout)getViewHelper().createViewFromInflator(R.layout.view_fixture_row);
        customizeFixtureRow(fixtureRow, fixture);
        return fixtureRow;
    }

    private void setUpFixtureRowTeamTextView(View row, FixtureWrapper fixture){

        TextView leftTeamTextView = (TextView)row.findViewById(R.id.leftTeamName);
        TextView rightTeamTextView = (TextView)row.findViewById(R.id.rightTeamName);

        Team homeTeam = getDataHelper().getTeam(fixture.getFixture().getHome_team_id());
        Team awayTeam = getDataHelper().getTeam(fixture.getFixture().getAway_team_id());

        setUpTeamName(homeTeam, leftTeamTextView);
        setUpTeamName(awayTeam, rightTeamTextView);


    }

    private void setUpTeamName(Team team ,TextView textView) {

        if (team != null)
            textView.setText(team.getName());
        else
            setTextViewAsTBC(textView);

    }

    private void setTextViewAsTBC(TextView textView){

        textView.setText(getActivity().getString(R.string.fixture_page_tbd));
        textView.setTextAppearance(getActivity(), R.style.schedulePageTBCText);

    }

    private void setUpFixtureStamp(View row, FixtureWrapper fixture){

        RelativeLayout alphaContainer = (RelativeLayout) row.findViewById(R.id.alphaContainer);
        ImageView stampView= (ImageView) row.findViewById(R.id.stampIcon);
        float alphaValue = getActivity().getDataHelper().getFloatValue(R.dimen.low_alpha);
        switch(fixture.getFixtureType()){

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
            case CANCELED:
                alphaContainer.setAlpha(alphaValue);
                stampView.setImageResource(R.drawable.cancelled_stamp);
                break;
            case RESCHEDULED:
                alphaContainer.setAlpha(alphaValue);
                stampView.setImageResource(R.drawable.rescheduled_stamp);
                break;
            default:
                break;

        }
    }

    private void setUpFixtureTeamIcons(View row , FixtureWrapper fixture ) {

        ImageView leftTeamIcon = (ImageView) row.findViewById(R.id.leftTeamIcon);
        ImageView rightTeamIcon = (ImageView) row.findViewById(R.id.rightTeamIcon);
        Team homeTeam = getDataHelper().getTeam(fixture.getFixture().getHome_team_id());
        Team awayTeam = getDataHelper().getTeam(fixture.getFixture().getAway_team_id());
        loadTeamIcon(leftTeamIcon, getTeamLogo(homeTeam));
        loadTeamIcon(rightTeamIcon, getTeamLogo(awayTeam));

    }

    private void loadTeamIcon(ImageView imageView, String iconUrl){

        if(iconUrl!= null && imageView !=null){
            getViewHelper().getPicasso().with(getActivity())
                    .load(iconUrl)
                    .error(R.drawable.team_logo_tbc)
                    .into(imageView);
        }
    }

    private void setUpFixtureCenterText(View row , FixtureWrapper fixture ) {

        TextView centerText = (TextView) row.findViewById(R.id.middleTextField);
        MitooEnum.TimeFrame fixtureTimeFrame = getDataHelper().getTimeFrame(fixture.getFixtureDate());
        switch (fixtureTimeFrame) {
            case FUTURE:
                centerText.setTextAppearance(getActivity(), R.style.schedulePageTimeText);
                centerText.setText(fixture.getDisplayableTime());
                break;
            case PAST:
                centerText.setTextAppearance(getActivity(), R.style.schedulePageScoreText);
                centerText.setText(fixture.getDisplayableScore());
                break;
            default:
                break;
        }
    }

    public void customizeFixtureRow(View row , FixtureWrapper fixture){

        setUpFixtureRowTeamTextView(row, fixture);
        setUpFixtureStamp(row, fixture);
        setUpFixtureCenterText(row, fixture);
        setUpFixtureTeamIcons(row, fixture);
        setUpFixtureOnClickListener(row, fixture);

    }

    private void setUpFixtureOnClickListener(View row, FixtureWrapper fixture){

        row.setOnClickListener(createFixtureItemClickedListener(fixture));
    }

    private String getTeamLogo(Team team) {

        String result = null;
        if (team != null) {
            result = getViewHelper().getRetinaUrl(team.getLogo_small());
        }
        return result;
    }

    private MitooActivity getActivity(){
        return getViewHelper().getActivity();
    }

    private DataHelper getDataHelper(){
        return getViewHelper().getActivity().getDataHelper();
    }

    private void fixtureItemClickAction(FixtureWrapper fixture){

        FragmentChangeEvent event = FragmentChangeEventBuilder
                .getSingletonInstance()
                .setFragmentID(R.id.fragment_fixture)
                .setBundle(createBundle(fixture))
                .build();
        BusProvider.post(event);

    }

    private Bundle createBundle(FixtureWrapper fixture){
        Bundle bundle = new Bundle();
        bundle.putInt(getFixtureIdKey(), fixture.getFixture().getId());
        return bundle;
    }

    private String getFixtureIdKey(){
        return getActivity().getString(R.string.bundle_key_fixture_id_key);
    }
}
