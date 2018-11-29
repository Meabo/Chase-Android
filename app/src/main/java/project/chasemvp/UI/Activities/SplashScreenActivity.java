package project.chasemvp.UI.Activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;
import project.chasemvp.Application.ChaseApplication;
import project.chasemvp.Model.Events.SucessEvent;
import project.chasemvp.Model.Events.ErrorEvent;
import project.chasemvp.Model.Events.LoginEvent;
import project.chasemvp.Model.MySharedPreferences;
import project.chasemvp.Model.POJO.Chaser;
import project.chasemvp.R;
import project.chasemvp.Model.Services.AuthService;
import project.chasemvp.UI.Interfaces.View.SplashScreenInterface;
import project.chasemvp.Presenters.SplashPresenter;

import static project.chasemvp.Utils.Constants.debug;

public class SplashScreenActivity extends AppCompatActivity implements SplashScreenInterface, ServiceConnection
{

    @BindView(R.id.splashscreen_facebook_login_button) FancyButton splash_fb_login;
    @Inject SplashPresenter splashPresenter;
    @Inject MySharedPreferences pref;
    CallbackManager callbackManager;
    private boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        ButterKnife.bind(this);
        callbackManager = CallbackManager.Factory.create();
        //initCallBackManager(this);
        ((ChaseApplication) getApplication()).getAppComponent().inject(this);
    }

    private void FastLogin(AuthService s)
    {
        if (pref.get("token") != null && pref.get("userid") != null)
        {
            splashPresenter.auth(s);
        }
    }


    @OnClick(R.id.splashscreen_facebook_login_button)
    public void LoginWithFacebook()
    {
        //splashPresenter.OnLoginFacebookButtonClick(this);
        initCallBackManager(this);

    }

    private void initCallBackManager(Activity activity)
    {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile, user_friends, email"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>()
                {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        Log.d(debug, "Success " + loginResult.getAccessToken().getToken());
                        splashPresenter.GetToken(loginResult.getAccessToken().getToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.v(debug, " OnCancel : yes");
                        //error_message = "Already connected, Retry to login";
                        LoginManager.getInstance().logOut();
                    }

                    @Override
                    public void onError(FacebookException e)
                    {
                        Log.d("chasedebug", e.getMessage());
                      //  error_message = e.getMessage();
                    }

                });

        Log.v(debug, "After Callback yes");
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SucessEvent auth)
    {
        if (auth.isSuccess())
            launchMainActivity();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Chaser c)
    {
        SaveAllInfo(c);
        launchMainActivity();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginEvent l)
    {
        pref.put("token", l.getToken().getAccess_token());
        pref.put("userid", l.getToken().getUserId());
    }


    private void SaveAllInfo(Chaser c )
    {
        pref.put("pseudo", c.getPseudo());
        pref.put("level", Integer.toString(c.getLevel()));
        pref.put("xp", Integer.toString(c.getXp()));
        pref.put("url_image", c.getUrl_image());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ErrorEvent errorEvent)
    {
        Log.d(debug, errorEvent.getError());
    }


    @Override
    public void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        EventBus.getDefault().register(this);
        Intent intent = new Intent(this, AuthService.class);
        mBound = bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (mBound)
        {
            unbindService(this);
            mBound = false;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service)
    {
        splashPresenter.attach(((AuthService.LocalBinder) service).getService());
        FastLogin(((AuthService.LocalBinder) service).getService());
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        splashPresenter.detach();
    }
}
