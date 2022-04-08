package com.itl.iap.notice.provider.core.task;


import com.itl.iap.notice.provider.exchanger.AbstractNoticeExchanger;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 通知任务类
 *
 * @author 曾慧任
 * @date  2020-06-29
 * @since jdk1.8
 */
public class NoticeTask implements Callable<Boolean>{

    private AbstractNoticeExchanger abstractNoticeExchanger;
    private Map<String, Object> notice;

    public NoticeTask(AbstractNoticeExchanger abstractNoticeExchanger, Map<String, Object> notice){
        this.abstractNoticeExchanger = abstractNoticeExchanger;
        this.notice=notice;
    }
    @Override
    public Boolean call() throws Exception {
        return abstractNoticeExchanger.exchanger(notice);
    }
}
