package project.chasemvp.Presenters;

import android.app.Activity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import project.chasemvp.Model.Events.ErrorEvent;
import project.chasemvp.Model.Events.LoginEvent;
import project.chasemvp.Model.PlayerAPI;
import project.chasemvp.Model.POJO.AccessToken;
import project.chasemvp.Model.POJO.Chaser;
import project.chasemvp.Model.Services.AuthService;
import project.chasemvp.UI.Interfaces.Presenters.SplashPresenterInterface;

import static project.chasemvp.Utils.Constants.debug;

/**
 * Created by Mehdi on 12/06/2017.
 */

public class SplashPresenter implements SplashPresenterInterface
{
    public PlayerAPI mPlayerAPI;
    private String error_message;
    @Inject CallbackManager callbackManager;

    @Inject public SplashPresenter(PlayerAPI f)
    {
        mPlayerAPI = f;

    }

     AuthService service;

    public CallbackManager getCallbackmanager()
    {
        return callbackManager;
    }
    private void initCallBackManager(Activity activity)
    {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>()
                {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        Log.d(debug, "Success " + loginResult.getAccessToken().getToken());
                        GetToken(loginResult.getAccessToken().getToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.v(debug, " OnCancel : yes");
                        error_message = "Already connected, Retry to login";
                        LoginManager.getInstance().logOut();
                    }

                    @Override
                    public void onError(FacebookException e)
                    {
                      //  e.printStackTrace();
                        Log.d("chasedebug", e.getMessage());
                        error_message = e.getMessage();

                    }

                });

        Log.v(debug, "After Callback yes");
    }


    public void GetToken(String token)
    {
        Observable<AccessToken> FbObservable = mPlayerAPI.getmPlayerAPI().getCredentials(token);
        FbObservable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers
                .mainThread()).subscribe(new Observer<AccessToken>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(AccessToken accessToken) {
                Log.d("chasedebug", "Access Token : " + accessToken.getAccess_token());
                //getProfile(accessToken.getUserId());
                getChaser(accessToken.getUserId());
                EventBus.getDefault().post(new LoginEvent(accessToken));
            }

            @Override
            public void onError(Throwable e)
            {
                Log.d("chasedebug",  e.getMessage());
                error_message = e.getMessage();
                EventBus.getDefault().post(new ErrorEvent(error_message));
            }

            @Override
            public void onComplete() {

            }
        });

    }

    public void getChaser(String userid)
    {
        Observable<Chaser> chaserObservable = mPlayerAPI.getmPlayerAPI().getChaser(userid);
        chaserObservable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Chaser>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Chaser chaser)
            {
                Log.d(debug, "Pseudo is " + chaser.getPseudo());
                EventBus.getDefault().post(chaser);
                service.auth();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    public void OnLoginFacebookButtonClick(Activity activity)
    {
        Log.d(debug, "Clicked");
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile, user_friends, email"));
        initCallBackManager(activity);
    }

    @Override
    public void attach(AuthService s)
    {
     service = s;
    }

    @Override
    public void detach() {

    }

    @Override
    public void auth(AuthService s) {
        s.auth();
    }
}
