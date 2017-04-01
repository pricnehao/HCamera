package com.haofugang.hfgcamera.internal.manager.listener;


import com.haofugang.hfgcamera.internal.utils.Size;

/**
 * Created by memfis on 8/14/16.
 */
public interface CameraOpenListener<CameraId, SurfaceListener> {
    void onCameraOpened(CameraId openedCameraId, Size previewSize, SurfaceListener surfaceListener);

    void onCameraReady();

    void onCameraOpenError();
}
