package com.itl.iap.mes.provider.controller;


import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.dto.LovData;
import com.itl.iap.mes.api.entity.Lov;
import com.itl.iap.mes.provider.common.BaseResponse;
import com.itl.iap.mes.provider.service.impl.LovServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Lov(List of value)
 * @author 胡广
 * @version 1.0
 * @name LovController
 * @description
 * @date 2019-08-15
 */
@RestController
@RequestMapping("sys/lov")
@Api(tags = "Lov")
public class LovController extends BaseRestController {

    @Autowired
    private LovServiceImpl lovService;


    @PostMapping(value = "/query")
    @ApiOperation(value = "查询列表", notes = "根据条件查询，分页起始0，1页数据一样")
    public ResponseData queryAll(@RequestBody Lov lov,
                                 @RequestParam("page") Integer page,
                                 @RequestParam("pageSize") Integer pageSize)
    {
        return ResponseData.success(lovService.findAll(lov,page,pageSize));

//        Page<Lov> lovPage = lovService.findAll(requestParam, PageRequest.of(page,pageSize));
//        return getBuilder(PaginationResponse.class).data(lovPage).build();
    }

    @GetMapping(value = "/getByCode")
    @ApiOperation(value = "查询详情", notes = "根据Code查询单个详情")
    public BaseResponse getByCode(@RequestParam("code") String code)
    {
        return getBuilder().data(lovService.getLovData(code)).build();
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "新增", notes = "新增")
    public BaseResponse add(@RequestBody LovData lovData, HttpServletRequest httpServletRequest)
    {
        return getBuilder().data(lovService.create(lovData)).build();
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "更新", notes = "更新")
    public BaseResponse save(@RequestBody LovData lovData, HttpServletRequest httpServletRequest)
    {
        return getBuilder().data(lovService.update(lovData)).build();
    }

    @PostMapping(value = "/remove/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public BaseResponse remove(@PathVariable("id") String id)
    {
        lovService.remove(id);
        return getBuilder().build();
    }


    /*
    @GetMapping(value = "/data")
    @ApiOperation(value = "删除", notes = "删除")
    @Deprecated
    public BaseResponse getLovData(@RequestParam("code") String code)
    {
        return getBuilder().data(lovService.getLovData(code)).build();
    }*/

    /**
     * 展示Lov的数据
     * @param lovData
     * @param code
     * @param page
     * @param pageSize
     * @return
     */
    @PostMapping(value = "/display")
    public BaseResponse getPreviewData(@RequestBody LovData lovData,
                                       @RequestParam("code") String code,
                                       @RequestParam("page") Integer page,
                                       @RequestParam("pageSize") Integer pageSize)
    {
        return getBuilder().data(lovService.showLov(code,lovData.getSearchParams(),page,pageSize)).build();
    }

    /**
     * 预览
     * @param lovData
     * @return
     */
    @PostMapping(value = "/preview")
    public BaseResponse getPreviewResultSet(@RequestBody LovData lovData)
    {
        List<Map<String,Object>> searchResult = lovService.getResultSet(lovData.getSqlStatement(),lovData.getSearchParams());
        com.github.pagehelper.Page<Map<String,Object>> pageResult = (com.github.pagehelper.Page<Map<String, Object>>) searchResult;
        lovData.setTotal(pageResult.getTotal());
        lovData.setSearchResult(searchResult);
        return getBuilder().data(lovData).build();
    }


    /**
     * 预览（使用SQL语句查询数据集)
     * @param lovData
     * @return
     */
    @PostMapping(value = "/preview/sql")
    public BaseResponse getPreviewSQLResultSet(@RequestBody LovData lovData)
    {
        List<Map<String,Object>> searchResult = lovService.getResultSet(lovData.getSqlStatement(),lovData.getSearchParams());
        com.github.pagehelper.Page<Map<String,Object>> pageResult = (com.github.pagehelper.Page<Map<String, Object>>) searchResult;
        lovData.setTotal(pageResult.getTotal());
        lovData.setSearchResult(searchResult);
        return getBuilder().data(lovData).build();
    }

    /**
     * 预览(从Service里面获取数据集)
     * @param lovData
     * @return
     */
    @PostMapping(value = "/preview/service")
    public BaseResponse getPreviewServiceResultSet(@RequestBody LovData lovData)
    {
        return getBuilder().data(lovService.getServiceData(lovData.getServiceExpression(),lovData.getSearchParams(),lovData.getPage().intValue(),lovData.getPageSize().intValue())).build();
    }



    /**
     * 批量删行（及联）
     * @param ids
     * @return
     */
    @PostMapping(value = "/batchRemoveEntry")
    public BaseResponse batchRemoveEntry(@RequestBody List<Long> ids)
    {
        lovService.removeEntries(ids);
        return getBuilder().build();
    }


    /**
     * 克隆Lov
     * @param source
     * @param target
     * @return
     */
    @PostMapping(value = "/clone")
    public BaseResponse batchRemoveEntry(@RequestParam("source") String source,@RequestParam("target") String target)
    {
        return getBuilder().data(lovService.clone(source,target)).build();
    }

}
