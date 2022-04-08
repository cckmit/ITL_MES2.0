package com.itl.iap.auth.shiro.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.itl.iap.common.base.utils.JWTUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.shiro.authc.AuthenticationToken;

import java.util.Date;

/**
 * JWTToken
 *
 * @author 汤俊
 * @date 2020-6-23 10:50
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
public class JWTToken implements AuthenticationToken {

    private static final long serialVersionUID = -8451637096112402805L;

    /**
     * 登陆模式
     */
    private String grantType;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    /**
     * 登录ip
     */
    private String host;
    /**
     * 登录用户名称
     */
    private String username;
    /**
     * 登录盐值
     */
    private String salt;
    /**
     * 登录token
     */
    private String token;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 多长时间过期，默认一小时
     */
    private long expireSecond;
    /**
     * 过期日期
     */
    private Date expireDate;


    private String principal;

    private String credentials;

    /**
     * 获取 principal
     *
     * @return token
     */
    @Override
    public Object getPrincipal() {
        return token;
    }

    /**
     * 获取 credentials
     *
     * @return token
     */
    @Override
    public Object getCredentials() {
        return token;
    }

    /**
     * 生成 JWTtoken
     * @param token accessToken
     * @param username 用户名
     * @param salt 盐值
     * @param expireSecond 过期时间
     * @param grantType 授权方式
     * @param clientId 客户端ID
     * @return JWTToken
     */
    public static JWTToken build(String token, String username, String salt, long expireSecond, String grantType, String clientId) {
        DecodedJWT decodedJwt = JWTUtil.getJwtInfo(token);
        Date createDate = decodedJwt.getIssuedAt();
        Date expireDate = decodedJwt.getExpiresAt();
        if (StringUtils.isEmpty(grantType)) {
            grantType = GrantType.AUTHORIZATION_CODE.toString();
        }
        return new JWTToken()
                .setUsername(username)
                .setToken(token)
                .setHost("127.0.0.1")
                .setSalt(salt)
                .setCreateDate(createDate)
                .setExpireSecond(expireSecond)
                .setExpireDate(expireDate)
                .setGrantType(grantType)
                .setClientId(clientId);
    }

}
