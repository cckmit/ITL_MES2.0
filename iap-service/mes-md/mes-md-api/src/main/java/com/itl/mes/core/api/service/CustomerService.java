package com.itl.mes.core.api.service;
/**
 * 客户表服务类
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.CustomerDTO;
import com.itl.mes.core.api.entity.Customer;


public interface CustomerService extends IService<Customer> {

    IPage<CustomerDTO> queryForList(CustomerDTO customerDTO);

    boolean update(CustomerDTO customerDTO);

    boolean add(CustomerDTO customerDTO) throws CommonException;
}
