package com.itl.mes.andon.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.andon.api.dto.AndonDetailDTO;
import com.itl.mes.andon.api.entity.Detail;
import com.itl.mes.andon.api.vo.DetailVo;

public interface DetailService extends IService<Detail> {
    Detail saveInUpdate(DetailVo detailVO);

    IPage<Detail> queryAndonDetail(AndonDetailDTO andonDetailDTO);
}
