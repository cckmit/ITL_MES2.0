package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.DispatchDTO;
import com.itl.mes.core.api.entity.Dispatch;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.vo.DispatchVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DispatchMapper extends BaseMapper<Dispatch> {
    /**
     * 查询所有的派工任务
     */
    IPage<Dispatch> queryAllDispatchTask(Page page,@Param("dispatchDTO") DispatchDTO dispatchDTO);

    /**
     * 确认派工查询列表
     * @param dispatchDTO
     * @return
     */
    List<Dispatch> okDispatchList(@Param("dispatchDTO") DispatchDTO dispatchDTO);

    IPage<DispatchVo> getAllCanPrintDispatch(Page page,@Param("dispatchDTO") DispatchDTO dispatchDTO);

    List<String> selectSfcByDispatchCode(@Param("dispatchDTO") DispatchDTO dispatchDTO);

    List<Item> selectBomDetailByBomBo(@Param("bomBo") String bomBo);

    String getDispatchTotalCountByOpAndOrder(@Param("operationBo") String operationBo,@Param("operationOrder") String operationOrder);
}
