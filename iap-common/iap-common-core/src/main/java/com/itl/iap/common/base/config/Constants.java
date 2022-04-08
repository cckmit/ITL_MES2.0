package com.itl.iap.common.base.config;

/**
 * 系统定制的常量
 *
 * @author 汤俊
 * @date 2020-10-22
 * @since jdk1.8
 */
public class Constants {

    public static String RESOURCE_SERVER_NAME = "oauth2-server";
    public static final String INVALID_CLIENT_DESCRIPTION = "客户端验证失败，错误的client_id/client_secret。";
    public static final String INVALID_CLIENT_REDIRECTURI = "客户端验证失败，错误的redirect。";
    public static final String INVALID_CLIENT_GRANT_TYPES = "客户端验证失败，错误的grantType。";
    public static final String INVALID_CODE_DESCRIPTION = "错误的授权码";
    public static final String INVALID_USER_DESCRIPTION = "用户名或密码错误";
    public static final String INVALID_TOKEN_NOTFOUND = "TOKEN不存在";
    public static final String INVALID_GRAND_TYPE_NOTFOUND = "授权模式不存在";

    /**
     * token的key
     */
    public static final String ACCESS_TOKEN = "Access-Token";

    public static final String PREFIX_USER_TOKEN = "PREFIX_USER_TOKEN_";

    public static final long EXPIRES_IN = 24 * 60 * 60 * 1000;

    public static final String ZERO = "0";


}
