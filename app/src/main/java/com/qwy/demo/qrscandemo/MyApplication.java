package com.qwy.demo.qrscandemo;

import android.app.Application;
import android.graphics.Bitmap;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;

/**
 * Created by qwy on 17/3/31.
 * 自定义application
 */
public class MyApplication extends Application {

    public static Bitmap globalBitmap = null;

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());
    }
}
