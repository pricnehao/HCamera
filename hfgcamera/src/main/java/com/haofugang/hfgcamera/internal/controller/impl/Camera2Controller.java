package com.haofugang.hfgcamera.internal.controller.impl;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.widget.Toast;

import com.haofugang.hfgcamera.internal.configuration.AnncaConfiguration;
import com.haofugang.hfgcamera.internal.configuration.ConfigurationProvider;
import com.haofugang.hfgcamera.internal.controller.CameraController;
import com.haofugang.hfgcamera.internal.controller.view.CameraView;
import com.haofugang.hfgcamera.internal.manager.CameraManager;
import com.haofugang.hfgcamera.internal.manager.impl.Camera2Manager;
import com.haofugang.hfgcamera.internal.manager.listener.CameraCloseListener;
import com.haofugang.hfgcamera.internal.manager.listener.CameraOpenListener;
import com.haofugang.hfgcamera.internal.manager.listener.CameraPhotoListener;
import com.haofugang.hfgcamera.internal.manager.listener.CameraVideoListener;
import com.haofugang.hfgcamera.internal.ui.BaseAnncaActivity;
import com.haofugang.hfgcamera.internal.ui.view.AutoFitTextureView;
import com.haofugang.hfgcamera.internal.utils.CameraHelper;
import com.haofugang.hfgcamera.internal.utils.CustomDialog;
import com.haofugang.hfgcamera.internal.utils.Size;

import java.io.File;


/**
 * Created by memfis on 7/6/16.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Camera2Controller implements CameraController<String>,
        CameraOpenListener<String, TextureView.SurfaceTextureListener>,
        CameraPhotoListener, CameraVideoListener, CameraCloseListener<String> {

    private final static String TAG = "Camera2Controller";

    private String currentCameraId;
    private ConfigurationProvider configurationProvider;
    private CameraManager<String, TextureView.SurfaceTextureListener, CaptureRequest.Builder, CameraDevice> camera2Manager;
    private CameraView cameraView;
    private boolean IsonBack = false;
    private File outputFile;

    public Camera2Controller(CameraView cameraView, ConfigurationProvider configurationProvider) {
        this.cameraView = cameraView;
        this.configurationProvider = configurationProvider;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        camera2Manager = Camera2Manager.getInstance();
        camera2Manager.initializeCameraManager(configurationProvider, cameraView.getActivity());
        currentCameraId = camera2Manager.getFaceBackCameraId();
    }

    @Override
    public void dispatchKeyEvent() {
        if (camera2Manager.isVideoRecording()) {

            new CustomDialog.Builder(cameraView.getActivity())
                    .setTitle("温馨提示：")
                    .setMessage("你确定要退出录制？")
                    .setPositiveButton("确定退出",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    IsonBack = true;
                                    cameraView.getActivity().onBackPressed();
                                    arg0.dismiss();
                                }

                            })
                    .setNegativeButton("继续录制",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }

                            }).create().show();
        } else {
            cameraView.getActivity().onBackPressed();
        }
    }


    @Override
    public void onResume() {
        camera2Manager.openCamera(currentCameraId, this);
    }

    @Override
    public void onPause() {

        if (camera2Manager.isVideoRecording()) {
            if(IsonBack)
            {
                camera2Manager.stopRecord();
                IsonBack =false;
            }else {
                if (((BaseAnncaActivity) cameraView.getActivity()).getTime() < 10) {
                    Toast.makeText(cameraView.getActivity(), "不支持后台录制!且录制时间少于10s，稍后请重新录制！", Toast.LENGTH_LONG).show();
                    camera2Manager.stopRecord();
                    cameraView.getActivity().onBackPressed();
                } else {
                    Toast.makeText(cameraView.getActivity(), "不支持后台录制！，视频录制停止！", Toast.LENGTH_LONG).show();
                    camera2Manager.stopVideoRecord();
                }
            }

        }
        camera2Manager.closeCamera(null);
        cameraView.releaseCameraPreview();
    }

    @Override
    public void onDestroy() {
        camera2Manager.releaseCameraManager();
    }

    @Override
    public void takePhoto() {
        outputFile = CameraHelper.getOutputMediaFile(cameraView.getActivity(), AnncaConfiguration.MEDIA_ACTION_PHOTO);
        camera2Manager.takePhoto(outputFile, this);
    }

    @Override
    public void startVideoRecord() {
        outputFile = CameraHelper.getOutputMediaFile(cameraView.getActivity(), AnncaConfiguration.MEDIA_ACTION_VIDEO);
        camera2Manager.startVideoRecord(outputFile, this);
    }

    @Override
    public void stopVideoRecord() {
        camera2Manager.stopVideoRecord();
    }

    @Override
    public boolean isVideoRecording() {
        return camera2Manager.isVideoRecording();
    }

    @Override
    public void switchCamera(final @AnncaConfiguration.CameraFace int cameraFace) {
        currentCameraId = camera2Manager.getCurrentCameraId().equals(camera2Manager.getFaceFrontCameraId()) ?
                camera2Manager.getFaceBackCameraId() : camera2Manager.getFaceFrontCameraId();

        camera2Manager.closeCamera(this);
    }

    @Override
    public void setFlashMode(@AnncaConfiguration.FlashMode int flashMode) {
        camera2Manager.setFlashMode(flashMode);
    }

    @Override
    public void switchQuality() {
        camera2Manager.closeCamera(this);
    }

    @Override
    public int getNumberOfCameras() {
        return camera2Manager.getNumberOfCameras();
    }

    @Override
    public int getMediaAction() {
        return configurationProvider.getMediaAction();
    }

    @Override
    public File getOutputFile() {
        return outputFile;
    }

    @Override
    public String getCurrentCameraId() {
        return currentCameraId;
    }

    @Override
    public void onCameraOpened(String openedCameraId, Size previewSize, TextureView.SurfaceTextureListener surfaceTextureListener) {
        cameraView.updateUiForMediaAction(AnncaConfiguration.MEDIA_ACTION_UNSPECIFIED);
        cameraView.updateCameraPreview(previewSize, new AutoFitTextureView(cameraView.getActivity(), surfaceTextureListener));
        cameraView.updateCameraSwitcher(camera2Manager.getNumberOfCameras());
    }

    @Override
    public void onCameraReady() {
        cameraView.onCameraReady();
    }

    @Override
    public void onCameraOpenError() {
        Log.e(TAG, "onCameraOpenError");
    }

    @Override
    public void onCameraClosed(String closedCameraId) {
        cameraView.releaseCameraPreview();

        camera2Manager.openCamera(currentCameraId, this);
    }

    @Override
    public void onPhotoTaken(File photoFile) {
        cameraView.onPhotoTaken();
    }

    @Override
    public void onPhotoTakeError() {
    }

    @Override
    public void onVideoRecordStarted(Size videoSize) {
        cameraView.onVideoRecordStart(videoSize.getWidth(), videoSize.getHeight());
    }

    @Override
    public void onVideoRecordStopped(File videoFile) {
        cameraView.onVideoRecordStop();
    }

    @Override
    public void onVideoRecordError() {

    }

    @Override
    public CameraManager getCameraManager() {
        return camera2Manager;
    }
}
