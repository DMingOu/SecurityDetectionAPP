package com.example.odm.securitydetectionapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: ODM
 * @date: 2019/7/27
 */
public class TimeUtil {


    /**
     * 展示当前系统时间
     *
     * @param currentTime the current time:System.currentTimeMillis()
     * @return the string
     */
    public static String showCurrentTime (long currentTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(currentTime);
        String time = simpleDateFormat.format(date);
        return time;
    }

    public static String transformToString(Long milliseconds) {
        Date date = new Date(Long.parseLong(milliseconds+""));
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }


}