/**
 * Copyright (C):  Evergrande Group
 * FileName: RoleService
 * Author:   huangjianming
 * Date:     2020-07-03 16:29
 * Description:
 */
package com.itl.iap.attachment.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.attachment.client.config.FallBackConfig;
import com.itl.iap.attachment.client.service.impl.RoleServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 角色服务远程调用
 *
 * @author 黄建明
 * @date 2020-7-03
 * @since jdk 1.8
 */
@FeignClient(value = "iap-system-provider", fallback = RoleServiceImpl.class, configuration = FallBackConfig.class)
public interface RoleService {

    @GetMapping("/iapSysRoleT/queryAllUserListByRoleId/{roleId}")
    @ApiOperation(value = "查询角色的所有用户", notes = "通过角色id查询所有用户列表")
    ResponseData<List<IapSysUserTDto>> queryAllUserListByRoleId(@PathVariable("roleId") String roleId);


}
