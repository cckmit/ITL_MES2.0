package com.itl.iap.system.provider.utils;


import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.system.api.dto.AdvancedQuery;
import com.itl.iap.system.api.dto.AdvancedQueryDto;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 崔翀赫
 * @date 2021/3/3$
 * @since JDK1.8
 */
public class QueryConditionUtil {

    public static String getWhereSql(AdvancedQueryDto advancedQueryDto) throws CommonException {
        LinkedList<String> flag = new LinkedList<>();
        final List<AdvancedQuery> list = advancedQueryDto.getAdvances();
        for (AdvancedQuery x : list) {
            if (x.getConnection().contains("(")) {
                flag.addLast("1");
            } else if (x.getConnection().contains(")")) {
                try {
                    flag.removeLast();
                } catch (Exception e) {
                    throw new CommonException(new CommonExceptionDefinition(500, "查询条件格式有误!"));
                }
            }
        }
        if (!flag.isEmpty()) {
            throw new CommonException(new CommonExceptionDefinition(500, "查询条件格式有误!"));
        }

        StringBuilder sb = new StringBuilder(512);
        for (AdvancedQuery advancedQuery : list) {
            if ("like".equals(advancedQuery.getCondition())) {
                advancedQuery.setValue("%" + advancedQuery.getValue().trim() + "%");
            }

            sb.append(advancedQuery.getColumn())
                    .append(" ")
                    .append(advancedQuery.getCondition())
                    .append(" '").append(advancedQuery.getValue().trim()).append("' ");

            sb.append(advancedQuery.getConnection()).append(" ");
        }
        return sb.toString();
    }
}
