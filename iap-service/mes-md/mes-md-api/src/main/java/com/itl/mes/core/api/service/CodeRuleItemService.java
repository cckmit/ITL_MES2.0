package com.itl.mes.core.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.entity.CodeRuleItem;

import java.util.List;

/**
 * <p>
 * 编码规则明细表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-19
 */
public interface CodeRuleItemService extends IService<CodeRuleItem> {

    List<CodeRuleItem> selectList();

    /**
     * 获取编码明细数据
     *
     * @param codeRuleBo codeRuleBo
     * @return List<CodeRuleItem>
     */
    List<CodeRuleItem> selectCodeRuleItemListByCodeRuleBo(String codeRuleBo);

    /**
     * 通过codeRuleBo查询明细数据
     *
     * @param codeRuleBo codeRuleBo
     * @return List<CodeRuleItem>
     */
    List<CodeRuleItem> selectForUpdateCodeRuleList(String codeRuleBo);
}