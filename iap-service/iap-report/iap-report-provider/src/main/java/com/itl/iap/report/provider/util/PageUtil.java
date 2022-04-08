package com.itl.iap.report.provider.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

public class PageUtil<T> {
    // 开始位置
    private static int fromIndex;

    // 结束位置
    private static int toIndex;

    public static<T> Page<T> getPage(Page page, List<T> list){
        Page pageObj=new Page(page.getCurrent(),page.getSize());
        // 判断集合是否为空
        if(list !=null && list.size()>0){
            pageObj.setTotal(list.size());
            // 计算
            fromIndex=(int)((page.getCurrent()-1)*page.getSize());
            toIndex=Math.toIntExact(Math.min(fromIndex + page.getSize(), pageObj.getTotal()));
            pageObj.setRecords(list.subList(fromIndex,toIndex));
        }else{
            pageObj.setTotal(0);
        }
        return pageObj;
    }
}
