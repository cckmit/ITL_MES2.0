package com.itl.iap.system.provider.service.impl;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.entity.IapSysRoleAuthT;
import com.itl.iap.system.api.dto.IapSysRoleAuthTDto;
import com.itl.iap.system.provider.mapper.IapSysRoleAuthTMapper;
import com.itl.iap.system.api.service.IapSysRoleAuthTService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * 角色权限中间表实现类
 *
 * @author 李虎
 * @date 2020-06-19
 * @since jdk1.8
 */
@Service
public class IapSysRoleAuthTServiceImpl extends ServiceImpl<IapSysRoleAuthTMapper, IapSysRoleAuthT> implements IapSysRoleAuthTService {

    @Resource
    private IapSysRoleAuthTMapper iapSysRoleAuthMapper;

    /**
     * 分页查询
     *
     * @param iapSysRoleAuthDto 角色权限实例
     * @return IPage<IapSysRoleAuthTDto>
     */
    @Override
    public IPage<IapSysRoleAuthTDto> pageQuery(IapSysRoleAuthTDto iapSysRoleAuthDto) {
        if (null == iapSysRoleAuthDto.getPage()) {
            iapSysRoleAuthDto.setPage(new Page(0, 10));
        }
        return iapSysRoleAuthMapper.pageQuery(iapSysRoleAuthDto.getPage(), iapSysRoleAuthDto);
    }

    /**
     * 分配权限，1 先批删 2 再批增
     *
     * @param iapSysRoleAuthT 角色权限实例
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchRemoveAndAdd(List<IapSysRoleAuthT> iapSysRoleAuthT) {
        if (iapSysRoleAuthT.size() > 0) {
            String roleId = iapSysRoleAuthT.get(0).getRoleId();
            if (StringUtil.isNotBlank(roleId)) {
                List<IapSysRoleAuthT> selectList = iapSysRoleAuthMapper.selectList(new QueryWrapper<IapSysRoleAuthT>().eq("role_id", roleId));
                if (selectList.size() > 0) {
                    // 1
                    iapSysRoleAuthMapper.delete(new QueryWrapper<IapSysRoleAuthT>().eq("role_id", roleId));
                }
            }
            // 2
            for (IapSysRoleAuthT sysRoleAuthT : iapSysRoleAuthT) {
                sysRoleAuthT.setId(UUID.uuid32());
            }
            boolean flag = iapSysRoleAuthMapper.insertBatch(iapSysRoleAuthT);
            if (flag) {
                return true;
            }
        }
        return false;
    }
}
