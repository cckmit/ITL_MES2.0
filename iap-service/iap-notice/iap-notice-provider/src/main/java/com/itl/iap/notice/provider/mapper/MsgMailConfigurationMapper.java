package com.itl.iap.notice.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.itl.iap.notice.api.dto.MsgMailConfigurationDto;
import com.itl.iap.notice.api.entity.MsgMailConfiguration;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 发送规则mapper
 *
 * @author 曾慧任
 * @date 2020-06-29
 * @since jdk1.8
 */
public interface MsgMailConfigurationMapper extends BaseMapper<MsgMailConfiguration> {

    /**
     * 根据id启用邮箱或短信
     * @param params 修改字段
     */
    void updateOther(Map<String, Object> params);

    /**
     * 分页查询
     * @param msgMailConfiguration 邮件配置实例
     * @param page 分页
     * @return IPage<MsgMailConfigurationDto>
     */
    IPage<MsgMailConfigurationDto> queryPage(Page page, @Param("query") MsgMailConfiguration msgMailConfiguration, @Param("type")String type);
}