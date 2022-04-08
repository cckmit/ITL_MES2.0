package com.itl.mes.me.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.me.api.dto.InstructorOperationDto;
import com.itl.mes.me.api.entity.InstructorOperation;
import com.itl.mes.me.api.service.InstructorOperationService;
import com.itl.mes.me.provider.mapper.InstructorOperationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yaoxiang
 * @date 2020/12/28
 * @since JDK1.8
 */
@Service
public class InstructorOperationServiceImpl extends ServiceImpl<InstructorOperationMapper, InstructorOperation> implements InstructorOperationService {
    @Resource
    private InstructorOperationMapper instructorOperationMapper;

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Map<String, List<Operation>> getOperations(String instructorBo) {
        // 查询已分配的指导书工序实体
        List<InstructorOperation> assignedAll = list(null);
        // 查询当前指导书已分配的指导书工序实体
        List<InstructorOperation> assigned = assignedAll.stream().filter(x -> instructorBo.equals(x.getInstructorBo())).collect(Collectors.toList());
        String site = UserUtils.getSite();
        Map<String, List<Operation>> ret = new HashMap<>();

        // 保存所有已分配工序的Bo为集合
        List<String> operationBosAll = new ArrayList<>();
        assignedAll.forEach(assignedOperationAll -> operationBosAll.add(assignedOperationAll.getOperationBo()));

        if (assigned.size() > 0) {
            // 保存当前指导书已分配工序的Bo为集合
            List<String> operationBos = new ArrayList<>();
            assigned.forEach(assignedOperation -> operationBos.add(assignedOperation.getOperationBo()));

            // 查询当前指导书的已分配工序
            List<Operation> assignedOperations = instructorOperationMapper.getOperations(operationBos, site, "IN");

            //查询未分配工序
            List<Operation> unassignedOperations = instructorOperationMapper.getOperations(operationBosAll, site,"NOT IN");

            ret.put("assigned", assignedOperations);
            ret.put("unassigned", unassignedOperations);
        } else {
            if(operationBosAll !=null && operationBosAll.size() !=0){
                List<Operation> unassignedOperations = instructorOperationMapper.getOperations(operationBosAll, site, "NOT IN");
                ret.put("unassigned", unassignedOperations);
            }else{
                List<Operation> unassignedOperations = instructorOperationMapper.getOperations(operationBosAll, site, null);
                ret.put("unassigned", unassignedOperations);
            }

            ret.put("assigned", null);

        }
        return ret;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveOperations(InstructorOperationDto instructorOperationDto) {
        String instructorBo = instructorOperationDto.getInstructorBo();
        // 查询已分配的工序
        QueryWrapper<InstructorOperation> query = new QueryWrapper<InstructorOperation>().eq("INSTRUCTOR_BO", instructorBo);
        List<InstructorOperation> instructorOperations = instructorOperationMapper.selectList(query);

        // 删除已分配的工序
        if (instructorOperations.size() > 0) {
            instructorOperationMapper.delete(query);
        }
        if (!(instructorOperationDto.getOperationBos().size() == 0)) {
            List<InstructorOperation> toSave = new ArrayList<>();
            instructorOperationDto.getOperationBos().forEach(operationBo -> toSave.add(new InstructorOperation(instructorBo, operationBo)));
            // 重新分配工序
            saveBatch(toSave);
        }
    }
}
