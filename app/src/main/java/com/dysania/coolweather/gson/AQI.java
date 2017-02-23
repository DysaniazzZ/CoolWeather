package com.dysania.coolweather.gson;

/**
 * Created by DysaniazzZ on 23/02/2017.
 * 天气接口返回的空气信息
 */
public class AQI {

    public AQICity city;

    public class AQICity {

        public String aqi;
        public String pm25;
    }
}
