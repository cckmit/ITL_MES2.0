package com.itl.iap.mes.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.dto.FaultDto;
import com.itl.iap.mes.api.entity.Fault;
import com.itl.iap.mes.api.entity.Printer;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.config.Constant;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.FaultMapper;
import com.itl.iap.mes.provider.mapper.LovEntryRepository;
import com.itl.iap.mes.provider.mapper.PrintMapper;
import com.itl.iap.mes.provider.utils.ExcelReader;
import com.itl.iap.mes.provider.utils.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;


@Service
public class PrintServiceImpl {

    @Autowired
    private PrintMapper printMapper;


    public IPage<Printer> findList(String printerName,Integer pageNum,Integer pageSize) {
        Page page = new Page(pageNum,pageSize);
        return printMapper.findList(page, printerName);
    }


}
