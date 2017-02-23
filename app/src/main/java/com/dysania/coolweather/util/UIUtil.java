package com.dysania.coolweather.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by DysaniazzZ on 23/02/2017.
 * UI工具类
 */
public class UIUtil {

    private static Toast mToast;

    /**
     * 创建Toast并显示
     *
     * @param context
     * @param msg
     */
    public static void createToast(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    /**
     * 创建Toast并显示
     *
     * @param context
     * @param strId   strings里的字符串id
     */
    public static void createToast(Context context, int strId) {
        createToast(context, context.getResources().getString(strId));
    }
}
