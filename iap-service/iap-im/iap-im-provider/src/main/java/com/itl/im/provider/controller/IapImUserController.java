package com.itl.im.provider.controller;

import com.itl.iap.attachment.client.service.OrganizationService;
import com.itl.iap.attachment.client.service.UserService;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * IM用户Controller(联系人列表、联系人详情)
 *
 * @author tanq
 * @date 2020-09-25
 * @since jdk1.8
 */
@Api("IM-查询用户控制层")
@RestController
@RequestMapping("/api/queryOrgPositionUser")
public class IapImUserController {

    @Resource
    private OrganizationService organizationService;

    @Resource
    private UserService userService;

    /**
     * 功能描述：联系人列表
     *
     * @param userOrPositOrOrganName
     * @return
     */
    @GetMapping(value = "/userById")
    @ApiOperation(value = "查询联系人列表", notes = "查询联系人列表")
    public ResponseData<List<Map>> queryOrgPositionUserTreeList(@RequestParam(value = "userOrPositOrOrganName", required = false) String userOrPositOrOrganName) {
        return organizationService.queryOrgPositionUserTreeList(userOrPositOrOrganName);
    }


    /**
     * 功能描述：根据用户名称精确查询用户信息
     *
     * @return
     */
    @PostMapping(value = "/preciseQueryUserInformation/{username}")
    @ApiOperation(value = "根据用户名称精确查询用户信息", notes = "根据用户名称精确查询用户信息")
    public ResponseData<IapSysUserTDto> preciseQueryUserInformation(@PathVariable("username") String username) {
        return userService.preciseQueryUserInformation(username);
    }

    /**
     * 模糊查询用户
     *
     * @return
     */
    @GetMapping(value = "/queryByUserName")
    @ApiOperation(value = "模糊查询用户", notes = "模糊查询用户")
    public ResponseData<List<IapSysUserTDto>> queryUserInfoByUserName(@RequestParam(value = "username", required = false) String username) {
        return userService.queryUserInfoByUserName(username);
    }
}
