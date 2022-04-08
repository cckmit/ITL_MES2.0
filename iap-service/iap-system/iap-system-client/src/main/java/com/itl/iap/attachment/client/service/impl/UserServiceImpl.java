package com.itl.iap.attachment.client.service.impl;

import com.itl.iap.attachment.client.service.UserService;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * User fallback
 *
 * @author 汤俊
 * @date 2020-6-30 10:51
 * @since jdk 1.8
 */
@Slf4j
@Component
public class UserServiceImpl implements UserService {

    @Override
    public ResponseData<IapSysUserTDto> queryByName(String userName) {
        log.error("sorry,user feign fallback userName:{}", userName);
        return null;
    }

    @Override
    public ResponseData<List<IapSysUserTDto>> getUserList() {
        log.error("sorry,user feign fallback ");
        return null;
    }

    @Override
    public ResponseData<IapSysUserTDto> getUserInfByName(String username) {
        log.error("sorry,user feign fallback userName:{}", username);
        return null;
    }

    public ResponseData<IapSysUserTDto> queryUserDetailsByUserId(String id) {
        log.error("sorry,user feign fallback userId:{}", id);
        return null;
    }
    @Override
    public ResponseData<List<IapSysUserTDto>> getUserByGroupId(String groupId) {
        log.error("sorry,user feign fallback ");
        return null;
    }

    @Override
    public ResponseData<List<IapSysUserTDto>> queryUserInfoByUserName(String username) {
        log.error("sorry,user feign fallback username:{}",username);
        return null;
    }

    @Override
    public ResponseData<IapSysUserTDto> preciseQueryUserInformation(String username) {
        log.error("sorry,user feign fallback username:{}",username);
        return null;
    }


}
