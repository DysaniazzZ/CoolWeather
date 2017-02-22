package com.dysania.coolweather.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.dysania.coolweather.base.CWApplication;

/**
 * Created by DysaniazzZ on 21/02/2017.
 * 设备工具类
 */
public class DeviceUtil {

    /**
     * 获取应用版本号
     *
     * @return
     */
    public static int getVersionCode() {
        int versionCode = 0;
        try {
            PackageManager packageManager = CWApplication.getAppContext().getPackageManager();
            String packageName = CWApplication.getAppContext().getPackageName();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            versionCode = packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取应用版本名
     *
     * @return
     */
    public static String getVersionName() {
        String versionName = "";
        try {
            PackageManager packageManager = CWApplication.getAppContext().getPackageManager();
            String packageName = CWApplication.getAppContext().getPackageName();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            versionName = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取应用名称
     *
     * @return
     */
    public static String getAppLabel() {
        String appLabel = "";
        try {
            PackageManager packageManager = CWApplication.getAppContext().getPackageManager();
            String packageName = CWApplication.getAppContext().getPackageName();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            appLabel = packageManager.getApplicationLabel(applicationInfo).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appLabel;
    }
}
