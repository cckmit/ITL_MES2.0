package com.xxl.job.admin.core.route;

import com.xxl.job.admin.core.route.strategy.*;
import com.xxl.job.admin.core.util.I18nUtil;

/**
 * Created by xuxueli on 17/3/10.
 */
public enum ExecutorRouteStrategyEnum {

    /**
     * jobconf_route_first
     */
    FIRST(I18nUtil.getString("jobconf_route_first"), new AbstractExecutorRouteFirst()),
    /**
     * jobconf_route_last
     */
    LAST(I18nUtil.getString("jobconf_route_last"), new AbstractExecutorRouteLast()),
    /**
     * jobconf_route_round
     */
    ROUND(I18nUtil.getString("jobconf_route_round"), new AbstractExecutorRouteRound()),
    /**
     * jobconf_route_random
     */
    RANDOM(I18nUtil.getString("jobconf_route_random"), new AbstractExecutorRouteRandom()),
    /**
     * jobconf_route_consistenthash
     */
    CONSISTENT_HASH(I18nUtil.getString("jobconf_route_consistenthash"), new AbstractExecutorRouteConsistentHash()),
    /**
     * jobconf_route_lfu
     */
    LEAST_FREQUENTLY_USED(I18nUtil.getString("jobconf_route_lfu"), new AbstractExecutorRouteLFU()),
    /**
     * jobconf_route_lru
     */
    LEAST_RECENTLY_USED(I18nUtil.getString("jobconf_route_lru"), new AbstractExecutorRouteLRU()),
    /**
     * jobconf_route_failover
     */
    FAILOVER(I18nUtil.getString("jobconf_route_failover"), new AbstractExecutorRouteFailover()),
    /**
     * jobconf_route_busyover
     */
    BUSYOVER(I18nUtil.getString("jobconf_route_busyover"), new AbstractExecutorRouteBusyover()),
    /**
     * jobconf_route_shard
     */
    SHARDING_BROADCAST(I18nUtil.getString("jobconf_route_shard"), null);

    ExecutorRouteStrategyEnum(String title, AbstractExecutorRouter router) {
        this.title = title;
        this.router = router;
    }

    private String title;
    private AbstractExecutorRouter router;

    public String getTitle() {
        return title;
    }
    public AbstractExecutorRouter getRouter() {
        return router;
    }

    public static ExecutorRouteStrategyEnum match(String name, ExecutorRouteStrategyEnum defaultItem){
        if (name != null) {
            for (ExecutorRouteStrategyEnum item: ExecutorRouteStrategyEnum.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }

}
