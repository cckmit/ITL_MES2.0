package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.dto.CustomerDTO;
import com.itl.mes.core.api.entity.Customer;
import com.itl.mes.core.api.service.CustomerService;
import com.itl.mes.core.provider.mapper.CustomerMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 客户表服务实现类
 * @author Terence
 * 2021/01/18
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;


    @Override
    public IPage<CustomerDTO> queryForList(CustomerDTO customerDTO){
        if(customerDTO.getPage() == null){
            customerDTO.setPage(new Page(0,10));
        }

        return customerMapper.queryForList(customerDTO.getPage(), customerDTO);
    }

    @Override
    public boolean update(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);

            customer.setModifyDate(new Date());
            customer.setModifyUser(UserUtils.getCurrentUser().getUserName());
            return customerMapper.updateById(customer) != 0;
    }

    /**
     * 新增客户
     * @param customerDTO
     * @return
     * @throws CommonException
     */
    @Override
    public boolean add(CustomerDTO customerDTO) throws CommonException {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);

        /**
         * 检测同一站点的客户编码是否唯一
         */
        QueryWrapper<Customer> customerQuery = new QueryWrapper();
        customerQuery.eq("CUSTOMER", customer.getCustomer())
                .eq("SITE", customer.getSite());
        int customerCount = customerMapper.selectCount(customerQuery);
        if(customerCount > 0){
            throw new CommonException("同一站点的客户编码不唯一", CommonExceptionDefinition.SUP_EXCEPTION);
        }

        try{
            customer.setBo("CUSTOMER:"+customer.getSite()+","+customer.getCustomer());
            customer.setSite(UserUtils.getSite());
            customer.setCreateDate(new Date());
            customer.setCreateUser(UserUtils.getCurrentUser().getUserName());
            return customerMapper.insert(customer) != 0;
        }catch (Exception e){
            throw new CommonException();
        }
    }
}
