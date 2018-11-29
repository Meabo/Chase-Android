package project.chasemvp.Model.Events;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import project.chasemvp.Model.POJO.GoogleMapResult.GoogleMapResult;
import project.chasemvp.Utils.Calculation;

/**
 * Created by Mehdi on 14/06/2017.
 */

public class GoogleMapsEvent
{
    GoogleMapResult googleMapResult;
    List<LatLng> poly;
    String distance;
    String time;
    String units = "km";

    public GoogleMapResult getGoogleMapResult() {
        return googleMapResult;
    }

    public List<LatLng> getPoly() {
        return poly;
    }

    public String getDistance() {
        return distance;
    }

    public String getTime() {
        return time;
    }

    public GoogleMapsEvent(GoogleMapResult googleMapResult)
    {
        this.googleMapResult = googleMapResult;
        this.poly = Calculation.decodePoly(this.googleMapResult.getRoutes().get(0).getOverviewPolyline().getPoints());
        this.distance = this.googleMapResult.getRoutes().get(0).getLegs().get(0).getDistance().getText();
        this.time = this.googleMapResult.getRoutes().get(0).getLegs().get(0).getDuration().getText();
    }
}
