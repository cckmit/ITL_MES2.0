package com.itl.mes.andon.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.andon.api.entity.Box;
import com.itl.mes.andon.api.service.BoxService;
import com.itl.mes.andon.api.vo.BoxForShowVo;
import com.itl.mes.andon.api.vo.BoxQueryVo;
import com.itl.mes.andon.api.vo.BoxVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
@RestController
@RequestMapping("/box")
@Api(tags = "安灯灯箱表")
public class BoxController {
    private final Logger logger = LoggerFactory.getLogger(BoxController.class);

    @Autowired
    private BoxService boxService;

    @GetMapping("/{id}")
    @ApiOperation(value = "通过主键查询数据")
    public ResponseData<Box> getBoxById(@PathVariable String id) {
        return ResponseData.success(boxService.getById(id));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存灯箱数据")
    public ResponseData<BoxVo> saveByBoxVo(@RequestBody BoxVo boxVo) throws CommonException {
        if (boxVo == null) {
            throw new CommonException("参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        boxService.saveByBoxVo(boxVo);
        return ResponseData.success(boxService.getBoxVoByBox(boxVo.getBox()));
    }

    @GetMapping("/query")
    @ApiOperation(value = "通过灯箱编号查询")
    @ApiImplicitParam(name = "box", value = "灯箱编号", dataType = "string", paramType = "query")
    public ResponseData<BoxVo> selectByBox(@RequestParam String box) throws CommonException {
        return ResponseData.success(boxService.getBoxVoByBox(box));
    }

    @PostMapping("/fuzzyQuery")
    @ApiOperation(value = "通过对象模糊查询")
    public ResponseData<List<Box>> selectByBoxVo(@RequestBody BoxVo boxVo) throws CommonException {
        return ResponseData.success(boxService.selectByBoxVo(boxVo));
    }

    @GetMapping("/mFuzzyQuery")
    @ApiOperation(value = "多条件模糊查询")
    public ResponseData<List<Box>> selectBox(String box, String boxName, String boxDesc) throws CommonException {
        return ResponseData.success(boxService.selectBox(box, boxName, boxDesc));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除灯箱数据")
    public ResponseData<String> delete(@RequestBody String[] boxes) throws CommonException {
        boxService.delete(boxes);
        return ResponseData.success("success");
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询灯箱")
    @ApiOperationSupport(params = @DynamicParameters(name = "BoxRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面,默认1"),
            @DynamicParameter(name = "limit", value = "分页大小,默认100,可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性,可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式,true/false,可不填"),
            @DynamicParameter(name = "box", value = "灯箱"),
            @DynamicParameter(name = "boxName", value = "灯箱名称"),
            @DynamicParameter(name = "state", value = "状态")
    }))
    public ResponseData<IPage<BoxForShowVo>> page(@RequestBody BoxQueryVo boxQueryVo) throws CommonException {
        return ResponseData.success(boxService.queryPage(boxQueryVo));
    }

    @PostMapping("/pageByState")
    @ApiOperation(value = "分页查询可用的灯箱")
    @ApiOperationSupport(params = @DynamicParameters(name = "BoxRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面,默认1"),
            @DynamicParameter(name = "limit", value = "分页大小,默认100,可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性,可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式,true/false,可不填"),
            @DynamicParameter(name = "box", value = "灯箱"),
            @DynamicParameter(name = "boxName", value = "灯箱名称")
    }))
    public ResponseData<IPage<BoxForShowVo>> pageForUse(@RequestBody BoxQueryVo boxQueryVo) throws CommonException {
        return ResponseData.success(boxService.queryPageForUse(boxQueryVo));
    }
}
