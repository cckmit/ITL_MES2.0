package com.itl.iap.auth.shiro.credentials;

import com.itl.iap.common.base.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

/**
 * JWT证书匹配
 *
 * @author 汤俊
 * @date 2020-6-27 9:28
 * @since jdk1.8
 */
@Slf4j
public class JWTCredentialsMatcher implements CredentialsMatcher {

    /**
     * 校验token是否正确
     *
     * @param authenticationToken
     * @param authenticationInfo
     * @return boolean
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        String token = authenticationToken.getCredentials().toString();
        String salt = authenticationInfo.getCredentials().toString();
        try {
            return JWTUtil.verify(token, salt);
        } catch (Exception e) {
            log.error("JWT Token CredentialsMatch Exception:" + e.getMessage(), e);
        }
        return false;
    }
}
