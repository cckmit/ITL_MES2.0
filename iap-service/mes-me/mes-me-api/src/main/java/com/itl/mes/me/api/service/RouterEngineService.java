package com.itl.mes.me.api.service;


import com.google.gson.JsonObject;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.entity.RouterProcess;
import com.itl.mes.me.api.entity.MeSfc;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 工艺路线引擎
 * </p>
 *
 * @author linjl
 * @since 2020-02-04
 */
public interface RouterEngineService {

    /**
     * 执行工艺路线，返回下一工序
     * */
    HashMap<String,String> executeRouter(String sfcBo) throws Exception;

    /**
     * 执行工艺路线，返回下一工序
     * */
    HashMap<String,String> executeRouter(MeSfc sfc) throws Exception;


//    /**
//     * 获取工艺路线执行信息
//     * */
//    List<Operation> getExecuteInfo(MeSfc sfc);

    /**
     * 暂停工艺路线
     * */
    boolean pauseRouter(MeSfc sfc);

    /**
     * 挂起工艺路线
     * */
    boolean hangupRouter(MeSfc sfc);

}