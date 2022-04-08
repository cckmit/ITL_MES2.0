/**
 * Copyright (C):  Evergrande Group
 * FileName: ProcessServiceImpl
 * Author:   huangjianming
 * Date:     2020-06-24 15:43
 * Description:
 */
package com.itl.iap.workflow.workflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.attachment.client.service.RoleService;
import com.itl.iap.attachment.client.service.UserService;
import com.itl.iap.workflow.workflow.service.ProcessService;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.model.xml.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流程过程实现类
 *
 * @author 黄建明
 * @date 2020-06-24
 * @since jdk1.8
 */
@Log4j2
@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    /**
     * 通过条件模糊查询员工列表
     * @param code 编码
     * @return IPage<Model>
     */
    @Override
    public IPage<Model> query(String code) {
        ResponseData<List<IapSysUserTDto>> obj = roleService.queryAllUserListByRoleId("49b2d0ba36f14210ad7a39a45e333111");
        System.out.println(JSON.toJSON(obj));
        return null;
    }
}
