package project.chasemvp.UI.Activities;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.chasemvp.Application.ChaseApplication;
import project.chasemvp.BuildConfig;
import project.chasemvp.Model.Events.ErrorEvent;
import project.chasemvp.Model.Events.GPS.EventGPS;
import project.chasemvp.Model.Events.GPS.EventGPSPlay;
import project.chasemvp.Model.Events.GoogleMapsEvent;
import project.chasemvp.Model.Events.Socket.SocketEventDistance;
import project.chasemvp.Model.Events.Socket.SocketEventGuardian;
import project.chasemvp.Model.Events.Socket.SocketEventListenToGuardian;
import project.chasemvp.Model.Events.Socket.SocketEventObject;
import project.chasemvp.Model.Events.Socket.SocketEventOthersLocation;
import project.chasemvp.Model.Events.Socket.SocketEventScore;
import project.chasemvp.Model.Events.Socket.SocketEventSkill;
import project.chasemvp.Model.Events.Socket.SocketEventSteal;
import project.chasemvp.Model.Events.Socket.SocketEventTime;
import project.chasemvp.Model.MySharedPreferences;
import project.chasemvp.Model.Services.LocationTrackingService;
import project.chasemvp.Presenters.LocationPresenter;
import project.chasemvp.R;
import project.chasemvp.UI.Interfaces.View.MapViewInterface;
import project.chasemvp.Utils.CustomTextView;
import project.chasemvp.Utils.Dialogs.DiscoverDialog;
import project.chasemvp.Utils.Dialogs.MultiPlayerDialog;

import static project.chasemvp.Utils.Constants.debug;

/**
 * Created by Mehdi on 13/06/2017.
 */

public class Map extends AppCompatActivity implements MapViewInterface, OnMapReadyCallback, ServiceConnection
{

    @BindView(R.id.constraintlayout_main) ConstraintLayout mainLayout;
    @BindView(R.id.mapview)  MapView mapView;

    @BindView(R.id.map_distance_value) CustomTextView map_distance_value;
    @BindView(R.id.map_score_value) CustomTextView map_score_value;
    @BindView(R.id.map_current_time) CustomTextView map_current_time;
    @BindView(R.id.map_current_speed) CustomTextView map_current_speed;

    @BindView(R.id.map_guardian_pseudo) CustomTextView map_guardian_pseudo;
    @BindView(R.id.map_my_pseudo) CustomTextView map_my_pseudo;

    @BindView(R.id.map_button_itinary) FloatingActionButton map_button_itinary;
    @BindView(R.id.map_button_catch) FloatingActionButton map_button_catch;
    @BindView(R.id.map_button_steal) FloatingActionButton map_button_steal;
    @BindView(R.id.map_button_skill) CircularImageView map_button_skill;

    @BindView(R.id.map_user_image) CircularImageView map_my_image;
    @BindView(R.id.map_guardian_image) CircularImageView map_guardian_image;

    @Inject MySharedPreferences pref;
    @Inject
    LocationPresenter locationPresenter;

    LocationTrackingService servicegps;

    GoogleMap mMap;
    Location initial;
    Marker me;
    HashMap<String, Marker> MarkersOnMap = new HashMap<>();
    Polyline line;
    boolean game_finished = false;
    boolean playing = false;
    MultiPlayerDialog dialog;
    PopupWindow popupWindow;


    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    DiscoverDialog welcome_dialog;

    @Override
    protected void onCreate(Bundle saved)
    {
        super.onCreate(saved);
        setContentView(R.layout.map);
        ((ChaseApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        //locationPresenter.getSocketModel().emit("pseudo", pref.get("pseudo"));
        dialog = new MultiPlayerDialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        welcome_dialog  = new DiscoverDialog(this, R.style.SplashTheme);

        Init_Head_Tab();
        CreateMap(saved);
        InitPopup();
    }

    private void Init_Head_Tab()
    {
        map_my_pseudo.setText(pref.get("pseudo"));
        Picasso.with(this)
                .load(pref.get("avatar_url"))
                .placeholder(R.mipmap.ic_launcher_round)
                .into(map_my_image);
    }


    private void CreateMap(Bundle saved)
    {
        mapView.onCreate(saved);
        mapView.onResume();
        map_button_itinary.setVisibility(View.GONE);
        locationPresenter.attachView(this);
        //locationPresenter.InitSocketAndLocation();

    }


    @Override
    public void onStart()
    {
        super.onStart();
        EventBus.getDefault().register(this);
        Intent intent = new Intent(this, LocationTrackingService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
        Log.d(debug,"onStart in Map");
    }

    @Override
    public void onStop()
    {
        super.onStop();
        EventBus.getDefault().unregister(this);
        //locationPresenter.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        Log.d(debug, "Activity onSavedInstance");

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();

        if (servicegps != null)
        {
            request_loc();
        }
    }


    @Override
    public void onDestroy()
    {
        mapView.onDestroy();
        Log.d(debug, "Activity onDestroy");
        Log.d(debug, "onDestroy " + getClass());
        EventBus.getDefault().unregister(this);
        if (!game_finished && playing)
        {
            Log.d(debug, "Quit before");
            locationPresenter.quit_before();

        }
        locationPresenter.detachView();
        super.onDestroy();

//        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventGPS eventGPS)
    {
        Log.d(debug, "Discover Dialog");
        if (eventGPS.isHigh_accuracy())
        {
            Log.d(debug, "GPS High");

            if (welcome_dialog != null)
                welcome_dialog.dismiss();
        }
        else
        {
            Log.d(debug, "GPS High");
            // locationPresenter.stopLocationUpdates();
            welcome_dialog.show();
            onStop();

        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventGPSPlay eventGPS)
    {
        if (eventGPS.isPlay())
        {
            onResume();
            //onResume();
        }
        else
        {
            finish();
        }

    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventDistance socketEventDistance)
    {
        UpdateDistance(socketEventDistance.getDistance());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventGuardian socketEventGuardian)
    {
        if (socketEventGuardian.isGot_object())
        ChangeState("Guardian", pref.get("pseudo"), pref.get("avatar_url"), 0, 0);
        else
            Toast.makeText(this, "You are too far", Toast.LENGTH_LONG).show();


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventListenToGuardian socketEventListenToGuardian)
    {

        Log.d(debug, "SocketEventListenToGuardian");
        ChangeState("Chaser", socketEventListenToGuardian.getPseudo(), socketEventListenToGuardian.getUrl_image(), socketEventListenToGuardian.getLatitude(), socketEventListenToGuardian.getLongitude());
    }


    private void BeginMainActivity()
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }


    public void open_dialog_before_leave()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure to leave ?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(getActivity(), "You clicked yes button", Toast.LENGTH_LONG).show();
                        BeginMainActivity();
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        open_dialog_before_leave();
    }


    private void InitPopup()
    {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupWindow = new PopupWindow(popupView, width, height, focusable);

    }


    private void ChangeState(String state, String pseudo, String url_image, double lat, double lng)
    {
        dialog.show_guardian(pseudo, url_image);
        Marker object = MarkersOnMap.get("object");
        if (object != null)
            object.remove();
        if (line != null)
            line.remove();

        map_guardian_pseudo.setText(pseudo);
        Picasso.with(this)
                .load(url_image)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(map_guardian_image);

        switch (state)
        {
            case "Chaser" :
                for (String s : MarkersOnMap.keySet())
                {
                    if (s.equals(pseudo))
                        MarkersOnMap.get(s).setVisible(true);
                    else
                        MarkersOnMap.get(s).setVisible(false);
                }

                /*Marker guardian = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
                guardian.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.target));*/
                //MarkersOnMap.put(pseudo, guardian);

                map_button_itinary.setVisibility(View.GONE);
                map_button_catch.setVisibility(View.GONE);
                map_button_skill.setVisibility(View.GONE);
                map_button_steal.setVisibility(View.VISIBLE);
                break;

            case "Guardian" :
                map_button_itinary.setVisibility(View.GONE);
                map_button_catch.setVisibility(View.GONE);
                map_button_skill.setVisibility(View.VISIBLE);
                map_button_steal.setVisibility(View.GONE);

                for (Marker m : MarkersOnMap.values())
                {
                    //m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.chasericon));
                    m.setVisible(true);
                }
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventObject socketEventObject)
    {
        Log.d(debug,"In Map");
        LatLng newposition = new LatLng(socketEventObject.getLatitude(), socketEventObject.getLongitude());
        MarkerOptions markerView = new MarkerOptions().position(newposition);
        Marker object = mMap.addMarker(markerView);
        object.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.surprise));
        MarkersOnMap.put("object", object);
        map_button_itinary.setVisibility(View.VISIBLE);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventOthersLocation socketEventOthersLocation)
    {
        Log.d(debug,"In OtherLocation of " + socketEventOthersLocation.getPseudo());

        //LatLng newposition = new LatLng(socketEventOthersLocation.getLatitude(), socketEventOthersLocation.getLongitude());
        Marker otherplayer = MarkersOnMap.get(socketEventOthersLocation.getPseudo());
        if (otherplayer != null)
            otherplayer.setPosition(socketEventOthersLocation.getMarkerOptions().getPosition());
        else
        {
            otherplayer = mMap.addMarker(socketEventOthersLocation.getMarkerOptions());
            MarkersOnMap.put(socketEventOthersLocation.getPseudo(), otherplayer);
            otherplayer.setIcon(BitmapDescriptorFactory.fromBitmap(socketEventOthersLocation.getBt()));
        }
    }




    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventScore socketEventscore)
    {
        Log.d(debug, "Score is " + socketEventscore.getScore());
        map_score_value.setText(Integer.toString(socketEventscore.getScore()));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventSkill socketEventSkill)
    {
        if (socketEventSkill.isSkill_used())
        {
            int s = socketEventSkill.getSkill();
            switch(s)
            {
                case 1 :  Skill_Disappear(socketEventSkill.getDuration(), socketEventSkill.getPseudo());
                    break;
                default:
                    break;

            }
        }
        else
        {
            Toast.makeText(this, "You are not the Guardian", Toast.LENGTH_LONG).show();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventSteal socketEventSteal)
    {
        map_score_value.setText(Integer.toString(socketEventSteal.getScore()));
        ChangeState("Guardian", pref.get("pseudo"), pref.get("avatar_url"), 0, 0 );
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventTime socketEventtime)
    {
        TimeCounter(socketEventtime.getTime());
        //locationPresenter.setGame_began(true);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ErrorEvent errorEvent)
    {
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(GoogleMapsEvent googleMapsEvent)
    {
        Log.d(debug,"In Map");
        //map_distance_value.setText(googleMapsEvent.getDistance());
        //map_time_value.setText(googleMapsEvent.getTime());

        line = mMap.addPolyline(new PolylineOptions()
                .addAll(googleMapsEvent.getPoly())
                .width(10)
                .color(Color.argb(200, 0, 0, 210))
                //.jointType(JointType.ROUND)
                .geodesic(true));
    }

    private void UpdateDistance(double d)
    {
        map_distance_value.setText(Double.toString(d));
    }

    private void Skill_Disappear(int duration, String pseudo)
    {
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        Marker m = MarkersOnMap.get(pseudo);
        m.setVisible(false);

        new CountDownTimer(duration * 1000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                m.setVisible(true);
            }
        }.start();

    }


    @Override
    public void generateMap(Location l)
    {
        initial = l;
        mapView.getMapAsync(this);
    }

    @Override
    public void updateLocationOnMap(Location location)
    {

        // double speed =  Math.round(location.getSpeed() / 3.6);
        //map_current_speed.setText(speed + " km/h");
        //  map_current_speed.setText(location.getSpeed() + " km/h");

     /*if (mMap != null)
       mMap.moveCamera( CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
       /* if (me != null)
        {
            LatLng b = new LatLng(location.getLatitude(), location.getLongitude());
            me.setPosition(b);
        }
        else
        {
            if (mMap != null)
                me = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_location)).flat(true));
        }
*/

    }

    @Override
    public void updateSpeed(Double speed)
    {
        map_current_speed.setText(Math.round(speed.doubleValue() * 3.6) + " km/h");
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public void begin_game_or_deny(String s) {

    }

    public void showSnackbar(final int mainTextStringId, final int actionStringId,
                             View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }


    private void TimeCounter(int time) {
        new CountDownTimer(time * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                map_current_time.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
                //here you can have your logic to set text to edittext
            }

            public void onFinish()
            {
                game_finished = true;
                locationPresenter.setGame_began(false);
                locationPresenter.disable_map_events();
                locationPresenter.unsubscribe_room();
                locationPresenter.detachView();
              //  locationPresenter.stopLocationUpdates();
                map_current_time.setText("Game Finished !");
                BeginScoreActivity();
            }

        }.start();
    }

    private void BeginScoreActivity()
    {
        Intent i = new Intent(this, Score.class);
        startActivity(i);
        finish();
    }

    @OnClick({R.id.map_button_catch, R.id.map_button_steal, R.id.map_button_itinary, R.id.map_button_skill})
    public void setMap_button_catch(View view)
    {
        switch(view.getId())
        {
            case R.id.map_button_itinary :
                Marker object = MarkersOnMap.get("object");
                LatLng l = object.getPosition();
                //serv.calculateitinary(object.getPosition());
                map_button_itinary.setVisibility(View.GONE);
                break;
            case R.id.map_button_catch :
                locationPresenter.getSocketModel().emit("calculatedistance_multi", servicegps.getmCurrentLocation().getLatitude(), servicegps.getmCurrentLocation().getLongitude());
                break;
            case R.id.map_button_steal:
               locationPresenter.getSocket().emit("stealobject_multi");
                break;
            case R.id.map_button_skill :
                locationPresenter.getSocket().emit("skill", 1);
                map_button_skill.setVisibility(View.GONE);
                break;

            default:
                break;
        }


    }

    private void animatecamera(LatLng l)
    {
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(l)
                        .zoom(15f)
                        .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }



    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.minimal));

        //animatecamera(new LatLng(initial.getLatitude(), initial.getLongitude()));
        locationPresenter.getSocket().emit("mapready");
        locationPresenter.init_socket_listener();
        playing = true;
        locationPresenter.setGame_began(true);


        // locationPresenter.startLocationUpdates();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(debug, "Request code is " + requestCode);
        if (requestCode == REQUEST_CHECK_SETTINGS)
        {
            Log.d(debug, "Request code is " + requestCode);
            servicegps.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.i(debug, "User chose not to make required location settings changes.");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(debug, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE)
        {
            if (grantResults.length <= 0)
            {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(debug, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (servicegps.getmRequestingLocationUpdates())
                {
                    Log.i(debug, "Permission granted, updates requested, starting location updates");
                    servicegps.startLocationUpdates();
                }
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    private void request_loc() {
        boolean requestingLocationupdates = servicegps.getmRequestingLocationUpdates();
        boolean checkpermission = servicegps.checkPermissions();
        if (requestingLocationupdates && checkpermission) {
            Log.d(debug, "Start location updates in OnResume");

            servicegps.startLocationUpdates();
        } else if (!checkpermission) {
            Log.d(debug, "RequestPermission in OnResume");

            servicegps.requestPermissions();
        }
    }
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        LocationTrackingService.MyBinder binder = (LocationTrackingService.MyBinder) iBinder;
        servicegps = binder.getService();
        servicegps.BindPresenter(locationPresenter);
        servicegps.setMulti(true);
        request_loc();
        Log.d(debug, "GPS Service is Connected : LocationTrackingService");
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        servicegps = null;

    }
}
