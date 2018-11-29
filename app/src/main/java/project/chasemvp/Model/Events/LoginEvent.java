package project.chasemvp.Model.Events;

import project.chasemvp.Model.POJO.AccessToken;

/**
 * Created by Mehdi on 12/06/2017.
 */

public class LoginEvent
{
    AccessToken token;

    public LoginEvent(){}
    public LoginEvent(AccessToken a)
    {
        token = a;
    }

    public AccessToken getToken() {
        return token;
    }
}
