package com.itl.mes.core.provider.controller;

import cn.hutool.core.bean.BeanUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.StrNotNull;
import com.itl.mes.core.api.entity.Logic;
import com.itl.mes.core.api.service.LogicService;
import com.itl.mes.core.api.vo.LogicVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author space
 * @since 2019-11-21
 */
@RestController
@RequestMapping("/monopy/logics")
@Api(tags = " SQL语句表" )
public class LogicController {
    private final Logger logger = LoggerFactory.getLogger(LogicController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public LogicService logicService;

    @GetMapping("/getLogicByLogicNoAndVersion")
    @ApiOperation(value="逻辑编号和版本查询数据")
    public ResponseData<Logic> getLogicByLogicNoAndVersion(String logicNo, String version ) throws CommonException {

        StrNotNull.validateNotNull( logicNo,"逻辑编号不能为空" );
        StrNotNull.validateNotNull( version,"版本不能为空" );
        return ResponseData.success(logicService.getLogicByLogicNoAndVersion( logicNo, version ));

    }

    @PostMapping("/save")
    @ApiOperation(value="保存数据")
    public ResponseData<LogicVo> save(@RequestBody LogicVo logicVo ) throws CommonException {

        LogicVo result = new LogicVo();
        logicService.saveLogic( logicVo );
        Logic logic = logicService.getLogicByLogicNoAndVersion( logicVo.getLogicNo(), logicVo.getVersion() );
        BeanUtil.copyProperties( logic,result );
        return ResponseData.success(result);

    }

}