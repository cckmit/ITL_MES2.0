package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.bo.CodeRuleHandleBO;
import com.itl.mes.core.api.entity.CodeRule;
import com.itl.mes.core.api.vo.CodeRuleFullVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 编码规则表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-19
 */
public interface CodeRuleService extends IService<CodeRule> {

    List<CodeRule> selectList();

    /**
     * 通过codeRuleHandleBO查询规则主表数据
     *
     * @param codeRuleHandleBO codeRuleHandleBO
     * @return CodeRule
     */
    CodeRule getCodeRule(CodeRuleHandleBO codeRuleHandleBO);

    /**
     * 通过codeRuleHandleBO查询规则主表数据，不存在则报错
     *
     * @param codeRuleHandleBO codeRuleHandleBO
     * @return CodeRule
     */
    CodeRule getExistCodeRule(CodeRuleHandleBO codeRuleHandleBO) throws CommonException;

    /**
     * 获取编号规则数据
     *
     * @param codeRuleType 编号类型，SN：车间作业控制，SO：工单，PK：包装 OTHER：其他
     * @return CodeRuleFullVo
     * @throws CommonException 异常
     */
    CodeRuleFullVo getCodeRuleType(String codeRuleType)throws CommonException;

    /**
     * 保存编号规则数据
     *
     * @param codeRuleFullVo codeRuleFullVo
     * @throws CommonException 异常
     */
    void saveCodeRule(CodeRuleFullVo codeRuleFullVo)throws CommonException;

    /**
     * 删除编号规则类型数据
     *
     * @param codeRuleType 编码规则类型，SN：车间作业控制，SO：工单，PK：包装，OTHER：其它
     * @param modifyDate 修改时间
     * @throws CommonException 异常
     */
    void deleteCodeRuleTypeData(String codeRuleType, Date modifyDate) throws CommonException;

    /**
     * 获取多个下一编码
     *
     * @param codeRuleBo codeRuleBo
     * @param number 生成编码的个数 必须是正整数
     * @return List<String>
     * @throws CommonException 异常
     */
    List<String> generatorNextNumberList(String codeRuleBo, int number) throws CommonException;

    /**
     * 获取多个下一编码
     *
     * @param codeRuleBo codeRuleBo
     * @param number 生成编码的个数 必须是正整数
     * @param paramMap 变量替换
     * @return List<String>
     * @throws CommonException 异常
     */
    List<String> generatorNextNumberList(String codeRuleBo, int number, Map<String, Object> paramMap) throws CommonException;

    /**
     * 获取一个下一编码
     *
     * @param codeRuleBo codeRuleBo
     * @return List<String>
     * @throws CommonException 异常
     */
    String generatorNextNumber(String codeRuleBo) throws CommonException;

    /**
     * 获取一个下一编码
     *
     * @param codeRuleBo 编码规则BO
     * @param paramMap 替换参数
     * @return String
     * @throws CommonException 异常
     */
    String generatorNextNumber(String codeRuleBo, Map<String, Object> paramMap) throws CommonException;

    /**
     * 下一编号示例
     *
     * @param codeRuleBo codeRuleBo
     * @return String
     * @throws CommonException 异常
     */
    String getNextNumberExample(String codeRuleBo) throws CommonException;
}
