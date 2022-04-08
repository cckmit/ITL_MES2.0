package com.itl.iap.auth.shiro.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.config.Constants;
import com.itl.iap.auth.entity.AuthClient;
import com.itl.iap.auth.service.IAuthClientService;
import com.itl.iap.auth.shiro.jwt.JWTToken;
import com.itl.iap.common.base.dto.UserTDto;
import com.itl.iap.common.base.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 客户端Realm
 *
 * @author 汤俊
 * @date 2020-7-6
 * @since jdk1.8
 */
@Slf4j
public class JWTClientRealm extends AuthorizingRealm implements CustomeRealm {

    @Autowired
    private IAuthClientService authClientService;

    @Override
    public String getGrantType() {
        return GrantType.CLIENT_CREDENTIALS.toString();
    }

    /**
     * 必须重写此方法，不然Shiro会报错
     *
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 访问控制。客户端默认角色为client,只能访问角色控制为client的函数
     *
     * @param principalCollection
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("--->>> JWT REALM doGetAuthorizationInfo");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        JWTToken token = (JWTToken) principalCollection.getPrimaryPrincipal();
        if (token == null) {
            log.error("授权失败，用户信息为空！！！");
            return null;
        }
        try {
            //客户端无角色与权限
            Set<String> roleList = new HashSet<>();
            roleList.add("client");
            Set<String> permissionList = new HashSet<>();
            simpleAuthorizationInfo.setRoles(roleList);
            simpleAuthorizationInfo.setStringPermissions(permissionList);
        } catch (Exception e) {
            log.error("授权失败，请检查系统内部错误!!!", e);
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 校验authenticationToken
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("--->>> JWT JWTClientRealm doGetAuthenticationInfo");
        // 校验token
        JWTToken jwtToken = (JWTToken) authenticationToken;
        if (ObjectUtils.isEmpty(jwtToken)) {
            throw new AuthenticationException("json web token is null");
        }
        AuthClient client = authClientService.getOne(new QueryWrapper<AuthClient>().lambda().eq(AuthClient::getClientId, jwtToken.getClientId()));
        UserTDto username = JWTUtil.getUsername(jwtToken.getToken());
        log.info("----------------->>>>>");
        String token = JWTUtil.sign(username.getUserName(), client.getClientId(), client.getClientSecret());
        JWTToken buildToken = JWTToken.build(token, client.getClientId(), client.getClientId(), Constants.EXPIRES_IN, GrantType.CLIENT_CREDENTIALS.toString(), client.getClientId());
        return new SimpleAuthenticationInfo(buildToken, client.getClientId(), getName());
    }
}
