/**
 * Copyright (C):  Evergrande Group
 * FileName: RoleServiceImpl
 * Author:   huangjianming
 * Date:     2020-07-03 16:31
 * Description:
 */
package com.itl.iap.attachment.client.service.impl;

import com.itl.iap.attachment.client.service.RoleService;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User fallback
 *
 * @author 黄建明
 * @date 2020-7-03
 * @since jdk 1.8
 */
@Slf4j
@Component
public class RoleServiceImpl implements RoleService {


    @Override
    public ResponseData<List<IapSysUserTDto>> queryAllUserListByRoleId(String roleId) {
        log.error("sorry,role feign fallback roleId:{}", roleId);
        return null;
    }
}
