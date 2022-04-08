package com.itl.iap.common.base.utils;


import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import cn.hutool.core.date.DateUtil;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 通用工具类
 */
public class CommonUtil {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 比较时间是否相等，不相等则抛出异常
     *
     * @param frontModifyDate 前台传送的时间
     * @param modifyDate      从数据库查询的时间
     * @throws CommonException
     */
    public static void compareDateSame(Date frontModifyDate, Date modifyDate) throws CommonException {

        if (frontModifyDate == null) {
            throw new CommonException("前台传送的时间为空!", CommonExceptionDefinition.TIME_VERIFY_EXCEPTION);
        }
        if (modifyDate != null) {
            if (frontModifyDate.getTime() != modifyDate.getTime()) {
                throw new CommonException("前台传送的时间和从数据库查询的时间不相等!", CommonExceptionDefinition.TIME_VERIFY_EXCEPTION);
            }
        }

    }

    //判断同年同周
    public static boolean isSameWeek(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            return isSameWeek(DateUtil.calendar(date1.getTime()), DateUtil.calendar(date2.getTime()));
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    //判断同年同周
    public static boolean isSameWeek(Calendar calA, Calendar calB) {
        if (calA != null && calB != null) {
            return calA.get(1) == calB.get(1) && calA.get(2) == calB.get(2) && calA.get(3) == calB.get(3);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    //判断同年同月
    public static boolean isSameMonth(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            return isSameMonth(DateUtil.calendar(date1.getTime()), DateUtil.calendar(date2.getTime()));
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    //判断同年同月
    public static boolean isSameMonth(Calendar calA, Calendar calB) {
        if (calA != null && calB != null) {
            return calA.get(1) == calB.get(1) && calA.get(2) == calB.get(2);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    //判断同年
    public static boolean isSameYear(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            return isSameYear(DateUtil.calendar(date1.getTime()), DateUtil.calendar(date2.getTime()));
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    //判断同年
    public static boolean isSameYear(Calendar calA, Calendar calB) {
        if (calA != null && calB != null) {
            return calA.get(1) == calB.get(1);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    /**
     * 判断是否需要重置序号
     * N: 从不
     * A: 总是
     * D: 每天
     * W: 每周
     * M: 每月
     * Y: 每年
     * E: 每周 - 星期日
     * F: 每周 - 星期一
     * G: 每周 - 星期二
     * H: 每周 - 星期三
     * I: 每周 - 星期四
     * J; 每周 - 星期五
     * K: 每周 - 星期六
     *
     * @param reset
     * @param lastUpdateDT
     * @param currentDT
     * @return
     */
    public static boolean shouldResetOccur(String reset, Date lastUpdateDT, Date currentDT) {

        if (reset.equals("A")) {
            return true;
        } else if ((reset.equals("D")) && !DateUtil.isSameDay(lastUpdateDT, currentDT)) {
            return true;
        } else if ((reset.equals("W")) && !isSameWeek(lastUpdateDT, currentDT)) {
            return true;
        } else if ((reset.equals("M")) && !isSameMonth(lastUpdateDT, currentDT)) {
            return true;
        } else if ((reset.equals("Y")) && !isSameYear(lastUpdateDT, currentDT)) {
            return true;
        } else if ((reset.equals("E")) && (dayResetNeeded(lastUpdateDT, currentDT, "E"))) {
            return true;
        } else if ((reset.equals("F")) && (dayResetNeeded(lastUpdateDT, currentDT, "F"))) {
            return true;
        } else if ((reset.equals("G")) && (dayResetNeeded(lastUpdateDT, currentDT, "G"))) {
            return true;
        } else if ((reset.equals("H")) && (dayResetNeeded(lastUpdateDT, currentDT, "H"))) {
            return true;
        } else if ((reset.equals("I")) && (dayResetNeeded(lastUpdateDT, currentDT, "I"))) {
            return true;
        } else if ((reset.equals("J")) && (dayResetNeeded(lastUpdateDT, currentDT, "J"))) {
            return true;
        } else if ((reset.equals("K")) && (dayResetNeeded(lastUpdateDT, currentDT, "K"))) {
            return true;
        }
        return false;
    }

    /**
     * 是否需要按固定周天重置
     *
     * @param lastUpdateDT
     * @param currentDT
     * @param resetDay
     * @return
     */
    public static boolean dayResetNeeded(Date lastUpdateDT, Date currentDT, String resetDay) {
        Calendar currentDateTime = Calendar.getInstance();
        currentDateTime.setTime(currentDT);
        currentDateTime.set(currentDateTime.get(Calendar.YEAR), currentDateTime.get(Calendar.MONTH), currentDateTime.get(Calendar.DATE), 23, 59, 59);

        Calendar lastUPdateDateTime = Calendar.getInstance();
        lastUPdateDateTime.setTime(lastUpdateDT);

        long diffDays = (lastUPdateDateTime.getTimeInMillis() - currentDateTime.getTimeInMillis()) / (1000 * 3600 * 24);
        int daysBetween = (int) diffDays;

        String lastUpdateDate = new StringBuilder().append(lastUPdateDateTime.get(Calendar.YEAR)).append(lastUPdateDateTime.get(Calendar.MONTH)).append(lastUPdateDateTime.get(Calendar.DATE)).toString();
        String currentDate = new StringBuilder().append(currentDateTime.get(Calendar.YEAR)).append(currentDateTime.get(Calendar.MONTH)).append(currentDateTime.get(Calendar.DATE)).toString();
        if (lastUpdateDate.equals(currentDate)) {
            return false;
        }

        if (daysBetween < -6) {
            return true;
        }

        Calendar nextDay = Calendar.getInstance();
        nextDay.setTime(lastUpdateDT);

        int dayDiffCount = Math.abs(daysBetween);
        int resetDow = getDayNumberFromResetDay(resetDay);
        for (int i = 0; i < dayDiffCount; i++) {
            nextDay.add(Calendar.DATE, 1);
            int dayNumber = nextDay.get(Calendar.DAY_OF_WEEK);
            if (dayNumber == resetDow) {
                return true;
            }
        }

        return false;
    }

    public static int getDayNumberFromResetDay(String reset) {
        if (reset.equals("E")) {
            return 0;
        }
        if (reset.equals("F")) {
            return 1;
        }
        if (reset.equals("G")) {
            return 2;
        }
        if (reset.equals("H")) {
            return 3;
        }
        if (reset.equals("I")) {
            return 4;
        }
        if (reset.equals("J")) {
            return 5;
        }
        if (reset.equals("K")) {
            return 6;
        }
        return -1;
    }


    /**
     * 任意进制数的相互转换。基本原理是先将原来的数转换为10进制数，再转换为新的进制数
     *
     * @param number                原来的数
     * @param originalBase          原来的数的进制
     * @param newBase               需要转换成的进制
     * @param usingUppercaseLetters 是否返回字母全大写的字符串
     * @return 新的进制数（String类型）或“无法转换”的警告
     */
    public static String baseConverter(long number, int originalBase, int newBase, boolean usingUppercaseLetters) {

        String originalNumber = Long.toString(number);
        String newNumber = Long.toString(Long.valueOf(originalNumber, originalBase), newBase);
        return usingUppercaseLetters ? newNumber.toUpperCase() : newNumber.toLowerCase();

    }

    public static String hex10ToAny(long number, int newBase, boolean usingUppercaseLetters) {

        return baseConverter(number, 10, newBase, usingUppercaseLetters);
    }

    public static String hex10ToAny(long number, int newBase) {

        return hex10ToAny(number, newBase, true);
    }

    /**
     * 检查list中对象是否重复 重复则返回重复对象下标。不重复返回-1
     *
     * @param objectList
     * @throws
     */
    public static int checkForDuplicates(List objectList) {
        List<String> list = new ArrayList<String>();
        for (Object object : objectList) {
            list.add(object.toString());
        }
        int k = -1;
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = (i + 1); j < list.size(); j++) {
                if (list.get(i).equals(list.get(j))) {
                    return j;
                }
            }
        }
        return k;
    }

    /**
     * 将时间转换成yyyy-mm--dd hh24:mm:ss 格式字符串
     *
     * @param date
     * @return
     */
    public static String getStrDate(Date date) {
        String format = formatter.format(date);
        return format;
    }

}
