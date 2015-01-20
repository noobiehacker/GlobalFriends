package co.mitoo.sashimi.models.jsonPojo;
import static org.mockito.Mockito.*;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.send.Login;
import co.mitoo.sashimi.utils.StaticString;

/**
 * Created by david on 14-11-19.
 */
public class MockPojo {


    public static Login getLogin(){
        Login login = new Login("abc@mitoo.com" , "abc123");
        return login;
    }
    public static Session getSession(){
        Session session = new Session();
        session.auth_token ="N2XjEx4Pexo61cpY2RYL";
        session.id =7;
        session.email ="abc@mitoo.com";
        return session;

    }

    public static List<League> getLeagueList(){
        List<League> league = new ArrayList<League>();
        for(int i = 0 ; i < 10 ; i ++) {
            League comp = new League();
            comp.setName( "Mock League" + i);
            league.add(comp);
        }
        return league;
    }

    public static List<Sport> getSportList(){
        List<Sport> sport = new ArrayList<Sport>();
        for(int i = 0 ; i < 100 ; i ++) {
            Sport item = new Sport();
            sport.add(item);
        }
        return sport;
    }

    public static Resources getResources(){
        Resources resources = mock(Resources.class);
        when(resources.getString(R.string.steak_apiary_end_point)).thenReturn(StaticString.steakApiaryEndPoint);
        return resources;
    }
}
