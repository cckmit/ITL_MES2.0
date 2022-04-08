package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.SiteUsr;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工厂用户关系维护 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-07-17
 */
@Repository
public interface SiteUsrMapper extends BaseMapper<SiteUsr> {

    List<Map<String,Object>> getSiteUsrByUsr(@Param("usr") String usr);

    Map<String,Object> getUsrInfoByUsr(@Param("usr") String usr);

    Map<String, Object> getUsrInfoByUsrByCard(@Param("userCardNumber")String usr);

    List<Map<String, Object>> getSiteUsrByCard(@Param("userCardNumber")String usr);
}