package project.chasemvp.Model;

import io.reactivex.Observable;
import project.chasemvp.Model.POJO.GoogleMapResult.GoogleMapResult;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mehdi on 14/06/2017.
 */

public class MapAPI
{
    public interface MapService
    {
        @GET("api/directions/json?key=AIzaSyCYj5fhTsE91fwpssiSdiicwMRQ3nfQrRc")
        Observable<GoogleMapResult> getDistanceDuration(@Query("units") String units, @Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode);

    }

    MapService mapService;

    public MapAPI(Retrofit retrofit)
    {
        mapService = retrofit.create(MapService.class);
    }

    public MapService getMapService() {
        return mapService;
    }
}
