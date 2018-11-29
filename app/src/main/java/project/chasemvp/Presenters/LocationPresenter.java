package project.chasemvp.Presenters;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import io.socket.client.Socket;
import project.chasemvp.Model.MapAPI;
import project.chasemvp.Model.Services.LocationTrackingService;
import project.chasemvp.Model.SocketModel;
import project.chasemvp.UI.Interfaces.Presenters.Base.BasePresenter;
import project.chasemvp.UI.Interfaces.Presenters.Base.Presenter;
import project.chasemvp.UI.Interfaces.View.MapViewInterface;

import static project.chasemvp.Utils.Constants.debug;

/**
 * Created by Mehdi on 04/08/2017.
 */

public class LocationPresenter<T extends MapViewInterface> extends BasePresenter<T> implements Presenter<T>
{

    public MapAPI getMapAPI() {
        return mapAPI;
    }

    MapAPI mapAPI;

    public SocketModel getSocketModel() {
        return socketModel;
    }

    SocketModel socketModel;

    public LocationPresenter(MapAPI mapAPI, SocketModel socketModel)
    {
        this.mapAPI = mapAPI;
        this.socketModel = socketModel;
    }
    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;


    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private Boolean mRequestingLocationUpdates;


    private String mLastUpdateTime;
    Location mLastLocation;

    public Activity getActivity() {
        return activity;
    }

    Activity activity;

    @Override
    public T getMvpView() {
        return mvpView;
    }

    T mvpView;
    Context context;

    public Boolean getGame_began() {
        return game_began;
    }

    Boolean game_began = false;
    LatLng dest;

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    boolean multi = false;

    public LocationTrackingService getServicegps() {
        return servicegps;
    }

    LocationTrackingService servicegps;



    @Override
    public void attachView(T mvpView)
    {
        Log.d(debug, "Attached view " + mvpView.getClass());
        super.attachView(mvpView);

        this.activity = mvpView.getActivity();
        this.context = activity.getApplicationContext();
        this.mvpView = mvpView;


        //context.startService(new Intent(context, LocationTrackingService.class));
       // servicegps.BindPresenter(this);
        //servicegps.onStartCommand(null,0,0);
    }
/*
    public void InitSocketAndLocation()
    {
        if (!socketModel.getSocket().connected())
        socketModel.connect(activity);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mSettingsClient = LocationServices.getSettingsClient(activity);
        mRequestingLocationUpdates = true;
        Log.d(debug, "Class is " + mvpView.getClass());
        InitLocationStuff();
    }
    */

    @Override
    public void detachView()
    {
        //stopLocationUpdates();
        super.detachView();
        //servicegps.stopLocationUpdates();
        //stopLocationUpdates();
        Log.d(debug, "Detach Presenter " + mvpView.getClass() + " from view");
        //socketModel.getSocket().off();
    }


/*
    private void InitLocationStuff()
    {
        Log.d(debug, "Init Stuff");
        createLocationRequest();
        createLocationCallback();
        buildLocationSettingsRequest();
    }
*/









    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    /*
   public void startLocationUpdates()
    {
        Log.d(debug, "Start Location updates");
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(debug, "All location settings are satisfied.");

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                        Log.d(debug, "Success StartLocationUpdates");
                        mvpView.generateMap(mLastLocation);

                        //Log.d(debug, "Success OnResult Enable GPS");



                    }
                })
                .addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(debug, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    //Log.d(debug, activity.getLocalClassName());
                                    rae.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(debug, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(debug, errorMessage);
                                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }

                        //updateUI();
                    }
                });
    }


    /**
     * Removes location updates from the FusedLocationApi.
     */
    /*
    public void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(debug, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }


        Log.d(debug, "In Stop Location");
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(activity, task ->
                {
                    mRequestingLocationUpdates = false;
                    Log.d(debug, "Location updates stopped");
                })
                .addOnFailureListener(activity, e -> Log.e(debug, "OnFailureListener"));;
        //EventBus.getDefault().postSticky(new LocationEvent("stop"));

                /**/



    public void setGame_began(boolean game_began) {
        this.game_began = game_began;
    }




    public Socket getSocket()
    {
        return socketModel.getSocket();
    }

    //Discover
    public void init_discover_map() {

        socketModel.discover();
        Log.d(debug, "Called Discover");
    }
    public void quit_before()
    {
        socketModel.quit_solo_game();
    }

    public void disable_map_events()
    {
        Log.d(debug, "Disable Socket events");
        socketModel.disable_listeners_game();
    }


    public void unsubscribe_room()
    {
        socketModel.getSocket().emit("unsubscribe");
    }

    // MultiPlayer
    public void init_socket_listener()
    {
        socketModel.listen_game();
        Log.d(debug, "Called socket");

    }
}
