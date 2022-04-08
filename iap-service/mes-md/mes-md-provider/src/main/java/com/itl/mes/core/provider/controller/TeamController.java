package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.entity.Team;
import com.itl.mes.core.api.service.TeamService;
import com.itl.mes.core.api.vo.EmployeeVo;
import com.itl.mes.core.api.vo.TeamVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * @author space
 * @since 2019-06-25
 */
@RestController
@RequestMapping("/teams")
@Api(tags = " 班组信息主表" )
public class TeamController {
    private final Logger logger = LoggerFactory.getLogger(TeamController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public TeamService teamService;

    /**
    * 根据id查询
    *
    * @param id 主键
    * @return
    */
    @GetMapping("/{id}")
    @ApiOperation(value="通过主键查询数据")
    public ResponseData<Team> getTeamById(@PathVariable String id) {
        return ResponseData.success(teamService.getById(id));
    }
   
   
    
	    @PostMapping("/save")
    @ApiOperation(value = "保存班组信息")
    public ResponseData<TeamVo> save(@RequestBody TeamVo teamVo) throws CommonException {
        teamService.save(teamVo);
        teamVo =  teamService.getTeamVoByTeam(teamVo.getTeam());
        return ResponseData.success(teamVo);
    }

    @GetMapping("/query")
    @ApiOperation(value = "查询班组信息")
    public ResponseData<TeamVo> getTeamVoByTeam(String team) throws CommonException {
        TeamVo groupVoByGrooup = teamService.getTeamVoByTeam(team);
        return ResponseData.success(groupVoByGrooup);
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除班组信息")
    public ResponseData<String> delete(String team,String modifyDate) throws CommonException {
        teamService.delete(team, DateUtil.parse(modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS));
        return ResponseData.success("success");
    }

    @GetMapping("/getEmployeeVo")
    @ApiOperation(value = "获取成员信息")
    public ResponseData<List<EmployeeVo>> getEmployeeVo(String employeeCode) throws CommonException {
        List<EmployeeVo> employeeVo = teamService.getEmployeeVo(employeeCode, null);
        return ResponseData.success(employeeVo);
    }
    
}