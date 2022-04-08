package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.MouldDto;
import com.itl.mes.core.api.entity.Mould;

import java.util.List;

public interface MouldService extends IService<Mould> {

   void insert(Mould mould);

   Mould getById(String bo);

   Mould returnMould(Mould mould);

   void batchDelete(List<String> bos) throws CommonException;

   IPage<Mould> queryPage(MouldDto mouldDto);
}
