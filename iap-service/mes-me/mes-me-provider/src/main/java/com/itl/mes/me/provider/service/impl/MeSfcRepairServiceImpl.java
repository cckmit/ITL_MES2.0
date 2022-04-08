package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.bo.*;
import com.itl.mes.me.api.dto.RepairInputDto;
import com.itl.mes.me.api.dto.ScrapOrRepairFinDto;
import com.itl.mes.me.api.entity.*;
import com.itl.mes.me.api.service.MeSfcRepairService;
import com.itl.mes.me.api.vo.RepairLogListVo;
import com.itl.mes.me.api.vo.RepairStationVo;
import com.itl.mes.me.api.vo.SendRepairDetailsVo;
import com.itl.mes.me.api.vo.SendRepairObjVo;
import com.itl.mes.me.provider.mapper.MeSfcRepairMapper;
import com.itl.mes.pp.api.entity.schedule.ScheduleEntity;
import com.itl.pp.core.client.PpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 维修业务层
 */
@Service
public class MeSfcRepairServiceImpl extends ServiceImpl<MeSfcRepairMapper, MeSfcRepair> implements MeSfcRepairService {

    @Autowired
    private SnServiceImpl snService;
    @Autowired
    private MeSfcNcLogServiceImpl meSfcNcLogService;
    @Autowired
    private MeSfcServiceImpl meSfcService;
    @Autowired
    private PpService ppService;
    @Autowired
    private MeSfcRepairMapper meSfcRepairMapper;
    @Autowired
    private MeSfcRepairServiceImpl meSfcRepairService;
    @Autowired
    private MeSfcWipLogServiceImpl meSfcWipLogService;

    /**
     * 返回上游数据
     */
    @Override
    public RepairStationVo frontDataVo(String snCode) {
        /**
         * 维修工位 产品上游信息展示
         */
        // "1002001002002105000001";
        // Sn dbSnEntiy = snService.getById("SN:1040,1002001002002105000001");

        // String site = UserUtils.getSite();
        // SnHandleBO snHandleBO = new SnHandleBO(site, snCode); //sn的Bo
        // Sn dbSnEntiy = snService.getById(snHandleBO.getBo());

        MeSfc sfc = meSfcService.getOne(new QueryWrapper<MeSfc>().eq("SFC", snCode));

        if (sfc == null) {
            throw new RuntimeException("SFC不存在");
        }

        String shopOrder = sfc.getShopOrderBo();
        String productLineBO = sfc.getProductLineBo();
        String productLine = new ProductLineHandleBO(productLineBO).getProductLine();
        String item = new ItemHandleBO(sfc.getItemBo()).getItem();

        String sfcSN = sfc.getSfc();
        String scheduleBo = sfc.getScheduleBo();
        ScheduleEntity data = ppService.getByScheduleBoProcess(scheduleBo).getData();
        String scheduleNo = data.getScheduleNo();

        //判断集合是否有值,查询出来是否是一条记录？
        List<MeSfcNcLog> dbSfcNcLogList = meSfcNcLogService.list(new QueryWrapper<MeSfcNcLog>().eq("SFC", snCode));
        String operation = new OperationHandleBO(dbSfcNcLogList.get(0).getOperationBo()).getOperation();
        String station = new StationHandleBO(dbSfcNcLogList.get(0).getStationBo()).getStation();
        String userBo = dbSfcNcLogList.get(0).getUserBo();
        Date recordTime = dbSfcNcLogList.get(0).getRecordTime();

        RepairStationVo repairStationVo = new RepairStationVo();
        repairStationVo.setSfc(sfcSN);
        repairStationVo.setShopOrder(shopOrder);
        repairStationVo.setProductLine(productLine);
        repairStationVo.setItem(item);
        repairStationVo.setScheduleNo(scheduleNo);
        repairStationVo.setOperation(operation);
        repairStationVo.setStation(station);
        repairStationVo.setUserBo(userBo);
        repairStationVo.setRecordTime(recordTime);

        /**
         * 送修详情 集合
         */
        List<String> badCodeBoList = dbSfcNcLogList.stream().map(MeSfcNcLog::getNcCodeBo).collect(Collectors.toList());
        List<SendRepairDetailsVo> sendRepairDetailsVo = meSfcRepairMapper.getSendRepairDetailsVo(badCodeBoList, snCode);
        repairStationVo.setSendRepairDetailsVo(sendRepairDetailsVo);

        /**
         * 且返回该成品的维修明细
         */
        List<RepairLogListVo> repairLogListVo = meSfcRepairMapper.getRepairLogListVo(snCode);
        repairStationVo.setRepairLogListVo(repairLogListVo);

        /**
         * 累计维修数量
         */
        Integer repairCount = meSfcRepairMapper.selectCount(new QueryWrapper<MeSfcRepair>().groupBy("SFC"));
        repairStationVo.setRepairCount(repairCount);
        return repairStationVo;
    }

    /**
     * 录入维修数据
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public SendRepairObjVo saveInputRepair(List<RepairInputDto> repairInputList) {
        /**
         * 测试所用参数
         * {
         *   "badItemBo": "ITEM:1020,MA001,1_1",
         *   "badItemSn": "SN:1040,1002001002002105000008",
         *   "dutyUnit": "1111",
         *   "remark": "2222",
         *   "repairMethod": "11111",
         *   "repairReason": "11111",
         *   "replaceItemSn": "1002001002002105000003",
         *   "sfc": "1002001002002105000001"
         * }
         */

        /**
         * 准备数据
         */
        if (repairInputList.size() == 0) {
            throw new RuntimeException("请添加录入参数");
        }
        MeSfc dbSfcEntity = meSfcService.getOne(new QueryWrapper<MeSfc>().eq("SFC", repairInputList.get(0).getSfc()));

        /**
         * WipLog的BO
         */
        String uuid32Wip = UUID.uuid32();

        if (dbSfcEntity != null) {
            MeSfcHandleBO meSfcHandleBO = new MeSfcHandleBO(dbSfcEntity.getBo());
            String site = meSfcHandleBO.getSite();
            /**
             * 更新MeSfc
             */
            MeSfc meSfc = meSfcService.updateLast(dbSfcEntity);
            /**
             * 存入生产日志表WipLog
             */
            MeSfcWipLog meSfcWipLog = new MeSfcWipLog();
            BeanUtil.copyProperties(meSfc, meSfcWipLog);
            meSfcWipLog.setBo(uuid32Wip).setCreateDate(new Date());
            meSfcWipLogService.save(meSfcWipLog);

            List<MeSfcRepair> meSfcRepairList = new ArrayList<>();
            for (RepairInputDto repairInputDto : repairInputList) {
                /**
                 * 前端传来的每一条repair记录必须和MeSfcNcLog一对一
                 * 返回送修数据的时候每一条送修数据携带MeSfcNcLog的BO,此处使用MeSfcNcLog的BO进行匹配
                 */
                MeSfcNcLog dbMeSfcNcLog = meSfcNcLogService.getOne(new QueryWrapper<MeSfcNcLog>().eq("BO", repairInputDto.getNgLogBo()).eq("SFC", repairInputDto.getSfc()));
                if (dbMeSfcNcLog == null) {
                    throw new RuntimeException("您所录入的不良物料: " + repairInputDto.getBadItemBo() + " 后台无记录:)");
                }

                /**
                 * 替换组件的itemBo
                 */
                Sn sn = snService.getOne(new QueryWrapper<Sn>().eq("SN", repairInputDto.getReplaceItemSn()));
                String itemBo = sn.getItemBo();
                MeSfcRepair meSfcRepair = new MeSfcRepair();
                meSfcRepair.setRepairReason(repairInputDto.getRepairReason());
                meSfcRepair.setRepairMethod(repairInputDto.getRepairMethod());
                meSfcRepair.setDutyUnit(repairInputDto.getDutyUnit());
                meSfcRepair.setRemark(repairInputDto.getRemark());
                meSfcRepair.setSfc(repairInputDto.getSfc());
                /**
                 * MeSfcRepair的NG_LOG_BO
                 */
                meSfcRepair.setNgLogBo(dbMeSfcNcLog.getBo());
                meSfcRepair.setReplaceItemBo(itemBo);
                meSfcRepair.setReplaceItemSn(repairInputDto.getReplaceItemSn());
                meSfcRepair.setNgItemSn(repairInputDto.getBadItemSn());
                String repairBo = UUID.uuid32();
                meSfcRepair.setBo(repairBo);
                meSfcRepair.setSite(site);
                meSfcRepair.setWipLogBo(uuid32Wip);
                meSfcRepair.setRepairTime(new Date());
                meSfcRepairList.add(meSfcRepair);

                /**
                 * 更新me_sfc_nc_log表的 IS_RAW_CHECK 是否检测原材料字段为true
                 */
                dbMeSfcNcLog.setIsRawCheck(true);
                meSfcNcLogService.updateById(dbMeSfcNcLog);
            }
            /**
             * 批量插入
             */
            if (meSfcRepairList.size() != 0) {
                meSfcRepairService.saveBatch(meSfcRepairList);
            }
        } else {
            throw new RuntimeException("成品SN不正确");
        }

        /**
         * 插入后返回维修明细
         */
        SendRepairObjVo sendRepairObjVo = new SendRepairObjVo();
        List<RepairLogListVo> repairLogListVo = meSfcRepairMapper.getRepairLogListVo(repairInputList.get(0).getSfc());
        if (repairLogListVo.size() != 0) {
            sendRepairObjVo.setWipLogBo(uuid32Wip);
            sendRepairObjVo.setRepairLogListVo(repairLogListVo);
            return sendRepairObjVo;
        } else {
            return null;
        }
    }

    /**
     * 报废或者维修完成判定 更新数据 返回累计维修数据
     */
    @Override
    public Integer scrapOrRepairFinish(ScrapOrRepairFinDto scrapOrRepairFinDto) {

        switch (scrapOrRepairFinDto.getInstructions()) {
            /**
             * 强制报废 FS
             */
            case "FS":
                MeSfc dbSfcEntity = meSfcService.getOne(new QueryWrapper<MeSfc>().eq("SFC", scrapOrRepairFinDto.getSfc()));
                if (dbSfcEntity != null) {
                    /**
                     * 更新MeSfc表的报废数量
                     */
                    if (dbSfcEntity.getScrapQty() != null) {
                        BigDecimal scrapQty = dbSfcEntity.getScrapQty();
                        BigDecimal one = new BigDecimal(1);
                        scrapQty.add(one);
                        dbSfcEntity.setScrapQty(scrapQty);
                    } else {
                        BigDecimal one = new BigDecimal(1);
                        dbSfcEntity.setScrapQty(one);
                    }
                    meSfcService.updateLast(dbSfcEntity);
                } else {
                    throw new RuntimeException("ME_SFC表并没有SFC为:" + scrapOrRepairFinDto.getSfc() + " 的记录");
                }

                Integer repairCount = meSfcRepairMapper.selectCount(new QueryWrapper<MeSfcRepair>().groupBy("SFC"));
                if (repairCount != null) {
                    return repairCount;
                } else {
                    return null;
                }

            case "RF":
                MeSfc dbSfcEntityRF = meSfcService.getOne(new QueryWrapper<MeSfc>().eq("SFC", scrapOrRepairFinDto.getSfc()));
                if (dbSfcEntityRF != null) {
                    /**
                     * 更新MeSfc表的良品数量
                     */
                    if (dbSfcEntityRF.getDoneQty() != null) {
                        BigDecimal doneQty = dbSfcEntityRF.getDoneQty();
                        BigDecimal one = new BigDecimal(1);
                        doneQty.add(one);
                        dbSfcEntityRF.setDoneQty(doneQty);
                    } else {
                        BigDecimal one = new BigDecimal(1);
                        dbSfcEntityRF.setDoneQty(one);
                    }
                    meSfcService.updateLast(dbSfcEntityRF);
                } else {
                    throw new RuntimeException("ME_SFC表并没有SFC为:" + scrapOrRepairFinDto.getSfc() + " 的记录");
                }

                Integer repairCountRF = meSfcRepairMapper.selectCount(new QueryWrapper<MeSfcRepair>().groupBy("SFC"));
                if (repairCountRF != null) {
                    return repairCountRF;
                } else {
                    return null;
                }

            default:
                Integer repairCountDe = meSfcRepairMapper.selectCount(new QueryWrapper<MeSfcRepair>().groupBy("SFC"));
                if (repairCountDe != null) {
                    return repairCountDe;
                } else {
                    return null;
                }
        }
    }


}
