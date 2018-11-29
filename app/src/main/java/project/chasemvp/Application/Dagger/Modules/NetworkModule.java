package project.chasemvp.Application.Dagger.Modules;

import com.facebook.CallbackManager;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import project.chasemvp.Model.ChasesAPI;
import project.chasemvp.Model.MapAPI;
import project.chasemvp.Model.PlayerAPI;
import project.chasemvp.Model.RoomAPI;
import project.chasemvp.Utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mehdi on 14/06/2017.
 */
@Module
public class NetworkModule
{

    @Provides
    @Named("apiUrl")
    String provideApiUrl() {
        return Constants.url_api;
    }

    @Provides
    @Named("serverUrl")
    String provideServerUrl() {
        return Constants.url;
    }

    @Provides
    @Named("googleMapsUrl")
    String provideGoogleMapsUrl()
    {
        return "https://maps.googleapis.com/maps/";
    }

    @Provides
    @Singleton
    @Named("RetrofitAPI")
    Retrofit providesRetrofitBuilderAPI(@Named("apiUrl") String apiUrl)
    {
       return new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    @Provides
    @Singleton
    @Named("RetrofitURL")
    Retrofit providesRetrofitBuilderURL(@Named("serverUrl") String serverUrl)
    {
        return new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @Named("RetrofitGoogleMaps")
    Retrofit providesRetrofitBuilderGoogleMaps(@Named("googleMapsUrl") String googleMapsUrl)
    {
        return new Retrofit.Builder()
                .baseUrl(googleMapsUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    @Provides
    @Singleton
    ChasesAPI provideChasesAPI(@Named("RetrofitAPI") Retrofit retrofit)
    {
        return new ChasesAPI(retrofit);
    }

    @Provides
    @Singleton
    PlayerAPI provideFacebookToken(@Named("RetrofitURL") Retrofit retrofit)
    {
        return new PlayerAPI(retrofit);
    }


    @Provides
    @Singleton
    RoomAPI provideRoomAPI(@Named("RetrofitAPI") Retrofit retrofit)
    {
        return new RoomAPI(retrofit);
    }


    @Provides
    @Singleton
    MapAPI provideMapAPI(@Named("RetrofitGoogleMaps") Retrofit retrofit)
    {
        return new MapAPI(retrofit);
    }

    @Provides
    @Singleton
    CallbackManager providesCallBackManager()
    {
       return CallbackManager.Factory.create();
    }

}
