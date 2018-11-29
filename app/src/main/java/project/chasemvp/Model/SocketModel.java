package project.chasemvp.Model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import project.chasemvp.Model.Events.Socket.Discover.SocketEventGotObject;
import project.chasemvp.Model.Events.Socket.Room.SocketEventBeginChase;
import project.chasemvp.Model.Events.Socket.Room.SocketEventOtherEntrance;
import project.chasemvp.Model.Events.Socket.Room.SocketEventOtherLeave;
import project.chasemvp.Model.Events.Socket.Room.SocketEventReady;
import project.chasemvp.Model.Events.Socket.Room.SocketEventRoomJoin;
import project.chasemvp.Model.Events.Socket.SocketEventDistance;
import project.chasemvp.Model.Events.Socket.SocketEventGuardian;
import project.chasemvp.Model.Events.Socket.SocketEventLevelXp;
import project.chasemvp.Model.Events.Socket.SocketEventListenToGuardian;
import project.chasemvp.Model.Events.Socket.SocketEventMarker;
import project.chasemvp.Model.Events.Socket.SocketEventObject;
import project.chasemvp.Model.Events.Socket.SocketEventOthersLocation;
import project.chasemvp.Model.Events.Socket.SocketEventResult;
import project.chasemvp.Model.Events.Socket.SocketEventScore;
import project.chasemvp.Model.Events.Socket.SocketEventSkill;
import project.chasemvp.Model.Events.Socket.SocketEventSteal;
import project.chasemvp.Model.Events.Socket.SocketEventTime;
import project.chasemvp.Model.Events.SucessEvent;
import project.chasemvp.Model.POJO.Chase;
import project.chasemvp.Utils.CircleTransform;

import static project.chasemvp.Utils.Constants.debug;

/**
 * Created by Mehdi on 14/06/2017.
 */
public class SocketModel
{
    public Socket getSocket() {
        return socket;
    }

    Socket socket;
    Boolean sologame = false;
    Picasso picasso;
    Activity activity;

    public SocketModel(Socket socket, Picasso p, Context context)
    {
        this.socket = socket;
        picasso = p;

    }

    public void connect() {
        socket.connect();
    }
    public void connect(Activity a) {
        activity = a;
        socket.connect();
    }

    public void init(String pseudo, String id, String avatar_url)
    {
        socket.emit("init", pseudo, id, avatar_url);
    }

    public void getresults()
    {
        Log.d(debug, "Call RESULTS");
        socket.emit("getresults");
        socket.on("getresults", listenToResults);
        socket.on("current_xp_level", listenToXpLevel);

    }
    public void auth(String token, String userid)
    {
        JSONObject o = new JSONObject();
        try
        {
            o.put("id", token);
            o.put("userId", userid);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("authentication", o);
        socket.on("authenticated", Authenticated);
        socket.on("notauthenticated", NotAuthenticated);
    }


    public void sologame(String t, double lat, double lng)
    {
        socket.emit("beginsologame", t, lat, lng);
        socket.on("beginsologame", listenToSoloGame);
    }


    public void emit(String event) {
        socket.emit(event);
    }

    public void emit(String event, double lat, double lng) {
        socket.emit(event, lat, lng);
    }

    public void emit(String event, int s) {
        socket.emit(event, s);
    }

    public void emit(String event, String s) {
        socket.emit(event, s);
    }

    /* Room Related actions */
    public void init_room(String userid, String gameid, String pseudo)
    {
        //connect();

        joinroom(userid, gameid, pseudo);
        Log.d(debug, "Room " + gameid);
        //socket.emit("roomentrance", userid, pseudo);
        socket.on("someoneentered", listenToOthersEntrance);
        socket.on("someoneleft", listenToOthersLeave);
        socket.on("someoneready", listenToOthersState);
        socket.on("someonenotready", listenToOthersStateNotReady);
        socket.on("begingame", listenToBeginGame);
        Log.d(debug, "Room listeners are ready");
    }



    public void joinroom(String userid, String gameid, String pseudo)
    {
        socket.emit("subscribe", userid, gameid, pseudo);
        socket.on("grantedaccess", listenToJoinRoom);
    }

    public void quitroom(String chaserid, String gameid)
    {
        //socket.off();
        Log.d(debug, "Id : " + chaserid + " " + gameid);
        socket.emit("unsubscribe", chaserid, gameid);
    }



    public void disable_listeners_game()
    {

        // socket.disconnect();
        Log.d(debug, socket+"");
        socket.off();

    }


    public void listen_solo_game()
    {
        socket.on("begintime", listenToBeginTimeSolo);
        socket.on("sendlocation", listenToObjectLocation); // We listen to object location first.
        socket.on("resultdistance", listenToResultDistanceSolo);
        socket.on("currentscore", listenToScore);
        socket.on("currentdistance", listenToDistance);
        socket.on("gamefinished", listenToGameFinished);
    }


    public void quit_solo_game()
    {
       /* socket.off("begintime", listenToBeginTimeSolo);
        socket.off("sendlocation", listenToObjectLocation); // We listen to object location first.
        socket.off("resultdistance", listenToResultDistanceSolo);
        socket.off("currentscore", listenToScore);
        socket.off("currentdistance", listenToDistance);
        socket.off("gamefinished", listenToGameFinished); */
        socket.emit("quitgame");
        socket.off();
    }


    public void listen_game()
    {
        Log.d(debug, "MultiGame called");


        socket.emit("resultpage_multi");
        socket.on("begintime", listenToBeginTime);
        // Socket is lauching time counter*/
        socket.on("sendlocation", listenToObjectLocation); // We listen to object location first.
        socket.on("resultdistance", listenToResultDistance);
        socket.on("currentscore", listenToScore);
        socket.on("currentdistance", listenToDistance);
        socket.on("gamefinished", listenToGameFinished);

        socket.on("skill", listenToSkills);
        socket.on("other_player_location", listenToOthersLocation); // Listen to all locations in real time
        socket.on("theguardian", listenToGuardian); // Event for the Guardian, change markers and ackknowledge the others.
        socket.on("youstealit", listenToSteal); // Game is finished after countdown


    }

    public void discover()
    {
        socket.emit("getgames");
        socket.on("games", listenToMarkers);
    }



    private Emitter.Listener listenToJoinRoom = new Emitter.Listener() {
        @Override
        public void call(final Object... args)
        {
            Log.d(debug, "Joined Room");
           /* Ack ack = (Ack) args[args.length - 1];
            ack.call("listenToJoinRoom");*/
            EventBus.getDefault().post(new SocketEventRoomJoin((boolean) args[0]));
        }
    };




    private Emitter.Listener listenToOthersState = new Emitter.Listener() {
        @Override
        public void call(final Object... args)
        {
            //Toast.makeText(app.getApplicationContext(), "Ok Authenticated", Toast.LENGTH_LONG).show();
            Log.d(debug, "Ok Chaser " + args[0] + "is ready");
            String id = (String) args[0];
            EventBus.getDefault().post(new SocketEventReady(true, id));

        }
    };



    private Emitter.Listener listenToOthersStateNotReady = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            //Toast.makeText(app.getApplicationContext(), "Ok Authenticated", Toast.LENGTH_LONG).show();
            Log.d(debug, "Ok Chaser " + args[0] + "is not ready !");
            String id = (String) args[0];
            EventBus.getDefault().post(new SocketEventReady(false, id));
                    /*
                    for (int i = 0; i < mAdapter.getList().size(); i++) {
                        if (mAdapter.getList().get(i).getId().equals(args[0])) {
                            RoomAdapter.ViewHolder r = (RoomAdapter.ViewHolder) mRecyclerView.findViewHolderForLayoutPosition(i);
                            r.mReadyView.setText("");
                        }
                    }
                    */

        }
    };

    private Emitter.Listener listenToOthersEntrance = new Emitter.Listener() {
        @Override
        public void call(final Object... args)
        {
            //Toast.makeText(app.getApplicationContext(), "Ok Authenticated", Toast.LENGTH_LONG).show();
            Log.d(debug, "Someone Entered in the room");
            socket.emit("ack_someoneentered", true);
            EventBus.getDefault().post(new SocketEventOtherEntrance(true));
        }
    };





    private Emitter.Listener listenToOthersLeave = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            //Toast.makeText(app.getApplicationContext(), "Ok Authenticated", Toast.LENGTH_LONG).show();
            // Toast.makeText(app.getApplicationContext(), "Someone Left the room", Toast.LENGTH_LONG).show();
            Log.d(debug, "Someone Left in the room");
            EventBus.getDefault().post(new SocketEventOtherLeave());



        }
    };



    private Emitter.Listener listenToSoloGame = new Emitter.Listener() {
        @Override
        public void call(final Object... args)
        {
            //Toast.makeText(app.getApplicationContext(), "Ok Authenticated", Toast.LENGTH_LONG).show();
            boolean result = (boolean) args[0];
            EventBus.getDefault().post(new SucessEvent(result));
            if (result)
                listen_solo_game();
        }
    };



    private Emitter.Listener listenToMarkers = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            //Toast.makeText(app.getApplicationContext(), "Ok Authenticated", Toast.LENGTH_LONG).show();
            //String json = (String) args[0];
            Log.d(debug, "Inside listenToMarkers");
            String json = args[0] + "";
            Gson gson = new Gson();
            Chase[] chaselist = gson.fromJson(json, Chase[].class);


            for (int i = 0; i < chaselist.length; i++)
            {
                Chase a = chaselist[i];
                Log.d(debug, "Game is : " + a.getUrl_image());

                /*activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
*/
                try {
                    Bitmap bitmap_big = picasso.load(a.getUrl_image()).get();

                    Bitmap bitmap = picasso.load(a.getUrl_image())
                    .resize(150, 150).transform(new CircleTransform()).get();


                    MarkerOptions area_marker_options = new MarkerOptions().position(new LatLng(a.getLocation().get("lat"), a.getLocation().get("lng")));
                    SocketEventMarker sem = new SocketEventMarker(area_marker_options, bitmap, bitmap_big, a);
                    EventBus.getDefault().post(sem);
                    //Log.d(debug, "Finished Picasso");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            Log.d(debug, "Size of Markers list is : " + chaselist.length);

/*
                });


            }*/
        }

    };


    private Emitter.Listener Authenticated = new Emitter.Listener() {
        @Override
        public void call(final Object... args)
        {
            //Toast.makeText(app.getApplicationContext(), "Ok Authenticated", Toast.LENGTH_LONG).show();
            Log.d(debug, "Ok I'm Authenticated" );
            EventBus.getDefault().post(new SucessEvent(true));

        }
    };

    private Emitter.Listener NotAuthenticated = new Emitter.Listener() {
        @Override
        public void call(final Object... args)
        {
            //Toast.makeText(app.getApplicationContext(), "Ok Authenticated", Toast.LENGTH_LONG).show();
            Log.d(debug, "Ok I'm not Authenticated !" );
            EventBus.getDefault().post(new SucessEvent(false));

        }
    };


    private Emitter.Listener listenToBeginGame = new Emitter.Listener()
    {
        @Override
        public void call(final Object... args)
        {
            Log.d(debug, "Game begins");
            EventBus.getDefault().post(new SocketEventBeginChase());
        }
    };



    private Emitter.Listener listenToObjectLocation = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(debug, "Got Object");
            double latitude = (double) args[0];
            double longitude = (double) args[1];
            EventBus.getDefault().post(new SocketEventObject(latitude, longitude));
        }
    };

    private Emitter.Listener listenToBeginTime = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            int time = (int) args[0];
            EventBus.getDefault().post(new SocketEventTime(time));
        }
    };

    private Emitter.Listener listenToBeginTimeSolo = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            int time = (int) args[0];
            EventBus.getDefault().post(new SocketEventTime(time));
            socket.emit("resultpage");
        }
    };

    private Emitter.Listener listenToResultDistance = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            boolean get_object = (boolean) args[1];
            EventBus.getDefault().post(new SocketEventGuardian(get_object));
            if (get_object) {
                socket.emit("getobject_multi");
            }
        }
    };

    private Emitter.Listener listenToResultDistanceSolo = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            double distance = (double) args[0];
            boolean get_object = (boolean) args[1];
            if (get_object) {
                socket.emit("getobject");
            }
            EventBus.getDefault().post(new SocketEventGotObject(get_object, distance));

        }
    };


    private Emitter.Listener listenToDistance = new Emitter.Listener() {
        @Override
        public void call(final Object... args)
        {
            Log.d(debug, "SocketEventDistance distance : " + args[0]);
            String distance = (String) args[0];
            Log.d(debug, "SocketEventDistance distance : " + distance);

            EventBus.getDefault().post(new SocketEventDistance(Double.valueOf(distance)));

        }
    };

    private Emitter.Listener listenToScore = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            int score = (int) args[0];
            EventBus.getDefault().post(new SocketEventScore(score));

        }
    };

    private Emitter.Listener listenToGameFinished = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {


        }
    };

    private Emitter.Listener listenToResults = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                int score = data.getInt("score");
                double distance = data.getDouble("distance");
                double speed_avg = data.getDouble("speed_avg");
                EventBus.getDefault().post(new SocketEventResult(score, distance, speed_avg));
                Log.d(debug, "GET RESULTS");

            } catch (JSONException e) {

            }

        }
    };


    private Emitter.Listener listenToSkills = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            boolean r = (boolean) args[0];
            int skill = (int) args[1];
            String pseudo = null;
            int duration = 0;
            if (r)
            {
                 pseudo = (String) args[2];
                 duration = (int) args[3];
                EventBus.getDefault().post(new SocketEventSkill(true, skill, pseudo,  duration));
            }
            else
                EventBus.getDefault().post(new SocketEventSkill(false));



        }


    };

    private Emitter.Listener listenToOthersLocation = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data_total = (JSONObject) args[0];
            try {
                String pseudo = data_total.getString("pseudo");
                JSONObject data = (JSONObject) data_total.get("location");
                boolean forchaser = data_total.getBoolean("forchaser");
                double latitude = data.getDouble("latitude");
                double longitude = data.getDouble("longitude");
                String url_image = data_total.getString("url_image");

                try {
                    Bitmap bitmap = picasso.load(url_image)
                            .resize(150, 150)
                            .transform(new CircleTransform()).get();
                    MarkerOptions area_marker_options = new MarkerOptions().position(new LatLng(latitude, longitude));
                    EventBus.getDefault().post(new SocketEventOthersLocation(pseudo, forchaser, area_marker_options, bitmap));

                } catch (IOException e)
                {
                    e.printStackTrace();
                }



            } catch (JSONException e) {
            }
        }


    };


    private Emitter.Listener listenToGuardian = new Emitter.Listener()
    {
        @Override
        public void call(final Object... args) {
            JSONObject data_total = (JSONObject) args[0];
            try
            {
                String pseudo = data_total.getString("pseudo");
                Log.d(debug, pseudo + " is the Guardian !");

                JSONObject data = (JSONObject) data_total.get("location");
                double latitude = (double) data.get("latitude");
                double longitude = (double) data.get("longitude");
                String url_image = data_total.getString("url_image");
                EventBus.getDefault().postSticky(new SocketEventListenToGuardian(pseudo, latitude, longitude, url_image));

            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }


    };


    private Emitter.Listener listenToSteal = new Emitter.Listener() {
        @Override
        public void call(final Object... args)
        {
            int score = (int) args[0];
            EventBus.getDefault().post(new SocketEventSteal(score, sologame));
        }
    };

    private Emitter.Listener  listenToXpLevel =  new Emitter.Listener() {
        @Override
        public void call(final Object... args)
        {
            int level = (int) args[0];
            int xp = (int) args[1];
            int xp_max = (int) args[2];
            Log.d(debug, level  + " " +  xp + " " + xp_max);
            EventBus.getDefault().post(new SocketEventLevelXp( level, xp, xp_max));
        }
    };


}



    /*
            markerViewOptions = new MarkerOptions().position(dest);


            object_marker = map.addMarker(markerViewOptions);
            object_marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ultraboostobject_icon));

            System.out.println("GOT IT " + data);

            // add the message to view
        }

    };
*/




