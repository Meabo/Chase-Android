package project.chasemvp.Application;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;
import project.chasemvp.Application.Dagger.Components.AppComponent;
import project.chasemvp.Application.Dagger.Components.DaggerAppComponent;
import project.chasemvp.Application.Dagger.Modules.AppModule;
import project.chasemvp.Application.Dagger.Modules.LocationModule;
import project.chasemvp.Application.Dagger.Modules.NetworkModule;
import project.chasemvp.Application.Dagger.Modules.PicassoModule;
import project.chasemvp.Application.Dagger.Modules.SocketModule;
import project.chasemvp.Application.Dagger.Modules.UserModule;

/**
 * Created by Mehdi on 12/06/2017.
 */

public class ChaseApplication extends Application
{
    private AppComponent appComponent;

    public void onCreate()
    {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Paper.init(this);

        appComponent = DaggerAppComponent.builder()
                // list of modules that are part of this component need to be created here too
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .socketModule(new SocketModule())
                .userModule(new UserModule())
                .picassoModule(new PicassoModule())
                .locationModule(new LocationModule())
                .build();

        Iconify.with(new FontAwesomeModule());
    }


    public AppComponent getAppComponent() {
        return appComponent;
    }
    private void GetPackageHash()
    {
        try {

            PackageInfo info =
                    getPackageManager().getPackageInfo("project.chasemvp",
                            PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}

