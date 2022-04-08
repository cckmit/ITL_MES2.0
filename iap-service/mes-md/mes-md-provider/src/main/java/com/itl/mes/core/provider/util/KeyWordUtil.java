package com.itl.mes.core.provider.util;

import com.itl.mes.core.api.dto.StepProduceDTO;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class KeyWordUtil {

    /**
     * 封装物料描述关键字
     * @param keyword
     * @return
     */
    public static List<String> encapsulationItemDescKeyWord(String keyword){
        List<String> keyWordList = null;
        if (StringUtils.isNotBlank(keyword)){
            keyWordList = Lists.newArrayList();
            for (String s : keyword.split("\\*")) {
                String word = "\"" + s + "*" + "\"";
                keyWordList.add(word);
            }
        }
        return keyWordList;
    }
}
