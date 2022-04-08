package com.itl.iap.common.util;

/**
 * 随机guid生成
 *
 * @author Linjl
 * @date 2020-06-12
 */
public class UUID {

    /**
     * 随机生成uuid
     *
     * @return String uuid
     */
    public static String uuid32() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}
