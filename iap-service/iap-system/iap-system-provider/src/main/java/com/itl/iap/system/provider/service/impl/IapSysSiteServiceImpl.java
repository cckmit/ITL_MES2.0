package com.itl.iap.system.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.system.provider.mapper.StationMapper;
import com.itl.mes.core.client.service.StationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class IapSysSiteServiceImpl {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private StationMapper stationMapper;

    public void changeSite(HttpSession session, HttpServletResponse response, String siteId) {
        if (StringUtils.isBlank(siteId)){
            throw new RuntimeException("请维护工厂");
        }
        String uuid = UUID.randomUUID().toString();
        Cookie userCookie = new Cookie("only", uuid);
        userCookie.setMaxAge(1 * 24 * 60 * 60);
        userCookie.setPath("/");
        response.addCookie(userCookie);
        redisTemplate.opsForValue().set("site-" + userUtil.getUser().getUserName() + ":" + uuid, siteId, 10, TimeUnit.HOURS);
    }

    public Map<String, Object> changeStation(HttpSession session, HttpServletResponse response, String stationId){
        log.info("工位ID==============>" + stationId);
        if (StringUtils.isBlank(stationId) || stationId.equals("undefined") || stationId.equals("null")){
            throw new RuntimeException("请维护工位");
        }
        Map<String, Object> byStation = stationMapper.getByStation(stationId,UserUtils.getSite());
        String workShop = null;
        String productLine = null;
        if (byStation != null) {
            workShop = StrUtil.toString(byStation.get("WORK_SHOP"));
            productLine = StrUtil.toString(byStation.get("PRODUCT_LINE"));
            if(productLine == null) {
                throw new RuntimeException("请维护产线");
            }
        }else {
            throw new RuntimeException("请维护产线");
        }
        String uuid = UUID.randomUUID().toString();
        Cookie userCookie = new Cookie("onlyStation", uuid);
        userCookie.setMaxAge(1 * 24 * 60 * 60);
        userCookie.setPath("/");
        response.addCookie(userCookie);
        redisTemplate.opsForValue().set("station-" + userUtil.getUser().getUserName() + ":" + uuid, stationId, 10, TimeUnit.HOURS);
        redisTemplate.opsForValue().set("workShop-" + userUtil.getUser().getUserName() + ":" + uuid, workShop, 10, TimeUnit.HOURS);
        redisTemplate.opsForValue().set("productLine-" + userUtil.getUser().getUserName() + ":" + uuid, productLine, 10, TimeUnit.HOURS);
        return byStation;
    }

    public void confirmSchedule(HttpSession session, HttpServletResponse response, String scheduleNo) {
        String uuid = UUID.randomUUID().toString();
        Cookie userCookie = new Cookie("onlyScheduleNo", uuid);
        userCookie.setMaxAge(1 * 24 * 60 * 60);
        userCookie.setPath("/");
        response.addCookie(userCookie);
        redisTemplate.opsForValue().set("schedultNo-" + userUtil.getUser().getUserName() + ":" + uuid, scheduleNo, 10, TimeUnit.HOURS);
    }
}
