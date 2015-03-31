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
            case R.id.fragment_sign_up:
                result = SignUpFragment.newInstance();
                break;
            case R.id.fragment_login:
                result = LoginFragment.newInstance();
                break;
            case R.id.fragment_reset_password:
                result = ResetPasswordFragment.newInstance();
                break;
            case R.id.fragment_search:
                result = SearchFragment.newInstance();
                break;
            case R.id.fragment_location_search:
                result = LocationSearchFragment.newInstance();
                break;
            case R.id.fragment_search_results:
                result = SearchResultsFragment.newInstance();
                break;
            case R.id.fragment_league:
                result = LeagueFragment.newInstance();
                break;
            case R.id.fragment_settings:
                result = SettingsFragment.newInstance();
                break;
            case R.id.fragment_sign_up_confirm:
                result = SignUpDoneFragment.newInstance();
                break;
            case R.id.fragment_home:
                result = HomeFragment.newInstance();
                break;
            case R.id.fragment_feed_back:
                result = FeedBackFragment.newInstance();
                break;
            case R.id.fragment_about_mitoo:
                result = AboutMitooFragment.newInstance();
                break;
            case R.id.fragment_fixture:
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
            default:
                result = SplashScreenFragment.newInstance();
        }
        return result;
    }

    public MitooFragment createTabFragment(int fragmentID , MitooEnum.FixtureTabType tabType){
        MitooFragment result = null;
        switch (fragmentID) {
            case R.id.fragment_fixture_tab:
                result = CompetitionSeasonTabFragment.newInstance(tabType);
                break;
            default:
                result = SplashScreenFragment.newInstance();
        }
        return result;
    }
}
