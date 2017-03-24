package me.keenor.mycommonlib;

import android.app.Application;
import android.content.Context;

import me.keenor.androidcommon.util.AppUtil;

/**
 * Created by chenliuchun on 17/3/16.
 */

public class MyApp extends Application {

    private static MyApp sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        // 设置app上下文
        sAppContext = this;

        // AndroidCommon module 工具类设置上下文
        AppUtil.setContext(this.getApplicationContext());

    }

    public static Context getContext() {
        return sAppContext.getApplicationContext();
    }

}
