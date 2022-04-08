package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.TeamHandleBO;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.entity.ProductLine;
import com.itl.mes.core.api.entity.Team;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.service.ProductLineService;
import com.itl.mes.core.api.service.TeamMemberService;
import com.itl.mes.core.api.service.TeamService;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.EmployeeVo;
import com.itl.mes.core.api.vo.TeamVo;
import com.itl.mes.core.provider.mapper.TeamMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 班组信息主表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-25
 */
@Service
@Transactional
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {


    @Autowired
    private TeamMapper teamMapper;
	
	@Autowired
    private CustomDataValService customDataValService;
    @Autowired
    private TeamMemberService teamMemberService;
    @Autowired
    private ProductLineService productLineService;
    @Resource
    private UserUtil userUtil;

    @Override
    public List<Team> selectList() {
        QueryWrapper<Team> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, team);
        return super.list(entityWrapper);
    }
	
	
	
	 @Override
     @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void save(TeamVo teamVo) throws CommonException {
        TeamHandleBO teamHandleBO = new TeamHandleBO(UserUtils.getSite(),teamVo.getTeam());
        Team teamEntity = teamMapper.selectById(teamHandleBO.getBo());
        String productLineBO =null;
        if(teamVo.getProductLine() != null){
            ProductLine productLine = productLineService.selectByProductLine(teamVo.getProductLine());
            productLineBO = productLine.getBo();
        }
        String employeeCode = null;
        if(teamVo.getLeader()!=null){
           EmployeeVo employee = teamMapper.getEmployee(teamVo.getLeader());
           if(employee==null)throw new CommonException("员工:"+teamVo.getLeader()+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
            employeeCode = employee.getEmployeeCode();
        }
        if(teamEntity==null){
            Team team = new Team();
            BeanUtils.copyProperties(teamVo,team);
            team.setLeader(employeeCode);
            team.setBo(teamHandleBO.getBo());
            team.setProductLineBo(productLineBO);
            team.setSite(UserUtils.getSite());
            team.setObjectSetBasicAttribute(userUtil.getUser().getUserName(),new Date());
            teamMapper.insert(team);
        }else{
            Team team = new Team();
            BeanUtils.copyProperties(teamVo,team);
            team.setLeader(employeeCode);
            team.setBo(teamHandleBO.getBo());
            team.setSite(UserUtils.getSite());
            team.setProductLineBo(productLineBO);
            team.setCreateDate(teamEntity.getCreateDate());
            team.setCreateUser(teamEntity.getCreateUser());
            team.setModifyDate(new Date());
            team.setModifyUser(userUtil.getUser().getUserName());
            teamMapper.updateById(team);
        }
        //保存自定义数据
        if(teamVo.getCustomDataValVoList()!=null){
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo(teamHandleBO.getBo());
            customDataValRequest.setSite(UserUtils.getSite());
            customDataValRequest.setCustomDataType(CustomDataTypeEnum.TEAM.getDataType());
            customDataValRequest.setCustomDataValVoList(teamVo.getCustomDataValVoList());
            customDataValService.saveCustomDataVal(customDataValRequest);
        }
         teamMemberService.save(teamHandleBO.getBo(),employeeCode,teamVo.getAssignedEmployeeVoList());
    }

    @Override
    public Team selectTeam(String team) throws CommonException {
        QueryWrapper<Team> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(Team.SITE,UserUtils.getSite());
        entityWrapper.eq(Team.TEAM,team);
        List<Team> groups = teamMapper.selectList(entityWrapper);
        if(groups.isEmpty()){
            throw new CommonException("班组:"+team+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }else{
            return groups.get(0);
        }

    }

    @Override
    public TeamVo getTeamVoByTeam(String team) throws CommonException {
        Team teamEntity = selectTeam(team);
        List<EmployeeVo> availableEmployeeList = teamMapper.getAvailableEmployeeList(UserUtils.getSite(),null, teamEntity.getBo());
        List<EmployeeVo> assignedEmployeeList = teamMapper.getAssignedEmployeeList(UserUtils.getSite(),teamEntity.getBo());
        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(UserUtils.getSite(), teamEntity.getBo(), CustomDataTypeEnum.TEAM.getDataType());
        TeamVo teamVo = new TeamVo();
        BeanUtils.copyProperties(teamEntity,teamVo);
        if(teamEntity.getProductLineBo()!=null){
            ProductLine productLine = productLineService.getById(teamEntity.getProductLineBo());
            teamVo.setProductLine(productLine.getProductLine());
        }
        teamVo.setCustomDataAndValVoList(customDataAndValVos);
        teamVo.setAvailbleEmployeeVoList(availableEmployeeList);
        teamVo.setAssignedEmployeeVoList(assignedEmployeeList);
        return teamVo;
    }

    @Override
    public List<EmployeeVo> getEmployeeVo(String employeeCode, String group) throws CommonException {
        List<EmployeeVo> availableEmployeeList = teamMapper.getAvailableEmployeeList(UserUtils.getSite(),employeeCode, group);
        return availableEmployeeList;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void delete(String group,Date modifyDate) throws CommonException {
        Team groupEntity = selectTeam(group);
        CommonUtil.compareDateSame(modifyDate,groupEntity.getModifyDate());
        teamMapper.deleteById(groupEntity.getBo());
        teamMemberService.delete(groupEntity.getBo());
    }


}