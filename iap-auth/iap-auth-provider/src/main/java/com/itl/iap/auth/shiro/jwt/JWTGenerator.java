package com.itl.iap.auth.shiro.jwt;

import com.itl.iap.common.base.utils.JWTUtil;
import lombok.Data;
import org.apache.oltu.oauth2.as.issuer.ValueGenerator;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

/**
 * JWT生成工具
 *
 * @author 汤俊
 * @date 2020-6-27 11:25
 * @since jdk1.8
 */
@Data
public class JWTGenerator implements ValueGenerator {

    /**
     * 用户名
     */
    private String username;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 登录盐值
     */
    private String salt;

    /**
     * 生成JWT token
     *
     * @return (String)token
     * @throws OAuthSystemException
     */
    @Override
    public String generateValue() throws OAuthSystemException {
        System.out.println("--->>> generateValue()");
        return JWTUtil.sign(username, clientId, salt);
    }

    /**
     * 生成JWT token
     *
     * @param param
     * @return (String)token
     * @throws OAuthSystemException
     */
    @Override
    public String generateValue(String param) throws OAuthSystemException {
        System.out.println("--->>> generateValuegenerateValue(String param)");
        return JWTUtil.sign(username, clientId, salt);
    }

}
