package com.itl.iap.workflow.workflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.camunda.bpm.model.xml.Model;

/**
 * 流程过程服务类
 *
 * @author 黄建明
 * @date 2020-06-24
 * @since jdk1.8
 */
public interface ProcessService {

    /**
     * 通过条件模糊查询员工列表
     * @param code 编码
     * @return IPage<Model>
     */
    IPage<Model> query(String code);
}
