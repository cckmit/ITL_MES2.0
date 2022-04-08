package com.itl.iap.mes.provider.common;

import lombok.Data;

/**
 * 分页
 * @author 胡广
 * @version 1.0
 * @name PageableData
 * @description
 * @date 2019-07-08
 */
@Data
public class PageableData implements java.io.Serializable {
    //页大小
    private int pageSize;
    //总页数
    private int pages;
    //当前页
    private int currentPage;
    //起始行
    private int startRow;
    //末行
    private int endRow;
    //总数
    private long total;
    //排序 升序(ASC)  降序(DESC)
    private String orderBy;

}
