package com.itl.im.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.im.provider.core.annotation.TokenAccount;
import iap.im.api.dto.IapImGroupDto;
import iap.im.api.entity.IapImGroupMember;
import iap.im.api.service.IapImGroupMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 群成员Controller
 *
 * @author mjl
 * @date 2020-10-10
 * @since jdk1.8
 */
@Api("IM-群成员控制层")
@RestController
@RequestMapping("im/groupMember")
public class IapImGroupMemberController {

    @Resource
    private UserUtil userUtil;

    @Resource
    private IapImGroupMemberService iapImGroupMemberService;

    /**
     * 批量删除群成员
     *
     * @param groupId
     * @param accountList
     * @return
     */
    @PostMapping(value = "/removeBatch/{groupId}")
    @ApiOperation(value = "批量删除群成员", notes = "批量删除群成员")
    public ResponseData removeBatch(@PathVariable String groupId, @RequestBody List<String> accountList) {

        return ResponseData.success(iapImGroupMemberService.removeBatch(accountList, groupId));
    }

    /**
     * 通过群ID查询该群的群成员列表
     *
     * @param groupId
     * @return
     */
    @GetMapping(value = "/getGroupMemberList")
    @ApiOperation(value = "通过群ID查询该群的群成员列表", notes = "通过群ID查询该群的群成员列表")
    public ResponseData<IapImGroupDto> selectGroupMemberListByGroupId(@RequestParam("groupId") String groupId) {
        return ResponseData.success(iapImGroupMemberService.selectGroupMemberListByGroupId(groupId));
    }

    /**
     * 修改是否禁用群聊天
     *
     * @param iapImGroupMember
     * @return
     */
    @PostMapping("/muteUser")
    @ApiOperation(value = "修改是否禁用群聊天", notes = "修改是否禁用群聊天")
    public ResponseData updateMuteUser(@RequestBody List<IapImGroupMember> iapImGroupMember) {

        return ResponseData.success(iapImGroupMemberService.updateMuteUser(iapImGroupMember));
    }

    /**
     * 主动离开某个群
     *
     * @param iapImGroupMember
     * @return
     */
    @PostMapping("/leaveGroup")
    @ApiOperation(value = "主动离开某个群", notes = "主动离开某个群")
    public ResponseData leaveGroup(@RequestBody IapImGroupMember iapImGroupMember) {

        String selfAccount = userUtil.getUser().getUserName();
        if (StringUtils.isNoneBlank(selfAccount, iapImGroupMember.getGroupId())) {
            return ResponseData.success(iapImGroupMemberService.leaveGroup(userUtil.getUser().getUserName(), iapImGroupMember.getGroupId()));
        }
        return ResponseData.success("主动离开群失败，请检查参数");

    }

}
