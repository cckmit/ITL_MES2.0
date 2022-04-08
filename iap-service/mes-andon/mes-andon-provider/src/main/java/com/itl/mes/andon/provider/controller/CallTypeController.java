package com.itl.mes.andon.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.andon.api.dto.CallTypeDTO;
import com.itl.mes.andon.api.entity.CallType;
import com.itl.mes.andon.api.entity.Grade;
import com.itl.mes.andon.api.entity.GradePush;
import com.itl.mes.andon.api.service.CallTypeService;
import com.itl.mes.andon.api.vo.AndonGradePushVo;
import com.itl.mes.andon.provider.mapper.CallTypeMapper;
import com.itl.mes.andon.provider.mapper.GradePushMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/callType")
@Api(tags = "安灯呼叫类型设置")
public class CallTypeController {

    @Autowired
    private CallTypeService callTypeService;

    @Autowired
    private CallTypeMapper callTypeMapper;

    @Autowired
    private GradePushMapper gradePushMapper;


    /**
     * 信息
     */
    @ApiOperation(value = "根据车间BO查询")
    @GetMapping("/{workShopBo}")
    public ResponseData<List<CallType>> info(@PathVariable("workShopBo") String workShopBo) {
        List<CallType> callTypeList = callTypeMapper.selectList(new QueryWrapper<CallType>().eq("workshop_bo",workShopBo));
        return ResponseData.success(callTypeList);
    }

    /**
     * 保存或更新
     */
    @ApiOperation(value = "保存或更新")
    @PostMapping("/saveOrUpdate")
    public ResponseData<CallType> saveOrUpdate(@RequestBody CallType callType) throws CommonException {
        callTypeService.saveCallType(callType);
        return ResponseData.success();
    }

    /**
     * 分页查询
     */
    @ApiOperation(value = "分页查询")
    @PostMapping("/query")
    public ResponseData<IPage<CallType>> query(@RequestBody CallTypeDTO callTypeDTO) throws CommonException {
        IPage<CallType> page = callTypeService.queryPage(callTypeDTO);
        return ResponseData.success(page);
    }

    /**
     * andon查看配置人员
     * @return List<GradePush>
     */
    @ApiOperation(value = "查看配置人员")
    @GetMapping("/queryGradePush/{id}")
    public ResponseData<List<GradePush>> queryGradePush(@PathVariable("id") String id) {
        List<GradePush> gradePushes = gradePushMapper.selectList(new QueryWrapper<GradePush>().eq("CALL_TYPE_ID", id));
        return ResponseData.success(gradePushes);
    }

    /**
     * andon查看配置人员
     * @return List<GradePush>
     */
    @ApiOperation(value = "APP获取配置人员")
    @PostMapping("/getGradePush")
    public ResponseData<List<GradePush>> getGradePush(@RequestBody Map<String,Object> params) {
        String workShopBo = params.getOrDefault("workShopBo", "").toString();
        String andonType = params.getOrDefault("andonType", "").toString();
        QueryWrapper<CallType> queryWrapper = new QueryWrapper<CallType>();
        if (StrUtil.isNotEmpty(workShopBo)){
            queryWrapper.eq("workshop_bo",workShopBo);
        }
        if (StrUtil.isNotEmpty(andonType)){
            queryWrapper.eq("andon_type",andonType);
        }
        CallType callType = callTypeMapper.selectOne(queryWrapper);
        return ResponseData.success(gradePushMapper.selectList(new QueryWrapper<GradePush>().eq("CALL_TYPE_ID",callType.getId())));
    }

    /**
     * andon添加配置人员
     * @return string
     */
    @ApiOperation(value = "添加配置人员")
    @PostMapping("/saveGradePush")
    public ResponseData<String> saveGradePush(@RequestBody List<GradePush> gradePush) {
        callTypeService.saveGradePush(gradePush);
        return ResponseData.success("success");
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除配置人员")
    @DeleteMapping("/deletePush")
    public ResponseData<String> deletePush(@RequestBody List<Integer> ids) {
        gradePushMapper.deleteBatchIds(ids);
        return ResponseData.success("success");
    }


    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/delete")
    public ResponseData<CallType> delete(@RequestBody List<String> ids) {
        callTypeService.removeByIds(ids);
        for (String id : ids) {      //删除对应的配置人员
            gradePushMapper.delete(new QueryWrapper<GradePush>().eq("CALL_TYPE_ID",id));
        }
        return ResponseData.success();
    }

    /**
     * 根据车间和呼叫类型查询人员
     */
    @ApiOperation(value = "根据车间和BO查询")
    @GetMapping("/{workshopBo}/{andonTypeBo}")
    public ResponseData<List<GradePush>> info(@PathVariable("workshopBo") String workshopBo,@PathVariable("andonTypeBo") String andonTypeBo) {
        QueryWrapper<CallType> queryWrapper = new QueryWrapper<CallType>();
        if (StrUtil.isNotEmpty(workshopBo)){
            queryWrapper.eq("workshop_bo",workshopBo);
        }
        if (StrUtil.isNotEmpty(andonTypeBo)){
            queryWrapper.eq("andon_type",andonTypeBo);
        }
        CallType callType = callTypeMapper.selectOne(queryWrapper);
        return ResponseData.success(gradePushMapper.selectList(new QueryWrapper<GradePush>().eq("CALL_TYPE_ID",callType.getId())));
    }
}
