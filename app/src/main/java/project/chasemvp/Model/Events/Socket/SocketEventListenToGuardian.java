package project.chasemvp.Model.Events.Socket;

/**
 * Created by Mehdi on 15/06/2017.
 */

public class SocketEventListenToGuardian
{
    String pseudo;
    double latitude;
    double longitude;
    String url_image;

    public SocketEventListenToGuardian(String pseudo, double latitude, double longitude, String url_image) {
        this.pseudo = pseudo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.url_image = url_image;
    }

    public String getUrl_image() {
        return url_image;
    }

    public String getPseudo() {
        return pseudo;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
