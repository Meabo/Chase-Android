package project.chasemvp.Application.Dagger.Modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import project.chasemvp.Model.MapAPI;
import project.chasemvp.Model.SocketModel;
import project.chasemvp.Presenters.LocationPresenter;

/**
 * Created by Mehdi on 04/08/2017.
 */
@Module(includes = {SocketModule.class, NetworkModule.class})
public class LocationModule
{
    @Provides
    @Singleton
    LocationPresenter provideLocationPresenter(MapAPI mapAPI, SocketModel socketModel )
    {
        return new LocationPresenter(mapAPI, socketModel) ;
    }

}
