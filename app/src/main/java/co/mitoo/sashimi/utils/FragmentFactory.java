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
        MitooFragment result = null;
        switch (event.getFragmentId()) {
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
            case R.id.fragment_league:
                result = LeagueFragment.newInstance();
                break;
            case R.id.fragment_settings:
                result = SettingsFragment.newInstance();
                break;
            case R.id.fragment_search_results:
                result = SearchResultsFragment.newInstance();
                break;
            case R.id.fragment_confirm:
                result = ConfirmFragment.newInstance();
                break;
            case R.id.fragment_home:
                result = HomeFragment.newInstance();
                break;
            case R.id.fragment_feed_back:
                result = FeedBackFragment.newInstance();
                break;
            default:
                result = SplashScreenFragment.newInstance();
        }
        if (event.getBundle() != null)
            result.setArguments(event.getBundle());
        return result;
    }
}
