package project.chasemvp.UI.Activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;
import project.chasemvp.Application.ChaseApplication;
import project.chasemvp.Model.Events.Socket.Room.SocketEventBeginChase;
import project.chasemvp.Model.Events.Socket.Room.SocketEventOtherEntrance;
import project.chasemvp.Model.Events.Socket.Room.SocketEventOtherLeave;
import project.chasemvp.Model.Events.Socket.Room.SocketEventPlayersInRoom;
import project.chasemvp.Model.Events.Socket.Room.SocketEventReady;
import project.chasemvp.Model.Events.Socket.Room.SocketEventRoomJoin;
import project.chasemvp.Model.MySharedPreferences;
import project.chasemvp.Presenters.LocationPresenter;
import project.chasemvp.Presenters.RoomPresenter;
import project.chasemvp.R;
import project.chasemvp.UI.Interfaces.View.RoomViewInterface;
import project.chasemvp.UI.Interfaces.View.SoloMapViewInterface;
import project.chasemvp.Utils.Adapters.RoomAdapter;
import project.chasemvp.Utils.CustomTextView;

import static project.chasemvp.Utils.Constants.debug;

/**
 * Created by Mehdi on 19/06/2017.
 */

public class Room extends AppCompatActivity implements RoomViewInterface, SoloMapViewInterface
{
    RoomAdapter roomAdapter;
    @BindView(R.id.room_recyclerview) RecyclerView recyclerView;

    @BindView(R.id.room_item_image) CircularImageView room_my_image;
    @BindView(R.id.room_item_pseudo) CustomTextView room_my_pseudo;
    @BindView(R.id.room_item_ready) CustomTextView room_my_ready;
    @BindView(R.id.room_item_level) CustomTextView room_my_level;

    @BindView(R.id.room_footer) ConstraintLayout room_footer;
    @BindView(R.id.room_ready_button) FancyButton room_ready_button;
    //@BindView(R.id.room_item_) CircularImageView room_my_image;



    @Inject RoomPresenter roomPresenter;
    @Inject MySharedPreferences pref;
    @Inject
    LocationPresenter locationPresenter;


    boolean isvisible = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room);

        ((ChaseApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);

        roomPresenter.joinroom(get_userid(), get_gameid(), get_pseudo());

        InitRecyclerView();
        //roomPresenter.init_listeners();
        roomPresenter.loadplayers(pref.get("gameid"), pref.get("id"));
    }

    private void init_listeners()
    {
      //  roomPresenter.getSocketModel().listen_room();
    }
    private void InitPlayerView()
    {
        Picasso.with(this).load(pref.get("avatar_url"))
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(room_my_image);
        room_my_pseudo.setText(pref.get("pseudo"));
        room_my_level.setText("Level " + pref.get("level", 0));
        room_footer.setVisibility(View.VISIBLE);
        room_ready_button.setVisibility(View.VISIBLE);

    }

    private void InitRecyclerView()
    {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        roomAdapter = new RoomAdapter(this);
        recyclerView.setAdapter(roomAdapter);
    }

    @OnClick(R.id.room_ready_button)
    public void gogo()
    {
        if (isvisible == false)
        {
            room_my_ready.setText("Ready");
            isvisible = true;
            roomPresenter.getSocketModel().emit("readytoplay", pref.get("id"));
        }
        else
        {
            room_my_ready.setText("");
            roomPresenter.getSocketModel().emit("notreadytoplay", pref.get("id"));
            isvisible = false;
        }

    }

    private void GoToMap()
    {
        Intent i = new Intent(this, SoloMap.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        roomPresenter.attachView(this);

        //locationPresenter.attachView(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        roomPresenter.detachView();
        //locationPresenter.detachView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventPlayersInRoom socketEventPlayersInRoom)
    {
        Log.d(debug, "Now " + socketEventPlayersInRoom.getPlayers().size() + " players in the list !");
        roomAdapter.addPlayers(socketEventPlayersInRoom.getPlayers());
    }


    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventRoomJoin socketEventRoomJoin)
    {
        if (socketEventRoomJoin.isJoinedroom())
            InitPlayerView();
    }


    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventBeginChase socketEventBeginChase)
    {
        Log.d(debug, "SocketEventBeginChase called");
        roomPresenter.setGame_began(true);
        Intent i = new Intent(this, Map.class);
        startActivity(i);
        finish();
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventOtherEntrance socketEventOtherEntrance)
    {
        if (socketEventOtherEntrance.isEntered())
        {

            Log.d(debug, "Reloading users in room");
            roomPresenter.loadplayers(pref.get("gameid"), pref.get("id"));

        }
    }


    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventOtherLeave socketEventOtherLeave)
    {
        roomPresenter.loadplayers(pref.get("gameid"), pref.get("id"));
    }

    private void ChangetoReady(String chaserid, boolean b)
    {

        for (int i = 0; i < roomAdapter.getPlayers().size(); i++) {
            if (roomAdapter.getPlayers().get(i).getId().equals(chaserid))
            {
                RoomAdapter.ViewHolder r = (RoomAdapter.ViewHolder) recyclerView.findViewHolderForLayoutPosition(i);
                if (b)
                {
                    r.ready.setText("Ready");
                    r.ready.setVisibility(View.VISIBLE);
                }
                else
                {
                    r.ready.setVisibility(View.INVISIBLE);

                }

            }
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventReady socketEventReady)
    {
        if (socketEventReady.isReady())
        {
            ChangetoReady(socketEventReady.getId(), true);
        }
        else
        {
            ChangetoReady(socketEventReady.getId(), false);

        }

    }




    @Override
    public String get_userid() {
        return pref.get("id");
    }

    @Override
    public String get_gameid() {
        return pref.get("gameid");
    }

    @Override
    public String get_pseudo() {
        return pref.get("pseudo");
    }


    @Override
    public void generateMap(Location l) {

    }

    @Override
    public void updateLocationOnMap(Location location) {

    }

    @Override
    public void updateSpeed(Double speed) {

    }

    @Override
    public AppCompatActivity getActivity() {
        return null;
    }

    @Override
    public void begin_game_or_deny(String s) {

    }

    @Override
    public void showSnackbar(int mainTextStringId, int actionStringId, View.OnClickListener listener) {

    }
}
