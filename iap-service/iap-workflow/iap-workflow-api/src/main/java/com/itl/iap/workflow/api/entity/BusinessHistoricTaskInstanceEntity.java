package com.itl.iap.workflow.api.entity;

import lombok.Data;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricTaskInstanceEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * 扩展实体
 *
 * @author 汤俊
 * @date 2020-07-09
 * @since jdk1.8
 */
@Data
public class BusinessHistoricTaskInstanceEntity extends HistoricTaskInstanceEntity implements HistoricTaskInstance {

    /**
     * 流程变量
     */
    private Map<String, Object> variables = new HashMap<>();


}
