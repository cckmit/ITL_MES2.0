package com.itl.iap.common.util;

/**
 * 随机工具类
 *
 * @author Lizi
 * @date:2019/10/16 10:10
 */
public class RandomNumber {
    /**
     * 生成账号或者密码
     *
     * @param length 8:账户 6:密码
     * @return int
     */
    public static int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }
}
