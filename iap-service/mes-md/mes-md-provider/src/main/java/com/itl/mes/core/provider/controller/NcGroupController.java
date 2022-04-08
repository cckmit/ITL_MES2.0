package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.service.NcGroupService;
import com.itl.mes.core.api.vo.NcCodeVo;
import com.itl.mes.core.api.vo.NcGroupVo;
import com.itl.mes.core.api.vo.OperationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author space
 * @since 2019-05-24
 */
@RestController
@RequestMapping("/ncGroups")
@Api(tags = " 不合格代码组表")
public class NcGroupController {
    private final Logger logger = LoggerFactory.getLogger(NcGroupController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public NcGroupService ncGroupService;


    @PostMapping("/save")
    @ApiOperation("保存不良数据组数据")
    public ResponseData<NcGroupVo> saveNcGroupVo(@RequestBody NcGroupVo ncGroupVo) throws CommonException {
        if (ncGroupVo == null) throw new CommonException("参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        ncGroupService.saveNcGroup(ncGroupVo);
        NcGroupVo ncGroupVoByNcGroup = ncGroupService.getNcGroupVoByNcGroup(ncGroupVo.getNcGroup());
        return ResponseData.success(ncGroupVoByNcGroup);
    }

    @GetMapping("/query")
    @ApiOperation(value = "查询不良数据组数据")
    public ResponseData<NcGroupVo> selectByNcGroup(String ncGroup) throws CommonException {
        if (StrUtil.isBlank(ncGroup))
            throw new CommonException("不良代码组编号参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        NcGroupVo ncGroupVoByNcGroup = ncGroupService.getNcGroupVoByNcGroup(ncGroup);
        return ResponseData.success(ncGroupVoByNcGroup);
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除不良代码组")
    public ResponseData<String> deleteByNcGroup(@RequestParam String ncGroup, String modifyDate) throws CommonException {
        ncGroupService.deleteByNcGroup(ncGroup, DateUtil.parse(modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS));
        return ResponseData.success("success");
    }


    @GetMapping("/getOperationList")
    @ApiOperation(value = "查询工序前500条数据")
    public ResponseData<List<OperationVo>> getOperationList(){
        List<OperationVo> operationVos = ncGroupService.getOperationList();
        return ResponseData.success(operationVos);
    }

    @GetMapping("/getNcCodeVoList")
    @ApiOperation(value = "查询不合格代码前500条数据")
    public ResponseData<List<NcCodeVo>> getNcCodeVoList(String ncCode, String ncName){
        List<NcCodeVo> ncCodeVoList = ncGroupService.getNcCodeVoList(ncCode, ncName);
        return ResponseData.success(ncCodeVoList);
    }


    /**
     @GetMapping("/fuzzyQuery")
     @ApiOperation(value = "模糊查询不良代码组")
     public  RestResult<List<NcGroup>> selectByNcGroupVo(@RequestBody NcGroupVo ncGroupVo) {
     try {
     List<NcGroup> ncGroups = ncGroupService.selectByNcGroupVo(ncGroupVo);
     return new RestResult<List<NcGroup>>(ncGroups);
     } catch (BusinessException e) {
     logger.error("selectByNcGroupVo -=- {}", ExceptionUtils.getFullStackTrace( e ) );
     return new RestResult<List<NcGroup>>( false, 10000, e.getMessage() );
     }

     }

     @PostMapping("/page")
     @ApiOperation(value = "分页查询不良代码组")
     @ApiOperationSupport(params = @DynamicParameters(name = "NcGroupRequestModel",properties = {
     @DynamicParameter( name="page", value = "页面，默认为1" ),
     @DynamicParameter( name="limit", value = "分页大小，默认100，可不填" ),
     @DynamicParameter( name="orderByField", value = "排序属性，可不填" ),
     @DynamicParameter( name="isAsc", value = "排序方式，true/false，可不填" ),
     @DynamicParameter( name="ncGroup", value = "不良代码组" ),
     @DynamicParameter( name="ncGroupName", value = "不良代码组名称" ),
     @DynamicParameter( name="ncGroupDesc", value = "描述" )
     }))
     public RestResult<Page<NcGroup>> page(@RequestBody Map<String,Object> params){
     EntityWrapper<NcGroup> entityWrapper = new EntityWrapper<>();
     entityWrapper.eq(NcGroup.SITE, UserUtils.getSite());
     if(!StrUtil.isBlank(params.getOrDefault("ncGroup","").toString())){
     entityWrapper.andNew().like(NcGroup.NC_GROUP,params.get("ncGroup").toString());
     }
     if(!StrUtil.isBlank(params.getOrDefault("ncGroupName","").toString())){
     entityWrapper.andNew().like(NcGroup.NC_GROUP_NAME,params.get("ncGroupName").toString());
     }
     if(!StrUtil.isBlank(params.getOrDefault("ncGroupDesc","").toString())){
     entityWrapper.andNew().like(NcGroup.NC_GROUP_DESC,params.get("ncGroupDesc").toString());
     }
     return new RestResult<Page<NcGroup>>();
     }
     */
}