package com.dysania.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DysaniazzZ on 23/02/2017.
 * 天气接口返回的基本信息
 */
public class Basic {

    @SerializedName("city")
    public String cityName; //由于JSON中的有些字段可能不太适合直接作为Java字段名，所以使用注解来使Java字段和JSON字段建立关联

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {

        @SerializedName("loc")
        public String updateTime;
    }
}
