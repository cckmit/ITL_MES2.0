package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.SnRule;

import java.util.List;

/**
 * <p>
 * 条码规则表 服务类
 * </p>
 *
 * @author space
 * @since 2019-08-03
 */
public interface SnRuleService extends IService<SnRule> {

    List<SnRule> selectList();

    void saveSnRule(SnRule snRule) throws CommonException;

    void updateSnRule(SnRule snRule) throws CommonException;

    void deleteSnRule(List<SnRule> snRule) throws CommonException;
}