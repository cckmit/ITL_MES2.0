package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.me.api.dto.RepairInputDto;
import com.itl.mes.me.api.dto.ScrapOrRepairFinDto;
import com.itl.mes.me.api.entity.MeSfcRepair;
import com.itl.mes.me.api.vo.RepairLogListVo;
import com.itl.mes.me.api.vo.RepairStationVo;
import com.itl.mes.me.api.vo.SendRepairObjVo;

import java.util.List;


/**
 * Sfc维修
 *
 * @author renren
 * @date 2021-01-25 14:43:26
 */
public interface MeSfcRepairService extends IService<MeSfcRepair> {

    RepairStationVo frontDataVo(String snCode);

    SendRepairObjVo saveInputRepair(List<RepairInputDto> repairInputList);

    Integer scrapOrRepairFinish(ScrapOrRepairFinDto scrapOrRepairFinDto);
}

