package com.itl.iap.notice.provider.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.itl.iap.notice.api.entity.MsgPublicTemplate;
import com.itl.iap.notice.api.entity.MsgSendRecord;
import com.itl.iap.notice.api.service.MsgSendRecordService;
import com.itl.iap.notice.api.service.NoticeService;
import com.itl.iap.notice.provider.core.task.NoticeTask;
import com.itl.iap.notice.provider.exchanger.AbstractNoticeExchanger;
import com.itl.iap.notice.provider.mapper.MsgPublicTemplateMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 发送通知实现类
 *
 * @author 曾慧任
 * @date 2020-06-29
 * @since jdk1.8
 */
@Service
@Slf4j
public class NoticeServiceImpl implements NoticeService, ApplicationContextAware {
    /**
     * 动态获取所有的消息适配器
     */
    private Collection<AbstractNoticeExchanger> exchangers;

    private ExecutorService executorService;
    @Autowired
    private MsgSendRecordService msgSendRecordService;
    @Resource
    private MsgPublicTemplateMapper msgPublicTemplateMapper;

    public NoticeServiceImpl() {
        Integer availableProcessors = Runtime.getRuntime().availableProcessors();
        Integer numOfThreads = availableProcessors * 2;
        executorService = new ThreadPoolExecutor(numOfThreads, numOfThreads, 0, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
    }

    /**
     * 发送消息
     *
     * @param notice
     */
    @Override
    public void sendMessage(Map<String, Object> notice) {
        log.info("don‘t use mq……get a message");
        if (notice.get("code") == null) {
            throw new RuntimeException("没有查找到模板");
        }
        QueryWrapper<MsgPublicTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", notice.get("code").toString());
        MsgPublicTemplate msgPublicTemplate = msgPublicTemplateMapper.selectOne(queryWrapper);
        if (msgPublicTemplate.getMessageTypeCode() == null) {
            throw new RuntimeException("没有查找到模板");
        }
        String[] array = msgPublicTemplate.getMessageTypeCode().split(",");
        for (int n = 0; n < array.length; n++) {
            if (StringUtils.isNotBlank(array[n])) {
                notice.put("type", array[n]);
                exchangers.forEach(item -> {
                    if (item.match(notice)) {
                        //开启线程池处理
                        executorService.submit(new NoticeTask(item, notice));
                    }
                });
            }
        }
    }

    /**
     * 更改状态为已读
     *
     * @param id
     */
    @Override
    public void updateReadFlag(String id) {
        MsgSendRecord param = new MsgSendRecord();
        param.setId(id);
        msgSendRecordService.updateReadFlag(param);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, AbstractNoticeExchanger> beansOfType = applicationContext.getBeansOfType(AbstractNoticeExchanger.class);
        this.exchangers = beansOfType.values();
    }

    @Override
    public Map getMsgSendRecordListByUser(String userId, Integer page, Integer pageSize) {

        Map map = msgSendRecordService.getMsgSendRecordListByUser(userId, page, pageSize);
        return map;
    }
}
