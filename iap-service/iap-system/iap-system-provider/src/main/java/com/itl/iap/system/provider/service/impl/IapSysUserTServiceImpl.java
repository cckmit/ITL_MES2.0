package com.itl.iap.system.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.dto.UserTDto;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.DtoUtils;
import com.itl.iap.common.util.PassWordUtil;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.dto.IapSysAuthTDto;
import com.itl.iap.system.api.dto.IapSysResourceTDto;
import com.itl.iap.system.api.dto.IapSysRoleTDto;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.system.api.dto.req.IapRoleUserQueryDTO;
import com.itl.iap.system.api.dto.req.IapRoleUserSaveDTO;
import com.itl.iap.system.api.dto.req.IapSysUserProveSaveDTO;
import com.itl.iap.system.api.dto.req.IapSysUserStationSaveDTO;
import com.itl.iap.system.api.entity.*;
import com.itl.iap.system.api.service.IapSysPositionUserTService;
import com.itl.iap.system.api.service.IapSysUserTService;
import com.itl.iap.system.provider.mapper.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * 用户表实现类
 *
 * @author 谭强
 * @date 2020-06-19
 * @since jdk1.8
 */
@Service
public class IapSysUserTServiceImpl extends ServiceImpl<IapSysUserTMapper, IapSysUserT> implements IapSysUserTService {

    @Resource
    private IapSysUserTMapper iapSysUserMapper;
    @Autowired
    private IapSysPositionUserTService iapSysPositionUserService;
    @Resource
    private IapSysRoleTMapper iapSysRoleMapper;
    @Resource
    private IapSysUserRoleTMapper iapSysUserRoleMapper;
    @Resource
    private IapSysRoleAuthTMapper iapSysRoleAuthMapper;
    @Resource
    private IapSysAuthTMapper iapSysAuthMapper;
    @Resource
    private IapSysAuthResourceTMapper iapSysAuthResourceMapper;
    @Resource
    private IapSysResourceTMapper iapSysResourceMapper;
    @Resource
    private IapSysUserProveTMapper iapSysUserProveTMapper;
    @Resource
    private IapSysUserStationTMapper iapSysUserStationTMapper;
    @Resource
    private StationMapper stationMapper;
    @Autowired
    private UserUtil userUtil;


    /**
     * 分页查询
     *
     * @param iapSysUserDto 用户实例
     * @return IPage<IapSysUserTDto>
     */
    @Override
    public IPage<IapSysUserTDto> pageQuery(IapSysUserTDto iapSysUserDto) {
        if (null == iapSysUserDto.getPage()) {
            iapSysUserDto.setPage(new Page(1, 10));
        }
//        long current = iapSysUserDto.getPage().getCurrent() - 1;
//        long size = iapSysUserDto.getPage().getSize();
        IPage<IapSysUserTDto> iapSysUserDtoPage = iapSysUserMapper.pageQuery(iapSysUserDto.getPage(),iapSysUserDto);
//        List<IapSysUserTDto> collect = iapSysUserDtoPage.stream().filter(distinctByKey(e -> e.getUserName()))
//                .skip(current * size).limit(size).collect(Collectors.toList());
//        long total;
//        long pages;
//        if (CollectionUtils.isEmpty(collect)) {
//            total = 0L;
//            pages = 0L;
//        } else {
//            total = iapSysUserDtoPage.size();
//            pages = total / size;
//            if (total % size != 0) {
//                pages++;
//            }
//        }
//        Page<IapSysUserTDto> iapSysUserTDtoPage = new Page<>(current, size);
//        iapSysUserTDtoPage.setTotal(total);
//        iapSysUserTDtoPage.setRecords(collect);
//        iapSysUserTDtoPage.setPages(pages);
        return iapSysUserDtoPage;
    }
    @Override
    public IPage<IapSysUserTDto> pageQueryByState(IapSysUserTDto iapSysUserDto) {
        if (null == iapSysUserDto.getPage()) {
            iapSysUserDto.setPage(new Page(1, 10));
        }
        long current = iapSysUserDto.getPage().getCurrent() - 1;
        long size = iapSysUserDto.getPage().getSize();
        List<IapSysUserTDto> iapSysUserDtoPage = iapSysUserMapper.pageQueryByState(iapSysUserDto);
        List<IapSysUserTDto> collect = iapSysUserDtoPage.stream().filter(distinctByKey(e -> e.getUserName()))
                .skip(current * size).limit(size).collect(Collectors.toList());
        long total;
        long pages;
        if (CollectionUtils.isEmpty(collect)) {
            total = 0L;
            pages = 0L;
        } else {
            total = iapSysUserDtoPage.size();
            pages = total / size;
            if (total % size != 0) {
                pages++;
            }
        }
        Page<IapSysUserTDto> iapSysUserTDtoPage = new Page<>(current, size);
        iapSysUserTDtoPage.setTotal(total);
        iapSysUserTDtoPage.setRecords(collect);
        iapSysUserTDtoPage.setPages(pages);
        return iapSysUserTDtoPage;
    }

    public <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * 新增用户
     *
     * @param iapSysUserT 用户实例
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean installOneUser(IapSysUserT iapSysUserT) {
        return iapSysUserMapper.insert(iapSysUserT) != 0 ? true : false;
    }

    /**
     * 修改用户
     *
     * @param iapSysUserT 用户实例
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateOneUserById(IapSysUserT iapSysUserT) {
        return iapSysUserMapper.updateById(iapSysUserT) != 0 ? true : false;
    }

    /**
     * 根据用户id集合删除用户
     *
     * @param ids 用户id列表
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeUserByIds(List<String> ids) {
        List<String> list = iapSysUserRoleMapper.selectList(new QueryWrapper<IapSysUserRoleT>()
                .in("user_id", ids))
                .stream().map(IapSysUserRoleT::getId).collect(Collectors.toList());
        removeUserRole(list);
        return iapSysUserMapper.deleteBatchIds(ids) != 0 ? true : false;
    }

    /**
     * 根据单个Id删除
     *
     * @param ids 用户id
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeUserByOneId(String ids) {
        List<String> list = iapSysUserRoleMapper.selectList(new QueryWrapper<IapSysUserRoleT>()
                .eq("user_id", ids))
                .stream().map(IapSysUserRoleT::getId).collect(Collectors.toList());
        removeUserRole(list);
        return iapSysUserMapper.deleteById(ids) != 0 ? true : false;
    }

    private void removeUserRole(List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            iapSysUserRoleMapper.deleteBatchIds(ids);
        }
    }

    /**
     * 获取每页员工的用户
     *
     * @param iapSysUserDto 用户实例
     * @return IPage<IapSysUserTDto>
     */
    @Override
    public IPage<IapSysUserTDto> getUserForEmp(IapSysUserTDto iapSysUserDto) {
        if (null == iapSysUserDto.getPage()) {
            iapSysUserDto.setPage(new Page(0, 10));
        }
        return iapSysUserMapper.getUserForEmp(iapSysUserDto.getPage(), iapSysUserDto);
    }

    /**
     * 通过聊天群ID查找群成员
     *
     * @param groupId 群id
     * @return List<IapSysUserTDto>
     */
    @Override
    public List<IapSysUserTDto> getUserByGroupId(String groupId) {
        return iapSysUserMapper.getUserByGroupId(groupId);
    }

    /**
     * 查询所有角色
     *
     * @return List<IapSysRoleTDto>
     */
    @Override
    public List<IapSysRoleTDto> queryAllRole() {
        return iapSysUserMapper.queryAllRole();
    }

    /**
     * 修改用户的岗位
     *
     * @param iapSysPositionUserT 用户岗位实例
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean savePositionIdAndUserId(List<IapSysPositionUserT> iapSysPositionUserT, String userId) {
        boolean userBoolean = false;
        userBoolean = iapSysPositionUserService.remove(new QueryWrapper<IapSysPositionUserT>()
                .eq("user_id", userId));
        if (!iapSysPositionUserT.isEmpty()) {
            userBoolean = iapSysPositionUserService.saveBatch(iapSysPositionUserT);
        }
        return userBoolean;
    }


    /**
     * 根据用户名查询个人信息
     *
     * @param userName 用户姓名
     * @return IapSysUserTDto
     */
    @Override
    public IapSysUserTDto queryByUserName(String userName) {
        List<String> sourceIds = new ArrayList<>();
        List<String> roleIds = new ArrayList<>();
        List<String> authIds = new ArrayList<>();
        //获取当前用户
        IapSysUserT iapSysUserT = iapSysUserMapper.selectOne(new QueryWrapper<IapSysUserT>().lambda().eq(IapSysUserT::getUserName, userName));
        if (iapSysUserT == null) {
            return null;
        }
        IapSysUserTDto userDto = DtoUtils.convertObj(iapSysUserT, IapSysUserTDto.class);
        //获取用户与角色关联关系
        List<IapSysUserRoleT> userRoles = iapSysUserRoleMapper.selectList(new QueryWrapper<IapSysUserRoleT>().lambda().eq(IapSysUserRoleT::getUserId, userDto.getId()));
        if (userRoles.isEmpty()) {
            return userDto;
        }
        //查询用户所有的角色
        for (IapSysUserRoleT userRole : userRoles) {
            roleIds.add(userRole.getRoleId());
        }
        List<IapSysRoleTDto> iapSysRoleDtos = DtoUtils.convertList(IapSysRoleTDto.class, iapSysRoleMapper.selectBatchIds(roleIds));
        //组装ROLE对象
        for (IapSysRoleTDto roleDto : iapSysRoleDtos) {
            List<IapSysRoleAuthT> roleAuths = iapSysRoleAuthMapper.selectList(new QueryWrapper<IapSysRoleAuthT>().lambda().eq(IapSysRoleAuthT::getRoleId, roleDto.getId()));
            for (IapSysRoleAuthT roleAuth : roleAuths) {
                authIds.add(roleAuth.getAuthId());
            }
            //组装权限对象
            if (authIds.size() != 0 && authIds != null) {
                List<IapSysAuthTDto> authDtos = DtoUtils.convertList(IapSysAuthTDto.class, iapSysAuthMapper.selectBatchIds(authIds));
                for (IapSysAuthTDto authDto : authDtos) {
                    //组装资源对象
                    List<IapSysAuthResourceT> authResources = iapSysAuthResourceMapper.selectList(new QueryWrapper<IapSysAuthResourceT>().lambda().eq(IapSysAuthResourceT::getAuthId, authDto.getId()));
                    for (IapSysAuthResourceT authResource : authResources) {
                        sourceIds.add(authResource.getResourceId());
                    }
                    if (sourceIds.size() > 0) {
                        authDto.setResources(DtoUtils.convertList(IapSysResourceTDto.class, iapSysResourceMapper.selectBatchIds(sourceIds)));
                    }
                }
                roleDto.setAuths(authDtos);
            }
        }
        userDto.setIapSysRoleTDtoList(iapSysRoleDtos);
        return userDto;
    }

    /**
     * 通过用户 id 查询用户信息
     *
     * @return IapSysUserTDto
     */
    @Override
    public IapSysUserTDto queryUserById() {
        UserTDto user = userUtil.getUser();
        IapSysUserTDto iapSysUserDto = DtoUtils.convertObj(user, IapSysUserTDto.class);
        IapSysUserTDto userDto = iapSysUserMapper.queryUserById(iapSysUserDto);
        return userDto;
    }

    /**
     * 通过用户 id 查询用户信息
     *
     * @return IapSysUserT
     */
    @Override
    public IapSysUserT queryUserId(String userId) {
        return iapSysUserMapper.selectById(userId);
    }

    /**
     * 根据用户id用户密码
     *
     * @param iapSysUserDto 用户实例
     * @return Integer
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateUserPwdById(IapSysUserTDto iapSysUserDto) {
        Date date = new Date();
        IapSysUserT iapSysUserT = DtoUtils.convertObj(iapSysUserDto, IapSysUserT.class);
        if (iapSysUserT.getUserPsw() != null) {
            iapSysUserT.setUserPsw(PassWordUtil.encrypt(iapSysUserT.getUserPsw(), iapSysUserT.getUserName()));
            iapSysUserT.setPwdUpdateTime(date);
        }
        iapSysUserT.setLastUpdateDate(date);
        return iapSysUserMapper.updateById(iapSysUserT);

    }

    /**
     * 方法功能描述：根据用户名称模糊查询用户信息
     *
     * @param username 用户姓名
     * @return List<IapSysUserTDto>
     */
    @Override
    public List<IapSysUserTDto> queryUserInfoByUserName(String username) {
        return iapSysUserMapper.queryUserInfoByUserName(username);
    }

    /**
     * 根据用户名称精确查询用户信息
     *
     * @param username 用户姓名
     * @return IapSysUserTDto
     */
    @Override
    public IapSysUserTDto preciseQueryUserInformation(String username) {
        return iapSysUserMapper.queryUserById(new IapSysUserTDto().setUserName(username));
    }

    /**
     * 查询拥有某个角色的用户
     *
     * @param iapRoleUserQueryDTO
     * @return
     */
    @Override
    public IPage<IapSysUserT> getRoleUserList(IapRoleUserQueryDTO iapRoleUserQueryDTO) {

        if (ObjectUtil.isEmpty(iapRoleUserQueryDTO.getPage())) {
            iapRoleUserQueryDTO.setPage(new Page(0, 10));
        }
        return iapSysUserMapper.getRoleUserList(iapRoleUserQueryDTO.getPage(), iapRoleUserQueryDTO);
    }

    @Override
    public IPage<IapSysUserT> getNotRoleUserList(IapRoleUserQueryDTO iapRoleUserQueryDTO) {
        if (ObjectUtil.isEmpty(iapRoleUserQueryDTO.getPage())) {
            iapRoleUserQueryDTO.setPage(new Page(0, 10));
        }
        return iapSysUserMapper.getNotRoleUserList(iapRoleUserQueryDTO.getPage(), iapRoleUserQueryDTO);
    }

    @Override
    @Transactional
    public void saveRoleUsers(IapRoleUserSaveDTO iapRoleUserSaveDTO) {


        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("role_id", iapRoleUserSaveDTO.getRoleId());
        iapSysUserRoleMapper.deleteByMap(columnMap);
        iapRoleUserSaveDTO.getUserIds().forEach(userId -> {
            IapSysUserRoleT iapSysUserRoleT = new IapSysUserRoleT();
            iapSysUserRoleT.setId(UUID.uuid32()).setCreateDate(new Date());
            iapSysUserRoleT.setUserId(userId);
            iapSysUserRoleT.setRoleId(iapRoleUserSaveDTO.getRoleId());
            iapSysUserRoleMapper.insert(iapSysUserRoleT);
        });
    }

    @Override
    public void saveUserProves(IapSysUserProveSaveDTO iapSysUserProveSaveDTO) {

        QueryWrapper<IapSysUserProveT> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", iapSysUserProveSaveDTO.getUserId());
        iapSysUserProveTMapper.delete(queryWrapper);
        iapSysUserProveSaveDTO.getProveIds().forEach(proveId -> {
            IapSysUserProveT iapSysUserProveT = new IapSysUserProveT();
            iapSysUserProveT.setProveId(proveId);
            iapSysUserProveT.setUserId(iapSysUserProveSaveDTO.getUserId());
            iapSysUserProveTMapper.insert(iapSysUserProveT);
        });
    }

    @Override
    public void saveUserStations(IapSysUserStationSaveDTO iapSysUserStationSaveDTO) {
        QueryWrapper<IapSysUserStationT> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", iapSysUserStationSaveDTO.getUserId());
        iapSysUserStationTMapper.delete(queryWrapper);
        iapSysUserStationSaveDTO.getStationBos().forEach(stationBo -> {

            IapSysUserStationT iapSysUserStationT = new IapSysUserStationT();
            iapSysUserStationT.setSite(UserUtils.getSite());
            iapSysUserStationT.setUserId(iapSysUserStationSaveDTO.getUserId());
            iapSysUserStationT.setStationBo(stationBo);
            iapSysUserStationTMapper.insert(iapSysUserStationT);
        });
        if (CollectionUtils.isNotEmpty(iapSysUserStationSaveDTO.getStationBos())){
            String productLineBo = stationMapper.getProductLineBoByStation(iapSysUserStationSaveDTO.getStationBos().get(0));
            if (StringUtils.isNotBlank(productLineBo)){
                IapSysUserT iapSysUserT = new IapSysUserT();
                iapSysUserT.setId(iapSysUserStationSaveDTO.getUserId());
                iapSysUserT.setProductLineBo(productLineBo);
                iapSysUserMapper.updateById(iapSysUserT);
            }
        }
    }

}
