package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.WorkShopHandleBO;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.entity.ProductLine;
import com.itl.mes.core.api.entity.WorkShop;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.service.ProductLineService;
import com.itl.mes.core.api.service.WorkShopService;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.WorkShopVo;
import com.itl.mes.core.provider.mapper.WorkShopMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 车间表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-05-23
 */
@Service
@Transactional
public class WorkShopServiceImpl extends ServiceImpl<WorkShopMapper, WorkShop> implements WorkShopService {


    @Autowired
    private WorkShopMapper workShopMapper;

    @Autowired
    private ProductLineService productLineService;

    @Autowired
    private CustomDataValService customDataValService;

    @Resource
    private UserUtil userUtil;

    @Override
    public List<WorkShop> selectList() {
        QueryWrapper<WorkShop> entityWrapper = new QueryWrapper<WorkShop>();
        //getEntityWrapper(entityWrapper, workShop);
        return super.list(entityWrapper);
    }

    /**
     * 验证车间是否存在
     *
     * @param site 工厂
     * @param workShop 车间
     * @return WorkShop
     * @throws CommonException 异常
     */
    @Override
    public WorkShop validateWorkShopIsExist( String site, String workShop ) throws CommonException {

        WorkShopHandleBO workShopHandleBO = new WorkShopHandleBO( site,workShop );
        WorkShop workShopEntity = workShopMapper.selectById( workShopHandleBO.getBo() );
        if( workShopEntity==null ){
            throw new CommonException( "车间"+workShop+"数据未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return workShopEntity;

    }

    /**
     * 验证车间是否被使用
     *
     * @param site 工厂
     * @param workShop 车间
     * @return true/false
     */
    public boolean validateWorkShopIsUsed( String site, String workShop ){

        //查询产线是否使用车间
        QueryWrapper<ProductLine> wrapperEntity = new QueryWrapper<>();
        ProductLine productLine = new ProductLine();
        productLine.setWorkShopBo( new WorkShopHandleBO( site, workShop ).getBo() );
        wrapperEntity.setEntity( productLine );
        List<ProductLine> productLineList = productLineService.list( wrapperEntity );
        if( productLineList.size() >0 ){ //代表数据被使用，不能删除
            return true;
        }
        return false;
    }

    /**
     * 通过车间查询车间相关数据
     *
     * @param workShop 车间
     * @return WorkShopVo
     * @throws CommonException 异常
     */
    @Override
    public WorkShopVo getWorkShopByWorkShop(String workShop) throws CommonException {

        String site = UserUtils.getSite();
        WorkShop workShopModel = validateWorkShopIsExist( site,workShop );
        WorkShopVo workShopVo = new WorkShopVo();
        BeanUtils.copyProperties( workShopModel,workShopVo );
        List<CustomDataAndValVo> customDataAndValVoList = customDataValService
                .selectCustomDataAndValByBoAndDataType( UserUtils.getSite(), workShopModel.getBo(), CustomDataTypeEnum.WORK_SHOP.getDataType() );
        workShopVo.setCustomDataAndValVoList( customDataAndValVoList );

        return workShopVo;

    }

    /**
     * 车间数据保存
     *
     * @param workShopVo workShopVo
     * @throws CommonException 异常
     */
    @Override
    @Transactional( rollbackFor = {Exception.class,RuntimeException.class} )
    public void saveWorkShop( WorkShopVo workShopVo ) throws CommonException {

        String site = UserUtils.getSite();
        WorkShopHandleBO workShopHandleBO = new WorkShopHandleBO( site, workShopVo.getWorkShop() );
        WorkShop workShopEntity = super.getById( workShopHandleBO.getBo() );
        if( workShopEntity==null ){ //代表数据未维护

            insertWorkShop( workShopVo );

        }else{ //代表数据已维护

            updateWorkShop( workShopEntity, workShopVo );
        }

        //保存自定义数据
        if( workShopVo.getCustomDataValVoList()!=null ){
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo( workShopHandleBO.getBo() );
            customDataValRequest.setSite( site );
            customDataValRequest.setCustomDataType( CustomDataTypeEnum.WORK_SHOP.getDataType() );
            customDataValRequest.setCustomDataValVoList( workShopVo.getCustomDataValVoList() );
            customDataValService.saveCustomDataVal( customDataValRequest );
        }

    }

    /**
     * 新增车间数据
     *
     * @param workShopVo workShopVo
     * @throws CommonException 异常
     */
    void insertWorkShop( WorkShopVo workShopVo ) throws CommonException {

        String site = UserUtils.getSite();
        WorkShopHandleBO workShopHandleBO = new WorkShopHandleBO( site, workShopVo.getWorkShop() );
        Date createDate = new Date();
        WorkShop workShop = new WorkShop();

        workShop.setBo( workShopHandleBO.getBo() );
        workShop.setSite( site );
        workShop.setWorkShop( workShopVo.getWorkShop() );
        workShop.setWorkShopDesc( StrUtil.isBlank( workShopVo.getWorkShopDesc() )?workShopVo.getWorkShop():workShopVo.getWorkShopDesc() );
        workShop.setState( workShopVo.getState() );
        workShop.setObjectSetBasicAttribute( userUtil.getUser().getUserName(), createDate );
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean( workShop ); //校验数据是否合规
        if( validResult.hasErrors() ){
            throw new CommonException( validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        //保存车间数据
        workShopMapper.insert( workShop );
    }

    /**
     * 更新车间数据
     *
     * @param workShop 车间
     * @param workShopVo workShopVo
     * @throws CommonException 异常
     */
    void updateWorkShop( WorkShop workShop,WorkShopVo workShopVo ) throws CommonException {

        Date frontModifyDate = workShopVo.getModifyDate(); //前台传递的时间值
        Date modifyDate = workShop.getModifyDate(); //后台数据库的值
        CommonUtil.compareDateSame( frontModifyDate, modifyDate ); //比较时间是否相等

        Date newModifyDate = new Date();
        WorkShop workShopEntity = new WorkShop();

        workShopEntity.setBo( workShop.getBo() );
        workShopEntity.setWorkShopDesc( StrUtil.isBlank( workShopVo.getWorkShopDesc() )?workShopVo.getWorkShop():workShopVo.getWorkShopDesc() );
        workShopEntity.setState( workShopVo.getState() );
        workShopEntity.setModifyUser( userUtil.getUser().getUserName() );
        workShopEntity.setModifyDate( newModifyDate );
        //更新车间数据
        Integer updateInt = workShopMapper.updateById( workShopEntity );
        if( updateInt==0 ){
            throw new CommonException( "数据已修改，请重新查询再执行保存操作", CommonExceptionDefinition.BASIC_EXCEPTION );
        }
    }

    /**
     * 删除车间数据
     *
     * @param workShop 车间
     * @param modifyDate 修改时间
     * @throws CommonException 异常
     */
    @Override
    public void deleteWorkShop( String workShop,Date modifyDate ) throws CommonException {

        String site = UserUtils.getSite();
        WorkShopHandleBO workShopHandleBO = new WorkShopHandleBO( site, workShop );
        WorkShop workShopEntity = validateWorkShopIsExist( site, workShop ); //验证车间是否存在
        CommonUtil.compareDateSame( modifyDate,workShopEntity.getModifyDate() );
        //查询车间是否已使用，已使用则不能删除
        //查询产线是否使用车间
        boolean usedFlag = validateWorkShopIsUsed( site, workShop ); //验证车间是否被使用
        if( usedFlag ){ //代表数据被使用，不能删除
            throw new CommonException( "车间"+workShop+"已使用，不能删除", CommonExceptionDefinition.BASIC_EXCEPTION );
        }
        QueryWrapper<WorkShop> wrapper = new QueryWrapper<>();
        wrapper.eq( WorkShop.BO, workShopHandleBO.getBo() ).eq( WorkShop.MODIFY_DATE, modifyDate );
        Integer integer = workShopMapper.delete( wrapper );
        if( integer==0 ){
            throw new CommonException( "数据已修改，请重新查询再执行删除操作", CommonExceptionDefinition.BASIC_EXCEPTION );
        }
        //删除车间自定义数据
        customDataValService.deleteCustomDataValByBoAndType( site, workShopHandleBO.getBo(), CustomDataTypeEnum.WORK_SHOP );

    }

}