package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.mes.core.api.bo.LogicHandleBO;
import com.itl.mes.core.api.entity.Logic;
import com.itl.mes.core.api.service.LogicService;
import com.itl.mes.core.api.vo.LogicVo;
import com.itl.mes.core.provider.mapper.LogicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * SQL语句表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-11-21
 */
@Service
@Transactional
public class LogicServiceImpl extends ServiceImpl<LogicMapper, Logic> implements LogicService {


    @Autowired
    private LogicMapper logicMapper;

    @Resource
    private UserUtil userUtil;

    @Override
    public List<Logic> selectList() {
        QueryWrapper<Logic> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, logic);
        return super.list(entityWrapper);
    }

    @Override
    public Logic getLogicByLogicHandleBO(LogicHandleBO logicHandleBO) throws CommonException {

        return logicMapper.selectById(logicHandleBO.getBo());
    }

    @Override
    public Logic getExistLogicByLogicHandleBO(LogicHandleBO logicHandleBO) throws CommonException {

        Logic logic = getLogicByLogicHandleBO(logicHandleBO);
        if (logic == null) {
            throw new CommonException("逻辑编号" + logicHandleBO.getLogicNo() + "，版本" + logicHandleBO.getVersion() + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return logic;
    }

    /**
     * 通过逻辑编号和版本查询数据
     *
     * @param logicNo 逻辑编号
     * @param version 版本
     * @return Logic
     * @throws CommonException 异常
     */
    @Override
    public Logic getLogicByLogicNoAndVersion(String logicNo, String version) throws CommonException {
        return getExistLogicByLogicHandleBO(new LogicHandleBO(logicNo, version));
    }

    /**
     * 查询逻辑编号当前版本内容
     *
     * @param logicNo 逻辑编号
     * @return String
     * @throws CommonException 异常
     */
    @Override
    public String getCurrentLogicNoContent(String logicNo) throws CommonException {

        QueryWrapper<Logic> wrapper = new QueryWrapper<>();
        wrapper.eq(Logic.LOGIC_NO, logicNo).eq(Logic.IS_CURRENT_VERSION, "Y");
        List<Logic> logicList = logicMapper.selectList(wrapper);
        if (logicList.size() == 0) {
            throw new CommonException("逻辑编号" + logicNo + "当前版本未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return logicList.get(0).getContent();
    }

    /**
     * 查询逻辑编号指定版本SQL内容
     *
     * @param logicNo 逻辑编号
     * @param version 版本
     * @return String
     * @throws CommonException 异常
     */
    @Override
    public String getLogicNoAndVersionContent(String logicNo, String version) throws CommonException {
        return getLogicByLogicNoAndVersion(logicNo, version).getContent();
    }

    /**
     * 保存逻辑编号
     *
     * @param logicVo 保存的内容
     * @throws CommonException 异常
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveLogic(LogicVo logicVo) throws CommonException {
        LogicHandleBO logicHandleBO = new LogicHandleBO(logicVo.getLogicNo(), logicVo.getVersion());
        Logic logicEntity = super.getById(logicHandleBO.getBo());
        if (logicEntity == null) {
            this.insertLogic(logicVo);
        } else {
            this.updateLogic(logicEntity, logicVo);
        }
    }

    private void insertLogic(LogicVo logicVo) {

        LogicHandleBO logicHandleBO = new LogicHandleBO(logicVo.getLogicNo(), logicVo.getVersion());
        Logic logic = new Logic();
        logic.setBo(logicHandleBO.getBo());
        logic.setLogicNo(logicHandleBO.getLogicNo());
        logic.setVersion(logicHandleBO.getVersion());
        logic.setLogicDesc(logicVo.getLogicDesc());
        logic.setIsCurrentVersion(logicVo.getIsCurrentVersion());
        logic.setContent(logicVo.getContent());
        logic.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());

        super.save(logic);
        if ("Y".equalsIgnoreCase(logicVo.getIsCurrentVersion())) {
            this.setOtherLogicNotCurrentVersion(logicHandleBO);
        }

    }

    private void updateLogic(Logic logic, LogicVo logicVo) throws CommonException {
        Date frontModifyDate = logicVo.getModifyDate();
        CommonUtil.compareDateSame(frontModifyDate, logic.getModifyDate());
        LogicHandleBO logicHandleBO = new LogicHandleBO(logicVo.getLogicNo(), logicVo.getVersion());
        Date newModifyDate = new Date();
        Logic logicEntity = new Logic();
        logicEntity.setBo(logicHandleBO.getBo());
        logicEntity.setLogicDesc(logicVo.getLogicDesc());
        logicEntity.setIsCurrentVersion(logicVo.getIsCurrentVersion());
        logicEntity.setContent(logicVo.getContent());
        logicEntity.setModifyUser(userUtil.getUser().getUserName());
        logicEntity.setModifyDate(newModifyDate);
        super.updateById(logicEntity);
        if ("Y".equalsIgnoreCase(logicVo.getIsCurrentVersion())) {
            this.setOtherLogicNotCurrentVersion(logicHandleBO);
        }
    }

    private void setOtherLogicNotCurrentVersion(LogicHandleBO logicHandleBO) {
        Logic logic = new Logic();
        logic.setIsCurrentVersion("N");
        QueryWrapper<Logic> wrapper = new QueryWrapper<>();
        wrapper.eq(Logic.LOGIC_NO, logicHandleBO.getLogicNo()).ne(Logic.VERSION, logicHandleBO.getVersion())
                .eq("IS_CURRENT_VERSION", "Y");
        super.update(logic, wrapper);
    }


}