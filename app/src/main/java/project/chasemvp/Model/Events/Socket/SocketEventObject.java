package project.chasemvp.Model.Events.Socket;

/**
 * Created by Mehdi on 14/06/2017.
 */

public class SocketEventObject
{

    double latitude;
    double longitude;
    String url_icon;


    public SocketEventObject(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getUrl_icon() {
        return url_icon;
    }
}
