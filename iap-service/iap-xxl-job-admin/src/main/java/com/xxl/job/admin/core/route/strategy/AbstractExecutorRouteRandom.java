package com.xxl.job.admin.core.route.strategy;

import com.xxl.job.admin.core.route.AbstractExecutorRouter;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.TriggerParam;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

/**
 * Created by xuxueli on 17/3/10.
 */
public class AbstractExecutorRouteRandom extends AbstractExecutorRouter {

    private static Random localRandom = new SecureRandom();

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        String address = addressList.get(localRandom.nextInt(addressList.size()));
        return new ReturnT<String>(address);
    }

}
