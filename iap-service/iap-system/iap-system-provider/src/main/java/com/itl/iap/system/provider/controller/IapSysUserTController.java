package com.itl.iap.system.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.attachment.api.dto.IapUploadFileDto;
import com.itl.iap.attachment.client.service.IapImUploadFileService;
import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.DtoUtils;
import com.itl.iap.common.util.PassWordUtil;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.system.api.dto.req.IapRoleUserQueryDTO;
import com.itl.iap.system.api.dto.req.IapRoleUserSaveDTO;
import com.itl.iap.system.api.dto.req.IapSysUserProveSaveDTO;
import com.itl.iap.system.api.dto.req.IapSysUserStationSaveDTO;
import com.itl.iap.system.api.entity.IapSysPositionUserT;
import com.itl.iap.system.api.entity.IapSysUserRoleT;
import com.itl.iap.system.api.entity.IapSysUserT;
import com.itl.iap.system.api.service.IapSysUserTService;
import com.itl.iap.system.provider.service.impl.IapSysUserRoleTServiceImpl;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户表Controller
 *
 * @author 谭强
 * @date 2020-06-19
 * @since jdk1.8
 */
@Api("System-用户管理控制器")
@RestController
@RequestMapping("/iapSysUserT")
public class IapSysUserTController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IapSysUserTService iapSysUserService;
    @Autowired
    private IapSysUserRoleTServiceImpl iapSysUserRoleService;
    @Autowired
    private IapImUploadFileService iapImUploadFileService;
    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping("/add")
    @ApiOperation(value = "新增用户表记录", notes = "新增用户表记录")
    public ResponseData add(@RequestBody IapSysUserT iapSysUserT) {
        logger.info("IapSysUserTDto add Record...");
        // 添加盐值（默认为用户名）
        iapSysUserT.setSalt(iapSysUserT.getUserName());
        // 加密密码
        iapSysUserT.setUserPsw(PassWordUtil.encrypt(iapSysUserT.getUserPsw(), iapSysUserT.getUserName()));
        if (StrUtil.isNotEmpty(iapSysUserT.getUserCardNumber())){
            IapSysUserT sysUser = iapSysUserService.getOne(new QueryWrapper<IapSysUserT>().eq("USER_CARD_NUMBER", iapSysUserT.getUserCardNumber()));
            if (sysUser!=null && !sysUser.getUserName().equals(iapSysUserT.getUserName())){
                return ResponseData.error("500","该卡号已被其他用户绑定");
            }
        }
        return ResponseData.success(iapSysUserService.installOneUser(iapSysUserT));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "根据用户ID删除用户表记录", notes = "根据用户ID删除用户表记录")
    public ResponseData delete(@RequestBody IapSysUserT iapSysUserT) {
        logger.info("IapSysUserTDto delete Record...");
        // 删除时查询中间表，并删除它
        return ResponseData.success(iapSysUserService.removeUserByOneId(iapSysUserT.getId()));
    }

    @PostMapping("/deleteList")
    @ApiOperation(value = "根据用户ID删除批量删除用户", notes = "根据用户ID删除批量删除用户")
    public ResponseData deleteList(@RequestBody List<String> userIds) {
        logger.info("IapSysUserTDto delete Record...");
        return ResponseData.success(iapSysUserService.removeUserByIds(userIds));

    }

    @PostMapping("/update")
    @ApiOperation(value = "根据用户ID修改用户表记录", notes = "根据用户ID修改用户表记录")
    public ResponseData update(@RequestBody IapSysUserT iapSysUserT) {
        logger.info("IapSysUserTDto updateRecord...");
        // 防止错误修改密码
        iapSysUserT.setUserPsw(null);
        if (StrUtil.isNotEmpty(iapSysUserT.getUserCardNumber())){
            IapSysUserT sysUser = iapSysUserService.getOne(new QueryWrapper<IapSysUserT>().eq("USER_CARD_NUMBER", iapSysUserT.getUserCardNumber()));
            if (sysUser!=null && !sysUser.getUserName().equals(iapSysUserT.getUserName())){
                return ResponseData.error("500","该卡号已被其他用户绑定");
            }
        }
        return ResponseData.success(iapSysUserService.updateOneUserById(iapSysUserT));
    }

    @PostMapping("/query")
    @ApiOperation(value = "分页查询用户表信息", notes = "分页查询用户表信息")
    public ResponseData queryRecord(@RequestBody IapSysUserTDto iapSysUserDto) {
        logger.info("IapSysUserTDto queryRecord...");
        return ResponseData.success(iapSysUserService.pageQuery(iapSysUserDto));
    }

    @PostMapping("/queryByState")
    @ApiOperation(value = "分页查询用户表信息ByState", notes = "分页查询用户表信息ByState")
    public ResponseData queryRecordByState(@RequestBody IapSysUserTDto iapSysUserDto) {
        logger.info("IapSysUserTDto queryRecordByState...");
        return ResponseData.success(iapSysUserService.pageQueryByState(iapSysUserDto));
    }

    @GetMapping("/queryByName/{userName}")
    @ApiOperation(value = "根据用户名查询用户信息", notes = "根据用户名查询用户信息")
    public ResponseData<IapSysUserTDto> queryByName(@PathVariable("userName") String userName) throws CommonException {
        logger.info("IapSysUserTDto queryByName...");
//        IapSysUserT user = iapSysUserTService.getOne(new QueryWrapper<IapSysUserT>().lambda().eq(IapSysUserT::getUserName, userName));
        IapSysUserTDto userDto = iapSysUserService.queryByUserName(userName);
        return ResponseData.success(userDto);
    }

    @GetMapping("/queryAllRole")
    @ApiOperation(value = "查询所有角色--角色授权", notes = "查询所有角色--角色授权")
    public ResponseData queryAllRole() {
        logger.info("IapSysUserTDto queryRecord...");
        return ResponseData.success(iapSysUserService.queryAllRole());
    }

    @PostMapping("/addRole/{userId}")
    @ApiOperation(value = "给用户新增角色记录", notes = "给用户新增角色记录")
    public ResponseData addRole(@RequestBody List<IapSysUserRoleT> iapSysUserRoleT, @PathVariable("userId") String userId) {
        logger.info("IapSysUserRoleTDto add Record...");
        Integer integer = iapSysUserRoleService.deleteRole(userId);
        if (!iapSysUserRoleT.isEmpty()) {
            iapSysUserRoleT.forEach(x -> {
                x.setId(UUID.uuid32());
            });
            return ResponseData.success(iapSysUserRoleService.saveBatch(iapSysUserRoleT));
        } else {
            return integer != 0 ? ResponseData.success(true) : ResponseData.success(false);
        }
    }

    @PostMapping("/addPosition/{userId}")
    @ApiOperation(value = "给用户新增岗位记录", notes = "给用户新增岗位记录")
    public ResponseData addPosition(@RequestBody List<IapSysPositionUserT> iapSysPositionUserTs, @PathVariable("userId") String userId) {
        logger.info("IapSysUserRoleTDto add Record...");
        if (!iapSysPositionUserTs.isEmpty()) {
            iapSysPositionUserTs.forEach(x -> {
                x.setId(UUID.uuid32());
            });
        }
        return ResponseData.success(iapSysUserService.savePositionIdAndUserId(iapSysPositionUserTs, userId));
    }

    @PostMapping("/getUserForEmp")
    @ApiOperation(value = "新增角色记录", notes = "新增角色记录")
    public ResponseData getUserForEmp(@RequestBody IapSysUserTDto iapSysUserDto) {
        logger.info("IapSysUserRoleTDto add Record...");
        return ResponseData.success(iapSysUserService.getUserForEmp(iapSysUserDto));
    }

    @PostMapping("/getUserInformationByUsername")
    @ApiOperation(value = "通过用户名查询用户信息", notes = "通过用户名查询用户信息")
    public ResponseData<IapSysUserTDto> getUserInformationByUsername(@RequestBody IapSysUserTDto iapSysUserDto) {

        return ResponseData.success(iapSysUserService.queryUserById());
    }

    @PostMapping("/updateUserPwdByUserId")
    @ApiOperation(value = "根据用户id用户密码", notes = "根据用户id用户密码")
    public ResponseData<Integer> updateUserPwdById(@RequestBody IapSysUserTDto iapSysUserDto) {
        return ResponseData.success(iapSysUserService.updateUserPwdById(iapSysUserDto));
    }

    @XxlJob("testPost")
    public ReturnT<String> execute(String param) {
        logger.info("======================》调度来了");
        return new ReturnT<>("成功");
    }

    @PostMapping("/userList")
    @ApiOperation(value = "查询所有用户", notes = "查询所有用户")
    public ResponseData<List<IapSysUserTDto>> getUserList() {
        List<IapSysUserT> list = iapSysUserService.list(new QueryWrapper<IapSysUserT>()
                .select("id", "user_name AS userName", "user_mobile AS userMobile", "real_name AS realName", "email", "gender", "birthday", "nick_name AS nickName", "avatar"));
        return ResponseData.success(DtoUtils.convertList(IapSysUserTDto.class, list));
    }

    @PostMapping("/getUserInfByName/{username}")
    @ApiOperation(value = "根据用户名查询用户", notes = "根据用户名查询用户")
    public ResponseData<IapSysUserTDto> getUserInfByName(@PathVariable String username) {
        IapSysUserT user = iapSysUserService.getOne(new QueryWrapper<IapSysUserT>()
                .eq("user_name", username)
                .select("id", "user_name AS userName", "user_mobile AS userMobile", "real_name AS realName", "email", "gender", "birthday", "nick_name AS nickName", "avatar"));
        return ResponseData.success(DtoUtils.convertObj(user, IapSysUserTDto.class));
    }

    @PostMapping("/getUserByGroupId/{groupId}")
    @ApiOperation(value = "通过聊天群ID查找群成员", notes = "通过聊天群ID查找群成员")
    ResponseData<List<IapSysUserTDto>> getUserByGroupId(@PathVariable("groupId") String groupId) {
        return ResponseData.success(iapSysUserService.getUserByGroupId(groupId));
    }

    @GetMapping("/queryUserInfoByUserName")
    @ApiOperation(value = "根据用户名称模糊查询用户信息", notes = "根据用户名称模糊查询用户信息")
    public ResponseData<List<IapSysUserTDto>> queryUserInfoByUserName(@RequestParam(value = "username", required = false) String username) {
        List<IapSysUserTDto> userInfoList = iapSysUserService.queryUserInfoByUserName(username);
        return ResponseData.success(userInfoList);
    }

    @PostMapping("/preciseQueryUserInformation/{username}")
    @ApiOperation(value = "根据用户名称精确查询用户信息", notes = "根据用户名称精确查询用户信息")
    public ResponseData<IapSysUserTDto> preciseQueryUserInformation(@PathVariable("username") String username) {
        IapSysUserTDto iapSysUserDto = iapSysUserService.preciseQueryUserInformation(username);
        return ResponseData.success(iapSysUserDto);
    }

    @PostMapping(value = "/updateAvatar")
    @ApiOperation(value = "用户头像修改", notes = "用户头像修改")
    public ResponseData<IapUploadFileDto> updateAvatar(@RequestParam(value = "userId", required = false) String userId, @RequestPart("file") MultipartFile file) {
        if (StringUtils.isEmpty(userId)) {
            userId = "";
        }
        IapUploadFileDto fileData = iapImUploadFileService.uploadFile(userId, file).getData();
        if (fileData != null && StringUtils.isNotEmpty(userId)) {
            IapSysUserT iapSysUserT = iapSysUserService.queryUserId(userId);
            iapSysUserT.setAvatar(fileData.getFileUrl());
            iapSysUserService.updateOneUserById(iapSysUserT);
            redisTemplate.delete("allGroupWithGroupMemberMap");
            return ResponseData.success(fileData);
        } else {
            return ResponseData.success(fileData);
        }
    }


    @PostMapping("/getRoleUserList")
    @ApiOperation(value = "查询拥有某个角色的用户", notes = "查询拥有某个角色的用户")
    public ResponseData getRoleUserList(@RequestBody IapRoleUserQueryDTO iapRoleUserQueryDTO) {

        return ResponseData.success(iapSysUserService.getRoleUserList(iapRoleUserQueryDTO));
    }


    @PostMapping("/getNotRoleUserList")
    @ApiOperation(value = "查询未拥有某个角色的用户", notes = "查询未拥有某个角色的用户")
    public ResponseData getNotRoleUserList(@RequestBody IapRoleUserQueryDTO iapRoleUserQueryDTO) {

        return ResponseData.success(iapSysUserService.getNotRoleUserList(iapRoleUserQueryDTO));
    }



    @ApiOperation(value = "将角色赋予用户", notes = "将角色赋予用户", httpMethod = "PUT")
    @PutMapping(value = "/saveRoleUsers")
    public ResponseData saveRoleUsers(@RequestBody IapRoleUserSaveDTO iapRoleUserSaveDTO) {
        iapSysUserService.saveRoleUsers(iapRoleUserSaveDTO);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "为用户分配证明", notes = "为用户分配证明", httpMethod = "PUT")
    @PutMapping(value = "/saveUserProves")
    public ResponseData saveRoleUsers(@RequestBody IapSysUserProveSaveDTO iapSysUserProveSaveDTO) {
        iapSysUserService.saveUserProves(iapSysUserProveSaveDTO);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "为用户分配工位", notes = "为用户分配工位", httpMethod = "PUT")
    @PutMapping(value = "/saveUserStations")
    public ResponseData saveUserStations(@RequestBody IapSysUserStationSaveDTO iapSysUserStationSaveDTO) {
        iapSysUserService.saveUserStations(iapSysUserStationSaveDTO);
        return ResponseData.success(true);
    }
}
