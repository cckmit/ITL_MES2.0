//package com.itl.iap.mes.provider.utils;
//
//
//import com.itl.iap.mes.provider.common.PageableData;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//
///**
// * 分页工具类
// * @author 胡广
// * @version 1.0
// * @name PageUtil
// * @description
// * @date 2019-09-03
// */
//public class PageUtil extends PageRequest{
//    public static final Pageable of(Integer page, Integer pageSize)
//    {
//        if(page > 0)
//        {
//            page = page - 1;
//        }
//        return super(page, pageSize);
//      //  return new PageRequest(page,pageSize,null);
//    }
//
//    public static final Pageable of(PageableData pageableData)
//    {
//        if(pageableData.getCurrentPage() > 0)
//        {
//            return new PageRequest(pageableData.getCurrentPage()-1,pageableData.getPageSize());
//        }
//        return new PageRequest(pageableData.getCurrentPage(),pageableData.getPageSize());
//    }
//}
