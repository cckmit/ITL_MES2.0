package com.itl.iap.notice.api.pojo;

import java.util.List;

/**
 * 返回数据包装
 *
 * @author Linjl
 * @date  2020/3/19
 * @since jdk1.8
 */
public class TableData<T> {

    private Long total;

    private List<T> rows;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
