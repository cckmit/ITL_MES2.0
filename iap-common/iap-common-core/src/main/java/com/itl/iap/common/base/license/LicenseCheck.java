package com.itl.iap.common.base.license;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 *
 *
 * @author 汤俊
 * @date 2020-1-9 14:40
 * @since 1.0.0
 */
@Slf4j
@Component
@Configuration
public class LicenseCheck {

    public LicenseCheck(){

    }

    @PostConstruct
    public void init() {
//        log.info("Start License Check...");
//        VerifyLicense vlicense = new VerifyLicense();
//        vlicense.install();
//        if (!vlicense.vertify()) {
//            //中断启动
//            Runtime.getRuntime().halt(1);
//        }
//        log.info("End License Check...");
    }
}
