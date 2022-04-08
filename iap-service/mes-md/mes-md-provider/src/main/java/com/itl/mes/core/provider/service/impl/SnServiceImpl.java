package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.CarrierHandleBO;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.bo.OperationHandleBO;
import com.itl.mes.core.api.bo.SnHandleBO;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.entity.Sn;
import com.itl.mes.core.api.service.ItemService;
import com.itl.mes.core.api.service.OperationService;
import com.itl.mes.core.api.service.SnService;
import com.itl.mes.core.provider.mapper.SnMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 条码信息表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-07-25
 */
@Service
@Transactional
public class SnServiceImpl extends ServiceImpl<SnMapper, Sn> implements SnService {


    @Autowired
    private SnMapper snMapper;

    //@Autowired
    //private CarrierDataService carrierDataService;

    @Autowired
    private ItemService itemService;

    //@Autowired
    //private UserPassLogService userPassLogService;

    @Autowired
    private OperationService operationService;


    @Override
    public List<Sn> selectList() {
        QueryWrapper<Sn> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, sn);
        return super.list(entityWrapper);
    }

    @Override
    public Sn getExitsSn(SnHandleBO snHandleBO) throws CommonException {
        Sn snEntity = snMapper.selectById(snHandleBO.getBo());
        if (snEntity == null) {
            throw new CommonException("未找到SN:" + snHandleBO.getSn() + "条码信息，请查找原因", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return snEntity;
    }


    //获取自定义数据 值
    @Override
    public String getSelfDefiningData(String site, String customDataType, String field, String customDataValBo) {
        String vals = snMapper.getSelfDefiningData(site,customDataType,field,customDataValBo);
        return vals;
    }

    @Override
    public IPage selectPageSN(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> mapList = snMapper.selectPageSN(page, params);
        page.setRecords(mapList);
        return page;
    }

    @Override
    public List<Map<String, Object>> selectPageShopOrderByShape(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
            params.put("stateBo","STATE:"+UserUtils.getSite()+",501");
        }
        return snMapper.selectPageShopOrderByShape(page, params);
    }


    //查询产品生产数据
    /*@Override
    public SnDataVo selectSnAndUserPassLog(String sn) throws BusinessException {
        Sn exitsSn = getExitsSn(new SnHandleBO(UserUtils.getSite(), sn));
        //查询载具信息
        EntityWrapper<CarrierData> carrierDataEntityWrapper = new EntityWrapper<>();
        carrierDataEntityWrapper.eq(CarrierData.SN, exitsSn.getSn())
                .eq(CarrierData.SITE, UserUtils.getSite());
        List<CarrierData> carrierData = carrierDataService.selectList(carrierDataEntityWrapper);
        SnDataVo snDataVo = new SnDataVo();
        snDataVo.setSn(exitsSn.getSn());
        snDataVo.setState(exitsSn.getState());
        if (carrierData.size() > 0){
            snDataVo.setCarrier(new CarrierHandleBO(carrierData.get(0).getCarrierBo()).getCarrier());
        }
        //查询物料信息
        Item exitsItem = itemService.getExitsItemByItemHandleBO(new ItemHandleBO(exitsSn.getItemBo()));

        //查询最近过站的工序 工单
        EntityWrapper<UserPassLog> userPassLogEntityWrapper = new EntityWrapper<>();
        userPassLogEntityWrapper.eq(UserPassLog.SN, exitsSn.getSn())
                .eq(UserPassLog.SITE, UserUtils.getSite()).orderBy(UserPassLog.CREATE_DATE, false);
        List<UserPassLog> userPassLogList = userPassLogService.selectList(userPassLogEntityWrapper);

        List<SnPassDataVo> snPassDataVos = new ArrayList<>();
        if (userPassLogList.size() > 0) {
            //根据时间排序 找出最近的
            *//*userPassLogList.sort(new Comparator<UserPassLog>() {
                @Override
                public int compare(UserPassLog o1, UserPassLog o2) {
                    return (int) (o2.getCreateDate().getTime() - o1.getCreateDate().getTime());
                }
            });*//*
            for (UserPassLog userPassLog : userPassLogList) {
                SnPassDataVo snPassDataVo = new SnPassDataVo();
                if (!StrUtil.isBlank(userPassLog.getOperationBo())) {
                    OperationHandleBO operationHandleBO = new OperationHandleBO(userPassLog.getOperationBo());
                    Operation operation = operationService.selectByOperation(operationHandleBO.getOperation(), operationHandleBO.getVersion());
                    snPassDataVo.setOperation(operation.getOperation());
                    snPassDataVo.setOperationDesc(operation.getOperationDesc());
                }
                snPassDataVo.setCreateDate(userPassLog.getCreateDate());
                snPassDataVo.setCreateUser(userPassLog.getCreateUser());
                snPassDataVos.add(snPassDataVo);
            }
        }
        if (userPassLogList.size() > 0) {
            UserPassLog userPassLog = userPassLogList.get(0);
            snDataVo.setItem(exitsItem.getItem());
            snDataVo.setItemDesc(exitsItem.getItemDesc());
            if (!StrUtil.isBlank(userPassLog.getOperationBo())) {
                OperationHandleBO operationHandleBO = new OperationHandleBO(userPassLog.getOperationBo());
                Operation operation = operationService.selectByOperation(operationHandleBO.getOperation(), operationHandleBO.getVersion());
                snDataVo.setOperation(operation.getOperation());
                snDataVo.setOperationDesc(operation.getOperationDesc());
            }
            snDataVo.setShopOrder(userPassLog.getShopOrder());
            snDataVo.setSnPassDataVos(snPassDataVos);
        }
        return snDataVo;
    }*/

    @Override
    public Sn  selectMaxSerial(String site, String complementCodeState, String ruleCode, String newDateSt) {
        return snMapper.selectMaxSerial(site, complementCodeState, ruleCode,newDateSt);
    }


    //修改工单排产数量
    @Override
    public void updateShopOrderSchedulQty( String shopOrderBO, int qty ) {
        snMapper.updateShopOrderSchedulQty( shopOrderBO,qty );
    }

    //修改工单超产状态 及 超产数量 加一
    @Override
    public void updateShopOrderOverfulfillQty( String shopOrderBO, int qty ) {
        snMapper.updateShopOrderOverfulfillQty(shopOrderBO,qty);
    }

    /**
     * 修改生产前工单返坯数
     * @param shopOrderBO
     * @param qty
     */
    @Override
    public void updateAttribute2ByShopOrderBoAndQty(String shopOrderBO, int qty) {
        snMapper.updateAttribute2ByShopOrderBoAndQty(shopOrderBO,qty);
    }

    /**
     * 修改生产前工单破损数
     * @param shopOrderBO
     * @param qty
     */
    @Override
    public void updateAttribute3ByShopOrderBoAndQty(String shopOrderBO, int qty) {
        snMapper.updateAttribute3ByShopOrderBoAndQty(shopOrderBO,qty);
    }

    /**
     * 修改生产后工单返坯数
     * @param shopOrderBO
     * @param qty
     */
    @Override
    public void updateAttribute4ByShopOrderBoAndQty(String shopOrderBO, int qty) {
        snMapper.updateAttribute4ByShopOrderBoAndQty(shopOrderBO,qty);
    }

    /**
     * 修改生产后工单破损数
     * @param shopOrderBO
     * @param qty
     */
    @Override
    public void updateAttribute5ByShopOrderBoAndQty(String shopOrderBO, int qty) {
        snMapper.updateAttribute5ByShopOrderBoAndQty(shopOrderBO,qty);
    }

}