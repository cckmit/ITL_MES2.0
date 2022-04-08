/**
 * Copyright (C):  Evergrande Group
 * FileName: UserTaskListener
 * Author:   huangjianming
 * Date:     2020-07-03 14:01
 * Description:
 */
package com.itl.iap.workflow.workflow.listener;


import com.alibaba.fastjson.JSONObject;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.attachment.client.service.PositionService;
import com.itl.iap.attachment.client.service.RoleService;
import com.itl.iap.workflow.workflow.enums.ApprovalTypeEnum;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.model.bpmn.impl.instance.UserTaskImpl;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户任务监听器进行权限分配
 *
 * @author 黄建明
 * @date 2020-07-03
 * @since jdk1.8
 */
@Log4j2
@Service
public class UserTaskListener implements TaskListener {

    @Autowired
    private RoleService roleService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private UserUtil userUtil;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("用户监听任务分配审批人开始，任务id：" + delegateTask.getId());

        String assignee = delegateTask.getAssignee();
        //若节点上已经设置受让人，则不做受让人赋值(查找历史节点上的受让人)
        List<HistoricActivityInstance> instances = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(delegateTask.getProcessInstanceId())
                .activityType("userTask")
                .activityId(delegateTask.getTaskDefinitionKey())
                .finished()
                .orderByHistoricActivityInstanceEndTime()
                .asc()
                .list();

        //驳回再次提交，则去查找该节点是否设置过受让人，避免重复领取任务
        boolean isBackTask = (delegateTask.getAssignee() == null || "".equals(delegateTask.getAssignee()))
                && instances != null && instances.size() > 0;
        if (isBackTask) {
            delegateTask.setAssignee(instances.get(0).getAssignee());
            log.info("该节点曾经设置过受让人<" + delegateTask.getTaskDefinitionKey() + ">:"
                    + instances.get(0).getAssignee());
            //当受让人为空，则通过设置赋值受让人信息，若不为空则跳过
        } else if (delegateTask.getAssignee() == null || "".equals(delegateTask.getAssignee())) {
            //获取拓展属性上的审批人员类型 和 审批人员/角色/岗位id
            final String typeName = "approvalType";
            final String typeId = "approvalId";
            String approvalType = "";
            String approvalId = "";

            //指定人加逻辑  1 先获取到 Variable 里面上个节点存的数据
            Object appoint = delegateTask.getVariable("appoint");
            if(appoint!=null){
                JSONObject jsonObject = JSONObject.parseObject(appoint.toString());
                if(jsonObject!=null){
                    approvalType = jsonObject.getString("appointType");
                    approvalId = jsonObject.getString("appointId");
                    log.info("上个节点指点的信息：approvalType：{}，approvalId：{}"
                            , approvalType, approvalId);
                }

            }


            //如果指定人为空才走这个逻辑
            if(StringUtils.isBlank(approvalType) && StringUtils.isBlank(approvalId)){
            UserTaskImpl startEvent = (UserTaskImpl) delegateTask.getBpmnModelElementInstance();
            ExtensionElements extensionElements = startEvent.getExtensionElements();
            if (extensionElements != null) {
                Collection<CamundaProperty> properties = extensionElements.getElementsQuery()
                        .filterByType(CamundaProperties.class)
                        .singleResult()
                        .getCamundaProperties();

                for (CamundaProperty property : properties) {
                    if (typeName.equalsIgnoreCase(property.getCamundaName())) {
                        approvalType = property.getCamundaValue();
                    } else if (typeId.equalsIgnoreCase(property.getCamundaName())) {
                        approvalId = property.getCamundaValue();
                    }
                }
            }
            log.info("节点审批获取到的审批类型信息,获取到的参数：approvalType：{}，approvalId：{}"
                    , approvalType, approvalId);

            }
            //根据获取到的类型和id匹配去查找对应的人员数据后赋值给节点审批人
            ApprovalTypeEnum userType = ApprovalTypeEnum.getEnumByValue(approvalType);
            switch (userType) {
                case SPECIFIED_USER:
                    //指定人员，直接赋值
                    delegateTask.setAssignee(approvalId);
                    break;
                case ROLE:
                    //指定角色，通过角色查找对应人员赋值(Assignee不设置空，候选人查不到)
                    ResponseData<List<IapSysUserTDto>> responseData =
                            roleService.queryAllUserListByRoleId(approvalId);
                    if (responseData != null && responseData.getData() != null) {
                        List<IapSysUserTDto> userList = responseData.getData();
                        //若角色只有一人，则任务直接设置给当事人
                        if (userList != null && userList.size() == 1) {
                            delegateTask.setAssignee(userList.get(0).getUserName());
                        } else {
                            List<String> idList = new ArrayList<>();
                            for (IapSysUserTDto user : userList) {
                                idList.add(user.getUserName());
                            }
                            delegateTask.setAssignee(null);
                            delegateTask.addCandidateUsers(idList);
                        }
                    } else {
                        throw new RuntimeException("该角色对应用户为空，请检查,角色id" + approvalId);
                    }
                    break;
                case JOBS:
                    //指定岗位，通过岗位查找对应人员赋值(Assignee不设置空，候选人查不到)
                    ResponseData<List<IapSysUserTDto>> userResponseData =
                            positionService.queryUserById(approvalId);
                    if (userResponseData != null && userResponseData.getData() != null) {
                        List<IapSysUserTDto> userList = userResponseData.getData();
                        //若岗位只有一人，则任务直接设置给当事人
                        if (userList != null && userList.size() == 1) {
                            delegateTask.setAssignee(userList.get(0).getUserName());
                        } else {
                            List<String> idList = new ArrayList<>();
                            for (IapSysUserTDto user : userList) {
                                idList.add(user.getUserName());
                            }
                            delegateTask.setAssignee(null);
                            delegateTask.addCandidateUsers(idList);
                        }
                    } else {
                        throw new RuntimeException("该岗位对应用户为空，请检查,岗位id" + approvalId);
                    }
                    break;
                case CURRENT:
                    //当前操作者(防止驳回赋值给当前操作人)
                    String nowOperation = (String)runtimeService.getVariable(delegateTask.getExecutionId(),"nowOperation");
                    delegateTask.setAssignee(nowOperation);
                    break;
                default:
                    throw new RuntimeException("节点审批未匹配到审批类型,获取到的参数：approvalType：" + approvalType
                            + "，approvalId：" + approvalId);
            }
            //清除全局变量
            if(appoint!=null){
                delegateTask.removeVariable("appoint");
               // delegateTask.setVariable("appoint",null);
            }
        }
    }
}
