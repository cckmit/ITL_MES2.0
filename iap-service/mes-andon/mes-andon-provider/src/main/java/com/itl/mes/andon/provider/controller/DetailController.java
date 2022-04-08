package com.itl.mes.andon.provider.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.andon.api.dto.AndonDetailDTO;
import com.itl.mes.andon.api.entity.Detail;
import com.itl.mes.andon.api.service.DetailService;
import com.itl.mes.andon.api.vo.DetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/detail")
@Api(value = "安灯明细",tags = "安灯明细表")
public class DetailController {

    @Autowired
    private DetailService detailService;
    /**
     * 根据id查询
     *
     * @param id 主键
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value="通过主键查询数据")
    public ResponseData<Detail> getDetailBy(@PathVariable String id) {
        return ResponseData.success(detailService.getById(id));
    }

    /**
     * 保存或新增安灯明细
     * @param detailVO
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存或新增安灯明细")
    public ResponseData<Detail> saveInUpdate(@RequestBody DetailVo detailVO) throws CommonException {
        if (detailVO == null) {
            throw new CommonException("参数值不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        Detail detail = detailService.saveInUpdate(detailVO);
        return ResponseData.success(detail);
    }

    /**
     * 删除安灯明细
     * @param id
     * @return
     */
    @DeleteMapping("/deleteDetail")
    @ApiOperation(value = "删除安灯明细")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "id", dataType = "String", paramType = "query")
    })
    public ResponseData deleteDetail(String id) throws CommonException {
        detailService.removeById(id);
        return ResponseData.success("success");
    }

    /**
     * 批量删除安灯明细
     * @param Ids
     * @return
     */
    @DeleteMapping("/deleteDetails")
    @ApiOperation(value = "批量删除安灯明细")
    public ResponseData deleteDetails(@RequestBody List<String> Ids) throws CommonException {
        detailService.removeByIds(Ids);
        return ResponseData.success("success");
    }

    /**
     * 精确查询
     * @param andonDetailDTO
     * @return
     */
    @PostMapping("/query")
    @ApiOperation(value = "分页查询安灯明细数据")
    public ResponseData<IPage<Detail>> queryAndonDetail(@RequestBody AndonDetailDTO andonDetailDTO) throws CommonException {
        //QualityPlanAtParameterVo qualityPlanAtParameterVo =  qualityPlanService.getQpapVoByQualityPlan(qualityPlan,version);
        IPage<Detail> list = detailService.queryAndonDetail(andonDetailDTO);
        return ResponseData.success(list);
    }
}
