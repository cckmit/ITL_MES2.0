package com.xxl.job.admin.core.alarm;

import com.xxl.job.admin.core.model.IapXxlJobInfo;
import com.xxl.job.admin.core.model.IapXxlJobLog;

/**
 * @author xuxueli 2020-01-19
 */
public interface JobAlarm {

    /**
     * job alarm
     *
     * @param info
     * @param jobLog
     * @return
     */
    public boolean doAlarm(IapXxlJobInfo info, IapXxlJobLog jobLog);

}
