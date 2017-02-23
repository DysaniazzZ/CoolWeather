package com.dysania.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DysaniazzZ on 23/02/2017.
 * 天气接口返回的天气预报
 */
public class Forecast {

    public String date;

    @SerializedName("cond")
    public More more;

    @SerializedName("tmp")
    public Temperature temperature;

    public class Temperature {

        public String max;
        public String min;
    }

    public class More {

        @SerializedName("txt_d")
        public String info;
    }
}
