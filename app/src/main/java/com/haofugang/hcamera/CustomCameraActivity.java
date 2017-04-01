package com.haofugang.hcamera;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.haofugang.hfgcamera.internal.configuration.AnncaConfiguration;
import com.haofugang.hfgcamera.internal.configuration.ConfigurationProvider;
import com.haofugang.hfgcamera.internal.controller.CameraController;
import com.haofugang.hfgcamera.internal.controller.impl.Camera1Controller;
import com.haofugang.hfgcamera.internal.controller.view.CameraView;
import com.haofugang.hfgcamera.internal.ui.AnncaCameraActivity;
import com.haofugang.hfgcamera.internal.utils.Size;

/**
 * Created by memfis on 2/7/17.
 */

public class CustomCameraActivity extends AnncaCameraActivity<Integer> {

    private static final int REQUEST_CODE = 404;

    @AnncaConfiguration.MediaAction
    private static final int PHOTO = AnncaConfiguration.MEDIA_ACTION_PHOTO;

    @AnncaConfiguration.MediaQuality
    private static final int QUALITY = AnncaConfiguration.MEDIA_QUALITY_HIGH;

    @AnncaConfiguration.FlashMode
    private static final int FLASH = AnncaConfiguration.FLASH_MODE_AUTO;

    @Override
    protected View getUserContentView(LayoutInflater layoutInflater, ViewGroup parent) {
        RelativeLayout customCameraLayout = (RelativeLayout) layoutInflater.inflate(R.layout.custom_camera_layout, parent, false);

        customCameraLayout.findViewById(R.id.take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCameraController().takePhoto();
            }
        });

        return customCameraLayout;
    }

    @Override
    public int getRequestCode() {
        return REQUEST_CODE;
    }

    @Override
    public int getMediaAction() {
        return PHOTO;
    }

    @Override
    public int getMediaQuality() {
        return QUALITY;
    }

    @Override
    public int getVideoDuration() {
        return 1000;
    }

    @Override
    public long getVideoFileSize() {
        return 5 * 1024 * 1024;
    }

    @Override
    public int getMinimumVideoDuration() {
        return 1000;
    }

    @Override
    public int getFlashMode() {
        return FLASH;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void updateCameraPreview(Size size, View cameraPreview) {
        setCameraPreview(cameraPreview, size);
    }

    @Override
    public void updateUiForMediaAction(@AnncaConfiguration.MediaAction int mediaAction) {

    }

    @Override
    public void updateCameraSwitcher(int numberOfCameras) {

    }

    @Override
    public void onPhotoTaken() {
        Toast.makeText(this, "Result file: " + String.valueOf(getCameraController().getOutputFile().toString()), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onVideoRecordStart(int width, int height) {

    }

    @Override
    public void onVideoRecordStop() {

    }

    @Override
    public void releaseCameraPreview() {

    }

    @Override
    public CameraController<Integer> createCameraController(CameraView cameraView, ConfigurationProvider configurationProvider) {
        return new Camera1Controller(cameraView, configurationProvider);
    }

    @Override
    protected void onScreenRotation(int degrees) {

    }
}
