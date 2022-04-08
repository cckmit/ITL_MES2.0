package com.itl.iap.auth.shiro.realm;

import com.itl.iap.auth.shiro.jwt.JWTToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 当配置了多个Realm时，我们通常使用的认证器是shiro自带的org.apache.shiro.authc.pam.ModularRealmAuthenticator，其中决定使用的Realm的是doAuthenticate()方法
 * 自定义Authenticator,通过grantType进行判断
 *
 * @author 汤俊
 * @date 2020-7-6
 * @since jdk1.8
 */
@Slf4j
public class JWTModularRealmAuthenticator extends ModularRealmAuthenticator {

    /**
     * 认证操作
     *
     * @param authenticationToken
     * @return AuthenticationInfo
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        log.info("JWTModularRealmAuthenticator:method doAuthenticate() execute ");
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();
        // 强制转换回自定义的CustomizedToken
//        UserToken userToken = (UserToken) authenticationToken;
        JWTToken jwtToken = (JWTToken) authenticationToken;
        // 登录类型
        String grantType = jwtToken.getGrantType();
        // 所有Realm
        Collection<Realm> realms = getRealms();
        // 登录类型对应的所有Realm
        List<Realm> typeRealms = new ArrayList<>();
        for (Realm realm : realms) {
            CustomeRealm customeRealm = (CustomeRealm) realm;
            if (customeRealm.getGrantType().contains(grantType)) {
                typeRealms.add(realm);
            }
        }
        // 判断是单Realm还是多Realm
        if (typeRealms.size() == 1) {
            log.info("doSingleRealmAuthentication() execute ");
            return doSingleRealmAuthentication(typeRealms.get(0), jwtToken);
        } else {
            log.info("doMultiRealmAuthentication() execute ");
            return doMultiRealmAuthentication(typeRealms, jwtToken);
        }
    }
}
