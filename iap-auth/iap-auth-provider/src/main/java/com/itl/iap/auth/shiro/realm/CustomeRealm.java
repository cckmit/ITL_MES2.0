package com.itl.iap.auth.shiro.realm;

import org.apache.oltu.oauth2.common.message.types.GrantType;

/**
 * 接口
 */

/**
 * 自定义的Realm
 *
 * @author 汤俊
 * @date 2020-10-22
 * @since jdk1.8
 */
public interface CustomeRealm {

    /**
     * 获取授权类型
     *
     * @return String
     */
    public String getGrantType();

}
