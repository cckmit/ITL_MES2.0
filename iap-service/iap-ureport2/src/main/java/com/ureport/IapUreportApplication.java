package com.ureport;

import com.bstek.ureport.console.UReportServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@ImportResource("classpath:ureport-console-context.xml")
public class IapUreportApplication {

    public static void main(String[] args) {
        SpringApplication.run(IapUreportApplication.class, args);
    }

    @Bean
    public ServletRegistrationBean build(){
        return new ServletRegistrationBean(new UReportServlet(),"/ureport/*");
    }

}