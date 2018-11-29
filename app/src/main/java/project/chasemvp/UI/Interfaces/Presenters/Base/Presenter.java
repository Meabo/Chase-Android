package project.chasemvp.UI.Interfaces.Presenters.Base;

import project.chasemvp.UI.Interfaces.View.Base.MvpView;

/**
 * Created by Mehdi on 03/07/2017.
 */

public interface Presenter<V extends MvpView>
{
    void attachView(V mvpView);

    void detachView();
}
