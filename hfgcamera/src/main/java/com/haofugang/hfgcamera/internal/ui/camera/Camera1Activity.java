package com.haofugang.hfgcamera.internal.ui.camera;

import android.media.CamcorderProfile;

import com.haofugang.hfgcamera.internal.configuration.AnncaConfiguration;
import com.haofugang.hfgcamera.internal.configuration.ConfigurationProvider;
import com.haofugang.hfgcamera.internal.controller.CameraController;
import com.haofugang.hfgcamera.internal.controller.impl.Camera1Controller;
import com.haofugang.hfgcamera.internal.controller.view.CameraView;
import com.haofugang.hfgcamera.internal.ui.BaseAnncaActivity;
import com.haofugang.hfgcamera.internal.ui.model.PhotoQualityOption;
import com.haofugang.hfgcamera.internal.ui.model.VideoQualityOption;
import com.haofugang.hfgcamera.internal.utils.CameraHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by memfis on 7/6/16.
 */
@SuppressWarnings("deprecation")
public class Camera1Activity extends BaseAnncaActivity<Integer> {

    @Override
    public CameraController<Integer> createCameraController(CameraView cameraView, ConfigurationProvider configurationProvider) {
        return new Camera1Controller(cameraView, configurationProvider);
    }

    @Override
    protected CharSequence[] getVideoQualityOptions() {
        List<CharSequence> videoQualities = new ArrayList<>();

        if (getMinimumVideoDuration() > 0)
            videoQualities.add(new VideoQualityOption(AnncaConfiguration.MEDIA_QUALITY_AUTO, CameraHelper.getCamcorderProfile(AnncaConfiguration.MEDIA_QUALITY_AUTO, getCameraController().getCurrentCameraId()), getMinimumVideoDuration()));

        CamcorderProfile camcorderProfile = CameraHelper.getCamcorderProfile(AnncaConfiguration.MEDIA_QUALITY_HIGH, getCameraController().getCurrentCameraId());
        double videoDuration = CameraHelper.calculateApproximateVideoDuration(camcorderProfile, getVideoFileSize());
        videoQualities.add(new VideoQualityOption(AnncaConfiguration.MEDIA_QUALITY_HIGH, camcorderProfile, videoDuration));

        camcorderProfile = CameraHelper.getCamcorderProfile(AnncaConfiguration.MEDIA_QUALITY_MEDIUM, getCameraController().getCurrentCameraId());
        videoDuration = CameraHelper.calculateApproximateVideoDuration(camcorderProfile, getVideoFileSize());
        videoQualities.add(new VideoQualityOption(AnncaConfiguration.MEDIA_QUALITY_MEDIUM, camcorderProfile, videoDuration));

        camcorderProfile = CameraHelper.getCamcorderProfile(AnncaConfiguration.MEDIA_QUALITY_LOW, getCameraController().getCurrentCameraId());
        videoDuration = CameraHelper.calculateApproximateVideoDuration(camcorderProfile, getVideoFileSize());
        videoQualities.add(new VideoQualityOption(AnncaConfiguration.MEDIA_QUALITY_LOW, camcorderProfile, videoDuration));

        CharSequence[] array = new CharSequence[videoQualities.size()];
        videoQualities.toArray(array);

        return array;
    }

    @Override
    protected CharSequence[] getPhotoQualityOptions() {
        List<CharSequence> photoQualities = new ArrayList<>();
        photoQualities.add(new PhotoQualityOption(AnncaConfiguration.MEDIA_QUALITY_HIGHEST, getCameraController().getCameraManager().getPhotoSizeForQuality(AnncaConfiguration.MEDIA_QUALITY_HIGHEST)));
        photoQualities.add(new PhotoQualityOption(AnncaConfiguration.MEDIA_QUALITY_HIGH, getCameraController().getCameraManager().getPhotoSizeForQuality(AnncaConfiguration.MEDIA_QUALITY_HIGH)));
        photoQualities.add(new PhotoQualityOption(AnncaConfiguration.MEDIA_QUALITY_MEDIUM, getCameraController().getCameraManager().getPhotoSizeForQuality(AnncaConfiguration.MEDIA_QUALITY_MEDIUM)));
        photoQualities.add(new PhotoQualityOption(AnncaConfiguration.MEDIA_QUALITY_LOWEST, getCameraController().getCameraManager().getPhotoSizeForQuality(AnncaConfiguration.MEDIA_QUALITY_LOWEST)));

        CharSequence[] array = new CharSequence[photoQualities.size()];
        photoQualities.toArray(array);

        return array;
    }

}
