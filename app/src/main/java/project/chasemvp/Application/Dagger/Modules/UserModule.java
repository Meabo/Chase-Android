package project.chasemvp.Application.Dagger.Modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mehdi on 16/06/2017.
 */
@Module
public class UserModule
{

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application)
    {
        return application.getSharedPreferences("user", Context.MODE_PRIVATE);
    }

}

