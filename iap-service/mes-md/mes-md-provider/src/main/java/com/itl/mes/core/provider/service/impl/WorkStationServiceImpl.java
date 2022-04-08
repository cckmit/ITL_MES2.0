package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.OperationHandleBO;
import com.itl.mes.core.api.bo.WorkStationHandleBO;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.dto.WorkStationDTO;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.entity.WorkStation;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.service.OperationService;
import com.itl.mes.core.api.service.WorkStationService;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.WorkStationVo;
import com.itl.mes.core.provider.mapper.WorkStationMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工步表 服务实现类
 * </p>
 *
 * @author space
 * @since 2021-03-11
 */
@Service
@Transactional
public class WorkStationServiceImpl extends ServiceImpl<WorkStationMapper, WorkStation> implements WorkStationService {

    @Autowired
    WorkStationService workStationService;
    @Autowired
    WorkStationMapper workStationMapper;
    @Autowired
    OperationService operationService;
    @Autowired
    private CustomDataValService customDataValService;
    @Resource
    private UserUtil userUtil;

    @Override
    public void saveByWorkStationVo(WorkStationVo workStationVo) throws CommonException {
//        QueryWrapper wrapper = new QueryWrapper<Operation>();
//        wrapper.eq("operation",workStationVo.getWorkingProcess());
//        Operation operation = operationService.getOne(wrapper);
        String bo=workStationVo.getBo();
//        WorkStationHandleBO workStationHandleBO = new WorkStationHandleBO(UserUtils.getSite(),workStationVo.getWorkingProcess(),workStationVo.getWorkStepCode());
//        String bo = workStationHandleBO.getBo();
//        WorkStation entityWorkStation = workStationMapper.selectById(bo);
        WorkStation workStation = new WorkStation();
        Date newDate = new Date();
        //if(entityWorkStation==null){
        // 判断BO是否为空
        if(StringUtils.isBlank(bo)){
            // 新增操作,生成新的BO
            WorkStationHandleBO workStationHandleBO = new WorkStationHandleBO(UserUtils.getSite(),workStationVo.getWorkingProcess(),workStationVo.getWorkStepCode());
            bo = workStationHandleBO.getBo();

            workStation.setBo(bo);
            workStation.setSite(UserUtils.getSite());
            workStation.setWorkingProcess(workStationVo.getWorkingProcess());
            workStation.setWorkStepCode(workStationVo.getWorkStepCode());
            workStation.setWorkStepName(workStationVo.getWorkStepName());
            workStation.setWorkStepDesc(workStationVo.getWorkStepDesc());
            workStation.setUpdatedBy(userUtil.getUser().getUserName());
            workStation.setUpdateTime(newDate);
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(workStation);
            if( validResult.hasErrors() ){
                throw new CommonException( validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            workStationMapper.insert(workStation);
        }else{
            workStation.setBo(bo);
            workStation.setSite(UserUtils.getSite());
            workStation.setWorkingProcess(workStationVo.getWorkingProcess());
            workStation.setWorkStepCode(workStationVo.getWorkStepCode());
            workStation.setWorkStepName(workStationVo.getWorkStepName());
            workStation.setWorkStepDesc(workStationVo.getWorkStepDesc());
            workStation.setUpdatedBy(userUtil.getUser().getUserName());
            workStation.setUpdateTime(newDate);

            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(workStation);
            if( validResult.hasErrors() ){
                throw new CommonException( validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            workStationMapper.updateById(workStation);
        }

        //保存自定义数据
        if( workStationVo.getCustomDataValVoList()!=null ){
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo( bo );
            customDataValRequest.setSite( UserUtils.getSite() );
            customDataValRequest.setCustomDataType( CustomDataTypeEnum.WORKSTATION.getDataType() );
            customDataValRequest.setCustomDataValVoList( workStationVo.getCustomDataValVoList() );
            customDataValService.saveCustomDataVal( customDataValRequest );
        }
    }

    @Override
    public IPage<WorkStation> query(WorkStationDTO workStationDTO) {
        if (ObjectUtil.isEmpty(workStationDTO.getPage())) {
            workStationDTO.setPage(new Page(0, 10));
        }
        workStationDTO.setSite(UserUtils.getSite());
        if(workStationDTO.getOperation() !=null && workStationDTO.getOperationVersion() !=null){
            OperationHandleBO operationHandleBO=new OperationHandleBO(UserUtils.getSite(),workStationDTO.getOperation(),workStationDTO.getOperationVersion());
            workStationDTO.setWorkingProcess(operationHandleBO.getBo());
        }
        IPage<WorkStation> workStationIPage = workStationMapper.selectByCondition(workStationDTO.getPage(), workStationDTO);
        for (WorkStation record : workStationIPage.getRecords()) {
            List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(UserUtils.getSite(), record.getBo(), CustomDataTypeEnum.WORKSTATION.getDataType());
            record.setCustomDataAndValVoList(customDataAndValVos);
        }
        return workStationMapper.selectByCondition(workStationDTO.getPage(),workStationDTO);
    }
}
