package project.chasemvp.Model.Services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import project.chasemvp.Model.Events.ErrorEvent;
import project.chasemvp.Model.Events.GoogleMapsEvent;
import project.chasemvp.Model.POJO.GoogleMapResult.GoogleMapResult;
import project.chasemvp.Presenters.LocationPresenter;
import project.chasemvp.R;

import static project.chasemvp.Utils.Constants.debug;

/**
 * Created by Mehdi on 26/10/2017.
 */

public class LocationTrackingService extends Service
{
    /** Location Stuff **/

    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 4000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;


    public Location getmCurrentLocation() {
        Log.d(debug, mCurrentLocation + " " + mLastLocation);

        return mCurrentLocation == null ? mLastLocation : mCurrentLocation;
    }

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;

    public Boolean getmRequestingLocationUpdates() {
        return mRequestingLocationUpdates;
    }

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private Boolean mRequestingLocationUpdates = false;

    /** End of Location Stuff **/


    @Inject EventBus eventBus;
    @Inject LocationPresenter locationPresenter;

    Activity activity;
    Boolean game_began = false;



    boolean multi = false;

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    Context context;
    Location mLastLocation;
    private boolean mRunning;
    private final IBinder mBinder = new MyBinder();




    public class MyBinder extends Binder {
        public LocationTrackingService getService() {
            return LocationTrackingService.this;
        }
    }


    public void BindPresenter(LocationPresenter p)
    {
        if (p != null)
            locationPresenter = p;

        InitSocketAndLocation();
    }

    public void calculateitinary(LatLng dest)
    {
        //this.dest = dest;
        Observable<GoogleMapResult> distanceObservable = locationPresenter.getMapAPI().getMapService().getDistanceDuration("metric", getmCurrentLocation().getLatitude() + "," + getmCurrentLocation().getLongitude(), dest.latitude + "," + dest.longitude, "walking");
        distanceObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers
                .mainThread()).subscribe(new Observer<GoogleMapResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GoogleMapResult gmr)
            {
                Log.d("chasedebug", gmr + "");
                EventBus.getDefault().post(new GoogleMapsEvent(gmr));
            }

            @Override
            public void onError(Throwable e)
            {
                EventBus.getDefault().post(new ErrorEvent("Error fetching data, please check your internet connection"));
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }




    public void UnBindPresenter()
    {
        locationPresenter = null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        mRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        Log.d(debug, "onStartCommand LocationTracking");
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }


    public void InitSocketAndLocation()
    {
        if (!locationPresenter.getSocket().connected())
            locationPresenter.getSocket().connect();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(locationPresenter.getActivity());
        mSettingsClient = LocationServices.getSettingsClient(locationPresenter.getActivity());
        mRequestingLocationUpdates = true;
        //Log.d(debug, "Class is " + mvpView.getClass());
        InitLocationStuff();
    }

    private void InitLocationStuff()
    {
        Log.d(debug, "Init Stuff");
        createLocationRequest();
        createLocationCallback();
        buildLocationSettingsRequest();
    }

    private void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback()
    {

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                super.onLocationResult(locationResult);
                //  Log.d(debug, "Location callback");

                //Log.d(debug, "mCurrentLocation is " + mCurrentLocation);
                String event = multi ? "location_multi" : "location";
               // Log.d(debug, event);
                locationPresenter.getMvpView().updateLocationOnMap(locationResult.getLastLocation());
                locationPresenter.getSocket().emit(event, locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());

                if (locationPresenter.getGame_began())
                {
                    if (mCurrentLocation != null)
                    {
                        double elapsedTime = (locationResult.getLastLocation().getTime() - mCurrentLocation.getTime()) / 1000; // Convert milliseconds to seconds
                        double calculatedSpeed = mCurrentLocation.distanceTo(locationResult.getLastLocation()) / elapsedTime;
                        Double speed = locationResult.getLastLocation().hasSpeed() ? locationResult.getLastLocation().getSpeed() : calculatedSpeed;
                        Log.d(debug, "Speed is " + speed);
                        locationPresenter.getMvpView().updateSpeed(speed);
                    }
                }

                mCurrentLocation = locationResult.getLastLocation();
                //mCurrentLocation = location;
                Log.d(debug, "My Current Location is : " + mCurrentLocation.getLatitude() + " and "  + mCurrentLocation.getLongitude());
                //mCurrentLocation);

                //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                //updateLocationUI();
            }
        };
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest()
    {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    public boolean checkPermissions()
    {
        int permissionState = ActivityCompat.checkSelfPermission(locationPresenter.getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions()
    {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(locationPresenter.getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(debug, "Displaying permission rationale to provide additional context.");
            locationPresenter.getMvpView().showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(locationPresenter.getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(debug, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(locationPresenter.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }




    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode)
        {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        // All required changes were successfully made
                        //mGoogleApiClient.connect();
                        locationPresenter.getMvpView().generateMap(mLastLocation);
                        Toast.makeText(context, "Location enabled by user!", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(context, "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();

                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
        }
    }


    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    public void startLocationUpdates()
    {
        Log.d(debug, "Start Location updates");
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(locationPresenter.getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(debug, "All location settings are satisfied.");

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                        Log.d(debug, "Success StartLocationUpdates");
                        locationPresenter.getMvpView().generateMap(mLastLocation);

                        //Log.d(debug, "Success OnResult Enable GPS");



                    }
                })
                .addOnFailureListener(locationPresenter.getActivity(), new OnFailureListener() {
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
                                    rae.startResolutionForResult(locationPresenter.getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(debug, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(debug, errorMessage);
                                Toast.makeText(locationPresenter.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }

                        //updateUI();
                    }
                });
    }


    /**
     * Removes location updates from the FusedLocationApi.
     */
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
                .addOnCompleteListener(locationPresenter.getActivity(), task ->
                {
                    mRequestingLocationUpdates = false;
                    Log.d(debug, "Location updates stopped");
                })
                .addOnFailureListener(locationPresenter.getActivity(), e -> Log.e(debug, "OnFailureListener"));;
        //EventBus.getDefault().postSticky(new LocationEvent("stop"));

                /**/


    }


    public LatLng getmyloc() {
        LatLng l = null;
        if (mCurrentLocation != null)
            l = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        return l;
    }

    public void begin_game_or_deny(String gameid)
    {
        locationPresenter.getSocketModel().sologame(gameid, getmCurrentLocation().getLatitude(), getmCurrentLocation().getLongitude());
    }


}
