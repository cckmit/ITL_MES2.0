package com.itl.iap.notice.api.pojo;

/**
 * 基础查询参数实体
 *
 * @author Linjl
 * @date  2020/3/19
 * @since jdk1.8
 */
public class BaseRequestPojo {

    protected Integer pageNum;

    protected Integer pageSize;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
