package com.dysania.coolweather.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by DysaniazzZ on 21/02/2017.
 * 日期工具类
 */
public class DateUtil {

    /**
     * 获取日期字符串
     *
     * @param timeMills
     * @return
     */
    public static String getDateString(long timeMills) {
        Date date = new Date(timeMills);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    /**
     * 获取星期几
     *
     * @param timeMills
     * @return
     */
    public static String getWeekString(long timeMills) {
        Date date = new Date(timeMills);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");
        String weekString = simpleDateFormat.format(date);
        return weekString;
    }

    /**
     * 获取欢迎词
     *
     * @return
     */
    public static String getHelloWords(long timeMills) {
        String helloWords;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMills);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 8) {
            helloWords = "早上好！";
        } else if (hour >= 8 && hour < 11) {
            helloWords = "上午好！";
        } else if (hour >= 11 && hour < 13) {
            helloWords = "中午好！";
        } else if (hour >= 13 && hour < 18) {
            helloWords = "下午好！";
        } else {
            helloWords = "晚上好！";
        }
        return helloWords;
    }
}
