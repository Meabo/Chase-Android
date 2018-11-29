package project.chasemvp.Model.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import project.chasemvp.Application.ChaseApplication;
import project.chasemvp.Model.Events.GPS.EventGPS;

import static project.chasemvp.Utils.Constants.debug;

/**
 * Created by Mehdi on 01/08/2017.
 */

public class GpsService extends BroadcastReceiver
{
    @Inject EventBus eventBus;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d(debug, "ENTERED");
        ChaseApplication app = (ChaseApplication) context.getApplicationContext();
        app.getAppComponent().inject(this);

        try
        {
            int locationMode = android.provider.Settings.Secure.getInt(context.getContentResolver(), android.provider.Settings.Secure.LOCATION_MODE);
            switch (locationMode)
            {
                case android.provider.Settings.Secure.LOCATION_MODE_HIGH_ACCURACY :
                    eventBus.postSticky(new EventGPS(true));
                    //Toast.makeText(context, "GPS High", Toast.LENGTH_LONG).show();

                    break;

                case android.provider.Settings.Secure.LOCATION_MODE_SENSORS_ONLY :
                    eventBus.postSticky(new EventGPS(true));
                    break;


                default:
                    eventBus.post(new EventGPS(false));
                    break;

            }
        } catch (android.provider.Settings.SettingNotFoundException e)
        {
            e.printStackTrace();
        }
    }



}
