package com.coder520.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zrkj on 2017/7/31.
 */
public class DateUtils {

    private static Calendar calendar = Calendar.getInstance();
    // 格式化星期
    public static int getTodayWeek() {
        calendar.setTime(new Date());
       int week = calendar.get(Calendar.DAY_OF_WEEK);
       if (week > 0) {
           return week-1;
       } else {
           return 7;
       }
    }

    //  计算时间差
    public static int getMunite(Date startDate, Date endDate){
        long start = startDate.getTime();
        long end = endDate.getTime();
        int munite = (int) (end - start) / (1000 * 60);
        return munite;
    }


    // 获取当天的某个时间
    public static Date getDate(int hour, int munite) {
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,munite);
        return calendar.getTime();
    }
}
