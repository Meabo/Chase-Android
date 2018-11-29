package project.chasemvp.Model.Events.Socket;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Mehdi on 15/06/2017.
 */


public class SocketEventOthersLocation
{
    String pseudo;
    boolean guardian_presence;
    MarkerOptions markerOptions;
    Bitmap bt;

    public SocketEventOthersLocation(String pseudo, boolean guardian_presence, MarkerOptions markerOptions, Bitmap bt) {
        this.pseudo = pseudo;
        this.guardian_presence = guardian_presence;
        this.markerOptions = markerOptions;
        this.bt = bt;
    }

    public String getPseudo() {
        return pseudo;
    }

    public boolean isGuardian_presence() {
        return guardian_presence;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public Bitmap getBt() {
        return bt;
    }
}
