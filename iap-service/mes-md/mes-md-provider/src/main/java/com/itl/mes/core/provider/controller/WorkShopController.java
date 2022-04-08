package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.entity.WorkShop;
import com.itl.mes.core.api.service.WorkShopService;
import com.itl.mes.core.api.vo.WorkShopVo;
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
 * @since 2019-05-23
 */
@RestController
@RequestMapping("/workShops")
@Api(tags = " 车间表" )
public class WorkShopController {
    private final Logger logger = LoggerFactory.getLogger(WorkShopController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public WorkShopService workShopService;


    /**
     * 分页查询信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @PostMapping("/page")
    @ApiOperation(value="分页查询物车间数据")
    @ApiOperationSupport( params = @DynamicParameters(name = "WorkShopRequestModel",properties = {
            @DynamicParameter( name="page", value = "页面，默认为1" ),
            @DynamicParameter( name="limit", value = "分页大小，默认20，可不填" ),
            @DynamicParameter( name="orderByField", value = "排序属性，可不填" ),
            @DynamicParameter( name="isAsc", value = "排序方式，true/false，可不填" ),
            @DynamicParameter( name="workShop", value = "车间，可不填" ),
            @DynamicParameter( name="state", value = "状态，可不填" ),
            @DynamicParameter( name="workShopDesc", value = "描述，可不填" )
    }) )
    public ResponseData<IPage<WorkShop>> page(@RequestBody Map<String, Object> params ){
        QueryWrapper<WorkShop> wrapper = new QueryWrapper<>();
        wrapper.eq( WorkShop.SITE, UserUtils.getSite() );
        if( !StrUtil.isBlank( params.getOrDefault( "workShop","" ).toString() ) ){
            wrapper.likeRight( WorkShop.SITE, params.get( "workShop" ).toString());
        }
        if( !StrUtil.isBlank( params.getOrDefault( "state","" ).toString() ) ){
            wrapper.likeRight( WorkShop.STATE, params.get( "state" ).toString() );
        }
        if( !StrUtil.isBlank( params.getOrDefault( "workShopDesc","" ).toString() ) ){
            wrapper.likeRight( WorkShop.WORK_SHOP_DESC, params.get( "workShopDesc" ).toString());
        }
        IPage<WorkShop> page = workShopService.page(new QueryPage<>(params), wrapper);
        return ResponseData.success(page);

    }

    /**
    * 根据workShop查询
    *
    * @param workShop 车间
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value="通过车间查询数据")
    @ApiImplicitParam(name="workShop",value="车间",dataType="string", paramType = "query")
    public ResponseData<WorkShopVo> getWorkShopByWorkShop(String workShop ) throws CommonException {
        return  ResponseData.success(workShopService.getWorkShopByWorkShop( workShop ));
    }

    @PostMapping("/save")
    @ApiOperation(value="保存车间数据")
    public ResponseData<WorkShopVo> saveWorkShop( @RequestBody WorkShopVo workShopVo ) throws CommonException {
        workShopService.saveWorkShop( workShopVo );
        return ResponseData.success(workShopService.getWorkShopByWorkShop( workShopVo.getWorkShop() ));
    }


    @GetMapping("/getWorkShopBySite")
    @ApiOperation(value="通过工厂查询数据")
    public List<WorkShop> getWorkShopBySite(){
        List<WorkShop> list = workShopService.list(new QueryWrapper<WorkShop>().lambda().eq(WorkShop::getSite, UserUtils.getSite()));
        return list;
    }

    @GetMapping( "/delete" )
    @ApiOperation( value="删除车间数据" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="workShop",value="车间",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="modifyDate",value="修改时间",dataType="string", paramType = "query")
    })
    public ResponseData<String> deleteWorkShop(String workShop, String modifyDate ) throws CommonException {
        if( StrUtil.isBlank( workShop ) ){
            throw new CommonException( "车间参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if( StrUtil.isBlank( modifyDate ) ){
            throw new CommonException( "修改时间不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        workShopService.deleteWorkShop( workShop.trim(), DateUtil.parse( modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS ) );
        return ResponseData.success( "success" );
    }


    @GetMapping("/siteWorkShopList")
    @ApiOperation(value="查询工厂下的车间")
    @ApiImplicitParam(name="site",value="工厂",dataType="string", paramType = "query")
    public ResponseData siteWorkShopList(String site){
        QueryWrapper<WorkShop> wrapper = new QueryWrapper<>();
        wrapper.eq("site",site);
        wrapper.select("bo","work_shop","work_shop_desc");
        return  ResponseData.success(workShopService.list(wrapper));
    }
}