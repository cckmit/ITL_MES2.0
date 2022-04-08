package com.itl.iap.system.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.util.CodeUtils;
import com.itl.iap.common.util.DtoUtils;
import com.itl.iap.common.util.PassWordUtil;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.dto.IapEmployeeTDto;
import com.itl.iap.system.api.entity.IapEmployeeT;
import com.itl.iap.system.api.entity.IapPositionEmployeeT;
import com.itl.iap.system.api.entity.IapSysPositionUserT;
import com.itl.iap.system.api.entity.IapSysUserT;
import com.itl.iap.system.provider.mapper.IapPositionEmployeeTMapper;
import com.itl.iap.system.provider.mapper.IapSysEmployeeTMapper;
import com.itl.iap.system.api.service.IapEmployeeTService;
import com.itl.iap.system.provider.mapper.IapSysPositionUserTMapper;
import com.itl.iap.system.provider.mapper.IapSysUserTMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 员工管理实现类
 *
 * @author 马家伦
 * @date 2020-06-17
 * @since jdk1.8
 */
@Service
@Slf4j
public class IapEmployeeTServiceImpl extends ServiceImpl<IapSysEmployeeTMapper, IapEmployeeT> implements IapEmployeeTService {

    @Resource
    IapSysEmployeeTMapper iapSysEmployeeMapper;

    @Resource
    IapPositionEmployeeTMapper iapPositionEmployeeMapper;
    @Resource
    IapSysPositionUserTMapper iapSysPositionUserMapper;
    @Resource
    private IapSysUserTMapper iapSysUserMapper;
    @Resource
    private UserUtil userUtil;

    public static final String DEFAULT_PASSWORD = "000000";

    /**
     * 通过条件模糊查询员工列表
     *
     * @param iapEmployeeDto 员工对象
     * @return IPage<IapEmployeeTDto>
     */
    @Override
    public IPage<IapEmployeeTDto> query(IapEmployeeTDto iapEmployeeDto) {
        if (iapEmployeeDto.getPage() == null) {
            iapEmployeeDto.setPage(new Page(0, 10));
        }
        try {
            return iapSysEmployeeMapper.queryList(iapEmployeeDto.getPage(), iapEmployeeDto);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加新员工
     *
     * @param iapEmployeeDto 员工对象
     * @return String
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String add(IapEmployeeTDto iapEmployeeDto) {
        IapEmployeeT iapEmployeeT = new IapEmployeeT();
        BeanUtils.copyProperties(iapEmployeeDto, iapEmployeeT);
        try {
            Date date = new Date();
            //初始数据
            iapEmployeeT.setId(UUID.uuid32()).setCreateDate(date).setLastUpdateDate(date);
            this.setCode(iapEmployeeT, date, 0);
            // 插入员工表
            iapSysEmployeeMapper.insert(iapEmployeeT);
            // 插入岗位-员工中间表
            IapPositionEmployeeT iapPositionEmployeeT = new IapPositionEmployeeT();
            iapPositionEmployeeT.setId(UUID.uuid32());
            iapPositionEmployeeT.setPositionId(iapEmployeeDto.getPositionId());
            iapPositionEmployeeT.setEmployeeId(iapEmployeeT.getId());
            iapSysEmployeeMapper.insertIntoIapSysPositionEmployeeT(iapPositionEmployeeT);
//            //测试发送短信
//            sendMessageUtils.sendMessage(iapPositionEmployeeT.getId(),
//                    "123", "13378691411", "79840981@qq.com",
//                    "", "237f7ed50a864294864e0469df659084", "admin", iapPositionEmployeeT);

            //选择开户功能后，自动创建系统用户信息
            /*if(IapEmployeeTDto.OPEN_ACCOUNT_1.equals(iapEmployeeDto.getOpenAccount())){
                IapSysUserT iapSysUser = new IapSysUserT();
                openAccount(iapEmployeeDto, iapSysUser);
                iapSysUserMapper.insert(iapSysUser);

                // 插入岗位-用户中间表
                IapSysPositionUserT iapSysPositionUser = new IapSysPositionUserT();
                iapSysPositionUser.setId(UUID.uuid32());
                iapSysPositionUser.setUserId(iapEmployeeDto.getUserId());
                iapSysPositionUser.setPositionId(iapEmployeeDto.getPositionId());
                iapSysPositionUserMapper.insert(iapSysPositionUser);
            }*/
            return iapEmployeeT.getId();
        } catch (RuntimeException | CommonException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void openAccount(IapEmployeeTDto iapEmployeeDto, IapSysUserT iapSysUser) {
        String userId = iapEmployeeDto.getUserId();
        Assert.notNull(userId, "用户ID数据缺失");
        iapSysUser.setId(UUID.uuid32());
        iapSysUser.setUserCode(iapEmployeeDto.getCode());
        iapSysUser.setUserName(userId);
        // 添加盐值（默认为用户名）
        iapSysUser.setSalt(userId);
        // 加密密码
        iapSysUser.setUserPsw(PassWordUtil.encrypt(DEFAULT_PASSWORD, userId));
        iapSysUser.setUserMobile(iapEmployeeDto.getContract());
        iapSysUser.setEmail(iapEmployeeDto.getEmail());
        iapSysUser.setRealName(iapEmployeeDto.getName());
        iapSysUser.setNickName(iapEmployeeDto.getName());
        iapSysUser.setRegisterDate(new Date());
        iapSysUser.setState(iapEmployeeDto.getStatus());
        //iapEmployeeDto 类型(0-经销商，1-内部) iapSysUser 户类型(0-内部用户, 1-外部用户)
        iapSysUser.setUserType(IapEmployeeTDto.TYPE_1.equals(iapEmployeeDto.getType()) ? IapSysUserT.TYPE_0 : IapSysUserT.TYPE_1);
        iapSysUser.setPwdUpdateTime(new Date());
    }

    /**
     * 更新员工信息
     *
     * @param iapEmployeeDto 员工对象
     * @return String
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String update(IapEmployeeTDto iapEmployeeDto) {
        IapEmployeeT iapEmployeeT = DtoUtils.convertObj(iapEmployeeDto,IapEmployeeT.class);
        // 修改员工表 iap_sys_employee_t
        iapSysEmployeeMapper.updateById(iapEmployeeT);
        // 判断是否修改岗位信息 iap_position_employee_t 表
        if (iapEmployeeDto.getPositionId() != null) {
            IapPositionEmployeeT iapPositionEmployeeInDb = iapPositionEmployeeMapper.selectOne(new QueryWrapper<IapPositionEmployeeT>().eq("employee_id", iapEmployeeT.getId()));
            if (iapPositionEmployeeInDb != null){
                if (iapPositionEmployeeInDb.getPositionId().equals(iapEmployeeDto.getPositionId())){
                    // 该员工已有岗位，且本次更新没有修改岗位
                    return iapEmployeeT.getId();
                }
                // 该员工已有岗位，且本次更新有修改岗位
                iapPositionEmployeeInDb.setPositionId(iapEmployeeDto.getPositionId());
                iapPositionEmployeeMapper.updateById(iapPositionEmployeeInDb);
            }else {
                // 该员工原来没有岗位，给该员工新增岗位
                IapPositionEmployeeT newIapPositionEmployee = new IapPositionEmployeeT();
                newIapPositionEmployee.setId(UUID.uuid32()).setEmployeeId(iapEmployeeDto.getId()).setPositionId(iapEmployeeDto.getPositionId());
                iapPositionEmployeeMapper.insert(newIapPositionEmployee);
            }
        }
        return iapEmployeeT.getId();
    }

    /**
     * 通过员工id删除
     *
     * @param iapEmployeeDto 员工对象
     * @return Boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deleteById(IapEmployeeTDto iapEmployeeDto) {
        if (iapEmployeeDto != null) {
            // 删除员工表
            iapSysEmployeeMapper.deleteById(iapEmployeeDto.getId());
            // 删除岗位员工中间表
             iapPositionEmployeeMapper.delete(new QueryWrapper<IapPositionEmployeeT>().eq("employee_id", iapEmployeeDto.getId()));
            return true;
        }
        return false;
    }

    /**
     * 通过传入的员工列表删除
     *
     * @param iapEmployeeDtoList 员工集合
     * @return Boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deleteByIapEmployeeDtoList(List<IapEmployeeTDto> iapEmployeeDtoList) {
        if (!iapEmployeeDtoList.isEmpty()) {
            iapEmployeeDtoList.forEach((IapEmployeeTDto iapEmployeeDto) -> this.deleteById(iapEmployeeDto));
            return true;
        }
        return false;
    }

    /**
     * 修改员工关联账号
     *
     * @param iapEmployeeDto 员工对象
     * @return Boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateUserId(IapEmployeeTDto iapEmployeeDto) {
        Date date = new Date();
        // 绑定了账号
        if (StringUtils.isNotEmpty(iapEmployeeDto.getUserId()) && iapEmployeeDto.getStatus() != null) {
            IapSysUserT iapSysUserT = iapSysUserMapper.selectById(iapEmployeeDto.getUserId());
            // 离职
            if (IapEmployeeTDto.STATUS_1.equals(iapEmployeeDto.getStatus())) {
                iapSysUserT.setState(Short.parseShort("1"));
            } else {
                iapSysUserT.setState(Short.parseShort("0"));
            }
            iapSysUserMapper.updateById(iapSysUserT);
        }

        Integer integer = iapSysEmployeeMapper.updateUserId(iapEmployeeDto);
        if (iapEmployeeDto.getPositionId() != null) {
            IapPositionEmployeeT iapPositionEmployeeTinDb = iapPositionEmployeeMapper.selectOne(new QueryWrapper<IapPositionEmployeeT>().eq("employee_id", iapEmployeeDto.getId()));
            // 如果员工存在岗位，则修改员工-岗位中间表。否则，新增到员工-岗位中间表
            if (iapPositionEmployeeTinDb != null){
                // 修改
                iapPositionEmployeeTinDb.setPositionId(iapEmployeeDto.getPositionId()).setLastUpdateDate(date);
                iapPositionEmployeeMapper.updateById(iapPositionEmployeeTinDb);
            }else {
                // 新增
                IapPositionEmployeeT iapPositionEmployeeT = new IapPositionEmployeeT();
                iapPositionEmployeeT.setId(UUID.uuid32());
                iapPositionEmployeeT.setEmployeeId(iapEmployeeDto.getId());
                iapPositionEmployeeT.setPositionId(iapEmployeeDto.getPositionId());
                iapPositionEmployeeMapper.insert(iapPositionEmployeeT);
            }

        }
        return integer != 0;
    }

    /**
     * 批量保存
     *
     * @param entityList 员工集合
     * @return boolean
     */
    @Override
    public boolean saveBatch(Collection<IapEmployeeT> entityList) {
        if (entityList != null && !entityList.isEmpty()){
            return iapSysEmployeeMapper.insertListFromExcel(entityList);
        }
        return false;
    }

    /**
     * 验证修改参数
     *
     * @param iapEmployeeT 员工对象
     */
    private void verifyUpdateParam(IapEmployeeT iapEmployeeT) {
        //编号存在，但不是当前ID
        if (!StringUtils.isEmpty(iapEmployeeT.getCode())) {
            if (iapSysEmployeeMapper.selectCount(new QueryWrapper<IapEmployeeT>().ne("id", iapEmployeeT.getId()).eq("code", iapEmployeeT.getCode())) != 0) {
                throw new RuntimeException();
            }
        }
    }

    /**
     * 设置编码
     *
     * @param obj  需要设置code属性的实体类
     * @param date 当前时间
     * @param num  默认0 大于 20跳出
     */
    private void setCode(IapEmployeeT obj, Date date, Integer num) throws CommonException {
        obj.setCode(CodeUtils.dateToCode("NX", date));
        if (num > CodeUtils.NUM) {
            log.error(CommonExceptionDefinition.getCurrentClassError() + "编码设置失败!");
            throw new CommonException(new CommonExceptionDefinition(500, "编码设置失败!"));
        }
        if (iapSysEmployeeMapper.selectCount(new QueryWrapper<IapEmployeeT>().ne("id", obj.getId()).eq("code", obj.getCode())) != 0) {
            num++;
            this.setCode(obj, date, num);
        }
    }
}
