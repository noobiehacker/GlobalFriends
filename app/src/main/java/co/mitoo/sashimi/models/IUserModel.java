package co.mitoo.sashimi.models;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.utils.events.JoinRequestEvent;
import co.mitoo.sashimi.utils.events.LoginRequestEvent;
import co.mitoo.sashimi.utils.events.ResetPasswordRequestEvent;

/**
 * Created by david on 14-11-11.
 */
public interface IUserModel {
    @Subscribe
    public void onLoginAttempt(LoginRequestEvent event);

    @Subscribe
    public void onJoinAttempt(JoinRequestEvent event);

    @Subscribe public void onResetPasswordAttempt(ResetPasswordRequestEvent event) ;

    void removeReferences();
}
