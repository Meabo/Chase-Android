package project.chasemvp.Model.Events.Socket;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.MarkerOptions;

import project.chasemvp.Model.POJO.Chase;

/**
 * Created by Mehdi on 12/07/2017.
 */

public class SocketEventMarker
{
    MarkerOptions markerOptions;
    Bitmap bt;
    Bitmap bt_big;
    Chase chase;

    public SocketEventMarker(MarkerOptions markerOptions, Bitmap bt, Bitmap bt_big, Chase chase) {
        this.markerOptions = markerOptions;
        this.bt = bt;
        this.bt_big = bt_big;
        this.chase = chase;
    }

    public Bitmap getBt_big() {
        return bt_big;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public Bitmap getBt() {
        return bt;
    }

    public Chase getChase() {
        return chase;
    }
}
