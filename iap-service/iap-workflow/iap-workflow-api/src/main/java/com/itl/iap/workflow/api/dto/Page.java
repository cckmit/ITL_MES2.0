package com.itl.iap.workflow.api.dto;

import lombok.Data;

/**
 * 工作流分页entity
 *
 * @author 李虎
 * @date 2020/7/1
 * @since jdk1.8
 */
@Data
public class Page {
    /**
     * 当前页
     */
    private Integer currentPage = 1;
    /**
     * 每页数量
     */
    private Integer pageSize = 10;
    /**
     * 总条数
     */
    private Integer total = 0;

    /**
     * <p>
     * 当前分页总页数
     * </p>
     */
    public Integer getPages() {
        if (getPageSize() == 0) {
            return 0;
        }
        Integer pages = getTotal() / getPageSize();
        if (getTotal() % getPageSize() != 0) {
            pages++;
        }
        return pages;
    }
}
