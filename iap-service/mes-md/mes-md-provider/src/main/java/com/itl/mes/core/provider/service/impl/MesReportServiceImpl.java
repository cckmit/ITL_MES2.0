package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.OpThroughRateDTO;
import com.itl.mes.core.api.entity.SfcWiplog;
import com.itl.mes.core.api.service.MesReportService;
import com.itl.mes.core.api.vo.OpThroughRateVo;
import com.itl.mes.core.provider.mapper.SfcWiplogMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MesReportServiceImpl implements MesReportService {

    @Autowired
    private SfcWiplogMapper sfcWiplogMapper;

    @Override
    public IPage<OpThroughRateVo> getOperationThroughRateReport(OpThroughRateDTO opThroughRateDTO) {
        if (ObjectUtil.isEmpty(opThroughRateDTO.getPage())){
            opThroughRateDTO.setPage(new Page(1,10));
        }
        String startTime = null;
        String endTime = null;
        if (StringUtils.isNotBlank(opThroughRateDTO.getStartTime())){
            startTime = opThroughRateDTO.getStartTime() + " 00:00:00";
        }
        if (StringUtils.isNotBlank(opThroughRateDTO.getEndTime())){
            endTime = opThroughRateDTO.getEndTime() + " 23:59:59";
        }
        opThroughRateDTO.setStartTime(startTime);
        opThroughRateDTO.setEndTime(endTime);
        IPage<OpThroughRateVo> opThroughRateVoList = sfcWiplogMapper.getOperationThroughRateReport(opThroughRateDTO.getPage(),opThroughRateDTO);
        for (OpThroughRateVo opThroughRateVo : opThroughRateVoList.getRecords()) {
            opThroughRateDTO.setOperationBo(opThroughRateVo.getOperationBo());
            opThroughRateDTO.setState("暂停");
//            List<OpThroughRateVo> recordsO = sfcWiplogMapper.getOperationThroughRateReport(opThroughRateDTO.getPage(), opThroughRateDTO).getRecords();
            List<OpThroughRateVo> recordsO = sfcWiplogMapper.getOpThroughRateByCon(opThroughRateDTO);
            //计算暂停的进站数
            BigDecimal stopInputQty = BigDecimal.ZERO;
            for (OpThroughRateVo throughRateVo : recordsO) {
                stopInputQty = stopInputQty.add(throughRateVo.getInputQty());
            }
            //实际进站数 = opThroughRateVoList查询出来的进站数 - 暂停的进站数
            BigDecimal realInputQty = opThroughRateVo.getInputQty().subtract(stopInputQty);
            opThroughRateVo.setInputQty(realInputQty);
            //直通率 = (出站数/实际进站数 * 100) %
            BigDecimal throughRate = opThroughRateVo.getDoneQty().divide(realInputQty,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));//直通率
            opThroughRateVo.setThroughRate(throughRate + "%");//直通率拼接百分号

            List<OpThroughRateVo.NcDetails> ncDetailsList = sfcWiplogMapper.selectWipNcByOperationBo(opThroughRateDTO.getStartTime(),opThroughRateDTO.getEndTime(),opThroughRateVo.getOperationBo());//不良明细
            List<OpThroughRateVo.ScrapDetails> scrapDetailsList = sfcWiplogMapper.selectWipScByOperationBo(opThroughRateDTO.getStartTime(),opThroughRateDTO.getEndTime(),opThroughRateVo.getOperationBo());//报废明细
            opThroughRateVo.setNcDetailsList(ncDetailsList);
            opThroughRateVo.setScrapDetailsList(scrapDetailsList);
        }
        return opThroughRateVoList;
    }
}
