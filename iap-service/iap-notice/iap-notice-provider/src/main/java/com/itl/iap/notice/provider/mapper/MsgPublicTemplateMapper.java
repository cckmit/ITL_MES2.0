package com.itl.iap.notice.provider.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.itl.iap.notice.api.dto.IapDictItemTDto;
import com.itl.iap.notice.api.dto.MsgPublicTemplateDto;
import com.itl.iap.notice.api.entity.MsgPublicTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公共消息模板(包含邮件、系统消息、短信模板等，如果是短信模板类型，则需要关联短信消息模板表)(MsgPublicTemplate)表数据库访问层
 *
 * @author liaochengdian
 * @date 2020-03-25
 * @since jdk1.8
 */
public interface MsgPublicTemplateMapper extends BaseMapper<MsgPublicTemplate> {

    /**
     * 根据编码查询
     *
     * @param code 编码
     * @return MsgPublicTemplate
     */
    MsgPublicTemplate getMsgPublicTemplateByCode(@Param("code") String code);

    /**
     * 分页查询
     *
     * @param query 消息模板实例
     * @return IPage<MsgPublicTemplateDto>
     */
    IPage<MsgPublicTemplateDto> queryPage(Page page, @Param("query") MsgPublicTemplateDto query,
                                          @Param("serviceModule") String serviceModule,
                                          @Param("noticeTypeCode") String noticeTypeCode);

    /**
     * 根据编码和值查询字典名称
     * @param code 编码
     * @param val 值
     * @return String
     */
    String getNameByCodeAndVal(@Param("code") String code,@Param("val") String val);

    /**
     * 根据编码查询数据字典
     *
     * @param code 编码
     * @return List<IapDictItemTDto>
     */
    List<IapDictItemTDto> getCode(@Param("code") String code);

    /**
     * 根据id查询
     *
     * @param id 消息模板id
     * @return MsgPublicTemplateDto
     */
    MsgPublicTemplateDto getById(@Param("id") String id,
                                 @Param("serviceModule") String serviceModule,
                                 @Param("noticeTypeCode") String noticeTypeCode);
}