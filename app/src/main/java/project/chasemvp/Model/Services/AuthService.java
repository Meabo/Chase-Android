package project.chasemvp.Model.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import javax.inject.Inject;

import project.chasemvp.Application.ChaseApplication;
import project.chasemvp.Model.MySharedPreferences;
import project.chasemvp.Model.SocketModel;

import static project.chasemvp.Utils.Constants.debug;

/**
 * Created by Mehdi on 19/06/2017.
 */

public class AuthService  extends Service
{
    private boolean isRunning = false;
    @Inject SocketModel socketmodel;
    @Inject MySharedPreferences pref;
    private final IBinder mBinder = new LocalBinder();


    public class LocalBinder extends Binder {
        public AuthService getService() {
            // Return this instance of LocalService so clients can call public methods
            return AuthService.this;
        }
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d(debug, "Service onCreate");
        ((ChaseApplication) getApplication()).getAppComponent().inject(this);
        
        isRunning = true;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }


    public void auth()
    {
        socketmodel.auth(pref.get("token"), pref.get("userid"));
        Log.d(debug, "Begin Authenticated" );

    }

}
