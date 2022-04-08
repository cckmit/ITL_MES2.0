package com.itl.mes.andon.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.entity.IapSysUserT;
import com.itl.mes.andon.api.entity.AndonException;
import com.itl.mes.andon.api.entity.Station;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ExceptionMapper extends BaseMapper<AndonException> {
    int queryMaxId();

    String userVerify(@Param("param") Map<String, String> param);

    String selectMaxCode();

    IPage<AndonException> repairList(Page page, @Param("condition") String condition, @Param("userName") String userName);

    Station selectStationDescByStation(@Param("station") String station);

    List<IapSysUserT> selectRoleUserList(@Param("roleId") String roleId);
}
