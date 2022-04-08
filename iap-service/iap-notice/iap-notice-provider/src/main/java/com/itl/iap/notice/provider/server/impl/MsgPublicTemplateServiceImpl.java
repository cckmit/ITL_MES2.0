package com.itl.iap.notice.provider.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.util.UUID;

import com.itl.iap.notice.api.dto.IapDictItemTDto;
import com.itl.iap.notice.api.dto.MsgPublicTemplateDto;
import com.itl.iap.notice.api.entity.MsgPublicTemplate;
import com.itl.iap.notice.api.service.MsgPublicTemplateService;
import com.itl.iap.notice.provider.mapper.MsgPublicTemplateMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * 公共消息模板(包含邮件、系统消息、短信模板等，如果是短信模板类型，则需要关联短信消息模板表)(MsgPublicTemplate)表服务实现类
 *
 * @author liaochengdian
 * @date 2020-03-25
 * @since jdk1.8
 */
@Service("msgPublicTemplateService")
public class MsgPublicTemplateServiceImpl extends ServiceImpl<MsgPublicTemplateMapper, MsgPublicTemplate> implements MsgPublicTemplateService {

    @Resource
    private MsgPublicTemplateMapper msgPublicTemplateMapper;

    /**
     * 根据id查询
     *
     * @param id 消息模板id
     * @return MsgPublicTemplateDto
     */
    @Override
    public MsgPublicTemplateDto getById(String id) {
        return msgPublicTemplateMapper.getById(id,
                MsgPublicTemplateDto.SERVICE_MODULE,
                MsgPublicTemplateDto.NOTICE_TYPE_CODE);
    }

    /**
     * 根据编码查询
     *
     * @param code 编码
     * @return MsgPublicTemplate
     */
    @Override
    public MsgPublicTemplate getMsgPublicTemplateByCode(String code) {
        return msgPublicTemplateMapper.getMsgPublicTemplateByCode(code);
    }

    @Override
    public boolean checkOwn(MsgPublicTemplate record) {
        QueryWrapper<MsgPublicTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", record.getCode());
        //此查询条件是验证修改操作时，编码验证
        if (!StringUtils.isEmpty(record.getId())) {
            queryWrapper.eq("id", record.getId());
        }
        List<MsgPublicTemplate> msgPublicTemplateList = msgPublicTemplateMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(msgPublicTemplateList)) {
            return true;
        }
        return false;
    }

    /**
     * 分页查询
     *
     * @param query 消息模板实例
     * @return IPage<MsgPublicTemplateDto>
     */
    @Override
    public IPage<MsgPublicTemplateDto> query(MsgPublicTemplateDto query) {
        if (query.getPage() == null) {
            query.setPage(new Page(0, 10));
        }
        //分割适用规则
        if (!StringUtils.isEmpty(query.getMessageTypeCode())) {
            String [] typeCodes = query.getMessageTypeCode().split(",");
            if (typeCodes.length != 0) {
                List<String> codes = new ArrayList<>(Arrays.asList(typeCodes));
                query.setTypeCodes(codes);
                query.setMessageTypeCode(null);
            }
        }
        IPage<MsgPublicTemplateDto> msgPublicTemplateDtoPage = msgPublicTemplateMapper.queryPage(query.getPage(), query,
                MsgPublicTemplateDto.SERVICE_MODULE,
                MsgPublicTemplateDto.NOTICE_TYPE_CODE);
        if (msgPublicTemplateDtoPage.getTotal() > 0) {
            //分割消息规则
            msgPublicTemplateDtoPage.getRecords().forEach(x -> {
                if (x.getMessageTypeCode() != null) {
                    String[] split = x.getMessageTypeCode().split(",");
                    StringBuilder str = new StringBuilder();
                    for (String s : split) {
                        if (!StringUtils.isEmpty(s)) {
                            str.append(msgPublicTemplateMapper.getNameByCodeAndVal("NX202006296492", s)).append(",");
                        }
                    }
                    if (str.toString().contains(",")) {
                        str = new StringBuilder(str.substring(0, str.length() - 1));
                    }
                    x.setMessageTypeName(str.toString());
                }
            });
        }
        return msgPublicTemplateDtoPage;
    }

    /**
     * 根据编码查询数据字典
     *
     * @param code 编码
     * @return List<IapDictItemTDto>
     */
    @Override
    public List<IapDictItemTDto> getCode(String code) {
        return msgPublicTemplateMapper.getCode(code);
    }

    /**
     * 修改消息模板
     *
     * @param record 消息模板实例
     */
    @Override
    public boolean update(MsgPublicTemplate record) {
        record.setUpdateTime(new Date());
        return updateById(record);
    }

    /**
     * 新增消息模板
     *
     * @param record 消息模板实例
     */
    @Override
    public void add(MsgPublicTemplate record) {
        if (StringUtils.isEmpty(record.getCode()) ||
                count(new QueryWrapper<MsgPublicTemplate>().eq("code", record.getCode())) != 0) {
            throw new RuntimeException("模板编码有误");
        }
        record.setId(UUID.uuid32());
        record.setCreateTime(new Date()).setCreateName("admin");
        msgPublicTemplateMapper.insert(record);
    }

}