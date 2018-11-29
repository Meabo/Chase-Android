package project.chasemvp.Utils.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import mehdi.sakout.fancybuttons.FancyButton;
import project.chasemvp.R;
import project.chasemvp.Utils.CustomTextView;

/**
 * Created by Mehdi on 14/07/2017.
 */

public class MultiPlayerDialog extends Dialog
{

    CustomTextView guardian_name;
    FancyButton dialog_btn;
    CircularImageView guardian_image;

    public MultiPlayerDialog(@NonNull Context context, int themeResId)
    {
        super(context, themeResId);
        this.setContentView(R.layout.dialog_guardian);
        guardian_name = (CustomTextView) findViewById(R.id.dialog_guardian_name);
        guardian_image = (CircularImageView) findViewById(R.id.dialog_guardian_image);
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

    public void show_guardian(String pseudo, String url_image)
    {
        guardian_name.setText(pseudo + " is the guardian");
        Picasso.with(getContext())
                .load(url_image)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(guardian_image);
        this.show();

    }

}
