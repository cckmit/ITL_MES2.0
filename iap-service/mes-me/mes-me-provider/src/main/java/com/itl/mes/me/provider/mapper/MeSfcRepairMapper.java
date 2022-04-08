package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.me.api.entity.MeSfcRepair;
import com.itl.mes.me.api.vo.RepairLogListVo;
import com.itl.mes.me.api.vo.SendRepairDetailsVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Sfc维修
 *
 * @author renren
 * 2021-01-25 14:43:26
 */
@Repository
public interface MeSfcRepairMapper extends BaseMapper<MeSfcRepair> {

    List<SendRepairDetailsVo> getSendRepairDetailsVo(@Param("listBo") List<String> listBo, @Param("snCode") String snCode);

    List<RepairLogListVo> getRepairLogListVo(@Param("sn") String sn);
}
