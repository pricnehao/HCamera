package com.haofugang.hfgcamera.internal.utils;

/**
 * Created by POST on 2017/3/22.
 * 视频录制常量
 */

public class Constant {
    public final static String VideoFileCache = "VIDEOCache";//视频文件夹
    public final static  long   MaxRECTime = 600;//最长录制的时间 单位以s结束
    public final static  int    MinRECTime = 10;//最短录制的时间 单位以s结束
    public final static  int    CPBitRate = 5; //压缩码率的倍数 十倍太模糊  不压缩太大 一般是5倍
    public final static  int    FrameRate = 3; //降低捕捉的帧的倍数


}
