package com.haofugang.hfgcamera.internal.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haofugang.hfgcamera.R;
import com.haofugang.hfgcamera.internal.configuration.AnncaConfiguration;
import com.haofugang.hfgcamera.internal.configuration.ConfigurationProvider;
import com.haofugang.hfgcamera.internal.controller.CameraController;
import com.haofugang.hfgcamera.internal.controller.view.CameraView;
import com.haofugang.hfgcamera.internal.ui.view.AspectFrameLayout;
import com.haofugang.hfgcamera.internal.utils.Size;
import com.haofugang.hfgcamera.internal.utils.Utils;


/**
 * Created by memfis on 12/1/16.
 * 拍摄界面
 */

abstract public class AnncaCameraActivity<CameraId> extends Activity
        implements ConfigurationProvider, CameraView, SensorEventListener {

    private SensorManager sensorManager = null;

    protected AspectFrameLayout previewContainer;
    protected ViewGroup userContainer;

    private CameraController<CameraId> cameraController;

    @AnncaConfiguration.SensorPosition
    protected int sensorPosition = AnncaConfiguration.SENSOR_POSITION_UNSPECIFIED;
    @AnncaConfiguration.DeviceDefaultOrientation
    protected int deviceDefaultOrientation;
    private int degrees = -1;
    /**
     * 自动对焦
     */
    Camera camera;
    Camera.Parameters parameters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraController = createCameraController(this, this);
        cameraController.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        int defaultOrientation = Utils.getDeviceDefaultOrientation(this);

        if (defaultOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            deviceDefaultOrientation = AnncaConfiguration.ORIENTATION_LANDSCAPE;
        } else if (defaultOrientation == Configuration.ORIENTATION_PORTRAIT) {
            deviceDefaultOrientation = AnncaConfiguration.ORIENTATION_PORTRAIT;
        }

        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT > 15) {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

        setContentView(R.layout.generic_camera_layout);

        previewContainer = (AspectFrameLayout) findViewById(R.id.previewContainer);
        userContainer = (ViewGroup) findViewById(R.id.userContainer);

        onProcessBundle(savedInstanceState);
        setUserContent();
    }

    protected void onProcessBundle(Bundle savedInstanceState) {

    }


    @Override
    protected void onResume() {
        super.onResume();

        cameraController.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        cameraController.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        cameraController.onDestroy();
    }

    public final CameraController<CameraId> getCameraController() {
        return cameraController;
    }

    public abstract CameraController<CameraId> createCameraController(CameraView cameraView, ConfigurationProvider configurationProvider);

    private void setUserContent() {
        userContainer.removeAllViews();
        userContainer.addView(getUserContentView(LayoutInflater.from(this), userContainer));
    }

    public final void setCameraPreview(View preview, Size previewSize) {
        if (previewContainer == null || preview == null) return;
        previewContainer.removeAllViews();
        previewContainer.addView(preview);

        previewContainer.setAspectRatio(previewSize.getHeight() / (double) previewSize.getWidth());
    }

    @Override
    public void onCameraReady() {
        onCameraControllerReady();
    }

    public final void clearCameraPreview() {
        if (previewContainer != null)
            previewContainer.removeAllViews();
    }

    protected abstract View getUserContentView(LayoutInflater layoutInflater, ViewGroup parent);
    /**
     * 传感器监听
     * @haofugang
     * @return
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        synchronized (this) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                if (sensorEvent.values[0] < 4 && sensorEvent.values[0] > -4) {
                    if (sensorEvent.values[1] > 0) {
                        // UP正方向
                        sensorPosition = AnncaConfiguration.SENSOR_POSITION_UP;
                        degrees = deviceDefaultOrientation == AnncaConfiguration.ORIENTATION_PORTRAIT ? 0 : 90;
                    }/* else if (sensorEvent.values[1] < 0) {
                        // UP SIDE DOWN  反方向
                        sensorPosition = AnncaConfiguration.SENSOR_POSITION_UP_SIDE_DOWN;
                        degrees = deviceDefaultOrientation == AnncaConfiguration.ORIENTATION_PORTRAIT ? 180 : 270;
                    }*/
                } else if (sensorEvent.values[1] < 4 && sensorEvent.values[1] > -4) {
                    if (sensorEvent.values[0] > 0) {
                        // LEFT
                        sensorPosition = AnncaConfiguration.SENSOR_POSITION_LEFT;
                        degrees = deviceDefaultOrientation == AnncaConfiguration.ORIENTATION_PORTRAIT ? 90 : 180;
                    } else if (sensorEvent.values[0] < 0) {
                        // RIGHT
                        sensorPosition = AnncaConfiguration.SENSOR_POSITION_RIGHT;
                        degrees = deviceDefaultOrientation == AnncaConfiguration.ORIENTATION_PORTRAIT ? 270 : 0;
                    }
                }
                onScreenRotation(degrees);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public final int getSensorPosition() {
        return sensorPosition;
    }

    @Override
    public final int getDegrees() {
        return degrees;
    }

    protected abstract void onScreenRotation(int degrees);

    protected void onCameraControllerReady() {
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() != KeyEvent.ACTION_UP) {
            cameraController.dispatchKeyEvent();
            return true;// 不响应按键抬起时的动作
        }
        return super.dispatchKeyEvent(event);
    }

}
