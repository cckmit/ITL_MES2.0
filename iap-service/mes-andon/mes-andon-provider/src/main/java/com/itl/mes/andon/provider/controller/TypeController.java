package com.itl.mes.andon.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.andon.api.dto.TypeDTO;
import com.itl.mes.andon.api.entity.Type;
import com.itl.mes.andon.api.service.TypeService;
import com.itl.mes.andon.api.vo.TypeVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author yaoxiang
 * @date 2020/12/15
 * @since JDK1.8
 */
@RestController
@RequestMapping("/type")
@Api(tags = "安灯类型表")
public class TypeController {
    private final Logger logger = LoggerFactory.getLogger(TypeController.class);

    @Autowired
    private TypeService typeService;

    @GetMapping("/{id}")
    @ApiOperation(value = "通过主键查询数据")
    public ResponseData<Type> getTypeById(@PathVariable String id) {
        return ResponseData.success(typeService.getById(id));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存安灯类型数据")
    public ResponseData<TypeVo> saveByTypeVo(@RequestBody TypeVo typeVo) throws CommonException {
        if (typeVo == null) {
            throw new CommonException("参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        typeService.saveByTypeVo(typeVo);
        return ResponseData.success(typeService.getTypeVoByType(typeVo.getAndonType()));
    }

    @GetMapping("/query")
    @ApiOperation(value = "通过安灯类型编号查询")
    @ApiImplicitParam(name = "type", value = "安灯类型编号", dataType = "string", paramType = "query")
    public ResponseData<TypeVo> selectByType(@RequestParam String type) throws CommonException {
        return ResponseData.success(typeService.getTypeVoByType(type));
    }

    @PostMapping("/fuzzyQuery")
    @ApiOperation(value = "通过对象模糊查询")
    public ResponseData<List<Type>> selectByTypeVo(@RequestBody TypeVo typeVo) throws CommonException {
        return ResponseData.success(typeService.selectByTypeVo(typeVo));
    }

    @GetMapping("/mFuzzyQuery")
    @ApiOperation(value = "多条件模糊查询")
    public ResponseData<List<Type>> selectType(String type, String typeName, String typeDesc) throws CommonException {
        return ResponseData.success(typeService.selectType(type, typeName, typeDesc));
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除安灯类型数据")
    public ResponseData<String> delete(@RequestBody List<String> bos) throws CommonException {
        typeService.delete(bos);
        return ResponseData.success("success");
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询安灯类型")
    @ApiOperationSupport(params = @DynamicParameters(name = "TypeRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面,默认1"),
            @DynamicParameter(name = "limit", value = "分页大小,默认100,可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性,可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式,true/false,可不填"),
            @DynamicParameter(name = "type", value = "安灯类型"),
            @DynamicParameter(name = "typeName", value = "安灯类型名称"),
            @DynamicParameter(name = "typeDesc", value = "安灯类型描述")
    }))
    public ResponseData<IPage<Type>> page(@RequestBody TypeDTO typeDTO) throws CommonException {
        return ResponseData.success(typeService.queryPage(typeDTO));
    }

    @PostMapping("/pageForUse")
    @ApiOperation(value = "分页查询可用的安灯类型")
    @ApiOperationSupport(params = @DynamicParameters(name = "TypeRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面,默认1"),
            @DynamicParameter(name = "limit", value = "分页大小,默认100,可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性,可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式,true/false,可不填"),
            @DynamicParameter(name = "type", value = "安灯类型"),
            @DynamicParameter(name = "typeName", value = "安灯类型名称"),
            @DynamicParameter(name = "typeDesc", value = "安灯类型描述")
    }))
    public ResponseData<IPage<Type>> pageForUse(@RequestBody TypeDTO typeDTO) throws CommonException {
        return ResponseData.success(typeService.queryPageForUse(typeDTO));
    }
}
