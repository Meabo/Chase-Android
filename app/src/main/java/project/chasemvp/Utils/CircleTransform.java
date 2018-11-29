package project.chasemvp.Utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

/**
 * Created by Mehdi on 12/07/2017.
 */

public class CircleTransform implements Transformation {
    @Override
    public Bitmap transform(Bitmap source) {
       Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

      return transformation.transform(source);
    }

    @Override
    public String key() {
        return "circle-image";
    }
}
