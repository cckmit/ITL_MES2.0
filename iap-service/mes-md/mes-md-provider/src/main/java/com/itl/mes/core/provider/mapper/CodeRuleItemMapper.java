package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.CodeRuleItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 编码规则明细表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-06-19
 */
@Repository
public interface CodeRuleItemMapper extends BaseMapper<CodeRuleItem> {

    List<CodeRuleItem> selectForUpdateCodeRuleList(@Param("codeRuleBo") String codeRuleBo);

}