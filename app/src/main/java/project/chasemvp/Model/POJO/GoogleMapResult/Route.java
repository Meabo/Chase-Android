package project.chasemvp.Model.POJO.GoogleMapResult;

import java.util.ArrayList;
import java.util.List;

public class Route
{
    private List<Leg> legs = new ArrayList<Leg>();
    private OverviewPolyline overview_polyline;

    public List<Leg> getLegs() {
        return legs;
    }
    public OverviewPolyline getOverviewPolyline() {
        return overview_polyline;
    }

}
