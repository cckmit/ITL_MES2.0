package com.itl.iap.common.util;

import java.util.List;

/**
 * 用户ID ThreadLocal
 *
 * @author 汤俊
 * @date 2019-12-31 0:28
 * @since 1.0.0
 */
public class UserRoleThreadLocal {

    //用于存储用户ID
    private static final ThreadLocal<List<String>> LOCAL = new ThreadLocal<List<String>>();

    public static void set(List<String> roles) {
        LOCAL.set(roles);
    }

    public static List<String> get() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }

}
