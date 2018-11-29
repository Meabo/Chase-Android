package project.chasemvp.Model;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import project.chasemvp.Model.POJO.Chase;
import project.chasemvp.Model.POJO.Chaser;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Mehdi on 11/06/2017.
 */

public class ChasesAPI
{
    public interface ChaseService
    {
        // Put all API calls concerning Chases (games).
        @GET("games")
        Observable<List<Chase>> getChasesList();

        @GET("games/{game_id}/chasers")
        Observable<List<Chaser>> getchasers(@Path("game_id") String game_id);

    }

    private ChaseService mChaseService;

    @Inject
    public ChasesAPI(@Named("RetrofitAPI") Retrofit retrofit)
    {
        mChaseService = retrofit.create(ChaseService.class);
    }


   public ChaseService getChaseService()
   {
       return this.mChaseService;
   }



}
