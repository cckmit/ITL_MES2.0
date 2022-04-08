package com.itl.iap.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具
 *
 * @author Linjl
 * @date 2020-06-12
 * @since jdk1.8
 */
public class DateUtil {

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 获取格式化时间（yyyy-MM-dd HH:mm:ss）
     *
     * @param timestamp 时间戳
     * @return String
     */
    public static String date(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }

    /**
     * 日期类型转字符串
     *
     * @param date
     * @return String
     */
    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 日期类型转字符串(转化为yyyyMMdd格式)
     *
     * @param date
     * @return String
     */
    public static String dateToYyyyMmDd(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }

    /**
     * 日期类型转字符串(转化为yyyyMM格式)
     *
     * @param date
     * @return String
     */
    public static String dateToYyyyMm(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        return sdf.format(date);
    }

    /**
     * 日期格式转化
     *
     * @param date
     * @param format
     * @return String
     */
    public static String dateFormat(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 把秒转换成日期
     * @param mss
     * @return
     */
    public static String formatDateTime(long mss) {
        String dateTimes = null;
        long days = mss / ( 60 * 60 * 24);
        long hours = (mss % ( 60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % ( 60 * 60)) /60;
        long seconds = mss % 60;
        if(days>0){
            dateTimes= days + "天" + hours + "小时" + minutes + "分钟"
                    + seconds + "秒";
        }else if(hours>0){
            dateTimes=hours + "小时" + minutes + "分钟"
                    + seconds + "秒";
        }else if(minutes>0){
            dateTimes=minutes + "分钟"
                    + seconds + "秒";
        }else{
            dateTimes=seconds + "秒";
        }
        return dateTimes;
    }
}
