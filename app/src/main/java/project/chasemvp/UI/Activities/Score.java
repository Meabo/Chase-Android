package project.chasemvp.UI.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import project.chasemvp.Application.ChaseApplication;
import project.chasemvp.Model.Events.Socket.SocketEventLevelXp;
import project.chasemvp.Model.Events.Socket.SocketEventResult;
import project.chasemvp.Model.MySharedPreferences;
import project.chasemvp.R;
import project.chasemvp.Presenters.ScorePresenter;
import project.chasemvp.Utils.CustomTextView;

/**
 * Created by Mehdi on 15/06/2017.
 */

public class Score extends AppCompatActivity
{
    @BindView(R.id.score_button_gohome) FancyButton gohome;
    @BindView(R.id.score_player_distance_value) CustomTextView score_distance;
    @BindView(R.id.viewKonfetti) KonfettiView viewKonfetti;
    @BindView(R.id.score_player_score_value) CustomTextView score_score;
    @BindView(R.id.score_player_image) CircularImageView score_player_image;


    @Inject ScorePresenter scorePresenter;
    @Inject
    MySharedPreferences pref;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);
        ((ChaseApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        InitKonfetti();
        scorePresenter.get_results();
    }
    private void InitKonfetti()
    {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        viewKonfetti.build()
                .addColors(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.WHITE, Color.MAGENTA)
                .setDirection(0.0, displaymetrics.widthPixels)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(5000L)
                .addShapes(Shape.RECT)
                .addSizes(new Size(12, 12), new Size(16, 16))
                .setPosition(displaymetrics.widthPixels / 2 , viewKonfetti.getWidth() + 50f, -50f, -50f)
                .stream(300, 5000L);
    }

    @OnClick(R.id.score_button_gohome)
    public void gobackhome()
    {

      GoDiscover();
    }

    private void GoHome()
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void GoDiscover()
    {
        Intent i = new Intent(this, Discover.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventResult socketEventResult)
    {
        UpdateUI(socketEventResult);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketEventLevelXp socketEventLevelXp)
    {
        pref.put("level", socketEventLevelXp.getLevel());
        pref.put("xp" , socketEventLevelXp.getXp());
        pref.put("xp_max", socketEventLevelXp.getXp_max());
    }


    private void UpdateUI(SocketEventResult s)
    {
        Picasso.with(this).load(pref.get("avatar_url")).into(score_player_image);
        score_distance.setText(String.valueOf(s.getDistance()));
        score_score.setText(String.valueOf(s.getScore()));
    }

}
