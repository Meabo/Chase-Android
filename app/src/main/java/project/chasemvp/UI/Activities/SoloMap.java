package project.chasemvp.UI.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.squareup.picasso.Target;

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
import project.chasemvp.Model.Events.GPS.EventGPS;
import project.chasemvp.Model.Events.GPS.EventGPSPlay;
import project.chasemvp.Model.Events.GoogleMapsEvent;
import project.chasemvp.Model.Events.Socket.Discover.SocketEventGotObject;
import project.chasemvp.Model.Events.Socket.SocketEventDistance;
import project.chasemvp.Model.Events.Socket.SocketEventMarker;
import project.chasemvp.Model.Events.Socket.SocketEventObject;
import project.chasemvp.Model.Events.Socket.SocketEventScore;
import project.chasemvp.Model.Events.Socket.SocketEventTime;
import project.chasemvp.Model.Events.SucessEvent;
import project.chasemvp.Model.MySharedPreferences;
import project.chasemvp.Model.POJO.Chase;
import project.chasemvp.Model.Services.LocationTrackingService;
import project.chasemvp.Presenters.LocationPresenter;
import project.chasemvp.R;
import project.chasemvp.UI.Interfaces.View.SoloMapViewInterface;
import project.chasemvp.Utils.CircleTransform;
import project.chasemvp.Utils.CustomTextView;
import project.chasemvp.Utils.Dialogs.CustomDialog;
import project.chasemvp.Utils.Dialogs.DiscoverDialog;

import static project.chasemvp.Utils.Constants.debug;

/**
 * Created by Mehdi on 28/06/2017.
 */

public class SoloMap extends AppCompatActivity implements SoloMapViewInterface,
        OnMapReadyCallback,
        View.OnClickListener, ServiceConnection
{
    GoogleMap map;
    Location initial;
    Marker me;
    boolean game_finished = false;
    boolean playing = false;
    boolean multi = false;
    Polyline line;
    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;


    @BindView(R.id.mapview) MapView mapView;

    @BindView(R.id.map_current_time) CustomTextView map_current_time;
    @BindView(R.id.map_distance_value) CustomTextView map_distance_value;
    @BindView(R.id.map_score_value) CustomTextView map_score_value;
    @BindView(R.id.map_current_speed) CustomTextView map_current_speed;
    @BindView(R.id.map_player_image) CircularImageView map_player_image;
    @BindView(R.id.map_button_itinary) FloatingActionButton map_button_itinary;
    @BindView(R.id.map_button_catch) FloatingActionButton map_button_catch;

    String current_area;
    Chase chase;


    @Inject LocationPresenter locationPresenter;
    @Inject MySharedPreferences pref;
    LocationTrackingService servicegps;

    HashMap<String, Marker> ChasesOnMap = new HashMap<>();
    HashMap<String, SocketEventMarker> ChasesInfo = new HashMap<>();
    HashMap<String, Marker> MarkersOnMap = new HashMap<>();

    public CustomDialog dialog;

    Bundle onsaved;
    DiscoverDialog welcome_dialog;//  = new DiscoverDialog(this, R.style.SplashTheme);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solo_map);
        ButterKnife.bind(this);
        ((ChaseApplication) getApplication()).getAppComponent().inject(this);
        onsaved  = savedInstanceState;
        EventBus.getDefault().register(this);

        welcome_dialog  = new DiscoverDialog(this, R.style.SplashTheme);
        chase = EventBus.getDefault().getStickyEvent(Chase.class);
        //InitToolBar();
        InitScreen();
        CreateMap();
        //Log.d(debug,"onCreate in Map");


    }

   /* private void InitToolBar()
    {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_chevron_left));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
*/


    @Subscribe
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

    /* @Subscribe
     public void OnEvent(LocationEvent locationEvent)
     {
         Log.d(debug, "In LocationEvent event");
         if (locationEvent.getEvent().equals("stop"))
         {
             Log.d(debug, "Activity " + this.getClass());
             finish();
         }
     }

 */
    @Subscribe
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


    private void InitScreen()
    {
        dialog = new CustomDialog(this, android.R.style.Theme_NoTitleBar_Fullscreen);

        Picasso.with(this).load(pref.get("avatar_url"))
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(map_player_image);
    }



   /* private void InitData()
    {
        chase = EventBus.getDefault().getStickyEvent(Chase.class);
        if (chase != null)
        {
            multi = true;
            LatLng l = new LatLng(chase.getLocation().get("lat"), chase.getLocation().get("lng"));
            animatecamera(l);
            //locationPresenter.calculateitinary(l);


        }
    }
    */
    private void CreateMap()
    {
        mapView.onCreate(onsaved);
        mapView.onResume();
        locationPresenter.attachView(this);
        //locationPresenter.InitSocketAndLocation();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Intent intent = new Intent(this, LocationTrackingService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
        Log.d(debug,"onStart in Map");
    }

    @Override
    public void onStop()
    {

        super.onStop();
        unbindService(this);
        // locationPresenter.stopLocationUpdates();
        // locationPresenter.getSocketModel().getSocket().off();
        Log.d(debug, "onStop " + this.getClass());

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //locationPresenter.detachView();

        Log.d(debug, "onDestroy " + getClass());
        EventBus.getDefault().unregister(this);
        if (!game_finished && playing)
        {
            Log.d(debug, "Quit before");
            locationPresenter.quit_before();
        }


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
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        Log.d(debug, "Activity onSavedInstance");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(debug, "OnResume SoloMap");
        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
        // location updates if the user has requested them.

        if (servicegps != null)
        {
            request_loc();
        }

        /*
        boolean requestingLocationupdates = locationPresenter.getServicegps().getmRequestingLocationUpdates();
        boolean checkpermission  = locationPresenter.getServicegps().checkPermissions();
        if (requestingLocationupdates && checkpermission)
        {
            Log.d(debug, "Start location updates in OnResume");

            locationPresenter.getServicegps().startLocationUpdates();
        } else if (!checkpermission)
        {
            Log.d(debug, "RequestPermission in OnResume");

            locationPresenter.getServicegps().requestPermissions();


        }

 */
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
    public void generateMap(Location l)
    {
        Log.d(debug, "Generate Map");
        if (l != null)
            initial = l;
//        animatecamera(new LatLng(l.getLatitude(), l.getLongitude()));
        mapView.getMapAsync(this);
    }

    @Override
    public void updateLocationOnMap(Location location)
    {
        try {
            if (me != null)
            {
                LatLng newpos = new LatLng(location.getLatitude(), location.getLongitude());
                me.setPosition(newpos);
                //            mapRipple.stopRippleMapAnimation();
            } else {
                Init_My_Marker(location.getLatitude(), location.getLongitude());
                if (!multi)
                    animatecamera(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        }
        catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    @Override
    public void updateSpeed(Double speed)
    {
        //Log.d(debug, speed + "");
        if (!speed.isNaN())
        map_current_speed.setText(Math.round(speed.doubleValue() * 3.6) + " km/h");
    }

    @Override
    public AppCompatActivity getActivity()
    {
        return this;
    }

    @Override
    public void begin_game_or_deny(String s) {

    }
/*

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventMarker socketEventMarker)
    {
        Marker marker = map.addMarker(socketEventMarker.getMarkerOptions());
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(socketEventMarker.getBt()));
        marker.setTitle(socketEventMarker.getChase().getName());

        ChasesOnMap.put(socketEventMarker.getChase().getName(), marker);
        ChasesInfo.put(socketEventMarker.getChase().getName(), socketEventMarker);
    }
*/


/*
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Area area) {


        Log.d(debug, "AreaEvent");
        MarkerOptions area_marker_options = new MarkerOptions().position(new LatLng(area.getCenter().getLat(), area.getCenter().getLng()));
       // PicassoMarker target = new PicassoMarker(marker);
        Marker marker = map.addMarker(area_marker_options);


        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(false)
                .build();


        Picasso.with(this)
                .load(area.getUrlImage())
                .resize(150,150)
                .transform(transformation)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
                    {
                        Log.d(debug, "Bitmap ok");

                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
                        marker.setTitle(area.getName());

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Log.d(debug, "Icon map failed");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });


        AreasOnMap.put(area.getName(), marker);
        ChasesInfo.put(area.getName(), area);
    }
*/

    @Override
    public void onBackPressed() {
        open_dialog_before_leave();
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SucessEvent event) {
        if (event.isSuccess())
        {
            if (line != null)
                line.remove();
            //InitViewSoloGame();
            locationPresenter.setGame_began(true);
            playing = true;
        } else {
            Toast.makeText(this, "You are too far to begin the game", Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventScore socketEventscore)
    {
        Log.d(debug, "Score is " + socketEventscore.getScore());
        map_score_value.setText(Integer.toString(socketEventscore.getScore()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventDistance socketEventDistance)
    {
        UpdateDistance(socketEventDistance.getDistance());
    }

    private void UpdateDistance(double d)
    {
        map_distance_value.setText(Double.toString(d));
    }


/*
    private void InitViewSoloGame()
    {
        discover_user.setVisibility(View.GONE);
        //discover_cs.removeView(area_view);
        View child = LayoutInflater.from(this).inflate(R.layout.discover_solo_header, discover_cs, false);
        discover_cs.addView(child);
        map_button_itinary.setVisibility(View.VISIBLE);
        map_button_catch.setVisibility(View.VISIBLE);

        map_current_time = (CustomTextView) child.findViewById(R.id.map_current_time);
        map_distance_value = (CustomTextView) child.findViewById(R.id.map_distance_value);
        map_score_value = (CustomTextView) child.findViewById(R.id.map_score_value);
        map_current_speed = (CustomTextView) child.findViewById(R.id.map_current_speed);
        map_my_image = (CircularImageView) child.findViewById(R.id.map_player_image);

        Picasso.with(this).load(pref.get("avatar_url")).into(map_my_image);
        //map_my_progress.setProgress(pref.get("xp", 0));
        //map_my_progress.setMax(pref.get("xp_max", 0));


    }

*/

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

            public void onFinish() {
                // locationPresenter.detachView();
                game_finished = true;
                map_current_time.setText("Game Finished !");
                Log.d(debug, "TimeFinished, on detach");

                runOnUiThread(new Runnable (){

                    @Override
                    public void run() {
                        locationPresenter.setGame_began(false);
                        locationPresenter.disable_map_events();
                        locationPresenter.detachView();
                        BeginScoreActivity();
                    }
                });


            }

        }.start();
    }


    private void BeginScoreActivity()
    {
        Intent i = new Intent(this, Score.class);
        startActivity(i);
        Log.d(debug, "Score Activity begin");
        finish();
        Log.d(debug, "Finished this activity");
    }


    private void BeginMainActivity()
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        this.finish();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventTime socketEventtime) {
        TimeCounter(socketEventtime.getTime());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventObject socketEventObject) {
        Log.d(debug, "In Map");
        LatLng newposition = new LatLng(socketEventObject.getLatitude(), socketEventObject.getLongitude());
        MarkerOptions markerView = new MarkerOptions().position(newposition);
        Marker object = map.addMarker(markerView);
        object.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.surprise));
        MarkersOnMap.put("object", object);
        // map_button_itinary.setVisibility(View.VISIBLE);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventGotObject eventGotObject)
    {
        if (line != null)
            line.remove();

        if (eventGotObject.isGot_object())
        {
            dialog.show_alert(pref.get("pseudo"));
            Marker object = MarkersOnMap.get("object");
            if (object != null)
                object.remove();
        }
        else
        {
            Toast.makeText(this, "You are too far, Distance : " + eventGotObject.getDistance(), Toast.LENGTH_LONG).show();
        }
        map_button_itinary.setVisibility(View.VISIBLE);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(GoogleMapsEvent googleMapsEvent)
    {
        Log.d(debug,"Draw Line");

        line = map.addPolyline(new PolylineOptions()
                .addAll(googleMapsEvent.getPoly())
                .width(10)
                .color(Color.argb(200, 0, 0, 210))
                .geodesic(true));
    }

    private void Init_My_Marker(double lat, double lng)
    {
        LatLng initial_pos = new LatLng(lat, lng);
        me = map.addMarker(new MarkerOptions().position(initial_pos));
       /* Picasso.with(this).load(pref.get("avatar_url")).get()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(me);
                */
        // me.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.user_location));
        Picasso.with(this).load(pref.get("avatar_url")) .resize(150, 150)
                .transform(new CircleTransform()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
            {
                me.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

       /* mapRipple = new MapRipple(map, initial_pos , this);
        mapRipple.withNumberOfRipples(1);
        mapRipple.withFillColor(Color.WHITE);
        mapRipple.withStrokeColor(Color.WHITE);
        mapRipple.withStrokewidth(1);      // 10dp
        mapRipple.withDistance(10);      // 2000 metres radius
        mapRipple.withRippleDuration(2000);    //12000ms
        mapRipple.withTransparency(0.5f);
        mapRipple.startRippleMapAnimation();*/
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventChase(Chase c)
    {
        Log.d(debug, "Got Chase");
        chase = c;
    }


    private void animatecamera(LatLng l)
    {
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(l)
                        .zoom(15f)
                        .build();


        CameraPosition cameraPosition1 = new CameraPosition.Builder()
                .target(l)
                .zoom(18f)
                .tilt(67.5f)
                .bearing(180)
                .build();


        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        Log.d(debug, "OnMapReady");
        map = googleMap;
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.minimal));
        map.getUiSettings().setCompassEnabled(false);

        //  locationPresenter.startLocationUp();
        Log.d(debug, "Called OnMapReady in SoloMap");
        if (chase != null)
        begin_game_or_deny(chase);
        //locationPresenter.init_discover_map();
        //InitData();
    }

/*
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    locationPresenter.init_location();

                } else {

                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
        }
    }*/


    public void showSnackbar(final int mainTextStringId, final int actionStringId,
                             View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
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



/*
    private PolygonOptions DrawBounds(List<Locationarea> locationareas) {
        PolygonOptions polygonoptions = new PolygonOptions()
                .strokeColor(Color.BLUE)
                .strokeWidth(3)
                .fillColor(0x330000FF);

        double[] centroid = {0.0, 0.0};
        int i = 0;
        for (Locationarea a : locationareas) {
            centroid[0] += a.getLat();
            centroid[1] += a.getLng();
            Log.d(debug, a.getLat() + " " + a.getLng());
            polygonoptions = polygonoptions.add(new LatLng(a.getLat(), a.getLng()));
            i++;
        }
        return polygonoptions;
    }
*/
/*
    private void InflateView(SocketEventMarker marker)
    {

           /* area_view = LayoutInflater.from(this).inflate(R.layout.discover_header, discover_cs, false);
            discover_cs.addView(area_view);

            /*View child = LayoutInflater.from(this).inflate(R.layout.discover_solo_header, discover_cs, false);
            discover_cs.addView(child);
            FancyButton bt = (FancyButton) area_view.findViewById(R.id.discover_header_go);
            bt.setTag(name);
            bt.setOnClickListener(this);
      AreaDialog areaDialog = new AreaDialog(this, this, marker);
     // areaDialog.show();
        //inflated_view = true;
        //String s = (String) v.getTag();
       // begin_game_or_deny(chase);

    }
    */

/*
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle() != null) {
            String name = marker.getTitle();
            SocketEventMarker socketEventMarker = ChasesInfo.get(name);
            Chase a = socketEventMarker.getChase();
            pref.put("gameid", a.getId());
            Log.d(debug, "On MarkerClick " + name);
            //InflateView(socketEventMarker);
        }
        return true;
    }
    */

    public void begin_game_or_deny(Chase chase)
    {
        current_area = chase.getName();
        Log.d(debug, "game id " + chase.getId());
        servicegps.begin_game_or_deny(chase.getId());
    }


    @OnClick({R.id.map_button_catch, R.id.map_button_itinary})
    public void setMap_button_catch(View view) {
        switch (view.getId()) {
            case R.id.map_button_itinary:
                Marker object = MarkersOnMap.get("object");
                servicegps.calculateitinary(object.getPosition());
                map_button_itinary.setVisibility(View.GONE);
                break;
            case R.id.map_button_catch:
                locationPresenter.getSocket().emit("calculatedistance", servicegps.getmCurrentLocation().getLatitude(), servicegps.getmCurrentLocation().getLongitude());
                break;
            default:
                break;
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(debug, "Request code is " + requestCode);
        if (requestCode == REQUEST_CHECK_SETTINGS)
        {
            Log.d(debug, "Request code is " + requestCode);
            locationPresenter.getServicegps().onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.i(debug, "User chose not to make required location settings changes.");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        LocationTrackingService.MyBinder binder = (LocationTrackingService.MyBinder) iBinder;
        servicegps = binder.getService();
        servicegps.BindPresenter(locationPresenter);
        request_loc();
        Log.d(debug, "GPS Service is Connected : LocationTrackingService");

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        servicegps = null;
    }

    @Override
    public void onClick(View v) {

    }
}
