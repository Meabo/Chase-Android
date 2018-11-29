package project.chasemvp.Model.Events.GPS;

/**
 * Created by Mehdi on 04/08/2017.
 */

public class LocationEvent
{
    String event;

    public LocationEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }
}
