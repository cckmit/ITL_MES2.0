package com.itl.iap.workflow.api.dto;

import com.google.common.collect.Lists;

import com.itl.iap.workflow.api.entity.BusinessTaskEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 汤俊
 * @date 2020-07-07
 * @since jdk1.8
 */
@Data
public class TodoDto extends Page {

    private String taskName;

    private String userName;

    private String businessKey;

    private Date createdBefore;

    private Date createdAfter;

    /**
     * 列表数据
     */
//    List<org.camunda.bpm.engine.task.Task> list = Lists.newArrayList();

//    List<Map<String, Object>> mapList = Lists.newArrayList();

    private List<BusinessTaskEntity> businessTaskEntities = Lists.newArrayList();
}
