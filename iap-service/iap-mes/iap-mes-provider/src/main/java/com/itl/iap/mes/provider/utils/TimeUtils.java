package com.itl.iap.mes.provider.utils;


import com.itl.iap.mes.provider.config.Constant;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtils {

    public static String nextMonth(String dateStr,int n,int d) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

       // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        if(Constant.YtdEnum.MONTH.getItem() == d){
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + n);
        }else if(Constant.YtdEnum.DAY.getItem() == d){
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + n);
        }else{
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + n);
        }

        date = calendar.getTime();
        String accDate = format.format(date);
        return accDate;
    }


    public static boolean checkBig(String dateStr, String otherDateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if(sdf.parse(dateStr).getTime()>=sdf.parse(otherDateStr).getTime()){
                return true;
            }
            return false;
        } catch (ParseException e) {
            throw new RuntimeException("转化时间失败");
        }

    }
    /*
    n间隔
    d 年0 月1 天2
     */
    public static void digui(List<String> list,String time, String endTime,int n,int d){
        if(checkBig(time,endTime)){
            return;
        }else{
            list.add(time);
            String nextTime = nextMonth(time,n,d);
            digui(list,nextTime,endTime,n,d);
        }
    }

    public static String afterNDay(int n){
        Calendar c=Calendar.getInstance();
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        c.setTime(new Date());
        c.add(Calendar.DATE,n);
        Date d2=c.getTime();
        String s=df.format(d2);
        return s;
    }


    public static void main(String[] args) {
//        BigDecimal a = new BigDecimal("2000");
//        BigDecimal b = new BigDecimal("3000");
//        System.out.print(a.compareTo(b));
    //    System.out.print(afterNDay(3));

   //     System.out.print(nextMonth("2020-10-31 23:40:06",1,2));

//        List<String> list = new ArrayList<>();
//
//        TimeUtils.digui(list,"2020-10-39 14:23:00","2020-10-30 14:23:00",2,2);
//        System.out.print(list);

    }
}
