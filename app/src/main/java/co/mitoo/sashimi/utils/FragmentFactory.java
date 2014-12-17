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
                return CompetitionFragment.newInstance();
            case R.id.fragment_competition:
                return CompetitionFragment.newInstance();
            default:
                return SplashScreenFragment.newInstance();
        }
    }

}
