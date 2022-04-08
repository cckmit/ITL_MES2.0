package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.service.SiteUsrService;
import com.itl.mes.core.api.vo.SiteUsrRequestVo;
import com.itl.mes.core.api.vo.SiteUsrResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author space
 * @since 2019-07-17
 */
@RestController
@RequestMapping("/siteUsrs")
@Api(tags = " 工厂用户关系维护" )
public class SiteUsrController {
    private final Logger logger = LoggerFactory.getLogger(SiteUsrController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public SiteUsrService siteUsrService;

    /**
    * 用户查询
    *
    * @param usrOrCardNumber
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value="通过用户查询数据")
    public ResponseData<SiteUsrResponseVo> getSiteUsrByUsr(@RequestParam("usr") String usrOrCardNumber,@RequestParam("type") String type ) throws CommonException {
        if( StrUtil.isBlank( usrOrCardNumber ) ){
            throw new CommonException( "用户不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        return ResponseData.success(siteUsrService.getSiteUsrByUsr( usrOrCardNumber,type ));
    }

    @PostMapping("/save")
    @ApiOperation(value="保存用户工厂对应关系")
    public ResponseData<SiteUsrResponseVo> saveUsrSite( @RequestBody SiteUsrRequestVo siteUsrRequestVo ) throws CommonException {
        if( StrUtil.isBlank( siteUsrRequestVo.getUsr() ) ){
            throw new CommonException( "用户不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        if( StrUtil.isBlank( siteUsrRequestVo.getDefaultSites() ) ){
            throw new CommonException( "默认工厂为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        siteUsrService.saveUsrSite( siteUsrRequestVo );
        return ResponseData.success(siteUsrService.getSiteUsrByUsr( siteUsrRequestVo.getUsr(),"0"));
    }

    @GetMapping("/delete")
    @ApiOperation(value="删除用户工厂对应关系")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "usr", value = "用户", dataType = "string", paramType = "query" ),
            @ApiImplicitParam( name = "modifyDate", value = "修改时间", dataType = "string", paramType = "query" )
    })
    public ResponseData<String> deleteUsrSite( String usr, String modifyDate  ) throws CommonException {
        if( StrUtil.isBlank( usr ) ){
            throw new CommonException( "用户不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        if( StrUtil.isBlank( modifyDate ) ){
            throw new CommonException( "请先查询再执行操作", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        siteUsrService.deleteUsrSite( usr, DateUtil.parse( modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS )  );
        return ResponseData.success("success" );
    }

}