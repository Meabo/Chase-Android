package project.chasemvp.UI.Activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.chasemvp.Application.ChaseApplication;
import project.chasemvp.Model.Events.GPS.EventGPS;
import project.chasemvp.Model.MySharedPreferences;
import project.chasemvp.Model.Services.GpsService;
import project.chasemvp.Model.Services.NetworkService;
import project.chasemvp.Presenters.MainPresenter;
import project.chasemvp.R;
import project.chasemvp.UI.Fragments.ChasesFragment;
import project.chasemvp.UI.Fragments.DetailFragment;
import project.chasemvp.UI.Interfaces.View.MainScreen;

import static project.chasemvp.Utils.Constants.debug;

public class MainActivity extends AppCompatActivity implements MainScreen, NavigationView.OnNavigationItemSelectedListener
{
    FragmentManager fragmentManager = getSupportFragmentManager();
    private BroadcastReceiver mNetworkReceiver;
    private BroadcastReceiver mGpsReceiver;



    View headerLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;

    @Inject MainPresenter mainPresenter;
    @Inject MySharedPreferences pref;
    //@Inject MapPresenter mapPresenter;
    @Inject EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ((ChaseApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);

        InitToolBarAndNavigationView();
        InitFragment();
        CustomThumbnail();

        eventBus.register(this);
        registerBroadcast();

    }


    private void registerBroadcast()
    {
        mNetworkReceiver = new NetworkService();
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mGpsReceiver = new GpsService();
        registerReceiver(mGpsReceiver, new IntentFilter(LocationManager.MODE_CHANGED_ACTION));

    }


    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStart()
    {
        super.onStart();

    }

    @Override
    public void onStop()
    {
        //  Log.d(debug, "onStop, OnDetach");
        super.onStop();
       // Log.d(debug, "OnStop");
        eventBus.unregister(this);
    }





    @Subscribe
    public void onEvent(EventGPS eventGPS)
    {
        if (eventGPS.isHigh_accuracy())
        {
            Toast.makeText(this, "GPS High", Toast.LENGTH_LONG).show();
            Log.d(debug,  "GPS High");
        }
        else
        {
            Toast.makeText(this, "GPS Off", Toast.LENGTH_LONG).show();
            Log.d(debug, "GPS Off");
        }

    }

    private void CustomThumbnail()
    {
        TextView pseudo_header = (TextView) headerLayout.findViewById(R.id.pseudo_header);
        TextView level_header = (TextView) headerLayout.findViewById(R.id.level_header);
        ImageView chaser_im = (ImageView) headerLayout.findViewById(R.id.chaser_im);

        pseudo_header.setText("Hello " + pref.get("pseudo"));
        level_header.setText("Level " + pref.get("level", 0));
        Picasso.with(this)
                .load(pref.get("avatar_url"))
                .resize(75, 75)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(chaser_im);
    }

    private void InitToolBarAndNavigationView()
    {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        headerLayout  = navigationView.getHeaderView(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
    private void InitFragment()
    {
        Fragment fragment = new ChasesFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment, "chases")
                .commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }


    @Override
    public void launchPostsActivity()
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void Discover()
    {
        Intent i = new Intent(this, Discover.class);
        startActivity(i);

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        int id = menuItem.getItemId();

        if (id == R.id.menu_challenges)
        {
            // Handle the camera action
        }
        if (id == R.id.menu_solo)
        {
            // Handle the camera action
            Discover();
        }

        else if (id == R.id.menu_profil) {

        }  else if (id == R.id.menu_preferences) {

        } else if (id == R.id.menu_about) {

        }

        //  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        Fragment detail = fragmentManager.findFragmentByTag("detail");

        if (detail instanceof DetailFragment && detail.isVisible()) {
            //fragmentManager.popBackStack();
            InitFragment();
        }
        else
        {
            finish();
        }
    }
}
