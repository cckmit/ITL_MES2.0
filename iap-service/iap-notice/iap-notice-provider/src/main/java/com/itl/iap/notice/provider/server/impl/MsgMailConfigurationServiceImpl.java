package com.itl.iap.notice.provider.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.util.UUID;
import com.itl.iap.notice.api.dto.MsgMailConfigurationDto;
import com.itl.iap.notice.api.dto.MsgPublicTemplateDto;
import com.itl.iap.notice.api.entity.MsgMailConfiguration;
import com.itl.iap.notice.api.service.MsgMailConfigurationService;
import com.itl.iap.notice.provider.mapper.MsgMailConfigurationMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送规则实现类
 *
 * @author 曾慧任
 * @date 2020-06-29
 * @since jdk1.8
 */
@Service
public class MsgMailConfigurationServiceImpl extends ServiceImpl<MsgMailConfigurationMapper, MsgMailConfiguration> implements MsgMailConfigurationService {

    @Resource
    private MsgMailConfigurationMapper msgMailConfigurationMapper;

    /**
     * 根据ID判断新增修改
     *
     * @param msgMailConfiguration 邮件配置实体
     */
    @Override
    public void addOrUpdate(MsgMailConfiguration msgMailConfiguration) {
        if (StringUtils.isBlank(msgMailConfiguration.getId())) {
            int num = msgMailConfigurationMapper.selectCount(new QueryWrapper<>());
            msgMailConfiguration.setCode(MsgMailConfiguration.PREV_CODE + num);
            msgMailConfiguration.setId(UUID.uuid32());
            msgMailConfigurationMapper.insert(msgMailConfiguration);
        } else {
            msgMailConfigurationMapper.updateById(msgMailConfiguration);
        }
    }

    /**
     * 根据id查询
     *
     * @param id 邮件配置id
     * @return MsgMailConfiguration
     */
    @Override
    public MsgMailConfiguration findById(String id) {
        return msgMailConfigurationMapper.selectById(id);
    }

    /**
     * 根据id删除
     *
     * @param id 邮件配置id
     */
    @Override
    public void delete(String id) {
        msgMailConfigurationMapper.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param msgMailConfiguration 邮件配置实例
     * @param page                 分页页数
     * @param pageSize             页面大小
     * @return IPage<MsgMailConfigurationDto>
     */
    @Override
    public IPage<MsgMailConfigurationDto> queryPage(MsgMailConfiguration msgMailConfiguration, Integer page, Integer pageSize) {
        return msgMailConfigurationMapper.queryPage(new Page(page, pageSize), msgMailConfiguration, MsgPublicTemplateDto.TYPE);
    }

    /**
     * 根据id启用邮箱或短信
     *
     * @param id     邮件配置id
     * @param enable 启用状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startUsing(String id, Integer enable) {
        MsgMailConfiguration msgMailConfiguration = new MsgMailConfiguration();
        msgMailConfiguration.setId(id);
        msgMailConfiguration.setEnable(enable);
        msgMailConfigurationMapper.updateById(msgMailConfiguration);
        QueryWrapper<MsgMailConfiguration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).eq("enable", enable);
        MsgMailConfiguration otherMsgMailConfiguration = msgMailConfigurationMapper.selectOne(queryWrapper);
        Map<String, Object> params = new HashMap<>();
        params.put("type", otherMsgMailConfiguration.getType());
        params.put("id", id);
        params.put("enable", getOtherEnable(enable));
        msgMailConfigurationMapper.updateOther(params);
    }

    /**
     * 批量删除发送规则
     *
     * @param msgMailConfigurationList 发送规则列表
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteBatch(List<MsgMailConfiguration> msgMailConfigurationList) {
        if (msgMailConfigurationList != null && !msgMailConfigurationList.isEmpty()) {
            List<String> ids = new ArrayList<>();
            for (MsgMailConfiguration msgMailConfiguration : msgMailConfigurationList) {
                if (msgMailConfiguration != null && msgMailConfiguration.getId() != null) {
                    ids.add(msgMailConfiguration.getId());
                }
            }
            msgMailConfigurationMapper.deleteBatchIds(ids);
            return true;
        }
        return false;
    }

    public Integer getOtherEnable(Integer enable) {
        if (MsgMailConfiguration.NO_ENABLE.equals(enable)) {
            return MsgMailConfiguration.ENABLE;
        }
        return MsgMailConfiguration.NO_ENABLE;
    }

}
