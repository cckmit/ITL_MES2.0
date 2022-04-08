package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.FeedInDTO;
import com.itl.mes.core.api.dto.QualityCheckListDTO;
import com.itl.mes.core.api.entity.FeedIn;
import com.itl.mes.core.api.entity.QualityCheckList;
import com.itl.mes.core.api.entity.Sfc;
import com.itl.mes.core.api.service.FeedInService;
import com.itl.mes.core.api.vo.FeedInVo;
import com.itl.mes.core.provider.mapper.FeedInMapper;
import com.itl.mes.core.provider.mapper.SfcMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.internal.dynalink.linker.LinkerServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/feedin")
@Api(tags = "上料" )
public class FeedInController {
    private final Logger logger = LoggerFactory.getLogger(FeedInController.class);

    @Autowired
    private FeedInService feedInService;

    @Autowired
    private FeedInMapper feedInMapper;

    @Autowired
    private SfcMapper sfcMapper;

    /**
     * 精确查询
     * @param feedInDTO
     * @return
     */
    @PostMapping("/queryList")
    @ApiOperation(value = "查询上料数据")
    public ResponseData<List<FeedInVo>> queryList(@RequestBody FeedInDTO feedInDTO) throws CommonException {
        //QualityPlanAtParameterVo qualityPlanAtParameterVo =  qualityPlanService.getQpapVoByQualityPlan(qualityPlan,version);
        List<FeedInVo> feedInVos = feedInService.queryList(feedInDTO);
        if (feedInVos==null){
            return ResponseData.error("500","当前批次不在该工单Bom组件中或未完成");
        }
        return ResponseData.success(feedInVos);
    }

    /**
     * 精确查询
     * @param feedInDTO
     * @return
     */
    @PostMapping("/query")
    @ApiOperation(value = "查询上料数据")
    public ResponseData<FeedInVo> query(@RequestBody FeedInDTO feedInDTO) throws CommonException {
        //QualityPlanAtParameterVo qualityPlanAtParameterVo =  qualityPlanService.getQpapVoByQualityPlan(qualityPlan,version);
        final FeedIn feedIn = feedInMapper.selectOne(new QueryWrapper<FeedIn>().eq("ASSY_SFC", feedInDTO.getSfc()).eq("STATE", "0"));
        if (feedIn!=null){
            return ResponseData.error("500","当前批次已处于上料中");
        }
        FeedInVo feedInVo = feedInService.query(feedInDTO);
        if (feedInVo==null){
            return ResponseData.error("500","当前批次不在该工单Bom组件中或未完成");
        }
        return ResponseData.success(feedInVo);
    }

    /**
     * 精确查询
     * @param feedInDTO
     * @return
     */
    @PostMapping("/uninstall")
    @ApiOperation(value = "卸载上料数据")
    public ResponseData<FeedIn> uninstall(@RequestBody FeedInDTO feedInDTO) throws CommonException {
        //QualityPlanAtParameterVo qualityPlanAtParameterVo =  qualityPlanService.getQpapVoByQualityPlan(qualityPlan,version);
        QueryWrapper<FeedIn> queryWrapper = new QueryWrapper<FeedIn>();
        if (StrUtil.isNotEmpty(feedInDTO.getShopOrderBo())){
            queryWrapper.eq("SHOP_ORDER_BO",feedInDTO.getShopOrderBo());
        }
        if (StrUtil.isNotEmpty(feedInDTO.getDeviceBo())){
            queryWrapper.eq("DEVICE_BO",feedInDTO.getDeviceBo());
        }
        if (StrUtil.isNotEmpty(feedInDTO.getSfc())){
            queryWrapper.eq("ASSY_SFC",feedInDTO.getSfc());
        }
        //FeedIn feedIn = feedInService.getOne(queryWrapper);
        FeedIn feed = new FeedIn();
        feed.setState("1");
        //feedIn.setState("1");
        //feedInService.updateById(feedIn);
        feedInService.update(feed,new QueryWrapper<FeedIn>().eq("DEVICE_BO",feedInDTO.getDeviceBo()));
        return ResponseData.success(feed);
    }
}
