package com.itl.mes.core.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yaoxiang
 * @date 2021/1/22
 * @since JDK1.8
 */
@Data
@Accessors(chain = true)
public class ItemForParamQueryDto {
    private String site;
    private List<String> columns = new ArrayList<>();
    private String isCustom;
    private String itemBo;
}
