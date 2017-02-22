package com.dysania.coolweather.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by DysaniazzZ on 21/02/2017.
 */
public class CWApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mContext;
    }
}
