package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.send.Login;

/**
 * Created by david on 14-11-26.
 */
public class LoginRequestEvent {

    private Login login;

    public LoginRequestEvent(String email, String password){
        this.login = new Login(email,password);
    }

    public Login getLogin() {
        return login;
    }
}
