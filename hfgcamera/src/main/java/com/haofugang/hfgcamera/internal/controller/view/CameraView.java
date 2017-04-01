package com.haofugang.hfgcamera.internal.controller.view;

import android.app.Activity;
import android.view.View;

import com.haofugang.hfgcamera.internal.configuration.AnncaConfiguration;
import com.haofugang.hfgcamera.internal.utils.Size;


/**
 * Created by memfis on 7/6/16.
 */
public interface CameraView {

    Activity getActivity();

    void updateCameraPreview(Size size, View cameraPreview);

    void updateUiForMediaAction(@AnncaConfiguration.MediaAction int mediaAction);

    void updateCameraSwitcher(int numberOfCameras);

    void onPhotoTaken();

    void onVideoRecordStart(int width, int height);

    void onVideoRecordStop();

    void releaseCameraPreview();

    void onCameraReady();
}
