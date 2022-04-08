package com.itl.iap.common.util;

import com.itl.iap.common.util.group.SplicingListElementFunction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 获取code的工具类
 * @author: LiShuaiPeng
 * @datetime 2019/10/22 14:16
 * @since JDK 1.8
 */
public class CodeUtils {
    /**
     * 最大循环生成code次数
     */
    public static final Integer NUM = 20;
    private static final String STR_DATE = "yyyyMMdd";

    /**
     * 生成账号或者密码
     * @param num 账号或密码的长度
     * @return
     */
    public static String getCode(Integer num) {
        return String.valueOf(RandomNumber.buildRandom(num));
    }

    /**
     * 获取随机编码
     * @param prefix
     * @param date
     * @return
     */
    public static String dateToCode(String prefix, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(STR_DATE);
        return prefix + sdf.format(date) + RandomNumber.buildRandom(4);
    }

    /**
     * 生产随机数（字母 + 数字混合）
     * @param length
     * @return
     */
    public static String getRandomNickname(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
    public static String formatAndPrint(SplicingListElementFunction formatter, String delimiter, List list) {
        return formatter.format(delimiter, list);
    }
}
