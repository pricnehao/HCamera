package com.haofugang.hfgcamera.internal.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.haofugang.hfgcamera.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



/**
 * Created by haofugang on 3/23/17.
 */
public class FlashSwitchView extends ImageButton {

    @FlashMode
    private int currentMode = FLASH_OFF;

    private FlashModeSwitchListener switchListener;
    private Drawable flashOnDrawable;
    private Drawable flashOffDrawable;


    private int tintColor = Color.WHITE;

    public static final int FLASH_ON = 0;
    public static final int FLASH_OFF = 1;


    @IntDef({FLASH_ON, FLASH_OFF})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FlashMode {
    }

    public interface FlashModeSwitchListener {
        void onFlashModeChanged(@FlashMode int mode);
    }

    public FlashSwitchView(@NonNull Context context) {
        this(context, null);
    }

    public FlashSwitchView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        flashOnDrawable = ContextCompat.getDrawable(context, R.drawable.ic_flash_on_white_24dp);
        flashOffDrawable = ContextCompat.getDrawable(context, R.drawable.ic_flash_off_white_24dp);

        init();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
        setOnClickListener(new FlashButtonClickListener());
        setIcon();
    }

    private void setIcon() {
        if (FLASH_OFF == currentMode) {
            setImageDrawable(flashOffDrawable);
        } else if (FLASH_ON == currentMode) {
            setImageDrawable(flashOnDrawable);
        }

    }

    private void setIconsTint(@ColorInt int tintColor) {
        this.tintColor = tintColor;
        flashOnDrawable.setColorFilter(tintColor, PorterDuff.Mode.MULTIPLY);
        flashOffDrawable.setColorFilter(tintColor, PorterDuff.Mode.MULTIPLY);

    }

    public void setFlashMode(@FlashMode int mode) {
        this.currentMode = mode;
        setIcon();
    }

    @FlashMode
    public int getCurrentFlasMode() {
        return currentMode;
    }

    public void setFlashSwitchListener(@NonNull FlashModeSwitchListener switchListener) {
        this.switchListener = switchListener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (Build.VERSION.SDK_INT > 10) {
            if (enabled) {
                setAlpha(1f);
            } else {
                setAlpha(0.5f);
            }
        }
    }

    private class FlashButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (FLASH_OFF == currentMode) {
                currentMode = FLASH_ON;
            } else if (FLASH_ON == currentMode) {
                currentMode = FLASH_OFF;
            }
            setIcon();
            if (switchListener != null) {
                switchListener.onFlashModeChanged(currentMode);
            }
        }
    }
}
