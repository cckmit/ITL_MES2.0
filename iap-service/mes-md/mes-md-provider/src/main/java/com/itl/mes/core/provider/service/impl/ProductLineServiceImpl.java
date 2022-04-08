package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ProductLineHandleBO;
import com.itl.mes.core.api.bo.WorkShopHandleBO;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.entity.ProductLine;
import com.itl.mes.core.api.entity.WorkShop;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.service.ProductLineService;
import com.itl.mes.core.api.service.WorkShopService;
import com.itl.mes.core.api.vo.ProductLineQueryVo;
import com.itl.mes.core.api.vo.ProductLineVo;
import com.itl.mes.core.provider.mapper.ProductLineMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 产线表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-05-24
 */
@Service
@Transactional
public class ProductLineServiceImpl extends ServiceImpl<ProductLineMapper, ProductLine> implements ProductLineService {


    @Autowired
    private ProductLineMapper productLineMapper;

    @Autowired
    private WorkShopService workShopService;

    @Autowired
    private CustomDataValService customDataValService;

    @Resource
    private UserUtil userUtil;

    @Override
    public List<ProductLine> selectList() {
        QueryWrapper<ProductLine> entityWrapper = new QueryWrapper<ProductLine>();
        //getEntityWrapper(entityWrapper, productLine);
        return super.list(entityWrapper);
    }


    /**
     * 新增产线数据
     *
     * @param site 工厂
     * @param userName 用户
     * @param bo bo
     * @param productLineVo productLineVo
     * @throws CommonException 异常
     */
    private void insertProductLine( String site, String userName, String bo, ProductLineVo productLineVo )throws CommonException {

        Date createDate = new Date();
        ProductLine productLineModel = new ProductLine();

        productLineModel.setBo( bo );
        productLineModel.setSite( site );
        productLineModel.setProductLine( productLineVo.getProductLine() );
        productLineModel.setProductLineDesc( productLineVo.getProductLineDesc() );
        if( !StrUtil.isBlank( productLineVo.getWorkShop() ) ){
            WorkShop workShopModel = workShopService.validateWorkShopIsExist( site,productLineVo.getWorkShop() );  //验证车间是否存在
            productLineModel.setWorkShopBo( workShopModel.getBo() );
        }
        productLineModel.setState( productLineVo.getState() );
        productLineModel.setObjectSetBasicAttribute( userName, createDate );
        productLineMapper.insert( productLineModel ); //保存产线数据


    }


    /**
     * 更新产线数据
     *
     * @param site 工厂
     * @param userName 用户
     * @param bo bo
     * @param modifyDate 修改时间
     * @param productLineVo productLineVo
     * @throws CommonException 异常
     */
    private void updateProductLine(String site, String userName, String bo, Date modifyDate, ProductLineVo productLineVo) throws CommonException{

        Date frontModifyDate = productLineVo.getModifyDate(); //前台传递的时间值
        CommonUtil.compareDateSame( frontModifyDate, modifyDate ); //比较时间是否相等

        Date newModifyDate = new Date();
        ProductLine productLineModel = new ProductLine();
        productLineModel.setBo( bo );
        productLineModel.setProductLineDesc( productLineVo.getProductLineDesc() );
        productLineModel.setState( productLineVo.getState() );
        if( !StrUtil.isBlank( productLineVo.getWorkShop() ) ){
            WorkShop workShopModel = workShopService.validateWorkShopIsExist( site,productLineVo.getWorkShop() );  //验证车间是否存在
            productLineModel.setWorkShopBo( workShopModel.getBo() );
        }else{
            productLineModel.setWorkShopBo( "" );
        }
        productLineModel.setModifyUser( userName );
        productLineModel.setModifyDate( newModifyDate );
        Integer updateInt = productLineMapper.updateById( productLineModel );
        if( updateInt==0 ){
            throw new CommonException( "数据已修改，请重新查询再执行保存操作", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

    }


    /**
     * 保存产线数据
     *
     * @param productLineVo productLineVo
     * @throws CommonException
     */
    @Override
    @Transactional( rollbackFor = {Exception.class,RuntimeException.class} )
    public void saveProductLine( ProductLineVo productLineVo ) throws CommonException {
        String site = UserUtils.getSite(); //获取工厂
        String userName = userUtil.getUser().getUserName();//获取用户
        ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO( site, productLineVo.getProductLine() );
        ProductLine productLineEntity = super.getById( productLineHandleBO.getBo() ); //查询产线数据

        if( productLineEntity==null ){ //新增

            insertProductLine( site, userName, productLineHandleBO.getBo(), productLineVo );

        }else{//更新

            updateProductLine( site, userName , productLineHandleBO.getBo(), productLineEntity.getModifyDate(), productLineVo );
        }
        //保存自定义数据
        if( productLineVo.getCustomDataValVoList()!=null ){
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo( productLineHandleBO.getBo() );
            customDataValRequest.setSite( site );
            customDataValRequest.setCustomDataType( CustomDataTypeEnum.PRODUCT_LINE.getDataType() );
            customDataValRequest.setCustomDataValVoList( productLineVo.getCustomDataValVoList() );
            customDataValService.saveCustomDataVal( customDataValRequest );
        }


    }


    /**
     * 通过产线删除产线相关数据
     *
     * @param productLine 产线
     * @param modifyDate 修改时间
     * @throws CommonException 异常
     */
    @Override
    public void deleteProductLineByProductLine( String productLine,Date modifyDate ) throws CommonException {

        String site = UserUtils.getSite();
        ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO( site, productLine );
        ProductLine productLineEntity = getExistProductLineByHandleBO( productLineHandleBO );
        CommonUtil.compareDateSame( modifyDate,productLineEntity.getModifyDate() );

        //查询产线是否已使用，已使用则不能删除
        //还没逻辑

        //删除
        QueryWrapper<ProductLine> delWrapper = new QueryWrapper<>();
        delWrapper.eq( WorkShop.BO, productLineEntity.getBo() ).eq( WorkShop.MODIFY_DATE, modifyDate );
        Integer integer = productLineMapper.delete( delWrapper );
        if( integer==0 ){
            throw new CommonException( "数据已修改，请重新查询再执行删除操作", CommonExceptionDefinition.BASIC_EXCEPTION );
        }
        //删除产线自定义数据值
        customDataValService.deleteCustomDataValByBoAndType( site, productLineHandleBO.getBo(), CustomDataTypeEnum.PRODUCT_LINE );

    }

    @Override
    public ProductLine selectByProductLine(String productLine) throws CommonException {
        QueryWrapper<ProductLine> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(ProductLine.SITE, UserUtils.getSite());
        entityWrapper.eq(ProductLine.PRODUCT_LINE,productLine);
        List<ProductLine> productLines = productLineMapper.selectList(entityWrapper);
        if(productLines.isEmpty()){
           throw new  CommonException("产线"+productLine+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION );
        }else{
            return productLines.get(0);
        }

    }


    /**
     * 通过ProductLineHandleBO查询产线数据
     *
     * @param productLineHandleBO productLineHandleBO
     * @return ProductLine
     */
    @Override
    public ProductLine getProductLineByHandleBO(ProductLineHandleBO productLineHandleBO ){

        return super.getById( productLineHandleBO.getBo() );
    }

    /**
     * 通过ProductLineHandleBO查询产线数据，未查到则报错
     *
     * @param productLineHandleBO productLineHandleBO
     * @return ProductLine
     * @throws CommonException 异常
     */
    @Override
    public ProductLine getExistProductLineByHandleBO(ProductLineHandleBO productLineHandleBO )throws CommonException{

        ProductLine productLine = getProductLineByHandleBO( productLineHandleBO );
        if( productLine==null ){
            throw new CommonException( "产线"+productLineHandleBO.getProductLine()+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION );
        }
        return productLine;
    }

    /**
     * 查询产线相关数据
     *
     * @param productLine 产线
     * @return ProductLineVo
     * @throws CommonException 异常
     */
    @Override
    public ProductLineVo getProductLineByProductLine( String productLine ) throws CommonException {

        ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO( UserUtils.getSite(), productLine );
        ProductLine productLineEntity = getExistProductLineByHandleBO( productLineHandleBO );
        ProductLineVo productLineVo = new ProductLineVo();
        BeanUtils.copyProperties( productLineEntity,productLineVo );
        if( !StrUtil.isBlank( productLineEntity.getWorkShopBo() ) ){
            productLineVo.setWorkShop( new WorkShopHandleBO( productLineEntity.getWorkShopBo() ).getWorkShop() );
        }
        productLineVo.setCustomDataAndValVoList( customDataValService
                .selectCustomDataAndValByBoAndDataType( UserUtils.getSite(), productLineHandleBO.getBo(),
                        CustomDataTypeEnum.PRODUCT_LINE.getDataType() ) );
        return productLineVo;

    }

    @Override
    public List<ProductLine> listByWorkShop(ProductLineQueryVo queryVo) {
        if (queryVo.getPage() == null) {
            queryVo.setPage(new Page(0, 10));
        }
        queryVo.setSite(UserUtils.getSite());
        return productLineMapper.listByWorkShop(queryVo.getPage(), queryVo);
    }


}
