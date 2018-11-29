package project.chasemvp.Utils.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import mehdi.sakout.fancybuttons.FancyButton;
import project.chasemvp.Model.POJO.Discover.Area;
import project.chasemvp.R;
import project.chasemvp.Utils.CustomTextView;

/**
 * Created by Mehdi on 23/06/2017.
 */

public class CustomDialog extends Dialog
{
    CustomTextView guardian_name;
    FancyButton dialog_btn;

    public CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.setContentView(R.layout.dialog_get_object);
        guardian_name = (CustomTextView) findViewById(R.id.dialog_guardian_name);
        dialog_btn = (FancyButton) findViewById(R.id.take_it_button);
        dialog_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
              dismiss();
            }
            // Perform button logic
        });


    }

    public void show_alert(String pseudo)
    {
        //guardian_name.setText(pseudo);
        this.show();

    }

    public void show_area(Area a)
    {
        guardian_name.setText(a.getName());
        this.show();
    }

}
