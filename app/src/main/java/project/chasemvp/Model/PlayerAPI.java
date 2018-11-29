package project.chasemvp.Model;

import java.util.List;

import io.reactivex.Observable;
import project.chasemvp.Model.POJO.AccessToken;
import project.chasemvp.Model.POJO.Avatar;
import project.chasemvp.Model.POJO.Chaser;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mehdi on 12/06/2017.
 */

public class PlayerAPI
{
    PlayerAPIService mPlayerAPI;

    public interface PlayerAPIService
    {
        // Put all API calls concerning Chases (games).
        @GET("/auth/facebook-token/callback")
        Observable<AccessToken> getCredentials(@Query("access_token") String access_token);


        @GET("/api/Chasers/{userId}")
        Observable<Chaser> getChaser(@Path("userId") String userId);

        @GET("/api/Chasers/checkpseudo")
        Observable<Object> checkpseudo(@Query("pseudo") String pseudo);

        @GET("/api/Avatars")
        Observable<List<Avatar>> getavatars();

    }


    public PlayerAPI(Retrofit retrofit)
    {
        mPlayerAPI = retrofit.create(PlayerAPIService.class);
    }

    public PlayerAPIService getmPlayerAPI()
    {
        return mPlayerAPI;
    }


}
