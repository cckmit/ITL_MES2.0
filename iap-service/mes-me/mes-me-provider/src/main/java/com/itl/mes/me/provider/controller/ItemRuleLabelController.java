package com.itl.mes.me.provider.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.entity.label.LabelEntity;
import com.itl.iap.mes.api.entity.label.LabelEntityParams;
import com.itl.mes.me.api.dto.itemRuleLabel.ItemColumns;
import com.itl.mes.me.api.dto.itemRuleLabel.ItemRuleLabelQueryDto;
import com.itl.mes.me.api.dto.itemRuleLabel.ItemRuleLabelSaveDto;
import com.itl.mes.me.api.dto.itemRuleLabel.ItemRuleLabelShowDto;
import com.itl.mes.me.api.service.ItemRuleLabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2021/1/21
 * @since JDK1.8
 */
@Api(tags = "物料规则模板")
@RestController
@RequestMapping("/itemRuleLabel")
public class ItemRuleLabelController {
    @Autowired
    private ItemRuleLabelService itemRuleLabelService;

    @PostMapping("/save")
    @ApiOperation("保存")
    public ResponseData<String> save(@RequestBody ItemRuleLabelSaveDto saveDto) throws CommonException {
        if (ObjectUtil.isNull(saveDto)) {
            throw new CommonException("参数不能为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        itemRuleLabelService.saveAndUpdate(saveDto);
        return ResponseData.success("success");
    }

    @PostMapping("/page")
    @ApiOperation("物料分页查询")
    public ResponseData<IPage<ItemRuleLabelShowDto>> page(@RequestBody ItemRuleLabelQueryDto queryDto) throws CommonException {
        return ResponseData.success(itemRuleLabelService.queryPage(queryDto));
    }
    @PostMapping("/pageBySO")
    @ApiOperation("工单分页查询")
    public ResponseData<IPage<ItemRuleLabelShowDto>> pageBySO(@RequestBody ItemRuleLabelQueryDto queryDto) throws CommonException {
        return ResponseData.success(itemRuleLabelService.queryListBySO(queryDto));
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除")
    public ResponseData<String> delete(@RequestBody String[] bos) throws CommonException {
        if (ArrayUtil.isEmpty(bos)) {
            throw new CommonException("参数为空!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        itemRuleLabelService.delete(bos);
        return ResponseData.success("success");
    }

    @GetMapping("/getItemColumns")
    @ApiOperation("获取物料表字段及自定义字段")
    public ResponseData<List<ItemColumns>> getItemColumns() {
        return ResponseData.success(itemRuleLabelService.getItemColumns());
    }

    @GetMapping("/getShopOrderColumns")
    @ApiOperation("获取工单表字段及自定义字段")
    public ResponseData<List<ItemColumns>> getShopOrderColumns() {
        return ResponseData.success(itemRuleLabelService.getShopOrderColumns());
    }

    @GetMapping("/getLabelEntityParams/{id}")
    @ApiOperation("获取标签模板变量")
    public ResponseData<List<LabelEntityParams>> getLabelEntityParams(@PathVariable("id")String id) {
        return ResponseData.success(itemRuleLabelService.getLabelEntityParams(id));
    }

    @GetMapping("/generatorCode/{bo}/{number}")
    @ApiOperation("生成该规则对应编码number条")
    public ResponseData<List<String>> generatorCode(@PathVariable("bo") String bo, @PathVariable("number") Integer number) throws CommonException {
        return ResponseData.success(itemRuleLabelService.generatorCode(bo, number));
    }

    @GetMapping("/get/{bo}")
    @ApiOperation("查询明细(for 编辑)")
    public ResponseData<ItemRuleLabelShowDto> getByBo(@PathVariable("bo") String bo) {
        return ResponseData.success(itemRuleLabelService.getByBo(bo));
    }
}
