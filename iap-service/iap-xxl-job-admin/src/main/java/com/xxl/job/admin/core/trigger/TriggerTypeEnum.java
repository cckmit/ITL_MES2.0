package com.xxl.job.admin.core.trigger;

import com.xxl.job.admin.core.util.I18nUtil;

/**
 * trigger type enum
 *
 * @author xuxueli 2018-09-16 04:56:41
 */
public enum TriggerTypeEnum {

    /**
     * jobconf_trigger_type_manual
     */
    MANUAL(I18nUtil.getString("jobconf_trigger_type_manual")),
    /**
     * jobconf_trigger_type_cron
     */
    CRON(I18nUtil.getString("jobconf_trigger_type_cron")),
    /**
     * jobconf_trigger_type_retry
     */
    RETRY(I18nUtil.getString("jobconf_trigger_type_retry")),
    /**
     * jobconf_trigger_type_parent
     */
    PARENT(I18nUtil.getString("jobconf_trigger_type_parent")),
    /**
     * jobconf_trigger_type_api
     */
    API(I18nUtil.getString("jobconf_trigger_type_api"));

    private TriggerTypeEnum(String title){
        this.title = title;
    }
    private String title;
    public String getTitle() {
        return title;
    }

}
