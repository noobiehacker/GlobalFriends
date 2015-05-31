package co.mitoo.sashimi.utils;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.views.fragments.*;

/**
 * Created by david on 14-12-12.
 */
public class FragmentFactory {
    private static FragmentFactory instance;

    public static FragmentFactory getInstance(){
        if(instance == null)
            instance = new FragmentFactory();
        return instance;
    }

    private FragmentFactory(){
    }

    public MitooFragment buildFragment(FragmentChangeEvent event) {
        MitooFragment result = createFragment(event.getFragmentId());
        if (event.getBundle() != null)
            result.setArguments(event.getBundle());
        return result;
    }

    public MitooFragment createFragment(int fragmentID){
        MitooFragment result = null;
        switch (fragmentID) {
            case R.id.fragment_landing:
                result = LandingFragment.newInstance();
                break;
            case R.id.fragment_splash:
                result = SplashScreenFragment.newInstance();
                break;
            case R.id.fragment_interest_confirm:
                result = InterestConfirmFragment.newInstance();
                break;
            case R.id.fragment_connect:
                result = ConnectFragment.newInstance();
                break;
            case R.id.fragment_location_selection:
                result = LocationSelectionFragment.newInstance();
                break;
            case R.id.fragment_map:
                result = MapFragment.newInstance();
                break;
            case R.id.fragment_option:
                result = OptionsFragment.newInstance();
                break;
            case R.id.fragment_result:
                result = ResultFragment.newInstance();
                break;
            case R.id.fragment_selection:
                result = SelectionFragment.newInstance();
                break;
            case R.id.fragment_home:
                result = HomeFragment.newInstance();
                break;

          /*  case R.id.fragment_competition:
                result = CompetitionSeasonFragment.newInstance();
                break;
            case R.id.fragment_notification:
                result = NotificationFragment.newInstance();
                break;
            case R.id.fragment_confirm_account:
                result = ConfirmAccountFragment.newInstance();
                break;
            case R.id.fragment_confirm_set_password:
                result = ConfirmSetPasswordFragment.newInstance();
                break;
            case R.id.fragment_confirm_done:
                result = ConfirmDoneFragment.newInstance();
                break;
            case R.id.fragment_fixture:
                result = FixtureFragment.newInstance();
                break;
            case R.id.fragment_pre_login:
                result = PreLoginFragment.newInstance();
                break;
            case R.id.fragment_pre_confirm:
                result = PreConfirmFragment.newInstance();
            case R.id.fragment_standings:
                result = StandingsFragment.newInstance();
                break;*/
            default:
                result = SplashScreenFragment.newInstance();
        }
        return result;
    }

    public MitooFragment createTabFragment(int fragmentID , MitooEnum.FixtureTabType tabType){
        MitooFragment result = null;
        switch (fragmentID) {
/*            case R.id.fragment_competition_tab:
                result = CompetitionSeasonTabFragment.newInstance(tabType);
                break;*/
            default:
                result = SplashScreenFragment.newInstance();
        }
        return result;
    }
}
