package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.bo.*;
import com.itl.mes.core.api.vo.BomComponnetVo;
import com.itl.mes.core.api.vo.BomVo;
import com.itl.mes.me.api.dto.ItemWithTemplateDto;
import com.itl.mes.me.api.dto.MeSfcAssyAssyDto;
import com.itl.mes.me.api.entity.*;
import com.itl.mes.me.api.service.*;
import com.itl.mes.me.api.util.GeneratorId;
import com.itl.mes.me.client.service.FeignService;
import com.itl.mes.me.provider.mapper.MeSfcAssyMapper;
import com.itl.mes.pp.api.entity.schedule.ScheduleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


@Service
public class MeSfcAssyServiceImpl extends ServiceImpl<MeSfcAssyMapper, MeSfcAssy> implements MeSfcAssyService {

    @Resource
    private MeSfcAssyMapper meSfcAssyMapper;

    @Autowired
    private MeSnService meSnService;

    @Autowired
    private SnService snService;

    @Autowired
    private FeignService feignService;

    @Autowired
    private MeSfcService meSfcService;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private MeSfcWipLogService meSfcWipLogService;

    @Autowired
    private InstructorOperationService instructorOperationService;

    @Autowired
    private InstructorItemService instructorItemService;

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public List<BomComponnetVo> getAssyList(MeSfcAssyAssyDto assyDto) throws CommonException {

        // ??????sn???item????????????.
        String site = UserUtils.getSite();
        String itemBo = new ItemHandleBO(site, assyDto.getItem(), assyDto.getItemVersion()).getBo();
        Sn sn_db = snService.getOne(new QueryWrapper<Sn>().lambda()
                .eq(Sn::getSn, assyDto.getSn())
                .eq(Sn::getItemBo, itemBo));
        if (ObjectUtil.isNull(sn_db)) {
            throw new CommonException("???Sn??????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        // ??????me_sfc???.
        ScheduleEntity schedule = meSfcAssyMapper.getSchedule(assyDto.getScheduleNo());
        StationHandleBO stationHandleBO = new StationHandleBO(site, UserUtils.getStation());

        // ??????Bom????????????.
        BomVo resp = feignService.selectByBo(schedule.getBomBo());
        if (ObjectUtil.isNull(resp)) {
            throw new CommonException("????????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        String userName = userUtil.getUser().getUserName();
        ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(site, UserUtils.getProductLine());
        MeSfc meSfc = new MeSfc().setSite(site)
                .setBo(new MeSfcHandleBO(site, assyDto.getSn()).getBo())
                .setSfc(assyDto.getSn())
                .setScheduleBo(schedule.getBo())
                .setShopOrderBo(new ShopOrderHandleBO(site, resp.getShopOrder()).getBo())
                .setWorkShopBo(new WorkShopHandleBO(site, UserUtils.getWorkShop()).getBo())
                .setProductLineBo(productLineHandleBO.getBo())
                .setOperationBo(meSfcAssyMapper.getOperationBoByStationBo(stationHandleBO.getBo()))
                .setStationBo(stationHandleBO.getBo())
                .setUserBo(userName)
                // TODO: shitBo
                .setTeamBo(meSfcAssyMapper.getTeamBoByUserBo(userName))
                .setItemBo(schedule.getItemBo())
                .setBomBo(schedule.getBomBo())
                .setSfcRouterBo(schedule.getRouterBo())
                .setIsBatch(false)
                .setState("??????")
                .setInTime(new Date())
                // TODO: SFC_STEP_ID.
                .setSfcQty(assyDto.getSfcQty())
                .setInputQty(assyDto.getReceiveQty());
        List<String> deviceBos = meSfcAssyMapper.getDeviceBo(stationHandleBO.getBo(), productLineHandleBO.getBo());
        if (CollectionUtil.isNotEmpty(deviceBos)) {
            meSfc.setDeviceBo(deviceBos.get(0));
        }
        MeSfc byId = meSfcService.getById(meSfc.getBo());
        if (ObjectUtil.isNotNull(byId)) {
            meSfcService.updateById(meSfc);
        } else {
            meSfcService.save(meSfc);
        }

        // ?????????????????????????????????????????????????????????sn
        List<BomComponnetVo> ret = resp.getBomComponnetVoList();

        return getComponentList(ret);
    }

    @Override
    public String assyComponentBySn(String sn, String componentSn) throws CommonException {
        if (StrUtil.isBlank(sn)) {
            throw new CommonException("?????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        // ??????SN??????????????????.
        verifySn(componentSn);

        // ??????????????????item??????.
        Sn sn_component = snService.getOne(new QueryWrapper<Sn>().lambda().eq(Sn::getSn, componentSn));

        Optional.ofNullable(sn_component).orElseThrow(() -> new CommonException("?????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION));

        ItemHandleBO itemHandleBO = new ItemHandleBO(sn_component.getItemBo());

        // ???????????????????????????????????????????????????bom????????????.
        boolean flag = false;
        MeSfc sfc = meSfcService.getOne(new QueryWrapper<MeSfc>().lambda().eq(MeSfc::getSfc, sn));
        // ??????Bom????????????.
        BomVo resp = feignService.selectByBo(sfc.getBomBo());
        if (ObjectUtil.isNull(resp)) {
            throw new CommonException("????????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        String traceMethod = null;
        BigDecimal qty = null;

        for (BomComponnetVo component : resp.getBomComponnetVoList()) {
            if (component.getComponent().equals(itemHandleBO.getItem())) {
                traceMethod = component.getZsType();
                qty = component.getQty();
                flag = true;
            }
        }
        if (!flag) {
            throw new CommonException("?????????SN???????????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        List<MeSfcAssy> assyList = meSfcAssyMapper.selectList(new QueryWrapper<MeSfcAssy>().lambda()
                .eq(MeSfcAssy::getComponenetBo, itemHandleBO.getBo()));
        if (Optional.ofNullable(assyList).isPresent()) {
            if (assyList.size() >= qty.intValue()) {
                throw new CommonException("???????????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }

        String site = UserUtils.getSite();
        String userName = userUtil.getUser().getUserName();
        Date newDate = new Date();
        // ??????me_sn???.
        insertMeSn(sn, componentSn, site, userName, newDate);

        // ??????me_sfc_wip_log???.
        MeSfcWipLog log = insertMeSfcWipLog(sfc, null);

        // ??????me_sfc_assy???.
        insertMeSfcAssy(sn, componentSn, site, userName, newDate, traceMethod, qty, itemHandleBO, log.getBo());

        return itemHandleBO.getItem();
    }

    @Override
    public List<ItemWithTemplateDto> getTemplates(String station) {
        StationHandleBO stationHandleBO = new StationHandleBO(UserUtils.getSite(), station);
        AtomicReference<List<ItemWithTemplateDto>> ret = new AtomicReference<>();

        Optional.ofNullable(meSfcAssyMapper.getOperationBoByStationBo(stationHandleBO.getBo())).ifPresent(operationBo -> {
            InstructorOperation instructorOperation = instructorOperationService.getOne(
                    new QueryWrapper<InstructorOperation>().lambda().eq(InstructorOperation::getOperationBo, operationBo));
            Optional.ofNullable(instructorOperation).ifPresent(io -> {
                try {
                    ret.set(instructorItemService.listWithTemplate(io.getInstructorBo()));
                } catch (CommonException commonException) {
                    commonException.printStackTrace();
                }
            });
        });
        return ret.get();
    }

    /**
     * ??????SN?????????????????????.
     *
     * @param sn
     * @throws CommonException
     */
    private void verifySn(String sn) throws CommonException {
        // ??????sn??????????????????.
        MeSn byId = meSnService.getById(sn);
        if (ObjectUtil.isNotNull(byId)) {
            throw new CommonException("???Sn????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param ret
     * @return
     * @throws CommonException
     */
    private List<BomComponnetVo> getComponentList(List<BomComponnetVo> ret) {
        String site = UserUtils.getSite();
        List<String> componentBos = new ArrayList<>();
        ret.forEach(x -> componentBos.add(new ItemHandleBO(site, x.getComponent(), x.getItemVersion()).getBo()));
        List<MeSfcAssy> meSfcAssies = meSfcAssyMapper.selectList(new QueryWrapper<MeSfcAssy>().lambda().in(MeSfcAssy::getComponenetBo, componentBos));
        if (CollectionUtil.isNotEmpty(meSfcAssies)) {
            for (MeSfcAssy meSfcAssy : meSfcAssies) {
                for (BomComponnetVo bomComponnetVo : ret) {
                    if (meSfcAssy.getComponenetBo().contains(bomComponnetVo.getComponent())) {
                        bomComponnetVo.setAssyFinish(true);
                        if (StrUtil.isBlank(bomComponnetVo.getItemSn())) {
                            bomComponnetVo.setItemSn(meSfcAssy.getAssembledSn());
                        } else {
                            bomComponnetVo.setItemSn(bomComponnetVo.getItemSn() + "," + meSfcAssy.getAssembledSn());
                        }
                    }
                }
            }
        }

        return ret;
    }

    private void insertMeSn(String sn, String componentSn, String site, String userName, Date newDate) {
        MeSn byId = meSnService.getById(sn);
        if (ObjectUtil.isNull(byId)) {
            MeSn sfcSn = new MeSn();
            sfcSn.setSn(sn)
                    .setSite(site)
                    .setObjectSetBasicAttribute(userName, newDate);
            meSnService.save(sfcSn);
        }
        MeSn meSn = new MeSn();
        meSn.setSn(componentSn)
                .setSite(site)
                .setObjectSetBasicAttribute(userName, newDate);
        meSnService.save(meSn);
    }

    private void insertMeSfcAssy(String sn, String componentSn, String site, String userName, Date newDate, String traceMethod, BigDecimal qty, ItemHandleBO itemHandleBO, String logBo) {
        MeSfcAssy meSfcAssy = new MeSfcAssy();
        meSfcAssy.setId(new GeneratorId().getSnowflake().nextIdStr())
                .setSite(site)
                .setSfc(sn)
                .setTraceMethod(traceMethod)
                .setComponenetBo(itemHandleBO.getBo())
                .setAssembledSn(componentSn)
                .setQty(qty)
                .setAssyTime(newDate)
                .setAssyUser(userName)
                .setWipLogBo(logBo)
                .setComponentState(1);
        save(meSfcAssy);
    }

    private MeSfcWipLog insertMeSfcWipLog(MeSfc sfc, Date outTime) {
        MeSfcWipLog meSfcWipLog = new MeSfcWipLog();
        BeanUtil.copyProperties(sfc, meSfcWipLog);
        meSfcWipLog.setBo(UUID.uuid32());
        meSfcWipLog.setCreateDate(new Date());
        meSfcWipLog.setResult("OK");
        meSfcWipLogService.save(meSfcWipLog);
        return meSfcWipLog;
    }
}
