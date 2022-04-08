package com.itl.mes.core.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.CustomerDTO;
import com.itl.mes.core.api.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@Api(value = "客户操作类",tags = "客户操作类")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/queryList")
    @ApiOperation(value = "查询")
    public ResponseData<IPage<CustomerDTO>> queryList(@RequestBody CustomerDTO customerDto){
        return ResponseData.success(customerService.queryForList(customerDto));
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新")
    public ResponseData update(@RequestBody CustomerDTO customerDTO){
        return ResponseData.success(customerService.update(customerDTO));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增")
    public ResponseData add(@RequestBody CustomerDTO customerDTO){
        boolean addCustomer;
        try{
            addCustomer = customerService.add(customerDTO);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseData.error("新增失败");
        }
        return ResponseData.success(addCustomer);
    }

    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CustomerDTO customerDTO){
        return ResponseData.success(customerService.removeById(customerDTO.getBo()));
    }
}
