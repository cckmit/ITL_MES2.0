package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.OperationOrderDTO;
import com.itl.mes.core.api.dto.SfcDto;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.entity.OperationOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工序表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-05-28
 */

public interface OperationMapper extends BaseMapper<Operation> {

    /**
     * 获取当前版本工序，返回字段包含：OPERATION：工序，OPERATION_DESC：工序描述
     *
     * @param site 工厂
     * @return List<Map<String,Object>>
     */
    List<Map<String, Object>> selectCurrentVersionOperations(@Param("site") String site);

    Integer updateOperationVersionType(@Param("site") String site, @Param("operation") String operation);

    List<Operation> selectTop(@Param("site") String site);

    IPage<OperationOrder> selectAllOrderOperation(Page page, @Param("sfcDto") SfcDto sfcDto);

    String selectShopOrderByOperationOrder(@Param("operationOrder") String operationOrder);

    /**
     * 判断该工序是否需要派工（通过自定义数据字段SEND_TO_DEVICE的值来判断，yes代表需要，no或者空代表不需要）
     * @param operationOrderBo 工序Bo
     */
    String isSendToDeviceByOperationBo(@Param("operationOrderBo") String operationOrderBo);

    //查询需要派工的工序
    IPage<Operation> queryCanDispatchOperation(Page page,@Param("operationOrderDTO")OperationOrderDTO operationOrderDTO);

    String selectWorkShopByUserName(@Param("userName") String userName);

    String isCanAutomaticReportWork(@Param("operationBo") String operationBo);

    List<String> listAllWorkStationByOperationBo(@Param("operationBo") String operationBo);
}