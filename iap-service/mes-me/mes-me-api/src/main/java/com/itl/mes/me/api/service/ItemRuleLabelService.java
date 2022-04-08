package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.mes.api.entity.label.LabelEntityParams;
import com.itl.mes.me.api.dto.itemRuleLabel.ItemColumns;
import com.itl.mes.me.api.dto.itemRuleLabel.ItemRuleLabelQueryDto;
import com.itl.mes.me.api.dto.itemRuleLabel.ItemRuleLabelSaveDto;
import com.itl.mes.me.api.dto.itemRuleLabel.ItemRuleLabelShowDto;
import com.itl.mes.me.api.entity.ItemRuleLabel;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 物料规则模板
 *
 * @author yx
 * @date 2021-01-21
 */
public interface ItemRuleLabelService extends IService<ItemRuleLabel> {

    /**
     * 保存
     * @param saveDto
     * @throws CommonException
     */
    void saveAndUpdate(ItemRuleLabelSaveDto saveDto) throws CommonException;

    /**
     * 物料分页查询
     * @param queryDto
     * @return
     * @throws CommonException
     */
    IPage<ItemRuleLabelShowDto> queryPage(ItemRuleLabelQueryDto queryDto) throws CommonException;

    /**
     * 工单分页查询
     * @param queryDto
     * @return
     * @throws CommonException
     */
    IPage<ItemRuleLabelShowDto> queryListBySO(ItemRuleLabelQueryDto queryDto) throws CommonException;

    /**
     * 删除
     * @param bos
     * @throws CommonException
     */
    void delete(String[] bos) throws CommonException;

    /**
     * 获取物料表字段及自定义字段
     * @return
     */
    List<ItemColumns> getItemColumns();

    /**
     * 获取工单表字段及自定义字段
     * @return
     */
    List<ItemColumns> getShopOrderColumns();

    /**
     * 获取标签模板变量
     * @return
     */
    List<LabelEntityParams> getLabelEntityParams(String id);

    /**
     * 生成该规则对应编码
     * @param bo
     * @param number
     * @return
     */
    List<String> generatorCode(String bo, Integer number);

    /**
     * 根据Bo获取规则模板明细
     * @param bo
     * @return
     */
    ItemRuleLabelShowDto getByBo(String bo);
}

