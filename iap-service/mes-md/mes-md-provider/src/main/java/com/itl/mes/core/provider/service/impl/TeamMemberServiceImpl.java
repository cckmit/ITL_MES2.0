package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.mes.core.api.entity.TeamMember;
import com.itl.mes.core.api.service.TeamMemberService;
import com.itl.mes.core.api.vo.EmployeeVo;
import com.itl.mes.core.provider.mapper.TeamMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 班组成员表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-25
 */
@Service
@Transactional
public class TeamMemberServiceImpl extends ServiceImpl<TeamMemberMapper, TeamMember> implements TeamMemberService {


    @Autowired
    private TeamMemberMapper teamMemberMapper;

    @Override
    public List<TeamMember> selectList() {
        QueryWrapper<TeamMember> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, teamMember);
        return super.list(entityWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void save(String teamBO,String employee, List<EmployeeVo> assignedEmployeeVoList) throws CommonException {
        delete(teamBO);
        if(employee != null){
            TeamMember teamMember = new TeamMember();
            teamMember.setTeamBo(teamBO);
            List<TeamMember> teamMembers = selectTeamMembersByUserBO(employee);
            if(teamMembers != null&&teamMembers.size()>0)throw new CommonException("班组长在其他班组中已存在", CommonExceptionDefinition.BASIC_EXCEPTION);
            teamMember.setUserBo(employee);
            teamMemberMapper.insert(teamMember);
        }
        if(assignedEmployeeVoList!=null&&!assignedEmployeeVoList.isEmpty()){
            for(EmployeeVo employeeVo:assignedEmployeeVoList){
                if(employeeVo.getEmployeeCode().equals(employee)){}else  {
                    TeamMember teamMember = new TeamMember();
                    teamMember.setTeamBo(teamBO);
                    teamMember.setUserBo(employeeVo.getEmployeeCode());
                    List<TeamMember> teamMembers = selectTeamMembersByUserBO(employeeVo.getEmployeeCode());
                    if (teamMembers != null && teamMembers.size() > 0) throw new CommonException("班组长在其他班组中已存在", CommonExceptionDefinition.BASIC_EXCEPTION);
                    teamMemberMapper.insert(teamMember);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void delete(String teamBO) throws CommonException {
        if (StrUtil.isBlank(teamBO))throw new CommonException("班组不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        QueryWrapper<TeamMember> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(TeamMember.TEAM_BO,teamBO);
        teamMemberMapper.delete(entityWrapper);
    }

    @Override
    public List<TeamMember> selectTeamMembersByUserBO(String userBO) {
        QueryWrapper<TeamMember> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(TeamMember.USER_BO,userBO);
        List<TeamMember> teamMembers = teamMemberMapper.selectList(entityWrapper);
        return teamMembers;
    }


}