package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.mes.core.api.entity.SnRule;
import com.itl.mes.core.api.service.SnRuleService;
import com.itl.mes.core.provider.mapper.SnRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 条码规则表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-08-03
 */
@Service
@Transactional
public class SnRuleServiceImpl extends ServiceImpl<SnRuleMapper, SnRule> implements SnRuleService {


    @Autowired
    private SnRuleMapper snRuleMapper;

    @Override
    public List<SnRule> selectList() {
        QueryWrapper<SnRule> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, snRule);
        return super.list(entityWrapper);
    }

    //保存编码规则
    @Override
    public void saveSnRule(SnRule snRule) throws CommonException {
        //补码结尾在表中只能有一项，不能重复；
        QueryWrapper<SnRule> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(SnRule.RULE_TYPE, "C");
        List<SnRule> snRules = super.list(entityWrapper);
        if (snRules.size() == 0) {
            //每种规则种类中的名称不能重复
            verifySnRuleRepeat(snRule);
            super.save(snRule);
        }else {
            if ("C".equals(snRule.getRuleType())){
                throw new CommonException( "补码结尾只能有一项，不能重复!" , CommonExceptionDefinition.SN_ENCODING_EXCEPTION);
            }
            //每种规则种类中的名称不能重复
            verifySnRuleRepeat(snRule);
            super.save(snRule);
        }

    }

    //编辑
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void updateSnRule(SnRule snRule) throws CommonException {
        //补码结尾在表中只能有一项，不能重复；
/*        EntityWrapper<SnRule> entityWrapper = new EntityWrapper<SnRule>();
        entityWrapper.eq(SnRule.RULE_TYPE, "C");
        List<SnRule> snRules = super.selectList(entityWrapper);
        if (snRules.size() == 0) {
            //每种规则种类中的名称不能重复
            verifySnRuleRepeat(snRule);
            super.updateById(snRule);
        }else {
            if ("C".equals(snRule.getRuleType())){
                throw new BusinessException( "补码结尾只能有一项，不能重复!" );
            }
            //每种规则种类中的名称不能重复
            verifySnRuleRepeat(snRule);
            super.updateById(snRule);
        }*/

        super.removeById(snRule);

        saveSnRule(snRule);
    }

    //删除
    @Override
    public void deleteSnRule(List<SnRule> snRule) throws CommonException {
        if (snRule.size() == 0){
            throw new CommonException( "请选择数据进行删除！", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        for (SnRule rule : snRule) {
            super.removeById(rule);
        }
    }


    //每种规则种类中的名称不能重复
    private SnRule verifySnRuleRepeat(SnRule snRule) throws CommonException {

        QueryWrapper<SnRule> entityWrapper2 = new QueryWrapper<>();
        entityWrapper2.eq(SnRule.RULE_TYPE, snRule.getRuleType());
        List<SnRule> snRuleList = super.list(entityWrapper2);
        for (SnRule rule : snRuleList) {
            if (rule.getRuleName().equals(snRule.getRuleName())) {
                throw new CommonException( "每种规则种类中的名称不能重复!", CommonExceptionDefinition.SN_ENCODING_EXCEPTION );
            }
        }
        return snRule;
    }

}