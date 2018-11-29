package project.chasemvp.UI.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.chasemvp.Application.ChaseApplication;
import project.chasemvp.Model.MySharedPreferences;
import project.chasemvp.Model.POJO.Chase;
import project.chasemvp.R;
import project.chasemvp.UI.Activities.Discover;
import project.chasemvp.UI.Activities.FullActivityAd;
import project.chasemvp.UI.Activities.Map;
import project.chasemvp.UI.Activities.Room;

/**
 * Created by Mehdi on 18/06/2017.
 */

public class DetailFragment extends Fragment implements OnMapReadyCallback {

    @BindView(R.id.detail_footer) ConstraintLayout cs;
    @BindView(R.id.detail_map_view) MapView mp;
    GoogleMap map;
    Bundle b;
    Chase chase;

    @Inject
    MySharedPreferences pref;


    public DetailFragment()
    {
    }

    @SuppressLint("ValidFragment")
    public DetailFragment(Chase ch)
    {
        chase = ch;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        b = savedInstanceState;
        ((ChaseApplication) getActivity().getApplication()).getAppComponent().inject(this);
        pref.put("gameid", chase.getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        ButterKnife.bind(this, view);
        mp.onCreate(b);
        mp.onResume();
        mp.getMapAsync(this);
        return view;
    }

    @OnClick(R.id.detail_footer)
    public void BeginFullView()
    {
        Discover();
    }

    private void Discover()
    {
        Intent i = new Intent(getActivity(), Discover.class);
        startActivity(i);
        EventBus.getDefault().postSticky(chase);
    }

    private void RoomActivity()
    {
        Intent i = new Intent(getActivity(), Room.class);
        startActivity(i);
    }

    private void MapActivity()
    {
        Intent i = new Intent(getActivity(), Map.class);
        startActivity(i);
    }
    private void FullViewAdActivity()
    {
        Intent i = new Intent(getActivity(), FullActivityAd.class);
        startActivity(i);
    }


    private void animatecamera(LatLng l)
    {
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(l)
                        .zoom(14f)
                        .build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;
        LatLng l = new LatLng(chase.getLocation().get("lat"), chase.getLocation().get("lng"));
        Marker m = map.addMarker(new MarkerOptions().position(l));
        animatecamera(m.getPosition());
        map.getUiSettings().setScrollGesturesEnabled(false);

    }
}
