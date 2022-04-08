package com.itl.iap.system.api.dto;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/**
 * @author 崔翀赫
 * @date 2021/3/3$
 * @since JDK1.8
 */
@Data
public class AdvancedQueryDto {

    /**
     * 页面唯一标识
     */
    private String id;
    /**
     * 拼接条件
     */
    private List<AdvancedQuery> advances;
    private Page page;


}
