package com.xxl.job.admin.controller;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

@Component
public class Test{



    @XxlJob("test")
    public ReturnT<String> demo(String param) {
        System.out.print(1111111);
        return ReturnT.SUCCESS;
    }

}
