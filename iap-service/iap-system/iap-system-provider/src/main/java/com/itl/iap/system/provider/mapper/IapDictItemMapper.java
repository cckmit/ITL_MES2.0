package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.system.api.dto.IapDictItemTDto;
import com.itl.iap.system.api.dto.IapDictTDto;
import com.itl.iap.system.api.entity.IapDictItemT;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典表详情表Controller
 *
 * @author 李骐光
 * @date 2020-06-16
 * @since jdk1.8
 */
public interface IapDictItemMapper extends BaseMapper<IapDictItemT> {
    /**
     * 通过字典编号和名称查询
     *
     * @param iapDictItemTDto 字典详情
     * @return 字典详情集合
     */
    List<IapDictItemTDto> queryDictCodeOrName(@Param("iapDictItemTDto") IapDictItemTDto iapDictItemTDto);

    /**
     * 通过IapDictId查询
     *
     * @param ids 字典主表id集合
     * @return 字典详情集合
     */
    List<IapDictItemTDto> selectByIapDictIdList(@Param("ids") List<String> ids);

    /**
     * 通过字典编号查询
     *
     * @param dictCode 字典编码
     * @return 字典详情集合
     */
    List<IapDictItemTDto> queryDictCode(@Param("dictCode") String dictCode);

}
