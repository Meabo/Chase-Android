package project.chasemvp.Application.Dagger.Components;

import javax.inject.Singleton;

import dagger.Component;
import project.chasemvp.Application.Dagger.Modules.AppModule;
import project.chasemvp.Application.Dagger.Modules.BusModule;
import project.chasemvp.Application.Dagger.Modules.LocationModule;
import project.chasemvp.Application.Dagger.Modules.NetworkModule;
import project.chasemvp.Application.Dagger.Modules.PicassoModule;
import project.chasemvp.Application.Dagger.Modules.SocketModule;
import project.chasemvp.Application.Dagger.Modules.UserModule;
import project.chasemvp.Model.Services.AuthService;
import project.chasemvp.Model.Services.GpsService;
import project.chasemvp.Model.Services.NetworkService;
import project.chasemvp.Presenters.ChoosePseudoPresenter;
import project.chasemvp.Presenters.SplashPresenter;
import project.chasemvp.UI.Activities.ChoosePseudo;
import project.chasemvp.UI.Activities.Discover;
import project.chasemvp.UI.Activities.MainActivity;
import project.chasemvp.UI.Activities.Map;
import project.chasemvp.UI.Activities.Room;
import project.chasemvp.UI.Activities.Score;
import project.chasemvp.UI.Activities.SoloMap;
import project.chasemvp.UI.Activities.SplashScreenActivity;
import project.chasemvp.UI.Fragments.ChasesFragment;
import project.chasemvp.UI.Fragments.DetailFragment;
import project.chasemvp.Utils.Dialogs.DiscoverDialog;

/**
 * Created by Mehdi on 11/06/2017.
 */
@Component(modules = {AppModule.class, NetworkModule.class, SocketModule.class, UserModule.class, PicassoModule.class, BusModule.class, LocationModule.class})
@Singleton
public interface AppComponent
{
    void inject(MainActivity activity);
    void inject(SplashScreenActivity activity);
    void inject (SplashPresenter splashPresenter);
    void inject(ChasesFragment fragment);
    void inject(AuthService service);
    void inject(ChoosePseudo choosePseudo);
    void inject (ChoosePseudoPresenter choosePseudoPresenter);
    void inject (Discover discover);
    void inject(Room room);
    void inject(DetailFragment fragment);
    void inject(Score score);
    void inject (GpsService gpsService);
    void inject (NetworkService networkService);
    void inject (DiscoverDialog discoverDialog);

    void inject (SoloMap solomapactivity);
    void inject(Map mapactivity);


}
