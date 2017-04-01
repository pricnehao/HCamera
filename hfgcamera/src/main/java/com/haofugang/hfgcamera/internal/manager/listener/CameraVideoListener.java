package com.haofugang.hfgcamera.internal.manager.listener;

import com.haofugang.hfgcamera.internal.utils.Size;

import java.io.File;


/**
 * Created by memfis on 8/14/16.
 */
public interface CameraVideoListener {
    void onVideoRecordStarted(Size videoSize);

    void onVideoRecordStopped(File videoFile);

    void onVideoRecordError();
}
