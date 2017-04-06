package com.haofugang.hfgcamera.internal.controller.impl;

import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.haofugang.hfgcamera.internal.configuration.AnncaConfiguration;
import com.haofugang.hfgcamera.internal.configuration.ConfigurationProvider;
import com.haofugang.hfgcamera.internal.controller.CameraController;
import com.haofugang.hfgcamera.internal.controller.view.CameraView;
import com.haofugang.hfgcamera.internal.manager.CameraManager;
import com.haofugang.hfgcamera.internal.manager.impl.Camera1Manager;
import com.haofugang.hfgcamera.internal.manager.listener.CameraCloseListener;
import com.haofugang.hfgcamera.internal.manager.listener.CameraOpenListener;
import com.haofugang.hfgcamera.internal.manager.listener.CameraPhotoListener;
import com.haofugang.hfgcamera.internal.manager.listener.CameraVideoListener;
import com.haofugang.hfgcamera.internal.ui.BaseAnncaActivity;
import com.haofugang.hfgcamera.internal.ui.view.AutoFitSurfaceView;
import com.haofugang.hfgcamera.internal.utils.CameraHelper;
import com.haofugang.hfgcamera.internal.utils.CustomDialog;
import com.haofugang.hfgcamera.internal.utils.Size;

import java.io.File;


/**
 * Created by memfis on 7/7/16.
 */

@SuppressWarnings("deprecation")
public class Camera1Controller implements CameraController<Integer>,
        CameraOpenListener<Integer, SurfaceHolder.Callback>, CameraPhotoListener, CameraCloseListener<Integer>, CameraVideoListener {

    private final static String TAG = "Camera1Controller";
    private boolean IsonBack = false;
    private CustomDialog customDialog;
    private Integer currentCameraId;
    private ConfigurationProvider configurationProvider;
    private CameraManager<Integer, SurfaceHolder.Callback, Camera.Parameters, Camera> cameraManager;
    private CameraView cameraView;

    private File outputFile;

    public Camera1Controller(CameraView cameraView, ConfigurationProvider configurationProvider) {
        this.cameraView = cameraView;
        this.configurationProvider = configurationProvider;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        cameraManager = Camera1Manager.getInstance();
        cameraManager.initializeCameraManager(configurationProvider, cameraView.getActivity());
        currentCameraId = cameraManager.getFaceBackCameraId();
    }

    @Override
    public void dispatchKeyEvent() {
        if (cameraManager.isVideoRecording()){

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
        }
          else {
            cameraView.getActivity().onBackPressed();
        }
    }



    @Override
    public void onResume() {
        cameraManager.openCamera(currentCameraId, this);
    }

    @Override
    public void onPause() {

        if (cameraManager.isVideoRecording()) {
            if(IsonBack)
            {
                cameraManager.stopRecord();
                IsonBack =false;
            }else {
                if (((BaseAnncaActivity) cameraView.getActivity()).getTime() < 10) {
                    Toast.makeText(cameraView.getActivity(), "不支持后台录制!且录制时间少于10s，稍后请重新录制！", Toast.LENGTH_LONG).show();
                    cameraManager.stopRecord();
                    cameraView.getActivity().onBackPressed();
                } else {
                    Toast.makeText(cameraView.getActivity(), "不支持后台录制！，视频录制停止！", Toast.LENGTH_LONG).show();
                    cameraManager.stopVideoRecord();
                }
            }

        }
        cameraManager.closeCamera(null);
    }

    @Override
    public void onDestroy() {
        cameraManager.releaseCameraManager();
    }

    @Override
    public void takePhoto() {
        outputFile = CameraHelper.getOutputMediaFile(cameraView.getActivity(), AnncaConfiguration.MEDIA_ACTION_PHOTO);
        cameraManager.takePhoto(outputFile, this);
    }

    @Override
    public void startVideoRecord() {
        outputFile = CameraHelper.getOutputMediaFile(cameraView.getActivity(), AnncaConfiguration.MEDIA_ACTION_VIDEO);
        cameraManager.startVideoRecord(outputFile, this);
    }

    @Override
    public void stopVideoRecord() {
        cameraManager.stopVideoRecord();
    }

    @Override
    public boolean isVideoRecording() {
        return cameraManager.isVideoRecording();
    }

    @Override
    public void switchCamera(@AnncaConfiguration.CameraFace final int cameraFace) {
        currentCameraId = cameraManager.getCurrentCameraId().equals(cameraManager.getFaceFrontCameraId()) ?
                cameraManager.getFaceBackCameraId() : cameraManager.getFaceFrontCameraId();

        cameraManager.closeCamera(this);
    }

    @Override
    public void setFlashMode(@AnncaConfiguration.FlashMode int flashMode) {
        cameraManager.setFlashMode(flashMode);
    }

    @Override
    public void switchQuality() {
        cameraManager.closeCamera(this);
    }

    @Override
    public int getNumberOfCameras() {
        return cameraManager.getNumberOfCameras();
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
    public Integer getCurrentCameraId() {
        return currentCameraId;
    }


    @Override
    public void onCameraOpened(Integer cameraId, Size previewSize, SurfaceHolder.Callback surfaceCallback) {
        cameraView.updateUiForMediaAction(configurationProvider.getMediaAction());
        cameraView.updateCameraPreview(previewSize, new AutoFitSurfaceView(cameraView.getActivity(), surfaceCallback));
        cameraView.updateCameraSwitcher(getNumberOfCameras());
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
    public void onCameraClosed(Integer closedCameraId) {
        cameraView.releaseCameraPreview();

        cameraManager.openCamera(currentCameraId, this);
    }

    @Override
    public void onPhotoTaken(File photoFile) {
        cameraView.onPhotoTaken();
    }

    @Override
    public void onPhotoTakeError() {
    }

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
        return cameraManager;
    }
}
