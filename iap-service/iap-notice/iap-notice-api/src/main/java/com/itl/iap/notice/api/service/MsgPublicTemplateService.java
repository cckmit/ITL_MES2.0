package com.itl.iap.notice.api.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.notice.api.dto.IapDictItemTDto;
import com.itl.iap.notice.api.dto.MsgPublicTemplateDto;
import com.itl.iap.notice.api.entity.MsgPublicTemplate;

import java.util.List;

/**
 * 公共消息模板Service
 *
 * @author liaochengdian
 * @date 2020-03-25
 * @since jdk1.8
 */
public interface MsgPublicTemplateService extends IService<MsgPublicTemplate> {

    /**
     * 根据id查询
     *
     * @param id 消息模板id
     * @return MsgPublicTemplateDto
     */
    MsgPublicTemplateDto getById(String id);

    /**
     * 根据编码查询
     *
     * @param code 编码
     * @return MsgPublicTemplate
     */
    MsgPublicTemplate getMsgPublicTemplateByCode(String code);

    boolean checkOwn(MsgPublicTemplate record);

    /**
     * 分页查询
     *
     * @param query 消息模板实例
     * @return IPage<MsgPublicTemplateDto>
     */
    IPage<MsgPublicTemplateDto> query(MsgPublicTemplateDto query);

    /**
     * 根据编码查询数据字典
     *
     * @param code 编码
     * @return List<IapDictItemTDto>
     */
    List<IapDictItemTDto> getCode(String code);

    /**
     * 修改消息模板
     *
     * @param record 消息模板实例
     */
    boolean update(MsgPublicTemplate record);

    /**
     * 新增消息模板
     *
     * @param record 消息模板实例
     */
    void add(MsgPublicTemplate record);
}
