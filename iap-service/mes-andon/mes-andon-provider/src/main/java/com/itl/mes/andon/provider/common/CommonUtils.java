package com.itl.mes.andon.provider.common;

import com.itl.mes.andon.provider.exception.CustomException;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @auth liuchenghao
 * @date 2021/1/8
 */
public class CommonUtils {

    /**
     * 设置时间格式
     */
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 本地文件（图片、excel等）转换成Base64字符串
     *
     * @param imgPath
     */
    public static String convertFileToBase64(String imgPath) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组进行Base64编码，得到Base64编码的字符串
        BASE64Encoder encoder = new BASE64Encoder();
        String base64Str = encoder.encode(data).replace("\n","");
        return base64Str;
    }

    /**
     * 将图片的base64文件转化成字符串集合,因可能有多张图片
     */

    public static List<String> convertImgToStringList(String imgs){
        List<String> imgStrs = new ArrayList<>();
        String[] paths = imgs.split(",");
        for (String s : paths) {
            String fileBase64 = convertFileToBase64(s);
            imgStrs.add(fileBase64);
        }
       return imgStrs;
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

    public static String getAfterTime(String time, Double n) {
        try {
            Date date = sdf.parse(time);
            Double num = n*60.0;
            long l = Math.round(num);
            long Time=(date.getTime()/1000)+60*l;
            date.setTime(Time*1000);

            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
