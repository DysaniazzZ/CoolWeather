package com.dysania.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DysaniazzZ on 23/02/2017.
 * 天气接口返回的生活指数
 */
public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort {

        @SerializedName("txt")
        public String info;
    }

    public class CarWash {

        @SerializedName("txt")
        public String info;
    }

    public class Sport {

        @SerializedName("txt")
        public String info;
    }
}
