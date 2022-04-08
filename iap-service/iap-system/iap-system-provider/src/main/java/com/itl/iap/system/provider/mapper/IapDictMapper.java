package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.dto.IapDictTDto;
import com.itl.iap.system.api.entity.IapDictT;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典表主表Mapper
 *
 * @author 李骐光
 * @date 2020-06-16
 * @since jdk1.8
 */
public interface IapDictMapper extends BaseMapper<IapDictT> {

    /**
     * 通过字典编号和名称查询
     *
     * @param iapDictTDto 字典表对象
     * @return 字典集合
     */
    List<IapDictTDto> queryDictCodeOrName(@Param("iapDictTDto") IapDictTDto iapDictTDto);

    /**
     * 分页查询
     *
     * @param iapDictTDto 字典表对象
     * @return 字典分页对象
     */
    IPage<IapDictTDto> query(Page page, @Param("iapDictTDto") IapDictTDto iapDictTDto);
    IPage<IapDictTDto> queryByState(Page page, @Param("iapDictTDto") IapDictTDto iapDictTDto);
}
