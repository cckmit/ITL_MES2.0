package com.itl.iap.notice.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.notice.api.dto.MsgMailConfigurationDto;
import com.itl.iap.notice.api.entity.MsgMailConfiguration;


import java.util.List;

/**
 * 发送规则Service
 *
 * @author 曾慧任
 * @date 2020-06-29
 * @since jdk1.8
 */
public interface MsgMailConfigurationService extends IService<MsgMailConfiguration> {

    /**
     * 根据ID判断新增修改
     * @param msgMailConfiguration 邮件配置实体
     */
    void addOrUpdate(MsgMailConfiguration msgMailConfiguration);

    /**
     * 根据id查询
     * @param id 邮件配置id
     * @return MsgMailConfiguration
     */
    MsgMailConfiguration findById(String id);

    /**
     * 根据id删除
     * @param id 邮件配置id
     */
    void delete(String id);

    /**
     * 分页查询
     * @param msgMailConfiguration 邮件配置实例
     * @param page 分页页数
     * @param pageSize 页面大小
     * @return IPage<MsgMailConfigurationDto>
     */
    IPage<MsgMailConfigurationDto> queryPage(MsgMailConfiguration msgMailConfiguration, Integer page, Integer pageSize);

    /**
     * 根据id启用邮箱或短信
     * @param id 邮件配置id
     * @param enable 启用状态
     */
    void startUsing(String id, Integer enable);

    /**
     * 批量删除发送规则
     * @param msgMailConfigurationList 发送规则列表
     * @return Boolean
     */
    Boolean deleteBatch(List<MsgMailConfiguration> msgMailConfigurationList);
}
