package com.itl.iap.mes.provider.common;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import com.itl.iap.mes.api.dto.LovData;
import com.itl.iap.mes.api.dto.LovEntryData;
import com.itl.iap.mes.api.entity.Lov;
import com.itl.iap.mes.api.entity.LovEntry;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 胡广
 * @version 1.0
 * @name BasicLovConverter
 * @description
 * @date 2019-08-22
 */
@Component("lovDataConverter")
public class BasicLovDataConverter implements Converter<Lov, LovData> {
    @Override
    public LovData convert(Lov lov) {
        LovData lovData = new LovData();
        BeanUtils.copyProperties(lov,lovData);
        if(CollectionUtils.isNotEmpty(lov.getEntries()))
        {
            List<LovEntryData> entryDataList = new ArrayList<>();
            for(LovEntry lovEntry : lov.getEntries())
            {
                LovEntryData lovEntryData = new LovEntryData();
                BeanUtils.copyProperties(lovEntry,lovEntryData);
                entryDataList.add(lovEntryData);
            }
            lovData.setEntries(entryDataList);
        }
        return lovData;
    }
}
