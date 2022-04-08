package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.system.api.dto.IapDictTDto;
import com.itl.iap.system.api.entity.IapDictT;

import java.util.List;

/**
 * 字典表主表Service
 *
 * @author 李骐光
 * @date 2020-06-16
 * @since jdk1.8
 */
public interface IapDictTService extends IService<IapDictT> {
    /**
     * 分页查询
     *
     * @param iapDictDto 字典表对象
     * @return 字典分页对象
     */
    IPage<IapDictTDto> query(IapDictTDto iapDictDto);
    IPage<IapDictTDto> queryByState(IapDictTDto iapDictDto);

    /**
     * 通过字典编号和名称查询
     *
     * @param iapDictDto 字典表对象
     * @return 字典集合
     */
    List<IapDictTDto> queryDictCodeOrName(IapDictTDto iapDictDto);

    /**
     * 新建字典
     *
     * @param iapDictDto 字典表对象
     * @return 字典表对象
     */
    IapDictTDto insertIapDictT(IapDictTDto iapDictDto) throws Exception;

    /**
     * 修改字典
     *
     * @param iapDictDto 字典表对象
     * @return 字典表id
     */
    String updateIapDictT(IapDictTDto iapDictDto);

    /**
     * 通过id批量删除字典
     *
     * @param ids 字典id集合
     * @return 删除成功与否
     */
    Boolean deleteByIds(List<String> ids);
}
