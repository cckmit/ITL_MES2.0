package com.itl.mes.pp.provider.util;

import com.itl.mes.pp.provider.common.CommonCode;
import com.itl.mes.pp.provider.exception.CustomException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TimeUtils {
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式


    public static String getAdvanceTime(String time, Double n) {
        try {
            Date date = sdf.parse(time);
            Double num = n*60.0;
            long l = Math.round(num);
            long Time=(date.getTime()/1000)-60*l;
            date.setTime(Time*1000);
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAfterTime(String time, Double n) {
        try {
            Date date = sdf.parse(time);
            Double num = n*60.0;
            long l = Math.round(num);
            long Time=(date.getTime()/1000)+60*l;
            date.setTime(Time*1000);

            System.out.print(sdf.format(date));


//            Date parse = sdf.parse(time);
//            //（3）计算推迟n小时的时间
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(parse);
//            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)+ n);
//            String endTime = sdf.format(calendar.getTime());
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param nowTime   当前时间
     * @param startTime	开始时间
     * @param endTime   结束时间
     * @return
     * @author sunran   判断当前时间在时间区间内
     */
    public static boolean isEffectiveDate(String nowTimeStr, String startTimeStr, String endTimeStr) {
        try {
            Date nowTime = sdf.parse(nowTimeStr);
            Date startTime = sdf.parse(startTimeStr);
            Date endTime = sdf.parse(endTimeStr);
            if (nowTime.getTime() == startTime.getTime()
                    || nowTime.getTime() == endTime.getTime()) {
                return true;
            }

            Calendar date = Calendar.getInstance();
            date.setTime(nowTime);

            Calendar begin = Calendar.getInstance();
            begin.setTime(startTime);

            Calendar end = Calendar.getInstance();
            end.setTime(endTime);

            if (date.after(begin) && date.before(end)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            throw new CustomException(CommonCode.FAIL);
        }
    }

    public static double getDatePoor(String endDateStr, String nowDateStr) {

        try {
            Date endDate = sdf.parse(endDateStr);
            Date nowDate = sdf.parse(nowDateStr);

            long nd = 1000 * 24 * 60 * 60;
            long nh = 1000 * 60 * 60;
            long nm = 1000 * 60;
            // long ns = 1000;
            // 获得两个时间的毫秒时间差异
            long diff = endDate.getTime() - nowDate.getTime();
            // 计算差多少天
            long day = diff / nd;
            // 计算差多少小时
            long hour = diff % nd / nh;
            // 计算差多少分钟
            long min = diff % nd % nh / nm;
            // 计算差多少秒//输出结果
            // long sec = diff % nd % nh % nm / ns;
            Double d = day*24+hour+(double)min/60;

            return (double)Math.round(d*10)/10;
        } catch (ParseException e) {
            throw new CustomException(CommonCode.FAIL);
        }

    }



    public static boolean isNormal(String startX,String endX,List<Map<String, Object>> originList){
        if (!isEmpty(startX) && !isEmpty(endX)) {
            startX = formatDate(startX);
            endX   = formatDate(endX);
            if (originList != null && originList.size() > 0) {
                List<String> startList = new ArrayList<String>();
                List<String> endList = new ArrayList<String>();
                for (int i = 0; i < originList.size(); i++) {
                    startList.add(formatDate((String) originList.get(i).get("start")));
                    endList.add(formatDate((String) originList.get(i).get("end")));
                }
                String minStart = startList.get(0);
                String maxEnd   = endList.get(endList.size()-1);
                if (endX.compareTo(minStart) < 0) {
                    return true;
                }
                if (startX.compareTo(maxEnd) > 0) {
                    return true;
                }
                int minStartIndex = 0;
                if (startX.compareTo(minStart) > 0) {
                    if (startX.compareTo(endList.get(minStartIndex)) > 0) {// 即startX>A1
                        for (int i = minStartIndex; i < startList.size(); i++) {
                            if (startX.compareTo(startList.get(i)) < 0) {
                                if (endX.compareTo(startList.get(i)) < 0) {
                                    return true;
                                }
                            } else {
                                minStartIndex += 1;
                                continue;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean isEmpty(String object) {
        if (object == null) {
            object = "";
        }
        return object.length() == 0;
    }
    public static String formatDate(String date) {
        if (date != null) {
            date = date.replace("/", "").replace(":", "").replace(" ", "");
        }else {
            date = "";
        }
        return date;
    }

    public static void main(String[] args) {
        List<Map<String, Object>> originList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("start", "2019/08/01 00:00:00");
        map1.put("end", "2019/12/31 24:00:00");
        originList.add(map1);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("start", "2020/02/14 00:00:00");
        map2.put("end", "2020/02/29 24:00:00");
        originList.add(map2);

        String startX = "2019/03/18 00:00:00";
        String endX = "2019/07/30 24:00:00";
        System.err.println("最小值之前[" + startX + "," + endX + "]:" + isNormal(startX, endX, originList));
        String startX2 = "2020/03/01 00:00:00";
        String endX2 = "2020/03/15 00:00:00";
        System.err.println("最大值之后[" + startX2 + "," + endX2 + "]:" + isNormal(startX2, endX2, originList));
        String startX3 = "2020/01/01 00:00:00";
        String endX3 = "2020/01/31 24:00:00";
        System.err.println("中间段[" + startX3 + "," + endX3 + "]:" + isNormal(startX3, endX3, originList));
        String startX4 = "2020/01/01 00:00:00";
        String endX4 = "2020/03/15 24:00:00";
        System.err.println("交叉段[" + startX4 + "," + endX4 + "]:" + isNormal(startX4, endX4, originList));


    }
}
