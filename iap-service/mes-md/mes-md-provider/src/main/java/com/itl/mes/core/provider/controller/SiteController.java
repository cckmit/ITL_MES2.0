package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.mes.core.api.entity.Site;
import com.itl.mes.core.api.service.SiteService;
import com.itl.mes.core.api.vo.SiteVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * @author space
 * @since 2019-05-21
 */
@RestController
@RequestMapping("/sites")
@Api(tags = " 工厂表" )
public class SiteController {
    private final Logger logger = LoggerFactory.getLogger(SiteController.class);

    @Autowired
    private HttpServletRequest request;


    @Autowired
    public SiteService siteService;


    /**
     * 分页查询信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @PostMapping("/page")
    @ApiOperation(value="分页查询物料组数据,参数为map,列出一些已存在的基本配置，可根据具体情况修改")
    @ApiOperationSupport( params = @DynamicParameters(name = "SiteRequestModel",properties = {
            @DynamicParameter( name="page", value = "页面，默认为1" ),
            @DynamicParameter( name="limit", value = "分页大小，默认20，可不填" ),
            @DynamicParameter( name="orderByField", value = "排序属性，可不填" ),
            @DynamicParameter( name="isAsc", value = "排序方式，true/false，可不填" ),
            @DynamicParameter( name="site", value = "工厂，可不填" ),
            @DynamicParameter( name="siteDesc", value = "描述，可不填" ),
            @DynamicParameter( name="siteType", value = "类型，可不填" )
    }) )
    public ResponseData<IPage<Site>> selectPageSiteList(@RequestBody Map<String, Object> params ){

        QueryWrapper<Site> wrapper = new QueryWrapper<>();
        if( !StrUtil.isBlank( params.getOrDefault( "site","" ).toString() ) ){
            wrapper.likeRight( Site.SITE, params.get( "site" ).toString());
        }
        if( !StrUtil.isBlank( params.getOrDefault( "siteDesc","" ).toString() ) ){
            wrapper.likeRight ( Site.SITE_DESC, params.get( "siteDesc" ).toString());
        }
        if( !StrUtil.isBlank( params.getOrDefault( "siteType","" ).toString() ) ){
            wrapper.likeRight( Site.SITE_TYPE, params.get( "siteType" ).toString() );
        }
        return ResponseData.success( siteService.page( new QueryPage<>( params ), wrapper ) );

    }

    /**
    * 根据id查询
    *
    * @param sites 主键
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value="通过工厂精确查询数据")
    public ResponseData<Site> getSiteById( String sites ) throws CommonException {

        Site result = siteService.getById( sites );
        if( result==null ){
            throw new CommonException( "工厂数据未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return ResponseData.success(result);
    }

    @PostMapping( "/save" )
    @ApiOperation(value="保存工厂")
    public ResponseData<SiteVo> saveSite(@RequestBody SiteVo siteVo ) throws CommonException {
        if( siteVo==null ){
            throw new CommonException( "参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        siteService.saveSite( siteVo );
        return ResponseData.success( siteVo );
    }


    @PostMapping("/list")
    @ApiOperation(value="查询工厂列表")
    public ResponseData list(){
        QueryWrapper<Site> wrapper = new QueryWrapper<>();
        wrapper.select("site","site_desc");
        return ResponseData.success(siteService.list(wrapper));

    }
}