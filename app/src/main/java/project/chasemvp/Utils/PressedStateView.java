package project.chasemvp.Utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Mehdi on 16/07/2017.
 */

public class PressedStateView extends View {
    public PressedStateView(Context context) {
        super(context);
    }

    public PressedStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PressedStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PressedStateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private Drawable overlay;

    public void setOverlay(Drawable overlay) {
        if (this.overlay != null) {
            this.overlay.setCallback(null);
        }
        this.overlay = overlay;
        this.overlay.setCallback(this);
        this.overlay.setBounds(0, 0, getWidth(), getHeight());
        invalidate();
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.overlay.setBounds(0, 0, w, h);
    }

    @Override protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == overlay;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (overlay != null) {
            overlay.setHotspot(x, y);
        }
    }

    @Override protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (overlay != null) {
            overlay.draw(canvas);
        }
    }
}