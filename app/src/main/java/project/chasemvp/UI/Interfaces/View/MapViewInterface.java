package project.chasemvp.UI.Interfaces.View;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import project.chasemvp.UI.Interfaces.View.Base.MvpView;

/**
 * Created by Mehdi on 14/06/2017.
 */

public interface MapViewInterface extends MvpView
{
    public void generateMap(Location l);
    public void updateLocationOnMap(Location location);
    void updateSpeed(Double speed);
    AppCompatActivity getActivity();
    void begin_game_or_deny(String s);
    public void showSnackbar(final int mainTextStringId, final int actionStringId,
                             View.OnClickListener listener);

}
