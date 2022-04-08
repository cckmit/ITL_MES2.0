package com.itl.iap.workflow.workflow.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultResponseEnum;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.attachment.client.service.PositionService;
import com.itl.iap.attachment.client.service.RoleService;
import com.itl.iap.attachment.client.service.UserService;
import com.itl.iap.workflow.api.dto.DoneDto;
import com.itl.iap.workflow.api.dto.TodoDto;
import com.itl.iap.workflow.api.entity.BusinessHistoricTaskInstanceEntity;
import com.itl.iap.workflow.api.entity.BusinessTaskEntity;
import com.itl.iap.workflow.workflow.enums.ApprovalTypeEnum;
import com.itl.iap.workflow.workflow.service.ProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.history.*;
import org.camunda.bpm.engine.rest.dto.runtime.ActivityInstanceDto;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ExecutionQuery;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.ws.rs.Produces;
import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 流程控制器
 *
 * @author 李虎
 * @date 2020-07-01
 * @since jdk1.8
 */
@Slf4j
@Api("流程控制器")
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;
    @Autowired
    private UserUtil userUtil;
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;
    @Autowired
    private PositionService positionService;

    @Autowired
    ExternalTaskService externalTaskService;
    @Resource
    private ProcessService processService;
    @Autowired
    RepositoryService repositoryService;

    @Autowired
    ManagementService managerService;

    /**
     * 获取用户的代办任务
     *
     * @return
     */
    @ApiOperation("获取用户的待办任务")
    @PostMapping("/getTodos1")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseData<TodoDto> todos1(@NonNull @RequestBody TodoDto todoDto) {
        //获取用户
        //TODO 后续将用户缓存redis中
        ResponseData<IapSysUserTDto> iapSysUserDtoResponseData = userService.queryByName(userUtil.getUser().getUserName());
        IapSysUserTDto user = iapSysUserDtoResponseData.getData();
        todoDto.setUserName(user.getUserName());

        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(todoDto.getUserName());
        TaskQuery candidateUserQuery = taskService.createTaskQuery().taskCandidateUser(todoDto.getUserName());
//        taskQuery.initializeFormKeys();
//        candidateUserQuery.initializeFormKeys();
        //获取当前用户
//        if (StringUtils.isNotBlank(todoDto.getTaskName())) {
//            taskQuery.taskNameLike(todoDto.getTaskName());
//            candidateUserQuery.taskNameLike(todoDto.getTaskName());
//        }
        if (null != todoDto.getCreatedAfter()) {
            taskQuery.taskCreatedBefore(todoDto.getCreatedAfter());
            candidateUserQuery.taskCreatedBefore(todoDto.getCreatedAfter());
        }
        if (null != todoDto.getCreatedBefore()) {
            taskQuery.taskCreatedAfter(todoDto.getCreatedBefore());
            candidateUserQuery.taskCreatedAfter(todoDto.getCreatedBefore());
        }
        if (StringUtils.isNotBlank(todoDto.getTaskName())) {
            taskQuery.processVariableValueLike("processName", "%" + todoDto.getTaskName() + "%");
            candidateUserQuery.processVariableValueLike("processName", "%" + todoDto.getTaskName() + "%");
        }
        if (StringUtils.isNotBlank(todoDto.getBusinessKey())) {
            taskQuery.processVariableValueLike("businessKey", "%" + todoDto.getBusinessKey() + "%");
            candidateUserQuery.processVariableValueLike("businessKey", "%" + todoDto.getBusinessKey() + "%");
        }
        //查询当前用户的代办任务,根据创建时间排序
        List<Task> todos = taskQuery.orderByTaskCreateTime().desc().list();
        todos.addAll(candidateUserQuery.orderByTaskCreateTime().desc().list());
        int total = todos.size();
        List<Task> collect1 = todos.stream().sorted(Comparator.comparing(Task::getCreateTime).reversed()).collect(Collectors.toList());
        //
        List<Task> collect = collect1.stream().skip(todoDto.getPageSize() * (todoDto.getCurrentPage() - 1)).limit(todoDto.getPageSize()).collect(Collectors.toList());
        collect.parallelStream().forEachOrdered(todo -> {
            BusinessTaskEntity businessTaskEntity = new BusinessTaskEntity(todo);
            businessTaskEntity.setVariables(taskService.getVariables(todo.getId()));
            //businessTaskEntity.getVariables().put("formKey", getTaskFormData(todo.getId()).getFormKey());
            String formKey = (String) businessTaskEntity.getVariables()
                    .get(todo.getTaskDefinitionKey() + "_formKey");
            if (formKey != null && !"".equals(formKey)) {
                businessTaskEntity.getVariables().put("formKey", formKey);
            }
            todoDto.getBusinessTaskEntities().add(businessTaskEntity);
            log.info("--->>>" + todo.getName());
        });
        todoDto.setTotal(total);
        return ResponseData.success(todoDto);
    }

    /**
     * 获取用户的已办任务
     *
     * @return
     */
    @ApiOperation("获取用户的已办任务")
    @PostMapping("/getDones")
    public ResponseData<DoneDto> dones(@NonNull @RequestBody DoneDto done) {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
        HistoricTaskInstanceQuery assigneeQuery = historicTaskInstanceQuery.taskAssignee(userUtil.getUser().getUserName()).finished();
        // 限制为在给定日期之后  启动的实例
        if (null != done.getStartedBefore()) {
            assigneeQuery.startedBefore(done.getStartedBefore());
        }
        // 限制在给定日期之前  完成的实例
        if (null != done.getStartedAfter()) {
            assigneeQuery.startedAfter(done.getStartedAfter());
        }
        if (null != done.getFinishedAfter()) {
            assigneeQuery.finishedBefore(done.getFinishedAfter());
        }
        if (null != done.getFinishedBefore()) {
            assigneeQuery.finishedAfter(done.getFinishedBefore());
        }
        if (StringUtils.isNotBlank(done.getTaskName())) {
            assigneeQuery.processVariableValueLike("processName", "%" + done.getTaskName() + "%");
        }
        if (StringUtils.isNotBlank(done.getBusinessKey())) {
            assigneeQuery.processVariableValueLike("businessKey", "%" + done.getBusinessKey() + "%");
        }

        //分页查询
        int total = assigneeQuery.orderByHistoricTaskInstanceEndTime().desc().list().size();
        List<HistoricTaskInstance> dones = assigneeQuery.listPage(done.getPages(), done.getPageSize());
        //设置总数
        done.setTotal(total);
        //设置数据
//        done.setList(dones);
        //
        Gson gson = new Gson();
        for (HistoricTaskInstance task : dones) {
            String taskStr = gson.toJson(task);
            BusinessHistoricTaskInstanceEntity bhtie = gson.fromJson(taskStr, BusinessHistoricTaskInstanceEntity.class);
            List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId()).executionIdIn(task.getExecutionId()).list();

            //整个流程的历史人
            List<HistoricActivityInstance> instances = historyService
                    .createHistoricActivityInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .finished()
                    .orderByHistoricActivityInstanceEndTime()
                    .asc()
                    .list();


//            instances.forEach((x,i)->{
//
//            });
//


//            instances.stream().map((x,i)->{
//                if(userName.equals(x.getAssignee()))
//                {
//                    HistoricActivityInstance historicActivityInstance = instances.get(i + 1);
//
//                }
//            });


            for (HistoricVariableInstance historicVariableInstance : list) {
                log.info("historicVariableInstance  name:{},value:{}",
                        historicVariableInstance.getName(), historicVariableInstance.getValue());
                bhtie.getVariables().put(historicVariableInstance.getName(), historicVariableInstance.getValue());
            }

            //获取正在执行的节点任务task，并获取runnerTask的formkey等值
            /*Task nowTask = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            if (nowTask != null) {
                bhtie.getVariables().put("formKey", getTaskFormData(nowTask.getId()).getFormKey());
            }*/


            done.getList().add(bhtie);
            log.info("--->>>" + task.getName());
        }
        return ResponseData.success(done);
    }


    /**
     * 根据ID获取任务  012d926c-bb6b-11ea-9422-68f72816b6ed
     *
     * @param taskId
     * @return
     */
    @ApiOperation("根据ID获取任务")
    @GetMapping("/getTaskById/{taskId}")
    public Task getTaskById(@PathVariable("taskId") String taskId) {
        System.out.println("測試-----" + taskId);
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    /**
     * 流程任务审批
     *
     * @param taskId    012d926c-bb6b-11ea-9422-68f72816b6ed
     * @param variables
     * @return
     */
    @ApiOperation("流程审批，同意")
    //@Transactional(rollbackFor = Exception.class)
    @PostMapping("/approveTaskById/{taskId}")
    public ResponseData<Boolean> approveTaskById(@PathVariable("taskId") String taskId, @RequestBody Map<String, Object> variables, @RequestParam("message") String message) {
        try {
            //TODO 后续将用户缓存redis中
            ResponseData<IapSysUserTDto> iapSysUserDtoResponseData = userService.queryByName(userUtil.getUser().getUserName());
            IapSysUserTDto user = iapSysUserDtoResponseData.getData();

            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            //设置当前处理人完成任务领取
            if (StringUtils.isEmpty(task.getAssignee())) {
                taskService.claim(taskId, user.getUserName());
            }
            //判断是否第一个节点页面
            String formKey = (String) taskService.getVariable(taskId, "formKey");
            if (formKey == null || "".equals(formKey)) {
                variables.put("formKey", getTaskFormData(taskId).getFormKey());
            }
            //每个节点保存formKey
            variables.put(task.getTaskDefinitionKey() + "_formKey", getTaskFormData(taskId).getFormKey());
            //操作类型
            variables.put(task.getId() + "_opType", "approve");
            variables.put("nowApprove", "approve");
            variables.put("nowOperation", userUtil.getUser().getUserName());

            //执行通过
            taskService.complete(taskId, variables);
            //为当前任务创建意见信息
            taskService.createComment(taskId, task.getProcessInstanceId(), message);
            return ResponseData.success(Boolean.TRUE);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseData.error(e.getMessage());
        }
    }


    /**
     * 流程任务审批
     *
     * @param taskId    012d926c-bb6b-11ea-9422-68f72816b6ed
     * @param variables
     * @return
     */
    @ApiOperation("流程审批，同意，并指定人")
    @PostMapping("/approveTaskAndAppointById/{taskId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务id", required = true),
            @ApiImplicitParam(name = "userName", value = "用户名", required = true),
            @ApiImplicitParam(name = "message", value = "审批信息）", required = true),
            @ApiImplicitParam(name = "appointId", value = "指定id（人，岗位，角色））", required = false)
    })
    public ResponseData approveTaskAndAppointById(@PathVariable("taskId") String taskId, @RequestBody Map<String, Object> variables, @RequestParam("userName") String userName, @RequestParam("message") String message, @RequestParam("appointId") String appointId) {
        //指定类型
        try {
            //TODO 后续将用户缓存redis中
            ResponseData<IapSysUserTDto> iapSysUserDtoResponseData = userService.queryByName(userUtil.getUser().getUserName());
            IapSysUserTDto user = iapSysUserDtoResponseData.getData();

            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            String executionId = task.getExecutionId();
            /**
             * 指定的关键步骤
             * 1 首先获取当前节点的 扩展属性，查看是否有指定人存在
             *   1.1 如果存在指定人，直接就走指定人
             *   1.2 如果不存在那么就按照正常流程走
             * 2 如果有，那么返回前端让前端传对应的信息
             * 3 接到消息存在 runtimeService.setVariable
             * */

            //获取到类型，这个类型是在监听器里面设置的
            Object appointType = runtimeService.getVariable(executionId, "appointType");
            //如果有类型，那么就让前端根据这个类型去查询数据，然后通过appointId
            if (appointType != null && StringUtils.isBlank(appointId)) {
                //返回给前端让选取具体的数据id给我
                return new ResponseData(ResultResponseEnum.SUCCESS.getCode(), "请选择指定人", appointType);
                //两个都不为空了，那么就直接存起来，等到下一个节点取出来
            } else if (appointType != null && StringUtils.isNotBlank(appointId)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("appointType", appointType.toString());
                jsonObject.put("appointId", appointId);
                runtimeService.setVariable(executionId, "appoint", jsonObject.toJSONString());
            }

            //设置当前处理人完成任务领取
            if (StringUtils.isEmpty(task.getAssignee())) {
                taskService.claim(taskId, user.getId());
            }
            //为当前任务创建意见信息
//            taskService.createComment(taskId, task.getProcessInstanceId(), message);
            //操作类型
            variables.put(task.getId() + "_opType", "approve");
            //执行通过
            taskService.complete(taskId, variables);
            //为当前任务创建意见信息
            taskService.createComment(taskId, task.getProcessInstanceId(), message);
            //清除掉全局变量的信息
            if (appointType != null) {
                runtimeService.removeVariable(executionId, "appointType");
                //runtimeService.setVariable(executionId,"appointType",null);
            }
            return ResponseData.success(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return ResponseData.success(Boolean.FALSE);
    }


    /**
     * @Description: 指定委托人，或者用户角色
     * @return:
     * @Author: lx
     * @date: 2020-09-09 14:56
     */
    @ApiOperation("指定处理人、角色、岗位")
    @GetMapping("/appointHandle")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务id", required = true),
            @ApiImplicitParam(name = "type", value = "类型，你传 1 用户id  ，2 角色  3 岗位", required = true),
            @ApiImplicitParam(name = "appointId", value = "具体的id（用户、角色、岗位）", required = true)
    })
    public ResponseData<Boolean> appointHandle(@PathVariable("taskId") String taskId, @PathVariable("type") String type, @PathVariable("appointId") String appointId) {
        try {
            if (StringUtils.isBlank(taskId)) {
                ResponseData.error("任务id不能为空");
            }
            //查询任务
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task != null) {
                //校验指定的类型
                ResponseData responseData = appointType(type, appointId, taskId);
                return responseData == null ? ResponseData.success(Boolean.TRUE) : responseData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseData.success(Boolean.FALSE);
    }


    /**
     * 流程驳回
     *
     * @param taskId  任务ID
     * @param message 驳回消息
     */
    @ApiOperation("流程驳回")
    //@Transactional(rollbackFor = Exception.class)
    @PostMapping("/rejectTaskById/{taskId}")
    public ResponseData<String> rejectTaskById(@PathVariable("taskId") String taskId, @RequestParam("message") String message, @RequestBody Map<String, Object> variables) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ActivityInstance tree = runtimeService.getActivityInstance(task.getProcessInstanceId());
        //获取上一个任务
        HistoricActivityInstance previousTask = getPreviousTask(task.getProcessInstanceId(), task.getTaskDefinitionKey());
        if (ObjectUtils.isEmpty(previousTask)) {
            log.error("该节点已为第一个节点,不能继续驳回！");
            return ResponseData.error("该节点已为第一个节点,不能继续驳回！");
        }
        variables.put("assignee", previousTask.getAssignee());
        variables.put(task.getId() + "_opType", "reject");
        variables.put("nowApprove", "reject");
        variables.put("nowOperation", userUtil.getUser().getUserName());
        //表单名称
//        taskVariable.put("formName", "测试表单名称");
        runtimeService.createProcessInstanceModification(task.getProcessInstanceId())
                .cancelActivityInstance(getInstanceIdForActivity(tree, task.getTaskDefinitionKey()))//关闭相关任务
                .startBeforeActivity(previousTask.getActivityId())//启动目标活动节点
                .setVariables(variables)//流程的可变参数赋值
                .execute();
        //为当前任务创建驳回信息
        taskService.createComment(task.getId(), task.getProcessInstanceId(), "驳回原因:" + message);
        return ResponseData.success("驳回成功");
    }


    /**
     * 流程未审批可以流程撤回
     *
     * @param taskId 任务ID
     */
    @ApiOperation("流程未审批可以流程撤回")
    @PostMapping("/fallbackTask/{taskId}")
    public ResponseData<String> rejectTaskById(@PathVariable("taskId") String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ActivityInstance tree = runtimeService.getActivityInstance(task.getProcessInstanceId());
        //获取上一个任务
        HistoricActivityInstance previousTask = getPreviousTask(task.getProcessInstanceId(), task.getTaskDefinitionKey());
        if (ObjectUtils.isEmpty(previousTask)) {
            runtimeService.deleteProcessInstance(task.getProcessInstanceId(), "未发起流程撤回成功");
            //log.error("该节点已为第一个节点,不能继续驳回！");
            return ResponseData.error("撤回成功");
        }
        return ResponseData.success("不能撤回，目前节点以及有人审批");
    }


    /**
     * 获取申请人节点（以后再做优化，选择节点驳回）
     *
     * @param processInstanceId
     * @param definitionKey
     * @return
     */
    private HistoricActivityInstance getPreviousTask(String processInstanceId, String definitionKey) {
        //获取历史任务
        List<HistoricActivityInstance> instances = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .activityType("userTask")
                .finished()
                .orderByHistoricActivityInstanceEndTime()
                .asc()
                .list();
        HistoricActivityInstance instance = instances.get(0);
        //需判断不是申请人节点
        if (definitionKey.equals(instance.getActivityId())) {
            return null;
        } else {
            return instance;
        }
    }

    /**
     * 获取流程活动实例
     *
     * @param activityInstance
     * @param activityId
     * @return
     */
    private String getInstanceIdForActivity(ActivityInstance activityInstance, String activityId) {
        ActivityInstance instance = getChildInstanceForActivity(activityInstance, activityId);
        if (instance != null) {
            return instance.getId();
        }
        return null;
    }

    /**
     * 获取子实例
     *
     * @param activityInstance
     * @param activityId
     * @return
     */
    private ActivityInstance getChildInstanceForActivity(ActivityInstance activityInstance, String activityId) {
        if (activityId.equals(activityInstance.getActivityId())) {
            return activityInstance;
        }
        for (ActivityInstance childInstance : activityInstance.getChildActivityInstances()) {
            ActivityInstance instance = getChildInstanceForActivity(childInstance, activityId);
            if (instance != null) {
                return instance;
            }
        }
        return null;
    }

    /**
     * @param taskId
     * @return
     */
    private TaskFormData getTaskFormData(String taskId) {
        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        return taskFormData;
    }


    /*@RequestMapping(value = "/{taskId}/complete", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public boolean executeTaskAction(@PathVariable String taskId, @RequestBody TaskActionRequest actionRequest) {
        Map<String, Object> variables = new HashMap<>();
        if (!CollectionUtils.isEmpty(actionRequest.getVariables())) {
            for (RestVariable rv : actionRequest.getVariables()) {
                variables.put(rv.getName(), rv.getValue());
            }
        }

        // 将任务标记为已完成并继续流程执行。该方法通常由任务列表用户界面在任务表单由受让人提交并且提供了所需的任务参数之后调用。
        taskService.complete(taskId, variables);

        return true;
    }*/


    /**
     * 我参与的任务
     *
     * @return
     */
    @PostMapping(value = "/tasks")
    public DoneDto getTasks(@RequestBody DoneDto doneDto) throws ParseException {
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                // 仅限于以参数值作为子字符串分配给用户的任务。和MySQL的模糊查询类似
                // .taskAssignee(finishedEntity.getUserName())
                //.taskHadCandidateUser("")
                // 限制为在给定日期之前  启动的实例
                .startedBefore(doneDto.getStartedBefore())
                // 限制为在给定日期之后  启动的实例
                .startedAfter(doneDto.getStartedAfter())
                // 限制在给定日期之前  完成的实例
                .finishedBefore(doneDto.getFinishedBefore())
                // 限制在给定日期之后  完成的实例
                .finishedAfter(doneDto.getFinishedAfter());


        /*HistoricTaskInstanceQuery query1 = historyService.createHistoricTaskInstanceQuery()
                // 仅限于以参数值作为子字符串分配给用户的任务。和MySQL的模糊查询类似
                .taskAssignee(finishedEntity.getUserName())
                //.taskHadCandidateUser("")
                // 限制为在给定日期之前  启动的实例
                .startedBefore(finishedEntity.getStartedBefore())
                // 限制为在给定日期之后  启动的实例
                .startedAfter(finishedEntity.getStartedAfter())
                // 限制在给定日期之前  完成的实例
                .finishedBefore(finishedEntity.getFinishedBefore())
                // 限制在给定日期之后  完成的实例
                .finishedAfter(finishedEntity.getFinishedAfter());*/

        if (doneDto.getFinished()) {
            // 只选择已完成的历史任务实例。
            query.finished();
        } else {
            query.unfinished();
        }
        ExecutionQuery executionQuery1 = runtimeService.createExecutionQuery();
        List<Execution> list1 = executionQuery1.list();
        System.out.println(list1);


        List<HistoricTaskInstance> historicTaskInstanceList = query.listPage((doneDto.getCurrentPage() - 1) * doneDto.getPageSize(), doneDto.getPageSize());
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
            ExecutionQuery executionQuery = runtimeService.createExecutionQuery().executionId(historicTaskInstance.getRootProcessInstanceId());
            List<Execution> list = executionQuery.list();
            Task task = taskService.newTask("");
            // TaskQuery taskQuery = taskService.createTaskQuery().executionId(historicTaskInstance.getExecutionId());
            TaskQuery taskQuery = taskService.createTaskQuery()
                    //.taskDefinitionKey(historicTaskInstance.getTaskDefinitionKey())
                    .processInstanceId(historicTaskInstance.getProcessInstanceId());
            //.processDefinitionId(historicTaskInstance.getProcessDefinitionId())
            // taskDefinitionKey

            //Object obj = formService.getRenderedTaskForm(historicTaskInstance.getProcessInstanceId());
            //VariableMap map = formService.getStartFormVariables(historicTaskInstance.getProcessInstanceId());

            List<Task> taskList = taskQuery.list();
            System.out.println(taskList.size());

            System.out.println(list.size());
        }
        doneDto.setList(historicTaskInstanceList);
        // 设置总条数
        doneDto.setTotal(query.list().size());


        return doneDto;
    }


    /**
     * @Description: 指定用户，或者角色，或者岗位
     * @return: type类型  1 用户  2 角色  3 岗位
     * @Author: lx
     * @date: 2020-09-11 15:52
     */
    public ResponseData appointType(String type, String appointId, String taskId) {
        //根据获取到的类型和id匹配去查找对应的人员数据后赋值给节点审批人
        ApprovalTypeEnum userType = ApprovalTypeEnum.getEnumByValue(type);
        switch (userType) {
            case SPECIFIED_USER:
                //指定人员，直接赋值
                taskService.setAssignee(taskId, appointId);
                break;
            case ROLE:
                //指定角色，通过角色查找对应人员赋值(Assignee不设置空，候选人查不到)
                ResponseData<List<IapSysUserTDto>> responseData =
                        roleService.queryAllUserListByRoleId(appointId);
                if (responseData != null && responseData.getData() != null) {
                    List<IapSysUserTDto> userList = responseData.getData();
                    for (IapSysUserTDto user : userList) {
                        taskService.addCandidateUser(taskId, user.getId());
                    }
                } else {
                    return ResponseData.error("该角色对应用户为空，请检查,角色id" + appointId);
                }
                break;
            case JOBS:
                //指定岗位，通过岗位查找对应人员赋值(Assignee不设置空，候选人查不到)
                ResponseData<List<IapSysUserTDto>> userResponseData =
                        positionService.queryUserById(appointId);
                if (userResponseData != null && userResponseData.getData() != null) {
                    List<IapSysUserTDto> userList = userResponseData.getData();
                    for (IapSysUserTDto user : userList) {
                        taskService.addCandidateUser(taskId, user.getId());
                    }
                } else {
                    return ResponseData.error("该岗位对应用户为空，请检查,岗位id" + appointId);
                }
                break;

            default:
                return ResponseData.error("节点审批未匹配到审批类型,获取到的参数：approvalType：" + type
                        + "，approvalId：" + appointId);
        }
        return null;
    }


    /**
     * 我的待办已办列表
     * 127.0.0.1:11114/myTaskController/tasks
     * 入参
     {
     "currentPage":1,
     "pageSize":10,
     "finished":true,完成状态：true已完成、false未完成(默认)
     "startedBefore":"2020-07-06T16:00:00.000Z",启动结束时间
     "startedAfter":"2020-07-06T16:00:00.000Z",启动开始时间
     "finishedBefore":"2020-07-06T16:00:00.000Z",完成结束时间
     "finishedAfter":"2020-07-06T16:00:00.000Z"完成开始时间
     }
     *
     */

    /**
     * 流程图 查实例 processInstanceId = 92ef53ac-bc13-11ea-9948-68f72816b6ed
     * 127.0.0.1:11114/rest/process-instance/{processInstanceId}/activity-instances
     * get请求
     *
     */


    /**
     * 流程图 查定义 processDefinitionId = test02:11:35903d61-bc12-11ea-9948-68f72816b6ed
     * 127.0.0.1:11114/rest/process-definition/{processDefinitionId}/xml
     * get请求
     *
     */


    /**
     * 获取用户的代办任务(受让人)
     *
     * @return
     */
    @ApiOperation("获取用户的等待办理任务")
    @PostMapping("/getTodos")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseData<TodoDto> getTodos(@NonNull @RequestBody TodoDto todoDto) {
        //通过userId获取中文名
        ResponseData<IapSysUserTDto> iapSysUserDtoResponseData = userService.queryByName(userUtil.getUser().getUserName());
        IapSysUserTDto user = iapSysUserDtoResponseData.getData();
        todoDto.setUserName(user.getUserName());
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(todoDto.getUserName());
        if (null != todoDto.getCreatedAfter()) {
            taskQuery.taskCreatedBefore(todoDto.getCreatedAfter());
        }
        if (null != todoDto.getCreatedBefore()) {
            taskQuery.taskCreatedAfter(todoDto.getCreatedBefore());
        }
        if (StringUtils.isNotBlank(todoDto.getTaskName())) {
            taskQuery.processVariableValueLike("processName", "%" + todoDto.getTaskName() + "%");
        }
        if (StringUtils.isNotBlank(todoDto.getBusinessKey())) {
            taskQuery.processVariableValueLike("businessKey", "%" + todoDto.getBusinessKey() + "%");
        }

        //查询查出我的待办
        List<Task> list = taskQuery.orderByTaskCreateTime().desc()
                .listPage(todoDto.getPageSize() * (todoDto.getCurrentPage() - 1)
                        , todoDto.getPageSize());
        long total = taskQuery.count();
        //初始化结果集
        list.parallelStream().forEachOrdered(todo -> {
            BusinessTaskEntity businessTaskEntity = new BusinessTaskEntity(todo);
            businessTaskEntity.setVariables(taskService.getVariables(todo.getId()));
            //businessTaskEntity.getVariables().put("formKey", getTaskFormData(todo.getId()).getFormKey());
            String formKey = (String) businessTaskEntity.getVariables()
                    .get(todo.getTaskDefinitionKey() + "_formKey");
            if (formKey != null && !"".equals(formKey)) {
                businessTaskEntity.getVariables().put("formKey", formKey);
            }
            todoDto.getBusinessTaskEntities().add(businessTaskEntity);
            log.info("--->>>" + todo.getName());

            //

        });

        todoDto.setTotal(Long.valueOf(total).intValue());
        return ResponseData.success(todoDto);
    }

    /**
     * 获取用户的待领任务(候选人)
     *
     * @return
     */
    @ApiOperation("获取用户的待领任务")
    @PostMapping("/getTodosReceives")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseData<TodoDto> getTodosReceives(@NonNull @RequestBody TodoDto todoDto) {
        //通过userId获取中文名
        ResponseData<IapSysUserTDto> iapSysUserDtoResponseData = userService.queryByName(userUtil.getUser().getUserName());
        IapSysUserTDto user = iapSysUserDtoResponseData.getData();
        todoDto.setUserName(user.getUserName());
        TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateUser(todoDto.getUserName());
        if (null != todoDto.getCreatedAfter()) {
            taskQuery.taskCreatedBefore(todoDto.getCreatedAfter());
        }
        if (null != todoDto.getCreatedBefore()) {
            taskQuery.taskCreatedAfter(todoDto.getCreatedBefore());
        }
        if (StringUtils.isNotBlank(todoDto.getTaskName())) {
            taskQuery.processVariableValueLike("processName", "%" + todoDto.getTaskName() + "%");
        }
        if (StringUtils.isNotBlank(todoDto.getBusinessKey())) {
            taskQuery.processVariableValueLike("businessKey", "%" + todoDto.getBusinessKey() + "%");
        }

        //查询查出我的待办
        List<Task> list = taskQuery.orderByTaskCreateTime().desc()
                .listPage(todoDto.getPageSize() * (todoDto.getCurrentPage() - 1)
                        , todoDto.getPageSize());
        long total = taskQuery.count();
        //初始化结果集
        list.parallelStream().forEachOrdered(todo -> {
            BusinessTaskEntity businessTaskEntity = new BusinessTaskEntity(todo);
            businessTaskEntity.setVariables(taskService.getVariables(todo.getId()));
            //businessTaskEntity.getVariables().put("formKey", getTaskFormData(todo.getId()).getFormKey());
            String formKey = (String) businessTaskEntity.getVariables()
                    .get(todo.getTaskDefinitionKey() + "_formKey");
            if (formKey != null && !"".equals(formKey)) {
                businessTaskEntity.getVariables().put("formKey", formKey);
            }
            todoDto.getBusinessTaskEntities().add(businessTaskEntity);
            log.info("--->>>" + todo.getName());
        });

        todoDto.setTotal(Long.valueOf(total).intValue());
        return ResponseData.success(todoDto);
    }


    /**
     * 流程任务领取
     *
     * @param entity 请求对象
     * @return
     */
    @ApiOperation("流程任务领取")
    @PostMapping("/toReceive")
    public ResponseData<Boolean> toReceive(@NonNull @RequestBody BusinessTaskEntity entity) {
        try {
            ResponseData<IapSysUserTDto> iapSysUserDtoResponseData = userService.queryByName(userUtil.getUser().getUserName());
            IapSysUserTDto user = iapSysUserDtoResponseData.getData();

            Task task = taskService.createTaskQuery().taskId(entity.getId()).singleResult();
            //设置当前处理人完成任务领取
            if (StringUtils.isEmpty(task.getAssignee())) {
                taskService.claim(entity.getId(), user.getUserName());
            }
            //taskService.createComment(task.getId(), task.getProcessInstanceId(), user.getUserName() + "领取了任务");
            return ResponseData.success(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseData.success(Boolean.FALSE);
    }


    /**
     * 流程任务删除
     *
     * @param taskId    012d926c-bb6b-11ea-9422-68f72816b6ed
     * @param variables
     * @return
     */
    @ApiOperation("流程删除")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/deleteTaskById/{taskId}")
    public ResponseData<Boolean> deleteTaskById(@PathVariable("taskId") String taskId
            , @RequestBody Map<String, Object> variables, @RequestParam("message") String message) {
        try {
            //TODO 后续将用户缓存redis中
            ResponseData<IapSysUserTDto> iapSysUserDtoResponseData =
                    userService.queryByName(userUtil.getUser().getUserName());
            IapSysUserTDto user = iapSysUserDtoResponseData.getData();

            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            //设置当前处理人完成任务领取
            if (StringUtils.isEmpty(task.getAssignee())) {
                taskService.claim(taskId, user.getUserName());
            }
            //全局变量设置删除为true，走删除节点
            taskService.setVariable(taskId, "toDelete", true);

            //操作类型
            variables.put(task.getId() + "_opType", "approve");
            variables.put("nowApprove", "approve");
            //执行通过
            taskService.complete(taskId, variables);
            //为当前任务创建意见信息
            taskService.createComment(taskId, task.getProcessInstanceId(), message);
            return ResponseData.success(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseData.success(Boolean.FALSE);
    }

    /**
     * 根据ID获取任务  012d926c-bb6b-11ea-9422-68f72816b6ed
     *
     * @param instanceId
     * @return
     */
    @ApiOperation("根据ID获取任务")
    @GetMapping("/getActivityInstances/{instanceId}")
    public ResponseData<ActivityInstanceDto> getActivityInstances(
            @PathVariable("instanceId") String instanceId) {
        ActivityInstance activityInstance = null;
        ActivityInstanceDto result = null;
        try {
            activityInstance = runtimeService.getActivityInstance(instanceId);
        } catch (AuthorizationException var4) {
            return ResponseData.success(null);
        } catch (ProcessEngineException var5) {
            return ResponseData.success(null);
        }
        if (activityInstance != null) {
            result = ActivityInstanceDto.fromActivityInstance(activityInstance);
        }
        return ResponseData.success(result);
    }

}
