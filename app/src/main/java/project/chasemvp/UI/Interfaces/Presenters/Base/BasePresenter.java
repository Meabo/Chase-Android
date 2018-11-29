package project.chasemvp.UI.Interfaces.Presenters.Base;

import project.chasemvp.Model.Services.LocationTrackingService;
import project.chasemvp.UI.Interfaces.View.Base.MvpView;

/**
 * Created by Mehdi on 03/07/2017.
 */

public class BasePresenter <T extends MvpView> implements Presenter<T>
{
    private T mMvpView;

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;

    }

    public void attachView(T mvpView, LocationTrackingService service) {
        mMvpView = mvpView;

    }


    @Override
    public void detachView() {
        //Log.d(debug, "Detach Presenter from view " + mMvpView.getClass());
        mMvpView = null;


    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public T getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }

}
