package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.entity.NcCode;
import com.itl.mes.core.api.service.NcCodeService;
import com.itl.mes.core.api.vo.NcCodeVo;
import com.itl.mes.core.api.vo.NcGroupVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * @author space
 * @since 2019-05-24
 */
@RestController
@RequestMapping("/ncCodes")
@Api(tags = " 不合格代码表" )
public class NcCodeController {
    private final Logger logger = LoggerFactory.getLogger(NcCodeController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public NcCodeService ncCodeService;


    /**
     * 根据List<BO>查询不合格代码对象
     * 结果集类型List<NcCode>
     * /ncCodes/selectListNcCode/byListBo
     */
    @PostMapping("/selectListNcCode/byListBo")
    public ResponseData<List<NcCode>> selectByListBo(@RequestBody List<String> boList){
        List<NcCode> ncCodes = (List<NcCode>) ncCodeService.listByIds(boList);
        return ResponseData.success(ncCodes);
    }

    /**
     * 不良代码插入
     */
    @PostMapping( "/save" )
    @ApiOperation(value="保存不良代码数据")
    public ResponseData<NcCodeVo> saveNcCode(@RequestBody NcCodeVo ncCodeVo) throws CommonException {
        if(ncCodeVo==null) {
            throw new CommonException("参数不能为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        ncCodeService.saveNcCode(ncCodeVo);
        NcCodeVo CodeVo =  ncCodeService.getNcCodeVoByNcCode(ncCodeVo.getNcCode());
        return ResponseData.success(CodeVo);
    }

    @GetMapping("/query")
    @ApiOperation(value="查询不良代码数据")
    public ResponseData<NcCodeVo> selectNcCodeByMap(String ncCode) throws CommonException {
        if(StrUtil.isBlank(ncCode))throw new CommonException("参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        NcCodeVo ncCodeVo =  ncCodeService.getNcCodeVoByNcCode(ncCode);
        return ResponseData.success(ncCodeVo);
    }

    @GetMapping("/delete")
    @ApiOperation(value="删除不良代码数据")
    public  ResponseData<String> deleteNcCode(String ncCode ,String modifyDate) throws CommonException {
        if(StrUtil.isBlank(ncCode)) throw new CommonException("不良代码不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        ncCodeService.deleteNcCode(ncCode, DateUtil.parse(modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS));
        return ResponseData.success( "success" );
    }

    @GetMapping("/getNcGroupVoList")
    @ApiOperation(value="查询前500条不良代码组页面初始化用")
    public ResponseData<List<NcGroupVo>> getNcGroupVoList() throws CommonException {
        List<NcGroupVo> ncGroupVoList = ncCodeService.getNcGroupVoList();
        return ResponseData.success(ncGroupVoList);
    }


    /**
    @GetMapping("/fuzzyQuery")
    @ApiOperation(value="模糊查询不良代码数据")
    public RestResult<List<NcCode>> selectNcCodeByLike(@RequestBody NcCodeVo ncCodeVo){
        try {
            if(ncCodeVo==null)throw new BusinessException("参数不能为空");
            EntityWrapper<NcCode> entityWrapper = new EntityWrapper<NcCode>();
            entityWrapper.like("NC_CODE",ncCodeVo.getNcCode());
            entityWrapper.like("NC_NAME",ncCodeVo.getNcName());
            entityWrapper.like("NC_DESC",ncCodeVo.getNcDesc());
            List<NcCode> ncCodes = ncCodeService.selectList(entityWrapper);
            return new RestResult<List<NcCode>>(ncCodes);
        } catch (BusinessException e) {
            logger.error("fuzzyQueryNcCode -=- {}", ExceptionUtils.getFullStackTrace(e) );
            return new RestResult<List<NcCode>>( false, 10001, e.getMessage() );
        }

    }



    @PostMapping("/page")
    @ApiOperation(value="分页查询不良代码数据,参数为map类型")
    @ApiOperationSupport( params = @DynamicParameters(name = "NcCodeRequestModel",properties = {
            @DynamicParameter( name="page", value = "页面，默认为1" ),
            @DynamicParameter( name="limit", value = "分页大小，默认100，可不填" ),
            @DynamicParameter( name="orderByField", value = "排序属性，可不填" ),
            @DynamicParameter( name="isAsc", value = "排序方式，true/false，可不填" ),
            @DynamicParameter( name="ncCode", value = "不良代码" ),
            @DynamicParameter( name="ncName", value = "不良代码名称" ),
            @DynamicParameter( name="ncDesc", value = "描述" )
    }) )
    public RestResult<Page<NcCode>> page(@RequestBody Map<String,Object> params){
        EntityWrapper<NcCode> entityWrapper = new EntityWrapper<>();
        String site = UserUtils.getSite();
        entityWrapper.eq(NcCode.SITE,site);
        if(!StrUtil.isBlank(params.getOrDefault("ncCode","").toString())){
            entityWrapper.andNew().like(NcCode.NC_CODE,params.get("ncCode").toString());
        }
        if(!StrUtil.isBlank(params.getOrDefault("ncName","").toString())){
            entityWrapper.andNew().like(NcCode.NC_NAME,params.get("ncName").toString());
        }
        if(!StrUtil.isBlank(params.getOrDefault("ncDesc","").toString())){
            entityWrapper.andNew().like(NcCode.NC_DESC,params.get("ncDesc").toString());
        }
        try {
            Page<NcCode> ncCodePage = ncCodeService.selectPage(new QueryPage<>(params), entityWrapper);
            return new RestResult<Page<NcCode>> (ncCodePage);
        }catch (BusinessException e){
            logger.error("getNcCodeById -=- {}", ExceptionUtils.getFullStackTrace( e ) );
            return new RestResult<Page<NcCode>> (false, 10000, e.getMessage() );
        }

    }

    */
    
}