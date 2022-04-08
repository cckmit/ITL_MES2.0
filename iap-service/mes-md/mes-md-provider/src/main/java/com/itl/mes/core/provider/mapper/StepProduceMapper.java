package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.entity.IapSysUserStationT;
import com.itl.iap.system.api.entity.IapSysUserT;
import com.itl.mes.core.api.dto.StepProduceDTO;
import com.itl.mes.core.api.entity.StepProduce;
import com.itl.mes.core.api.vo.StepProduceVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StepProduceMapper extends BaseMapper<StepProduce> {
    //查询可以进站的工序工单(线圈车间)
    IPage<StepProduceVo> getCanInputStationOpOrder(Page page, @Param("stepProduceDTO") StepProduceDTO stepProduceDTO);
    //查询可以出站的工序工单
    IPage<StepProduceVo> getCanOutputStationOpOrder(Page page,@Param("stepProduceDTO") StepProduceDTO stepProduceDTO);
    //查询所有配置这个工位的用户
    List<IapSysUserStationT> selectUserIdByStationBo(@Param("stationBo") String stationBo);
    //根据用户ID查询用户详细信息
    IapSysUserT selectUserInfoById(@Param("userId") String userId,@Param("userBo") String userBo);
    //根据工序工单查询进站数量
    String selectInputQty(@Param("operationOrder") String operationOrder,@Param("operationBo") String operationBo);
}
