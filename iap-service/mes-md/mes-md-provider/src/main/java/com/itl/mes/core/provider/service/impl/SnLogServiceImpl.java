package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.bo.SnHandleBO;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.ShopOrder;
import com.itl.mes.core.api.entity.Sn;
import com.itl.mes.core.api.entity.SnLog;
import com.itl.mes.core.api.service.ItemService;
import com.itl.mes.core.api.service.ShopOrderService;
import com.itl.mes.core.api.service.SnLogService;
import com.itl.mes.core.api.service.SnService;
import com.itl.mes.core.api.vo.SnLogVo;
import com.itl.mes.core.provider.mapper.SnLogMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * SN日志表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-09-25
 */
@Service
@Transactional
public class SnLogServiceImpl extends ServiceImpl<SnLogMapper, SnLog> implements SnLogService {


    @Autowired
    private SnLogMapper snLogMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ShopOrderService shopOrderService;

    @Autowired
    private SnService snService;

    @Override
    public List<SnLog> selectList() {
        QueryWrapper<SnLog> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, snLog);
        return super.list(entityWrapper);
    }

    @Override
    public IPage<SnLogVo> selectPageByDate(IPage<SnLogVo> page, Map<String, Object> params) throws CommonException {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<SnLog> snLogs = snLogMapper.selectPageByDate(page,params);

        List<SnLogVo> snLogVos = new ArrayList<>();
        if (snLogs.size() > 0) {
            for (SnLog snLog : snLogs) {
                SnLogVo snLogVo = new SnLogVo();
                BeanUtils.copyProperties(snLog, snLogVo);
                Item item = itemService.selectByItemAndSite(snLog.getItem(), UserUtils.getSite());
                snLogVo.setItemDesc(item.getItemDesc());
                ShopOrder existShopOrder = shopOrderService.getExistShopOrder(new ShopOrderHandleBO(UserUtils.getSite(), snLog.getShopOrder()));
                snLogVo.setOrderQty(existShopOrder.getOrderQty());
                snLogVos.add(snLogVo);
            }
        }

        page.setRecords(snLogVos);
        return page;
    }

    @Override
    public void exportMouldGroup(String site, HttpServletResponse response,String item, String startNumber, String endNumber, String startCreateDate, String endCreateDate) throws CommonException {

        Map<String, Object> params = new HashMap<>();
        params.put("site", site);
        params.put("item", item);
        params.put("startNumber", startNumber);
        params.put("endNumber", endNumber);
        params.put("startCreateDate", startCreateDate);
        params.put("endCreateDate", endCreateDate);
        List<SnLog> snLogs = snLogMapper.selectPageByDate(params);

        /*EntityWrapper<SnLog> entityWrapper = new EntityWrapper<SnLog>();
        entityWrapper.eq(SnLog.SITE, site);
        List<SnLog> snLogs = snLogMapper.selectList(entityWrapper);*/
        List<SnLogVo> snLogVos = new ArrayList<>();
        if (snLogs.size() > 0) {
            for (SnLog snLog : snLogs) {
                SnLogVo snLogVo = new SnLogVo();
                BeanUtils.copyProperties(snLog, snLogVo);
                Item itemEntity = itemService.selectByItemAndSite(snLog.getItem(),site);
                snLogVo.setItemDesc(itemEntity.getItemDesc());
                ShopOrder existShopOrder = shopOrderService.getExistShopOrder(new ShopOrderHandleBO(site, snLog.getShopOrder()));
                snLogVo.setOrderQty(existShopOrder.getOrderQty());
                snLogVos.add(snLogVo);
            }
        }
        ExcelUtils.exportExcel(snLogVos,"SN条码日志表","SN条码日志表",SnLogVo.class, "SN条码日志表", response);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteSnLog(List<SnLogVo> snLogs) throws CommonException {
        if (snLogs != null && !snLogs.isEmpty()) {
            for (SnLogVo snLogVo : snLogs) {
                QueryWrapper<SnLog> snLogEntityWrapper = new QueryWrapper<>();
                snLogEntityWrapper.eq(SnLog.SHOP_ORDER, snLogVo.getShopOrder()).eq(SnLog.START_NUMBER, snLogVo.getStartNumber())
                        .eq(SnLog.END_NUMBER, snLogVo.getEndNumber())
                        .eq(SnLog.CREATE_DATE, snLogVo.getCreateDate())
                        .eq(SnLog.SITE, UserUtils.getSite());
                List<SnLog> snLogList = snLogMapper.selectList(snLogEntityWrapper);
                if (snLogList.size() > 0) {
                    snLogMapper.deleteById(snLogList.get(0).getBo());
                }
                //同时删除SN表里数据
                //查询开始号段 和 结束号段 获取信息
                Sn startExitsSn = snService.getExitsSn(new SnHandleBO(UserUtils.getSite(), snLogVo.getStartNumber()));
                Sn endExitsSn = snService.getExitsSn(new SnHandleBO(UserUtils.getSite(), snLogVo.getEndNumber()));
                List<Sn> sns = snLogMapper.getSnByDateJudge(startExitsSn.getComplementCodeState(),
                        startExitsSn.getItemType(),startExitsSn.getMaxSerialNumber(),endExitsSn.getMaxSerialNumber());
                if (sns.size() == 0) {
                    deleteSn(startExitsSn,endExitsSn);
                }else {
                    throw new CommonException("号段当中有SN已被使用不能删除", CommonExceptionDefinition.SN_ENCODING_EXCEPTION);
                }
            }
        } else {
            throw new CommonException("参数不能为空!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
    }


    private void deleteSn( Sn startCode, Sn endCode) {
        QueryWrapper<Sn> snEntityWrapper = new QueryWrapper<>();
        snEntityWrapper.eq(Sn.COMPLEMENT_CODE_STATE, startCode.getComplementCodeState())
                .eq(Sn.ITEM_TYPE, startCode.getItemType())
                .gt(Sn.MAX_SERIAL_NUMBER, startCode.getMaxSerialNumber() - 1)
                .lt(Sn.MAX_SERIAL_NUMBER, endCode.getMaxSerialNumber() + 1);
        snService.remove(snEntityWrapper);
    }

}