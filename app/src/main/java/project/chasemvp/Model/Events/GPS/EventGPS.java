package project.chasemvp.Model.Events.GPS;

/**
 * Created by Mehdi on 01/08/2017.
 */

public class EventGPS
{
    boolean high_accuracy;

    public EventGPS(boolean high_accuracy) {
        this.high_accuracy = high_accuracy;
    }

    public boolean isHigh_accuracy() {
        return high_accuracy;
    }
}
