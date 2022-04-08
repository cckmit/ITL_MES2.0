package com.itl.mes.core.api.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @author yaoxiang
 * @date 2021/1/4
 * @since JDK1.8
 */
@Data
public class ProductLineQueryVo {
    private String workShopBo;
    private Page page;
    private String site;
}
