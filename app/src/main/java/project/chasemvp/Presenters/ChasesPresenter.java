package project.chasemvp.Presenters;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import project.chasemvp.Model.Events.ChasesEvent;
import project.chasemvp.Model.Events.ErrorEvent;
import project.chasemvp.Model.ChasesAPI;
import project.chasemvp.Model.POJO.Chase;

/**
 * Created by Mehdi on 11/06/2017.
 */

public class ChasesPresenter
{
    public ChasesAPI mChasesAPI;

    @Inject
    public ChasesPresenter(ChasesAPI chasesAPI) {
        this.mChasesAPI = chasesAPI;
    }

    public void loadChasesFromAPI()
    {
        Observable<List<Chase>> chaseObservable = mChasesAPI.getChaseService().getChasesList();


        chaseObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers
                .mainThread()).subscribe(new Observer<List<Chase>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Chase> chases)
            {
                if (chases.size() == 0) {
                    EventBus.getDefault().post(new ErrorEvent("Error fetching data, please check your internet connection"));
                }
                else
                {
                    Log.d("chasedebug", chases.get(0).toString());
                    EventBus.getDefault().post(new ChasesEvent(chases));
                }

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

    public ChasesAPI getChasesAPI()
    {
        return mChasesAPI;
    }
}
