package project.chasemvp.Presenters;

import android.util.Log;

import javax.inject.Inject;

import project.chasemvp.Model.SocketModel;

import static project.chasemvp.Utils.Constants.debug;

/**
 * Created by Mehdi on 28/06/2017.
 */

public class ScorePresenter
{
    SocketModel socketModel;
    @Inject public ScorePresenter(SocketModel model)
    {
        this.socketModel = model;
    }

    public void get_results()
    {
        //socketModel.connect();
        socketModel.getresults();
        Log.d(debug, "Called Get_results method");
    }
}
