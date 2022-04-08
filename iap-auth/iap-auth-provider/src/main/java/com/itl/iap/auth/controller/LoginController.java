package com.itl.iap.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api("Auth-登录控制层")
@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ApiOperation(value = "登录", notes = "登录")
    public String loginView() {
        return "login";
    }
}
