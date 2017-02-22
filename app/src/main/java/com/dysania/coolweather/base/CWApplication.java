package com.dysania.coolweather.base;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by DysaniazzZ on 21/02/2017.
 */
public class CWApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initSDKs();
    }

    public static Context getAppContext() {
        return mContext;
    }

    /**
     * 用于初始化一些集成的SDK
     */
    private void initSDKs() {
        LitePal.initialize(mContext);   //初始化数据库框架：LitePal
    }
}
