package com.itl.iap.system.provider.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.dto.AdvancedQueryDto;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.system.provider.utils.QueryConditionUtil;
import com.itl.iap.system.api.service.AdvanceService;
import com.itl.iap.system.provider.mapper.AdvanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author 崔翀赫
 * @date 2021/3/5$
 * @since JDK1.8
 */
@Service
public class AdvanceServiceImpl implements AdvanceService {

    @Autowired
    private AdvanceMapper advanceMapper;

    @Override
    public IPage<Map> queryAdvance(AdvancedQueryDto advancedQueryDtos) throws CommonException {
        if (advancedQueryDtos.getPage() == null) {
            advancedQueryDtos.setPage(new Page(0, 10));
        }
        final Map<String, String> columnsDefault = advanceMapper.getColumnsDefault(advancedQueryDtos.getId());
        final List<Map> maps = advanceMapper.advanceQuery(QueryConditionUtil.getWhereSql(advancedQueryDtos),
                columnsDefault.get("FROM"), columnsDefault.get("COLUMNS_DEFAULT"));

        final IPage<Map> mapIPage = new Page<>();
        mapIPage.setRecords(maps);
        return mapIPage;
    }

    @Override
    public List<Map<String, String>> getColumn(String pageId) {
        final String tables = advanceMapper.getTables(pageId);

        final List<String> strings = Arrays.asList(tables.split(","));
        List<Map<String, String>> list = advanceMapper.getColumn(strings);
        list.forEach(x -> {
            if (".".equals(x.get("columnLabel"))) {
                x.put("columnLabel", x.get("columnName"));
            } else {
                String[] split = x.get("columnLabel").split("\\.");
                String[] splitName = x.get("columnName").split("\\.");
                if ("".equals(split[0])) {
                    x.put("columnLabel", splitName[0] + "." + split[1]);
                } else if ("".equals(split[1])) {
                    x.put("columnLabel", split[0] + "." + splitName[1]);
                }
            }
        });

        return list;
    }

}
