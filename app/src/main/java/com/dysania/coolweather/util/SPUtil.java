package com.dysania.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DysaniazzZ on 23/02/2017.
 * SharedPreferences存储工具类
 */
public class SPUtil {

    private static SharedPreferences sp;

    private static SharedPreferences getSharedPreferences(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("Weather_Cache", Context.MODE_PRIVATE);
        }
        return sp;
    }

    public static void putString(Context context, String key, String value) {
        sp = getSharedPreferences(context);
        sp.edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key, String defValue) {
        sp = getSharedPreferences(context);
        return sp.getString(key, defValue);
    }

    public static void remove(Context context, String key) {
        sp = getSharedPreferences(context);
        sp.edit().remove(key).apply();
    }

    public static void clear(Context context) {
        sp = getSharedPreferences(context);
        sp.edit().clear().apply();
    }
}
