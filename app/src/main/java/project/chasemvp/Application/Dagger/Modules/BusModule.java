package project.chasemvp.Application.Dagger.Modules;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mehdi on 01/08/2017.
 */
@Module
public class BusModule
{
    @Provides
    @Singleton
    EventBus providesEventBus()
    {
        return EventBus.getDefault();
    }
}
