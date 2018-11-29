package project.chasemvp.UI.Interfaces.Presenters;

import com.google.android.gms.maps.model.LatLng;

import io.socket.client.Socket;

/**
 * Created by Mehdi on 14/06/2017.
 */

public interface MapPresenterInterface
{

    void calculateitinary(LatLng dest);
    void disable_map_events();
    void quit_before();
    void onMapReady();
    Socket getSocket();
}
