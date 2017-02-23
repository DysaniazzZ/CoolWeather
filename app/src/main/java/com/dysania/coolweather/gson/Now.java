package com.dysania.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DysaniazzZ on 23/02/2017.
 * 天气接口返回的实况天气
 */
public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {

        @SerializedName("txt")
        public String info;
    }
}
