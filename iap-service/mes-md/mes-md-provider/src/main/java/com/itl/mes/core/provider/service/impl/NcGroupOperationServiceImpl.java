package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.NcGroupOperation;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.service.NcGroupOperationService;
import com.itl.mes.core.api.service.OperationService;
import com.itl.mes.core.api.vo.NcGroupOperationVo;
import com.itl.mes.core.provider.mapper.NcGroupOperationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 不合格组工序表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-15
 */
@Service
@Transactional
public class NcGroupOperationServiceImpl extends ServiceImpl<NcGroupOperationMapper, NcGroupOperation> implements NcGroupOperationService {


    @Autowired
    private NcGroupOperationMapper ncGroupOperationMapper;
    @Autowired
    private OperationService operationService;

    @Override
    public List<NcGroupOperation> selectList() {
        QueryWrapper<NcGroupOperation> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, ncGroupOperation);
        return super.list(entityWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void save(String ncGroupBO, List<NcGroupOperationVo> ncGroupOperationVos) throws CommonException {
        delete(ncGroupBO);
        if(!StrUtil.isBlank(ncGroupBO)&&!ncGroupOperationVos.isEmpty()){
            for(NcGroupOperationVo groupOperationVo:ncGroupOperationVos){
                Operation operation = operationService.selectByOperation(groupOperationVo.getOperation(), groupOperationVo.getVersion());
                NcGroupOperation ncGroupOperation = new NcGroupOperation();
                ncGroupOperation.setNcGroupBo(ncGroupBO);
                ncGroupOperation.setOperationBo(operation.getBo());
                ncGroupOperationMapper.insert(ncGroupOperation);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void delete(String ncGroupBO) {
          if(!StrUtil.isBlank(ncGroupBO)){
              QueryWrapper<NcGroupOperation> entityWrapper = new QueryWrapper<>();
              entityWrapper.eq(NcGroupOperation.NC_GROUP_BO,ncGroupBO);
              ncGroupOperationMapper.delete(entityWrapper);
          }
    }


}