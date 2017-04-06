package com.haofugang.hfgcamera.internal.controller;

import android.os.Bundle;

import com.haofugang.hfgcamera.internal.configuration.AnncaConfiguration;
import com.haofugang.hfgcamera.internal.manager.CameraManager;

import java.io.File;

/**
 * Created by memfis on 7/6/16.
 */
public interface CameraController<CameraId> {

    void onCreate(Bundle savedInstanceState);

    void dispatchKeyEvent();

    void onResume();

    void onPause();

    void onDestroy();

    void takePhoto();

    void startVideoRecord();

    void stopVideoRecord();

    boolean isVideoRecording();

    void switchCamera(@AnncaConfiguration.CameraFace int cameraFace);

    void switchQuality();

    void setFlashMode(@AnncaConfiguration.FlashMode int flashMode);

    int getNumberOfCameras();

    @AnncaConfiguration.MediaAction
    int getMediaAction();

    CameraId getCurrentCameraId();

    File getOutputFile();

    CameraManager getCameraManager();
}
