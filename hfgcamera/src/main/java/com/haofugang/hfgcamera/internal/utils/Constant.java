package com.haofugang.hfgcamera.internal.utils;

/**
 * Created by POST on 2017/3/22.
 * 视频录制常量
 */

public class Constant {
    public final static String VideoFileCache = "VIDEOCache";//视频文件夹
    public final static  long   MaxRECTime = 600;//最长录制的时间 单位以s结束
    public final static  int    MinRECTime = 11;//最短录制的时间 单位以s结束
    public final static  int    CPBitRate = 6; //压缩码率的倍数 十倍太模糊  不压缩太大 一般是5倍 小米5s原配是 20000000
    public final static  int    FrameRate = 1; //降低捕捉的帧的倍数   小米5s  s在caera1中设置会出现闪退


}
