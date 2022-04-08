package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.entity.IapSysRoleT;
import com.itl.iap.system.api.entity.IapSysUserRoleT;
import com.itl.iap.system.api.entity.IapSysUserT;
import com.itl.mes.core.api.dto.StemDispatchDTO;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.OperationOrder;
import com.itl.mes.core.api.entity.StemDispatch;
import com.itl.mes.core.api.vo.ManualInlayVo;
import com.itl.mes.core.api.vo.StemDispatchListVo;
import com.itl.mes.core.api.vo.StepNcVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface StemDispatchMapper extends BaseMapper<StemDispatch> {
    IPage<StemDispatchListVo> queryStemDispatchList(Page page, @Param("stemDispatchDTO") StemDispatchDTO stemDispatchDTO);

    IPage<OperationOrder> queryNeedDispatchOrders(Page page, @Param("stemDispatchDTO") StemDispatchDTO stemDispatchDTO);

    /**
     * 根据用户组id查询下面所有的用户
     * @param stemDispatchDTO
     * @return
     */
    List<IapSysUserT> selectUserT(@Param("stemDispatchDTO") StemDispatchDTO stemDispatchDTO);

    StemDispatchListVo selectStemReportWork(@Param("id") String id);

    StepNcVo.Item getItemInfoByShopOrder(@Param("shopOrderBo") String shopOrderBo);

    List<ManualInlayVo> queryManualInlay(@Param("stemDispatchDTO") StemDispatchDTO stemDispatchDTO);

    List<StemDispatch> getSumQtyByYxUserAndItem(@Param("stemDispatchDTO") StemDispatchDTO stemDispatchDTO);

    /**
     * 查询所有摇线组的用户组ID
     * @return
     */
    List<IapSysRoleT> getYxGroupId();

    String selectUserTByUserId(@Param("userId") String userId);

    /**
     * 根据传入条件查询做了哪些物料
     * @param stemDispatchDTO
     * @return
     */
    List<String> selectItemByCondition(@Param("stemDispatchDTO") StemDispatchDTO stemDispatchDTO);
    /**
     * 根据传入条件查询有哪些人
     * @param stemDispatchDTO
     * @return
     */
    List<String> selectUserByCondition(@Param("stemDispatchDTO") StemDispatchDTO stemDispatchDTO);

    /**
     * 根据人员ID查询属于哪个用户组
     */
    List<String> getRoleByUserId(@Param("userId") String userId);

    IPage<StemDispatch> listAllCanCancelDispatchOrder(Page page,@Param("dispatchOrder") String dispatchOrder,@Param("itemDescKeyWordList") List<String> itemDescKeyWordList);

    /**
     * 查询该工序下所有需要派工的工步
     * @param operationBo
     * @return
     */
    List<String> listAllNeedDispatchOperationOrder(@Param("operationBo") String operationBo);

    String sumQtyByOperationOrderAndStep(@Param("operationOrder") String operationOrder,@Param("step") String step);

    /**
     * 查询所有不需要派工的工步
     * @return
     */
    List<String> listUnNeedDispatchStep();

    String getInsideNoByUserName(@Param("userName") String userName);

    List<String> getOperationOrderByDispatchCode(@Param("dispatchCode") String dispatchCode);

    List<String> selectOperationOrderByStepDispatchCode(@Param("stepDispatchCode") String stepDispatchCode);

    List<IapSysUserT> selectUserWithOutInList(@Param("userIds") Set<String> userIds, @Param("roleId") String roleId);
}
