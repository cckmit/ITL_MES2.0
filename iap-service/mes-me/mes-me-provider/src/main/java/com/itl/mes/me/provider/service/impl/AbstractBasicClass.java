package com.itl.mes.me.provider.service.impl;

import com.itl.mes.me.api.entity.MeSfc;

/**
 * 整个工序流程
 * 抽象类(AbstractClass)
 * 具象类(ConcreteClass)
 * Biz层继承该类 协调各service处理业务 Biz--<Business>
 */
public abstract class AbstractBasicClass implements AbstractBasicInterface {

    /**
     * sfc表随“工序执行步骤”更新
     */
    @Override
    public MeSfc updateSfcTable(MeSfc sfc){
        return null;
    }



}
