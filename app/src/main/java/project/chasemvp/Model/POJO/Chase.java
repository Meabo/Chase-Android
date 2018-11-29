package project.chasemvp.Model.POJO;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Mehdi on 11/06/2017.
 */

public class Chase
{
    String id;

    String name;
    String url_image;

    int capacity;

    private HashMap<String, Double> location;

    Date creation_date;

    long duration;

    boolean activated;

    public Chase(String id, String name, String url_image, int capacity, HashMap<String, Double> location, Date creation_date, long duration, boolean activated)
    {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.location = location;
        this.creation_date = creation_date;
        this.duration = duration;
        this.activated = activated;
        this.url_image = url_image;
    }

    public String getUrl_image() {
        return url_image;
    }

    public Chase(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public HashMap<String, Double> getLocation() {
        return location;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isActivated() {
        return activated;
    }
}
