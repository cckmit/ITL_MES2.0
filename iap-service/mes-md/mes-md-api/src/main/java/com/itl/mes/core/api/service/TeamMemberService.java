package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.TeamMember;
import com.itl.mes.core.api.vo.EmployeeVo;

import java.util.List;

/**
 * <p>
 * 班组成员表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-25
 */
public interface TeamMemberService extends IService<TeamMember> {

    List<TeamMember> selectList();

    void  save(String teamBO, String employee, List<EmployeeVo> assignedEmployeeVoList)throws CommonException;

    void  delete(String teamBO)throws CommonException;

    List<TeamMember> selectTeamMembersByUserBO(String userBO)throws CommonException;
}