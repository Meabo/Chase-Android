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
import project.chasemvp.Model.Events.Socket.Room.SocketEventPlayersInRoom;
import project.chasemvp.Model.POJO.Chaser;
import project.chasemvp.Model.RoomAPI;
import project.chasemvp.Model.SocketModel;
import project.chasemvp.UI.Interfaces.Presenters.Base.BasePresenter;
import project.chasemvp.UI.Interfaces.View.RoomViewInterface;

import static project.chasemvp.Utils.Constants.debug;

/**
 * Created by Mehdi on 19/06/2017.
 */

public class RoomPresenter extends BasePresenter<RoomViewInterface>
{
    RoomAPI roomAPI;
    SocketModel socketModel;
    RoomViewInterface mvpView;
    boolean game_began = false;

    public void setGame_began(boolean game_began) {
        this.game_began = game_began;
    }

    @Inject
    public RoomPresenter(RoomAPI roomAPI, SocketModel socketModel)
    {
        this.roomAPI = roomAPI;
        this.socketModel = socketModel;
    }

    public SocketModel getSocketModel() {
        return socketModel;
    }

    @Override
    public void attachView(RoomViewInterface mvpView)
    {
        super.attachView(mvpView);
        this.mvpView = mvpView;
    }

    @Override
    public void detachView()
    {
        super.detachView();
        if (!game_began)
        {
            //Log.d(debug, "Id : " + mvpView.get_userid() + " " + mvpView.get_gameid());
            quitroom(mvpView.get_userid(), mvpView.get_gameid());
        }


    }



    public void loadplayers(String game_id, String chaserid)
    {
        Log.d(debug, "Game id is : " + game_id);
        Observable<List<Chaser>> playersObservable = roomAPI.getRoomService().getplayersinroom(game_id, chaserid);

        playersObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers
                .mainThread()).subscribe(new Observer<List<Chaser>>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Chaser> players)
            {
                Log.d(debug, players.toString());
                //players.remove()
                EventBus.getDefault().post(new SocketEventPlayersInRoom(players));
            }

            @Override
            public void onError(Throwable e) {
               e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });

    }



    public void joinroom(String userid, String gameid, String pseudo)
    {
        socketModel.init_room(userid, gameid, pseudo);
    }
    public void quitroom(String chaserid, String gameid){
        socketModel.quitroom(chaserid, gameid);
    }
}
