package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.*;
import com.itl.mes.core.api.entity.CodeRule;
import com.itl.mes.core.api.entity.CodeRuleItem;
import com.itl.mes.core.api.service.CodeRuleItemService;
import com.itl.mes.core.api.service.CodeRuleService;
import com.itl.mes.core.api.vo.CodeRuleFullVo;
import com.itl.mes.core.api.vo.CodeRuleItemVo;
import com.itl.mes.core.provider.mapper.CodeRuleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 编码规则表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-19
 */
@Service
@Transactional
@Slf4j
public class CodeRuleServiceImpl extends ServiceImpl<CodeRuleMapper, CodeRule> implements CodeRuleService {


    @Autowired
    private CodeRuleMapper codeRuleMapper;

    @Autowired
    private CodeRuleItemService codeRuleItemService;

    @Override
    public List<CodeRule> selectList() {
        QueryWrapper<CodeRule> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, codeRule);
        return super.list(entityWrapper);
    }


    /**
     * 通过codeRuleHandleBO查询规则主表数据
     *
     * @param codeRuleHandleBO codeRuleHandleBO
     * @return CodeRule
     */
    @Override
    public CodeRule getCodeRule(CodeRuleHandleBO codeRuleHandleBO) {
        return codeRuleMapper.selectById(codeRuleHandleBO.getBo());
    }

    /**
     * 通过codeRuleHandleBO查询规则主表数据，不存在则报错
     *
     * @param codeRuleHandleBO codeRuleHandleBO
     * @return CodeRule
     */
    @Override
    public CodeRule getExistCodeRule(CodeRuleHandleBO codeRuleHandleBO) throws CommonException {

        CodeRule codeRule = getCodeRule(codeRuleHandleBO);
        if (codeRule == null) {
            throw new CommonException("编码规则未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return codeRule;
    }

    /**
     * 获取编号规则数据
     *
     * @param codeRuleType 编号类型，SN：车间作业控制，SO：工单，PK：包装
     * @return CodeRuleFullVo
     * @throws CommonException 异常
     */
    @Override
    public CodeRuleFullVo getCodeRuleType(String codeRuleType) throws CommonException {

        String site = UserUtils.getSite();
        CodeRuleFullVo codeRuleFullVo = null;
        CodeRuleHandleBO codeRuleHandleBO = new CodeRuleHandleBO(site, codeRuleType);
        codeRuleFullVo = getCodeRuleTypeDataByCodeRuleHandleBO(codeRuleHandleBO);
        return codeRuleFullVo;

    }

    /**
     * 通过codeRuleHandleBO查询规则数据
     *
     * @param codeRuleHandleBO codeRuleHandleBO
     * @return CodeRuleFullVo
     * @throws CommonException 异常
     */
    private CodeRuleFullVo getCodeRuleTypeDataByCodeRuleHandleBO(CodeRuleHandleBO codeRuleHandleBO) throws CommonException {

        CodeRule codeRule = getExistCodeRule(codeRuleHandleBO);
        CodeRuleFullVo codeRuleFullVo = new CodeRuleFullVo();
        codeRuleFullVo.setCodeRuleType(codeRule.getCodeRuleType());
        codeRuleFullVo.setCodeRuleDesc(codeRule.getCodeRuleDesc());
        codeRuleFullVo.setModifyDate(codeRule.getModifyDate());

        //查询主表数据
        QueryWrapper<CodeRuleItem> codeRuleItemWrapper = new QueryWrapper<>();
        codeRuleItemWrapper.eq(CodeRuleItem.CODE_RULE_BO, codeRule.getBo()).orderByAsc(CodeRuleItem.SEQ);
        //查询明细表数据
        List<CodeRuleItem> codeRuleItemList = codeRuleItemService.list(codeRuleItemWrapper);

        //组装明细数据
        List<CodeRuleItemVo> codeRuleItemVoList = new ArrayList<>();
        CodeRuleItemVo codeRuleItemVo = null;
        if (codeRuleItemList.size() > 0) {
            for (CodeRuleItem codeRuleItem : codeRuleItemList) {
                codeRuleItemVo = new CodeRuleItemVo();
                BeanUtils.copyProperties(codeRuleItem, codeRuleItemVo);
                if ("3".equals(codeRuleItem.getSectType())) {
                    //当前序列值转换为10进制对应的数
                    codeRuleItemVo.setCurrentSequence(CommonUtil.hex10ToAny(codeRuleItem.getCurrentSequence().longValue(),
                            codeRuleItem.getBase()));
                    if (codeRuleItem.getMinSequence() != null) {
                        codeRuleItemVo.setMinSequence(CommonUtil.hex10ToAny(codeRuleItem.getMinSequence().longValue(),
                                codeRuleItem.getBase()));
                    }
                    if (codeRuleItem.getMaxSequence() != null) {
                        codeRuleItemVo.setMaxSequence(CommonUtil.hex10ToAny(codeRuleItem.getMaxSequence().longValue(),
                                codeRuleItem.getBase()));
                    }
                    if (codeRuleItem.getIncr() != null) {
                        codeRuleItemVo.setIncr(CommonUtil.hex10ToAny(codeRuleItem.getIncr().longValue(),
                                codeRuleItem.getBase()));
                    }
                }
                codeRuleItemVoList.add(codeRuleItemVo);
            }
            //设置编码示例
            codeRuleFullVo.setCodeExample(getNextNumberExample(codeRuleItemList));
        }
        codeRuleFullVo.setCodeRuleItemVoList(codeRuleItemVoList);
        return codeRuleFullVo;

    }

    /**
     * 保存编号规则数据
     *
     * @param codeRuleFullVo codeRuleFullVo
     * @throws CommonException 异常
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveCodeRule(CodeRuleFullVo codeRuleFullVo) throws CommonException {
        String site = UserUtils.getSite();
        CodeRule codeRule = null;
        CodeRuleHandleBO codeRuleHandleBO = new CodeRuleHandleBO(site, codeRuleFullVo.getCodeRuleType());
        codeRule = codeRuleMapper.selectById(codeRuleHandleBO.getBo());
        if (codeRule == null) { //新增
            Date createDate = new Date();
            //组装主表数据
            codeRule = new CodeRule();
            codeRule.setBo(codeRuleHandleBO.getBo());
            codeRule.setSite(site);
            codeRule.setCodeRuleType(codeRuleFullVo.getCodeRuleType());
            codeRule.setCodeRuleDesc(codeRuleFullVo.getCodeRuleDesc());
            codeRule.setObjectSetBasicAttribute(UserUtils.getCurrentUser().getUserName(), createDate);
            //保存数据
            insertCodeRuleData(codeRule, codeRuleFullVo.getCodeRuleItemVoList());
        } else { //更新
            CommonUtil.compareDateSame(codeRuleFullVo.getModifyDate(), codeRule.getModifyDate());
            //组装主表新数据
            CodeRule codeRuleEntity = new CodeRule();
            codeRuleEntity.setCodeRuleDesc(codeRuleFullVo.getCodeRuleDesc() == null ? "" : codeRuleFullVo.getCodeRuleDesc());
            codeRuleEntity.setModifyDate(new Date());
            codeRuleEntity.setModifyUser(UserUtils.getCurrentUser().getUserName());
            updateCodeRuleData(codeRule, codeRuleEntity, codeRuleFullVo.getCodeRuleItemVoList());
        }
    }


    /**
     * 新增编码规则数据
     *
     * @param codeRule           编码对象
     * @param codeRuleItemVoList codeRuleItemVoList
     * @throws CommonException 异常
     */
    private void insertCodeRuleData(CodeRule codeRule, List<CodeRuleItemVo> codeRuleItemVoList) throws
            CommonException {
        //保存主表
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(codeRule);
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        codeRule.setCreateUser(UserUtils.getCurrentUser().getUserName());
        codeRuleMapper.insert(codeRule);

        //保存明细表数据
        insertCodeRuleItemData(codeRule.getSite(), codeRule.getBo(), codeRule.getCreateDate(), codeRuleItemVoList);


    }

    /**
     * 更新编码规则数据
     *
     * @param odlCodeRule        数据库存在的编码对象
     * @param newCodeRule        修改编码对象
     * @param codeRuleItemVoList 编码数据明细
     * @throws CommonException 异常
     */
    private void updateCodeRuleData(CodeRule odlCodeRule, CodeRule
            newCodeRule, List<CodeRuleItemVo> codeRuleItemVoList) throws CommonException {

        //更新主表
        ValidationUtil.ValidResult validResult = ValidationUtil.validateProperty(newCodeRule, "codeRuleDesc");
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        QueryWrapper<CodeRule> codeRuleWrapper = new QueryWrapper<>();
        codeRuleWrapper.eq(CodeRule.BO, odlCodeRule.getBo()).eq(CodeRule.MODIFY_DATE, odlCodeRule.getModifyDate());
        Integer integer = codeRuleMapper.update(newCodeRule, codeRuleWrapper);
        if (integer == 0) {
            throw new CommonException("数据已修改，请查询后再执行操作", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        //删除主表对应的明细表数据然后再保存
        QueryWrapper<CodeRuleItem> wrapper = new QueryWrapper<>();
        wrapper.eq(CodeRuleItem.CODE_RULE_BO, odlCodeRule.getBo());
        codeRuleItemService.remove(wrapper);

        //保存明细表数据
        insertCodeRuleItemData(odlCodeRule.getSite(), odlCodeRule.getBo(), newCodeRule.getModifyDate(), codeRuleItemVoList);

    }

    /**
     * 新增明细表数据
     *
     * @param site               工厂
     * @param codeRuleBo         编码规则主表BO
     * @param createDate         创建日期
     * @param codeRuleItemVoList 编码数据明细
     * @throws CommonException 异常
     */
    private void insertCodeRuleItemData(String site, String codeRuleBo, Date
            createDate, List<CodeRuleItemVo> codeRuleItemVoList)
            throws CommonException {

        ValidationUtil.ValidResult validResult = null;
        //保存明细表
        List<CodeRuleItem> codeRuleItemList = new ArrayList<>();
        CodeRuleItem codeRuleItem = null;
        CodeRuleItemHandleBO codeRuleItemHandleBO = null;
        for (CodeRuleItemVo codeRuleItemVo : codeRuleItemVoList) {
            codeRuleItemHandleBO = new CodeRuleItemHandleBO(site, codeRuleBo, codeRuleItemVo.getSeq().toString());
            codeRuleItem = new CodeRuleItem();
            codeRuleItem.setBo(codeRuleItemHandleBO.getBo());
            codeRuleItem.setSite(site);
            codeRuleItem.setCodeRuleBo(codeRuleBo);
            codeRuleItem.setSeq(codeRuleItemVo.getSeq());
            codeRuleItem.setSectType(codeRuleItemVo.getSectType());
            codeRuleItem.setSectParam(codeRuleItemVo.getSectParam());
            codeRuleItem.setBase(codeRuleItemVo.getBase());
            codeRuleItem.setLenSequence(codeRuleItemVo.getLenSequence());
            codeRuleItem.setModifyDate(createDate);
            codeRuleItem.setReset(codeRuleItemVo.getReset());
            if ("3".equals(StrUtil.trim(codeRuleItemVo.getSectType()))) { //代表是计数

                if (StrUtil.isBlank(codeRuleItemVo.getCurrentSequence())) {
                    throw new CommonException("当前序列不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                if (codeRuleItem.getBase() == null) {
                    throw new CommonException("进制不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                if (codeRuleItem.getBase() < 2 || codeRuleItem.getBase() > 36) {
                    throw new CommonException("进制必须在2到36之间取值", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                //当前序列值转换为10进制对应的数
                codeRuleItem.setCurrentSequence(new BigDecimal(Long.valueOf(codeRuleItemVo.getCurrentSequence(),
                        codeRuleItem.getBase())));
                if (!StrUtil.isBlank(codeRuleItemVo.getMinSequence())) {
                    codeRuleItem.setMinSequence(new BigDecimal(Long.valueOf(codeRuleItemVo.getMinSequence(),
                            codeRuleItem.getBase())));
                    if (codeRuleItem.getCurrentSequence().compareTo(codeRuleItem.getMinSequence()) < 0) {
                        throw new CommonException("当前序列不能小于最小序列", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                }
                if (!StrUtil.isBlank(codeRuleItemVo.getMaxSequence())) {
                    codeRuleItem.setMaxSequence(new BigDecimal(Long.valueOf(codeRuleItemVo.getMaxSequence(),
                            codeRuleItem.getBase())));
                    if (codeRuleItem.getMaxSequence().compareTo(codeRuleItem.getMinSequence()) < 0) {
                        throw new CommonException("最大序列不能小于当前序列", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                }
                if (!StrUtil.isBlank(codeRuleItemVo.getIncr())) {
                    codeRuleItem.setIncr(Integer.valueOf(codeRuleItemVo.getIncr(),
                            codeRuleItem.getBase()));
                }

            } else { //代表是 固定、时间、变量
                if (StrUtil.isBlank(codeRuleItem.getSectParam())) {
                    throw new CommonException("段参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                if ("2".equals(StrUtil.trim(codeRuleItemVo.getSectType()))) { //日期
                    try {
                        DateUtil.format(new Date(), codeRuleItem.getSectParam());
                    } catch (Exception e) {
                        throw new CommonException("日期类型段参数格式不正确", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                }
                if ("4".equals(StrUtil.trim(codeRuleItemVo.getSectType()))) {
                    Pattern pattern = Pattern.compile("\\{\\S+\\}");
                    Matcher matcher = pattern.matcher(codeRuleItem.getSectParam());
                    if (!matcher.matches()) {
                        throw new CommonException("变量格式维护有误", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                }
            }
            validResult = ValidationUtil.validateBean(codeRuleItem);
            if (validResult.hasErrors()) {
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            codeRuleItemList.add(codeRuleItem);
        }
        if (codeRuleItemList.size() > 0) {
            codeRuleItemService.saveBatch(codeRuleItemList);
        }
    }


    /**
     * 删除编号规则类型数据
     *
     * @param codeRuleType 编码规则类型，SN：车间作业控制，SO：工单，PK：包装，OTHER：其它
     * @param modifyDate   修改时间
     * @throws CommonException 异常
     */
    @Override
    public void deleteCodeRuleTypeData(String codeRuleType, Date modifyDate) throws CommonException {
        String site = UserUtils.getSite();
        CodeRuleHandleBO codeRuleHandleBO = new CodeRuleHandleBO(site, codeRuleType);
        deleteCodeRuleTypeDataByHandleBO(codeRuleHandleBO, modifyDate); //删除编码规则类型数据
    }

    /**
     * 通过codeRuleHandleBO删除编码规则类型数据
     *
     * @param codeRuleHandleBO codeRuleHandleBO
     * @param modifyDate       修改时间
     * @throws CommonException 异常
     */
    private void deleteCodeRuleTypeDataByHandleBO(CodeRuleHandleBO codeRuleHandleBO, Date modifyDate) throws
            CommonException {

        CodeRule codeRule = getExistCodeRule(codeRuleHandleBO);
        CommonUtil.compareDateSame(modifyDate, codeRule.getModifyDate());
        QueryWrapper<CodeRule> wrapper = new QueryWrapper<>();
        wrapper.eq(CodeRule.BO, codeRule.getBo()).eq(CodeRule.MODIFY_DATE, modifyDate);

        //删除主表数据
        Integer integer = codeRuleMapper.delete(wrapper);
        if (integer == 0) {
            throw new CommonException("数据已修改，请查询后再执行删除操作", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        //删除明细表数据
        QueryWrapper<CodeRuleItem> codeRuleItemWrapper = new QueryWrapper<>();
        codeRuleItemWrapper.eq(CodeRuleItem.CODE_RULE_BO, codeRule.getBo());
        codeRuleItemService.remove(codeRuleItemWrapper);
    }


    /**
     * 生成多个下一编码
     *
     * @param codeRuleItemList codeRuleItemList
     * @param number           生成编码的个数 必须是正整数
     * @param paramMap         变量替换
     * @return List<String>
     * @throws CommonException 异常
     */
    private List<String> generatorNextNumberList(List<CodeRuleItem> codeRuleItemList, int number, Map<
            String, Object> paramMap) throws CommonException {

        //按seq进行升序排序
        if (codeRuleItemList.size() > 1) codeRuleItemList.sort(Comparator.comparing(CodeRuleItem::getSeq));
        //数据段类型（1固定，2日期，3计数）
        Date generatorDate = new Date(); //编码生成日期
        List<StringBuilder> nextNumberList = new ArrayList<>(number); //初始化个数长度
        for (int i = 0; i < number; i++) {
            nextNumberList.add(new StringBuilder());
        }
        for (int i = 0; i < nextNumberList.size(); i++) { //循环个数

            for (CodeRuleItem codeRuleItem : codeRuleItemList) {
                if ("1".equals(codeRuleItem.getSectType())) {//1固定
                    nextNumberList.get(i).append(codeRuleItem.getSectParam());
                } else if ("2".equals(codeRuleItem.getSectType())) {//2日期
                    if (codeRuleItem.getSectParam() != null) {
                        try {
                            nextNumberList.get(i).append(DateUtil.format(generatorDate, codeRuleItem.getSectParam()));
                        } catch (Exception e) {
                            throw new CommonException("日期格式" + codeRuleItem.getSectParam() + "转换异常", CommonExceptionDefinition.VERIFY_EXCEPTION);
                        }
                    }
                } else if ("3".equals(codeRuleItem.getSectType())) {//3计数
                    BigDecimal currentSeq = codeRuleItem.getCurrentSequence();
                    Integer base = codeRuleItem.getBase(); //进制
                    if (base == null || base < 2) {
                        base = 10;
                    }
                    Integer incr = codeRuleItem.getIncr(); //增量
                    if (incr == null || incr < 1) {
                        incr = 1;
                    }
                    BigDecimal len = codeRuleItem.getLenSequence(); //长度
                    BigDecimal minSeq = codeRuleItem.getMinSequence(); //最小序列
                    if (minSeq == null) {
                        minSeq = BigDecimal.ZERO;
                    }
                    BigDecimal maxSeq = codeRuleItem.getMaxSequence(); //最大序列
                    String rest = codeRuleItem.getReset();
                    boolean restFlag = CommonUtil.shouldResetOccur(rest, generatorDate, codeRuleItem.getModifyDate()); //时间是否重置
                    BigDecimal nextDecimal = currentSeq; //下一序号数字，未进行进制转换
                    String nextNumber = "";//下一序号数字，进行进制转换
                    if (restFlag) {
                        nextDecimal = minSeq.add(new BigDecimal(incr * i));
                    } else {
                        nextDecimal = nextDecimal.add(new BigDecimal(incr * (i + 1)));
                    }
                    if (maxSeq != null && maxSeq.doubleValue() > 0) {
                        if (nextDecimal.subtract(maxSeq).doubleValue() > 0) {
                            throw new CommonException("下一编码超过最大序列", CommonExceptionDefinition.VERIFY_EXCEPTION);
                        }
                    }

                    //调用此方法的方法会用到修改后的数据更新
                    if ((i + 1) == number) {
                        codeRuleItem.setCurrentSequence(nextDecimal); //修改现有数据的当前序列
                        codeRuleItem.setModifyDate(generatorDate); //修改现有数据的修改时间
                    }

                    nextNumber = Integer.toString(nextDecimal.intValue(), base).toUpperCase();

                    //编码前方补0
                    if (len != null && len.intValue() > 0) {
                        nextNumberList.get(i).append(StrUtil.fillBefore(nextNumber, '0', len.intValue()));
                    } else {
                        nextNumberList.get(i).append(nextNumber);
                    }

                } else if ("4".equals(codeRuleItem.getSectType())) { //变量
                    if (!StrUtil.isBlank(codeRuleItem.getSectParam()) && paramMap != null && !paramMap.isEmpty()) {
                        String secParam = codeRuleItem.getSectParam();
                        for (String key : paramMap.keySet()) {
                            secParam = secParam.replace(key, paramMap.get(key).toString());
                        }
                        nextNumberList.get(i).append(secParam);
                    }
                } else {
                    nextNumberList.get(i).append(codeRuleItem.getSectParam());
                }
            }

        }
        return nextNumberList.stream().map(StringBuilder::toString).collect(Collectors.toList());

    }

    /**
     * 获取多个下一编码
     *
     * @param codeRuleBo codeRuleBo
     * @param number     生成编码的个数 必须是正整数
     * @return List<String>
     * @throws CommonException 异常
     */
    @Override
    public List<String> generatorNextNumberList(String codeRuleBo, int number) throws CommonException {
        return generatorNextNumberList(codeRuleBo, number, null);
    }

    /**
     * 获取多个下一编码
     *
     * @param codeRuleBo codeRuleBo
     * @param number     生成编码的个数 必须是正整数
     * @param paramMap   变量替换
     * @return List<String>
     * @throws CommonException 异常
     */
    @Override
    public List<String> generatorNextNumberList(String codeRuleBo, int number, Map<String, Object> paramMap) throws
            CommonException {

        CodeRule codeRule = getExistCodeRule(new CodeRuleHandleBO(codeRuleBo));
        List<CodeRuleItem> codeRuleItemList = codeRuleItemService.selectForUpdateCodeRuleList(codeRuleBo);
        List<String> nextNumberList = generatorNextNumberList(codeRuleItemList, number, paramMap); //获取编号
        if (codeRuleItemList.size() > 0) {
            //更新主表时间
            CodeRule updateCodeRule = new CodeRule();
            updateCodeRule.setModifyDate(codeRuleItemList.get(0).getModifyDate());
            updateCodeRule.setBo(codeRule.getBo());
            codeRuleMapper.updateById(updateCodeRule);
            //更新明细表时间
            List<CodeRuleItem> updateCodeRuleItemList = new ArrayList<>();
            CodeRuleItem updateCodeRuleItem = null;
            for (CodeRuleItem codeRuleItem : codeRuleItemList) {
                updateCodeRuleItem = new CodeRuleItem();
                updateCodeRuleItem.setBo(codeRuleItem.getBo());
                updateCodeRuleItem.setCurrentSequence(codeRuleItem.getCurrentSequence());
                updateCodeRuleItem.setModifyDate(codeRuleItem.getModifyDate());
                updateCodeRuleItemList.add(updateCodeRuleItem);
            }
            codeRuleItemService.updateBatchById(updateCodeRuleItemList);
        }

        return nextNumberList;
    }

    /**
     * 获取一个下一编码
     *
     * @param codeRuleBo codeRuleBo
     * @return List<String>
     * @throws CommonException 异常
     */
    @Override
    public String generatorNextNumber(String codeRuleBo) throws CommonException {

        return generatorNextNumberList(codeRuleBo, 1).get(0);
    }

    /**
     * 获取一个下一编码
     *
     * @param codeRuleBo 编码规则BO
     * @param paramMap   替换参数
     * @return String
     * @throws CommonException 异常
     */
    @Override
    public String generatorNextNumber(String codeRuleBo, Map<String, Object> paramMap) throws CommonException {

        return generatorNextNumberList(codeRuleBo, 1, paramMap).get(0);
    }

    /**
     * 下一编号示例
     *
     * @param codeRuleItemList codeRuleItemList
     * @return String
     */
    private String getNextNumberExample(List<CodeRuleItem> codeRuleItemList) throws CommonException {
        //按seq进行升序排序
        if (codeRuleItemList.size() > 1) codeRuleItemList.sort(Comparator.comparing(CodeRuleItem::getSeq));
        //数据段类型（1固定，2日期，3计数）
        Date generatorDate = new Date(); //编码生成日期
        StringBuilder stringBuilder = new StringBuilder();
        for (CodeRuleItem codeRuleItem : codeRuleItemList) {
            if ("1".equals(codeRuleItem.getSectType())) {//1固定
                stringBuilder.append(codeRuleItem.getSectParam());
            } else if ("2".equals(codeRuleItem.getSectType())) {//2日期
                if (codeRuleItem.getSectParam() != null) {
                    try {
                        stringBuilder.append(DateUtil.format(generatorDate, codeRuleItem.getSectParam()));
                    } catch (Exception e) {
                        throw new CommonException("日期格式" + codeRuleItem.getSectParam() + "转换异常", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                }
            } else if ("3".equals(codeRuleItem.getSectType())) {//3计数
                String nextNumber = Integer.toString(codeRuleItem.getCurrentSequence().intValue(), codeRuleItem.getBase()).toUpperCase();
                //编码前方补0
                if (codeRuleItem.getLenSequence() != null && codeRuleItem.getLenSequence().intValue() > 0) {
                    stringBuilder.append(StrUtil.fillBefore(nextNumber, '0', codeRuleItem.getLenSequence().intValue()));
                } else {
                    stringBuilder.append(nextNumber);
                }

            } else {
                stringBuilder.append(codeRuleItem.getSectParam());
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 下一编号示例
     *
     * @param codeRuleBo codeRuleBo
     * @return String
     * @throws CommonException 异常
     */
    @Override
    public String getNextNumberExample(String codeRuleBo) throws CommonException {

        List<CodeRuleItem> codeRuleItemList = codeRuleItemService.selectCodeRuleItemListByCodeRuleBo(codeRuleBo);
        return getNextNumberExample(codeRuleItemList);
    }


}
