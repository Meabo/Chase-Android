package project.chasemvp.Utils.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;
import project.chasemvp.Application.ChaseApplication;
import project.chasemvp.Model.Events.GPS.EventGPSPlay;
import project.chasemvp.R;

/**
 * Created by Mehdi on 01/08/2017.
 */

public class DiscoverDialog extends Dialog
{
    @BindView(R.id.discover_dialog_dismiss)
    ImageView dismiss;

    @BindView(R.id.discover_dialog_jouer)
    FancyButton play;

    @Inject
    EventBus eventBus;

    public DiscoverDialog(@NonNull Context context, int themeresid)
    {
        super(context, R.style.Theme_Transparent);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.discoverdialog);
        ButterKnife.bind(this);
        ChaseApplication app = (ChaseApplication) context.getApplicationContext();
        app.getAppComponent().inject(this);
        //getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //show();
    }

    @OnClick({R.id.discover_dialog_dismiss,  R.id.discover_dialog_jouer})
    public void onclick(View view)
    {
        switch (view.getId())
        {
            case R.id.discover_dialog_jouer:
                eventBus.post(new EventGPSPlay(true));
                dismiss();
                break;
            case R.id.discover_dialog_dismiss:
                eventBus.post(new EventGPSPlay(false));
                break;
        }

    }

}
