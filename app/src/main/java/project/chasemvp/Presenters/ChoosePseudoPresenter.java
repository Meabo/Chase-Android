package project.chasemvp.Presenters;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import project.chasemvp.Model.Events.ErrorEvent;
import project.chasemvp.Model.POJO.Avatar;
import project.chasemvp.Model.POJO.Chaser;
import project.chasemvp.Model.PlayerAPI;
import project.chasemvp.Model.SocketModel;
import project.chasemvp.UI.Interfaces.Presenters.ChoosePseudoPresenterInterface;
import project.chasemvp.Utils.CircleTransform;

import static project.chasemvp.Utils.Constants.debug;

/**
 * Created by Mehdi on 22/06/2017.
 */

public class ChoosePseudoPresenter implements ChoosePseudoPresenterInterface
{

    PlayerAPI playerAPI;

    public SocketModel getSocketModel() {
        return socketModel;
    }

    SocketModel socketModel;


    @Inject public ChoosePseudoPresenter(PlayerAPI playerAPI, SocketModel socketModel)
    {
        this.playerAPI = playerAPI;
        this.socketModel = socketModel;
    }

    public void getAvatars()
    {
        Observable<List<Avatar>> avatars = playerAPI.getmPlayerAPI().getavatars();
        avatars.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers
                .mainThread()).subscribe(new Observer<List<Avatar>> () {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Avatar> avatars) {

                EventBus.getDefault().post(avatars);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }


    });

    }

    public void saveImage(String url, Context context)
    {
        Log.d(debug, "Begin saving image");

        Single<Bitmap> bitmapSingle = Single.create(new SingleOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(SingleEmitter<Bitmap> e) throws Exception {
                e.onSuccess(Picasso.with(context).load(url).resize(150, 150)
                        .transform(new CircleTransform()).get());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        bitmapSingle.observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Bitmap>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Bitmap bitmap)
            {
                EventBus.getDefault().post(bitmap);
                Log.d(debug, "Success bitmap " +   bitmap.getByteCount() + " " + bitmap.getHeight());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();

            }
        });
    }
    @Override
    public void checkpseudo(String pseudo)
    {
        Observable<Object> checkresponse = playerAPI.getmPlayerAPI().checkpseudo(pseudo);
        checkresponse.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers
                .mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object s)
            {
                Log.d("debug", s + "");
                LinkedTreeMap<String, LinkedTreeMap> ltp = (LinkedTreeMap) s;
                LinkedTreeMap ss = ltp.get("chaser");
                String pseudo = (String) ss.get("pseudo");
                double level = (double) ss.get("level");
                double xp = (double) ss.get("xp");
                String email = (String) ss.get("email");
                String id = (String) ss.get("id");


                if (ss != null)
                    EventBus.getDefault().post(new Chaser(pseudo, (int) level, (int) xp, email, id));
                else
                    EventBus.getDefault().post(new ErrorEvent("Error"));


            }

            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });


    }
}
