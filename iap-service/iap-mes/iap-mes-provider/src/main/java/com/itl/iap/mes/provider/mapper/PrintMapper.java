package com.itl.iap.mes.provider.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.dto.FaultDto;
import com.itl.iap.mes.api.entity.Fault;
import com.itl.iap.mes.api.entity.Printer;
import org.apache.ibatis.annotations.Param;

public interface PrintMapper extends BaseMapper<Printer> {

    IPage<Printer> findList(Page page, @Param("printerName") String printerName);
}
