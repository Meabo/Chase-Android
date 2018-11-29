package project.chasemvp.Model;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import project.chasemvp.Model.POJO.Chaser;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mehdi on 19/06/2017.
 */

public class RoomAPI
{
    RoomService roomService;
    public interface RoomService
    {
        @GET("Chasers/getplayersinroom")
        Observable<List<Chaser>> getplayersinroom(@Query("gameid") String gameid, @Query("userid") String userid);
    }


    @Inject
    public RoomAPI(@Named("RetrofitAPI") Retrofit retrofit)
    {
        roomService = retrofit.create(RoomAPI.RoomService.class);
    }

    public RoomService getRoomService() {
        return roomService;
    }
}
