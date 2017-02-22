package com.dysania.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by DysaniazzZ on 22/02/2017.
 */
public class County extends DataSupport {

    private int id;
    private String countyName;  //县名
    private String weatherId;   //天气id
    private int cityId;         //所属城市id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
