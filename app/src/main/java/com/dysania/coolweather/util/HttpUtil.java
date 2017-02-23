package com.dysania.coolweather.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by DysaniazzZ on 22/02/2017.
 * Http请求工具类
 */
public class HttpUtil {

    public static void sendOkHttpRequest(String address, Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
