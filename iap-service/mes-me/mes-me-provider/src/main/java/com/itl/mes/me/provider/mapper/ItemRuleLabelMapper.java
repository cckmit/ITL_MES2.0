package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.entity.label.LabelEntityParams;
import com.itl.mes.me.api.dto.itemRuleLabel.ItemColumns;
import com.itl.mes.me.api.dto.itemRuleLabel.ItemRuleLabelQueryDto;
import com.itl.mes.me.api.dto.itemRuleLabel.ItemRuleLabelShowDto;
import com.itl.mes.me.api.entity.ItemRuleLabel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料规则模板
 *
 * @author yx
 * @date 2021-01-21
 */
@Mapper
public interface ItemRuleLabelMapper extends BaseMapper<ItemRuleLabel> {

    /**
     * 获取物料的自定义字段名称
     * @param site
     * @return
     */
    List<String> getCustoms(@Param("site") String site);

    /**
     * 物料分页查询
     * @param page
     * @param queryDto
     * @return
     */
    IPage<ItemRuleLabelShowDto> queryList(Page page, @Param("queryDto") ItemRuleLabelQueryDto queryDto);

    /**
     * 工单分页查询
     * @param page
     * @param queryDto
     * @return
     */
    IPage<ItemRuleLabelShowDto> queryListBySO(Page page, @Param("queryDto") ItemRuleLabelQueryDto queryDto);

    /**
     * 获取物料表字段及自定义字段
     * @return
     */
    List<ItemColumns> getItemColumns(@Param("site") String site);

    /**
     * 获取工单表字段及自定义字段
     * @return
     */
    List<ItemColumns> getShopOrderColumns(@Param("site") String site);

    /**
     * 获取标签模板变量
     * @return
     */
    List<LabelEntityParams> getLabelEntityParams(@Param("id")String id);

    /**
     * 根据Bo获取规则模板明细
     * @param bo
     * @return
     */
    ItemRuleLabelShowDto getByBo(@Param("bo") String bo);


}
