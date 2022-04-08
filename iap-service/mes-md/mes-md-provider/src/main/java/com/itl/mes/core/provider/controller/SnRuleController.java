package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.mes.core.api.entity.SnRule;
import com.itl.mes.core.api.service.SnRuleService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *
 * @author space
 * @since 2019-08-03
 */
@RestController
@RequestMapping("/monopy/snRules")
@Api(tags = " 条码规则表" )
public class SnRuleController {
    private final Logger logger = LoggerFactory.getLogger(SnRuleController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public SnRuleService snRuleService;

    /**
    * 根据id查询
    *
    * @param id 主键
    * @return
    */
    @GetMapping("/{id}")
    @ApiOperation(value="通过主键查询数据")
    public ResponseData<SnRule> getSnRuleById(@PathVariable String id) {
        return ResponseData.success(snRuleService.getById(id));
    }


    /**
     * 分页查询信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @PostMapping("/page")
    @ApiOperation(value="分页查询条码规则数据")
    @ApiOperationSupport( params = @DynamicParameters(name = "WorkShopRequestModel",properties = {
            @DynamicParameter( name="page", value = "页面，默认为1" ),
            @DynamicParameter( name="limit", value = "分页大小，默认20，可不填" ),
            @DynamicParameter( name="orderByField", value = "排序属性，可不填" ),
            @DynamicParameter( name="isAsc", value = "排序方式，true/false，可不填" ),
            @DynamicParameter( name="ruleName", value = "名称，可不填" ),
            @DynamicParameter( name="ruleCode", value = "代码，可不填" )
    }) )
    public ResponseData<IPage<SnRule>> snRule(@RequestBody Map<String, Object> params ){
        QueryWrapper<SnRule> wrapper = new QueryWrapper<>();
        if (!StrUtil.isBlank((String)params.getOrDefault("ruleName","" ))) {
            wrapper.eq(SnRule.RULE_NAME,params.get("ruleName").toString());
        }
        if (!StrUtil.isBlank((String)params.getOrDefault("ruleCode","" ))) {
            //wrapper.andNew().like( SnRule.RULE_CODE, params.get("ruleCode").toString(), SqlLike.RIGHT );
            wrapper.eq(SnRule.RULE_CODE,params.get("ruleCode").toString());
        }
        IPage<SnRule> page = snRuleService.page( new QueryPage<>( params ), wrapper );
        return ResponseData.success( page );

    }

    @PostMapping("/save")
    @ApiOperation(value="保存编码规则数据")
    public ResponseData<String> saveSnRule(@RequestBody SnRule snRule ) throws CommonException {
        snRuleService.saveSnRule( snRule );
        return ResponseData.success( "success" );
    }


    @PostMapping("/update")
    @ApiOperation(value="编辑编码规则数据")
    public ResponseData<String> updateSnRule(@RequestBody SnRule snRule ) throws CommonException {
        snRuleService.updateSnRule( snRule );
        return ResponseData.success( "success" );
    }


    @PostMapping("/delete")
    @ApiOperation(value="删除编码规则数据")
    public ResponseData<String> deleteSnRule(@RequestBody List<SnRule> snRule ) throws CommonException {
        snRuleService.deleteSnRule( snRule );
        return ResponseData.success( "success" );
    }
}