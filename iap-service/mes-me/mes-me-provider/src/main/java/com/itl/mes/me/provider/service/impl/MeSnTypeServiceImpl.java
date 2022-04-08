package com.itl.mes.me.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.me.api.entity.MeSnType;
import com.itl.mes.me.api.service.MeSnTypeService;
import com.itl.mes.me.provider.mapper.MeSnTypeMapper;
import org.springframework.stereotype.Service;

@Service("meSnTypeService")
public class MeSnTypeServiceImpl extends ServiceImpl<MeSnTypeMapper, MeSnType> implements MeSnTypeService {


}
