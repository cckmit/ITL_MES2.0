package com.itl.iap.system.provider.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.attachment.api.dto.IapUploadFileDto;
import com.itl.iap.attachment.client.service.IapImUploadFileService;
import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.DtoUtils;
import com.itl.iap.common.util.PassWordUtil;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.system.api.dto.req.IapRoleUserQueryDTO;
import com.itl.iap.system.api.dto.req.IapRoleUserSaveDTO;
import com.itl.iap.system.api.entity.IapSysPositionUserT;
import com.itl.iap.system.api.entity.IapSysUserRoleT;
import com.itl.iap.system.api.entity.IapSysUserT;
import com.itl.iap.system.api.service.IapSysUserTService;
import com.itl.iap.system.provider.service.impl.IapSysSiteServiceImpl;
import com.itl.iap.system.provider.service.impl.IapSysUserRoleTServiceImpl;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/site")
public class IapSysSiteController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IapSysSiteServiceImpl iapSysSiteService;

    @PostMapping("/changeSite")
    @ApiOperation(value = "切换工厂", notes = "切换工厂")
    public ResponseData changeSite(HttpSession session, HttpServletResponse response, @RequestParam(value = "siteId") String siteId) {
        iapSysSiteService.changeSite(session,response,siteId);
        return ResponseData.success(true);
    }

    @PostMapping("/changeStation")
    @ApiOperation(value = "切换工位", notes = "切换工位")
    public ResponseData changeStation(HttpSession session, HttpServletResponse response, @RequestParam(value = "stationId") String stationId) {
        Map s = iapSysSiteService.changeStation(session, response, stationId);
        return ResponseData.success(s);
    }

    @PostMapping("/confirmSchedule")
    @ApiOperation(value = "确认选择排程", notes = "确认选择排程")
    public ResponseData confirmSchedule(HttpSession session, HttpServletResponse response, @RequestParam(value = "scheduleNo") String scheduleNo) {
        iapSysSiteService.confirmSchedule(session, response, scheduleNo);
        return ResponseData.success(true);
    }
}
