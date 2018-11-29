package project.chasemvp.Utils.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;
import project.chasemvp.Model.Events.Socket.SocketEventMarker;
import project.chasemvp.R;
import project.chasemvp.UI.Activities.Discover;
import project.chasemvp.UI.Activities.Room;
import project.chasemvp.UI.Activities.SoloMap;
import project.chasemvp.Utils.CustomTextView;

/**
 * Created by Mehdi on 04/07/2017.
 */

public class AreaDialog extends Dialog
{

    CustomTextView area_name;
    @BindView(R.id.discover_header_sologame)FancyButton solo;
    @BindView(R.id.discover_header_multigame) FancyButton multi;
    @BindView(R.id.discover_header_image) ImageView header_image;
    @BindView(R.id.discover_dismiss) FloatingActionButton fab;

    Context context;
    Discover discover;
    SocketEventMarker socketEventMarker;



    public AreaDialog(@NonNull Context context, Discover discover, SocketEventMarker socketEventMarker)
    {
        super(context, android.R.style.Theme_NoTitleBar_Fullscreen);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.discover_header);
        ButterKnife.bind(this);
       // EventBus.getDefault().register(this);

        this.context = context;
        this.socketEventMarker = socketEventMarker;
        this.discover = discover;

        header_image.setBackground(new BitmapDrawable(context.getResources(), socketEventMarker.getBt_big()));

        this.show();
    }


    @OnClick({R.id.discover_header_sologame, R.id.discover_header_multigame, R.id.discover_dismiss})
    public void onclick(View view)
    {
        switch(view.getId())
        {
            case R.id.discover_header_sologame :
                EventBus.getDefault().postSticky(socketEventMarker.getChase());
                BeginSoloMap();
                // discover.begin_game_or_deny(socketEventMarker.getChase());
                dismiss();
                break;

            case R.id.discover_header_multigame :
                GoRoom();
                dismiss();
                break;

            case R.id.discover_dismiss :
               dismiss();
                break;
        }
    }
    private void GoRoom()
    {

        //soloMap.getLocationPresenter().stopLocationUpdates();
        Intent i = new Intent(discover.getActivity(), Room.class);
        discover.getActivity().startActivity(i);
        discover.getActivity().finish();

    }

    private void BeginSoloMap()
    {
        //soloMap.getLocationPresenter().stopLocationUpdates();
        Intent i = new Intent(discover.getActivity(), SoloMap.class);
        discover.getActivity().startActivity(i);
        discover.getActivity().finish();
    }
/*
    @Subscribe
    public void OnEvent(LocationEvent locationEvent)
    {
        Log.d(debug, "In LocationEvent event");
        if (locationEvent.getEvent().equals("stop"))
        {
            Log.d(debug, "Activity " + this.getClass());

        }
    }
*/
}
