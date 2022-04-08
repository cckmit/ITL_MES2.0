package com.itl.mes.me.provider.controller;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.dto.InstructorItemDto;
import com.itl.mes.me.api.dto.ItemWithTemplateDto;
import com.itl.mes.me.api.entity.InstructorItem;
import com.itl.mes.me.api.service.InstructorItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2020/12/25
 * @since JDK1.8
 */
@RestController
@RequestMapping("/instructorItem")
@Api(tags = "作业指导书内容")
public class InstructorItemController {

    @Autowired
    private InstructorItemService itemService;

    @PostMapping("/save")
    @ApiOperation(value = "保存")
    public ResponseData<String> save(@RequestBody InstructorItemDto instructorItemDto) throws CommonException {
        if (instructorItemDto == null) {
            throw new CommonException("参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        itemService.saveAndUpdate(instructorItemDto);
        return ResponseData.success("success");
    }

    @PostMapping("/list/{instructorBo}")
    @ApiOperation(value = "查询列表,不带模板")
    public ResponseData<List<InstructorItem>> list(@PathVariable("instructorBo") String instructorBo) throws CommonException {
        return ResponseData.success(itemService.listAll(instructorBo));
    }

    @PostMapping("/listWithTemplate/{instructorBo}")
    @ApiOperation(value = "查询列表,带模板(指导书预览)")
    public ResponseData<List<ItemWithTemplateDto>> listWithTemplate(@PathVariable("instructorBo") String instructorBo) throws CommonException {
        return ResponseData.success(itemService.listWithTemplate(instructorBo));
    }


    @DeleteMapping("/delete/{itemBo}")
    @ApiOperation(value = "删除,path传参")
    public ResponseData<String> delete(@PathVariable("itemBo") String itemBo) throws CommonException {
        itemService.delete(itemBo);
        return ResponseData.success("success");
    }
}
