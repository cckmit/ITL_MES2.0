package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.QualityCheckList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface QualityCheckListMapper extends BaseMapper<QualityCheckList> {

    String userVerify(@Param("param") Map<String, String> param);

    String selectMaxCode();

    List<QualityCheckList> selectQualityInfo(@Param("checkDevice") String checkDevice, @Param("operationBo") String operationBo, @Param("shopOrder") String shopOrder);
}
