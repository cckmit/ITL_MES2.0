package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.CustomerDTO;
import com.itl.mes.core.api.entity.Customer;
import org.apache.ibatis.annotations.Param;

public interface CustomerMapper extends BaseMapper<Customer> {
    IPage<CustomerDTO> queryForList(Page page, @Param("customerDTO") CustomerDTO customerDTO);
}
