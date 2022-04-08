package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.Team;
import com.itl.mes.core.api.vo.EmployeeVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 班组信息主表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-06-25
 */

public interface TeamMapper extends BaseMapper<Team> {
	
	  List<EmployeeVo> getAvailableEmployeeList(@Param("site") String site, @Param("employeeCode") String employeeCode, @Param("teamBO") String teamBO);

      List<EmployeeVo> getAssignedEmployeeList(@Param("site") String site,@Param("teamBO") String teamBO);

      EmployeeVo  getEmployee(@Param("employeeCode") String employeeCode);


}