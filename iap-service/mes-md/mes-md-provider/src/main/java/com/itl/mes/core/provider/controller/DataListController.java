package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.entity.DataList;
import com.itl.mes.core.api.service.DataListService;
import com.itl.mes.core.api.vo.DataListFullVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author space
 * @since 2019-06-03
 */
@RestController
@RequestMapping("/dataLists")
@Api(tags = " 数据列表表" )
public class DataListController {
    private final Logger logger = LoggerFactory.getLogger(DataListController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public DataListService dataListService;

    /**
    * 根据id查询
    *
    * @param id 主键
    * @return
    */
    @GetMapping("/{id}")
    @ApiOperation(value="通过主键查询数据")
    public ResponseData<DataList> getDataListById(@PathVariable String id) {
        return ResponseData.success( dataListService.getById(id));
    }



    /**
     * 保存数据列表数据
     *
     * @param dataListFullVo
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value="保存数据列表数据")
    public ResponseData<DataListFullVo> saveDataList(@RequestBody DataListFullVo dataListFullVo ) throws CommonException {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(dataListFullVo);
        if( validResult.hasErrors() ){
            throw new CommonException( validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        dataListService.saveDataList(dataListFullVo);
        dataListFullVo = dataListService.getDataListFullVoByDataList(dataListFullVo.getDataList());
        return ResponseData.success(dataListFullVo);
    }



    /**
     * 根据dataList查询
     *
     * @param dataList 数据列表编号
     * @return
     */
    @GetMapping("/query")
    @ApiOperation(value="通过列表编号查询数据")
    @ApiImplicitParam(name="dataList",value="列表编号",dataType="string", paramType = "query")
    public ResponseData<DataListFullVo> getDataListByDataList(String dataList ) throws CommonException {
        if (StrUtil.isBlank(dataList)) {
            throw new CommonException("列表编号不能为空!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        return ResponseData.success(dataListService.getDataListFullVoByDataList(dataList));
    }




    //删除DataList
    @GetMapping( "/delete" )
    @ApiOperation(value="通过数据列表编号删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name="dataList",value="数据列表编号",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="modifyDate",value="修改时间",dataType="string", paramType = "query")
    })
    public ResponseData<String> deleteDataListByDataList( String dataList, String modifyDate ) throws CommonException {
        if( StrUtil.isBlank( dataList ) ){
            throw new CommonException( "参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        dataListService.deleteDataListByDataList( dataList, DateUtil.parse( modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS ) );
        return ResponseData.success( "success" );
    }


}