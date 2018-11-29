package project.chasemvp.UI.Interfaces.Presenters;

import project.chasemvp.Model.Services.AuthService;

/**
 * Created by Mehdi on 19/06/2017.
 */

public interface SplashPresenterInterface
{
    void attach(AuthService s);
    void detach();
    void auth(AuthService s);

}
