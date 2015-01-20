package co.mitoo.sashimi.utils;

import android.app.Fragment;
import co.mitoo.sashimi.R;
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

    public Fragment buildFragment(int id){
        switch(id){
            case R.id.fragment_landing:
                return LandingFragment.newInstance();
            case R.id.fragment_splash:
                return SplashScreenFragment.newInstance();
            case R.id.fragment_join:
                return JoinFragment.newInstance();
            case R.id.fragment_login:
                return LoginFragment.newInstance();
            case R.id.fragment_reset_password:
                return ResetPasswordFragment.newInstance();
            case R.id.fragment_search:
                return SearchFragment.newInstance();
            case R.id.fragment_league:
                return LeagueFragment.newInstance();
            case R.id.fragment_user_profile:
                return SettingsFragment.newInstance();
            case R.id.fragment_search_results:
                return SearchResultsFragment.newInstance();
            case R.id.fragment_confirm:
                return ConfirmFragment.newInstance();
            case R.id.fragment_home:
                return HomeFragment.newInstance();
            default:
                return SplashScreenFragment.newInstance();
        }
    }

}
