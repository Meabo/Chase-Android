package project.chasemvp.Model;

import java.util.List;

import io.reactivex.Observable;
import project.chasemvp.Model.POJO.Chaser;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Mehdi on 03/07/2017.
 */

public class ResultAPI
{
    public interface ResultService
    {
        @GET("api/result/")
        Observable<List<Chaser>> getplayersinroom(@Path("game_id") String game_id);
    }
}
