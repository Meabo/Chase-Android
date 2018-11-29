package project.chasemvp.Model.Events.Socket.Discover;

/**
 * Created by Mehdi on 29/06/2017.
 */

public class SocketEventGotObject
{
   boolean got_object;
   double distance;

    public double getDistance() {
        return distance;
    }

    public SocketEventGotObject(boolean got_object, double distance) {
        this.got_object = got_object;
        this.distance = distance;
    }



    public boolean isGot_object() {
        return got_object;
    }
}
