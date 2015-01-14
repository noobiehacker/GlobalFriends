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
    public void onLoginRequest(LoginRequestEvent event);

    @Subscribe
    public void onJoinRequest(JoinRequestEvent event);

    @Subscribe public void onResetPasswordRequest(ResetPasswordRequestEvent event) ;

    void removeReferences();
}
