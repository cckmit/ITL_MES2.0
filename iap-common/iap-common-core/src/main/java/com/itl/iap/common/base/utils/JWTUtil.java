package com.itl.iap.common.base.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.itl.iap.common.base.config.Constants;
import com.itl.iap.common.base.dto.UserTDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * JWT 工具类
 *
 * @author 汤俊
 * @date 2020-6-23 10:51
 * @since jdk1.8
 */
@Slf4j
public class JWTUtil {

    /**
     * 过期时间 24小时
     */
    public static final String SHIRO_USER_NAME = "username";
    public static final String SHIRO_CLIENT_ID = "clientId";
    public static final String SHIRO_USER_SALT = "salt";
    public static final String SHIRO_ISSUER = "Issuer";
    public static final String SHIRO_SUBJECT = "long_token";


    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @param salt  盐值
     * @return 是否正确
     */
    public static boolean verify(String token, String salt) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(salt);
            JWTVerifier verifier = JWT.require(algorithm)
                    // 签发人
                    .withIssuer(SHIRO_ISSUER)
                    // 主题
                    .withSubject(SHIRO_SUBJECT)
                    // 签发的目标
//                    .withAudience(jwtProperties.getAudience())
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            if (jwt != null) {
                return true;
            }
        } catch (Exception e) {
            log.error("The token is invalid{}", e.getMessage());
        }
        return false;
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static UserTDto getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            UserTDto userTDto = new UserTDto();
            userTDto.setClientId(jwt.getClaim(SHIRO_CLIENT_ID).asString());
            userTDto.setUserName(jwt.getClaim(SHIRO_USER_NAME).asString());
            return userTDto;
        } catch (JWTDecodeException e) {
            log.error("error：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 解析token，获取token数据
     *
     * @param token
     * @return
     */
    public static DecodedJWT getJwtInfo(String token) {
        return JWT.decode(token);
    }

    /**
     * 生成TOKEN,24小时后过期
     *
     * @param username 用户名
     * @param salt     盐值
     * @return 加密的token
     */
    public static String sign(String username, String clientId, String salt) {
        Date expireDate = new Date(System.currentTimeMillis() + Constants.EXPIRES_IN);
        //加盐值
        Algorithm algorithm = Algorithm.HMAC256(salt);
        // 附带username信息
        return JWT.create()
                .withClaim(SHIRO_USER_NAME, username)
                .withClaim(SHIRO_CLIENT_ID, clientId)
                .withClaim(SHIRO_USER_SALT, salt)
                // jwt唯一id
                .withJWTId(uuid32())
                // 签发人
                .withIssuer(SHIRO_ISSUER)
                // 主题
                .withSubject(SHIRO_SUBJECT)
                // 签发的目标
//                .withAudience(jwtProperties.getAudience())
                // 签名时间
                .withIssuedAt(new Date())
                // token过期时间
                .withExpiresAt(expireDate)
                // 签名
                .sign(algorithm);
    }

    /**
     * 生成 uuid
     *
     * @return String
     */
    public static String uuid32() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }


}
