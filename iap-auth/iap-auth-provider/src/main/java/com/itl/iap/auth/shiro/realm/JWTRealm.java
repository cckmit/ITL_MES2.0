package com.itl.iap.auth.shiro.realm;

import com.alibaba.fastjson.JSON;
import com.itl.iap.common.base.config.Constants;
import com.itl.iap.auth.shiro.jwt.JWTToken;
import com.itl.iap.common.base.utils.JWTUtil;
import com.itl.iap.auth.util.RedisUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapSysAuthTDto;
import com.itl.iap.system.api.dto.IapSysRoleTDto;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.attachment.client.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;
import java.util.Set;

/**
 * 自定义JWTRealm
 *
 * @author 汤俊
 * @date 2020-6-23 11:11
 * @since jdk1.8
 */
@Slf4j
public class JWTRealm extends AuthorizingRealm implements CustomeRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取授权模式
     *
     * @return String
     */
    @Override
    public String getGrantType() {
        return GrantType.AUTHORIZATION_CODE.toString() + "," + GrantType.PASSWORD.toString();
    }

    /**
     * 必须重写此方法，不然Shiro会报错
     *
     * @param token
     * @return boolean
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 访问控制。比如某个用户是否具有某个操作的使用权限
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
            ResponseData userRes = userService.queryByName(token.getUsername());
            IapSysUserTDto userDto = (IapSysUserTDto) userRes.getData();
            List<IapSysRoleTDto> roles = userDto.getIapSysRoleTDtoList();
            Set<String> roleList = new HashSet<>();
            Set<String> permissionList = new HashSet<>();
            for (IapSysRoleTDto role : roles) {
                roleList.add(role.getRoleCode());
                for (IapSysAuthTDto auth : role.getAuths()) {
                    permissionList.add(auth.getAuthName());
                }
            }
            simpleAuthorizationInfo.setRoles(roleList);
            simpleAuthorizationInfo.setStringPermissions(permissionList);
//            return simpleAuthorizationInfo;
        } catch (Exception e) {
            log.error("授权失败，请检查系统内部错误!!!", e);
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 用户身份识别(登录")
     *
     * @param authenticationToken
     * @return AuthenticationInfo
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("--->>> JWT REALM doGetAuthenticationInfo");
        // 校验token
        JWTToken jwtToken = (JWTToken) authenticationToken;
        if (ObjectUtils.isEmpty(jwtToken)) {
            throw new AuthenticationException("json web token is null");
        }
        String salt = jwtToken.getSalt();
        if (StringUtils.isBlank(salt)) {
            throw new AuthenticationException("salt is null");
        }

        String userName = jwtToken.getUsername();
//        IapSysUserT user = userService.findUserByName(userName);
        ResponseData userRes = userService.queryByName(userName);
        if (userRes == null) {
            throw new AuthenticationException("user is not found");
        }

        IapSysUserTDto user = (IapSysUserTDto) userRes.getData();
        redisUtil.set(user.getUserName() + "-user", JSON.toJSON(user), Constants.EXPIRES_IN);
        log.info("----------------->>>>>");
        String token = JWTUtil.sign(userName, jwtToken.getClientId(), userName);
        //TODO SALT字段需要替换
        JWTToken buildToken = JWTToken.build(token, userName, salt, Constants.EXPIRES_IN, GrantType.AUTHORIZATION_CODE.toString(), null);
        return new SimpleAuthenticationInfo(buildToken, salt, getName());
    }

}
