package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.entity.CodeRuleItem;
import com.itl.mes.core.api.service.CodeRuleItemService;
import com.itl.mes.core.provider.mapper.CodeRuleItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 编码规则明细表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-19
 */
@Service
@Transactional
public class CodeRuleItemServiceImpl extends ServiceImpl<CodeRuleItemMapper, CodeRuleItem> implements CodeRuleItemService {


    @Autowired
    private CodeRuleItemMapper codeRuleItemMapper;

    @Override
    public List<CodeRuleItem> selectList() {
        QueryWrapper<CodeRuleItem> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, codeRuleItem);
        return super.list(entityWrapper);
    }

    /**
     * 获取编码明细数据
     *
     * @param codeRuleBo codeRuleBo
     * @return List<CodeRuleItem>
     */
    @Override
    public List<CodeRuleItem> selectCodeRuleItemListByCodeRuleBo(String codeRuleBo){

        QueryWrapper<CodeRuleItem> wrapper = new QueryWrapper<>();
        wrapper.eq( CodeRuleItem.CODE_RULE_BO, codeRuleBo ).orderByAsc( CodeRuleItem.SEQ );
        return codeRuleItemMapper.selectList( wrapper );
    }

    /**
     * 通过codeRuleBo查询明细数据
     *
     * @param codeRuleBo codeRuleBo
     * @return List<CodeRuleItem>
     */
    @Override
    public List<CodeRuleItem> selectForUpdateCodeRuleList(String codeRuleBo){

        return codeRuleItemMapper.selectForUpdateCodeRuleList( codeRuleBo );
    }

}