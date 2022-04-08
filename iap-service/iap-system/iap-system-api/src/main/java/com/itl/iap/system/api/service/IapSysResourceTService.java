
package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.system.api.dto.IapSysResourceTDto;
import com.itl.iap.system.api.entity.IapSysResourceT;


import java.util.List;
import java.util.Set;

/**
 * 菜单Controller
 *
 * @author 谭强
 * @date 2020-06-20
 * @since jdk1.8
 */
public interface IapSysResourceTService extends IService<IapSysResourceT> {

    /**
     * 查询全部菜单
     *
     * @return List<IapSysResourceTDto>
     */
    List<IapSysResourceTDto> queryList();

    /**
     * 删除树
     *
     * @param iapSysResourceDto 菜单对象
     * @return Integer
     */
    Integer deleteTree(List<IapSysResourceTDto> iapSysResourceDto);

    /**
     * 获取动态菜单
     *
     * @return List<IapSysResourceTDto>
     */
    List<IapSysResourceTDto> getAllMenu();
}