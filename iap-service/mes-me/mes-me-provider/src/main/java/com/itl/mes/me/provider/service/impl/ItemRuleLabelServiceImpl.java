package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctc.wstx.util.StringUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.entity.label.LabelEntityParams;
import com.itl.mes.core.api.bo.CodeRuleHandleBO;
import com.itl.mes.core.api.dto.CodeGenerateDto;
import com.itl.mes.core.api.dto.ItemForParamQueryDto;
import com.itl.mes.me.api.dto.itemRuleLabel.ItemColumns;
import com.itl.mes.me.api.dto.itemRuleLabel.ItemRuleLabelQueryDto;
import com.itl.mes.me.api.dto.itemRuleLabel.ItemRuleLabelSaveDto;
import com.itl.mes.me.api.dto.itemRuleLabel.ItemRuleLabelShowDto;
import com.itl.mes.me.api.entity.ItemRuleLabelDetail;
import com.itl.mes.me.api.service.ItemRuleLabelDetailService;
import com.itl.mes.me.api.util.GeneratorId;
import com.itl.mes.me.client.service.FeignService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.itl.mes.me.provider.mapper.ItemRuleLabelMapper;
import com.itl.mes.me.api.entity.ItemRuleLabel;
import com.itl.mes.me.api.service.ItemRuleLabelService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


@Service
public class ItemRuleLabelServiceImpl extends ServiceImpl<ItemRuleLabelMapper, ItemRuleLabel> implements ItemRuleLabelService {
    @Resource
    private ItemRuleLabelMapper itemRuleLabelMapper;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private ItemRuleLabelDetailService itemRuleLabelDetailService;

    @Autowired
    private FeignService feignService;

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveAndUpdate(ItemRuleLabelSaveDto saveDto) throws CommonException {
        verify(saveDto.getBo(), saveDto.getItemBo(), saveDto.getLabelType());

        String userName = userUtil.getUser().getUserName();
        String site = UserUtils.getSite();

        // 获取物料的自定义字段名称
        List<String> customs = itemRuleLabelMapper.getCustoms(site);

        if (StrUtil.isBlank(saveDto.getBo())) {
            insertEntity(saveDto, site, userName, customs);
        } else {
            updateEntity(saveDto, site, userName, customs);
        }
    }



    /**
     * 验证一个物料和一个类型只能有一条数据
     * @param itemBo
     * @param type
     * @throws CommonException
     */
    private void verify(String bo, String itemBo, String type) throws CommonException {
        LambdaQueryWrapper<ItemRuleLabel> query = new QueryWrapper<ItemRuleLabel>()
                .lambda()
                .eq(ItemRuleLabel::getItemBo, itemBo)
                .eq(ItemRuleLabel::getLabelType, type);
        if (bo != null) {
            query.ne(ItemRuleLabel::getBo, bo);
        }
        ItemRuleLabel one = super.getOne(query);
        if (ObjectUtil.isNotEmpty(one)) {
            throw new CommonException("一个物料和一个类型只能有一条数据!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    @Override
    public IPage<ItemRuleLabelShowDto> queryPage(ItemRuleLabelQueryDto queryDto) throws CommonException {
        if (queryDto.getPage() == null) {
            queryDto.setPage(new Page(0, 10));
        }
        queryDto.getPage().setDesc("irl.CREATE_DATE");
        queryDto.setSite(UserUtils.getSite());
        try {
            return itemRuleLabelMapper.queryList(queryDto.getPage(), queryDto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException(e.getMessage(), CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    @Override
    public IPage<ItemRuleLabelShowDto> queryListBySO(ItemRuleLabelQueryDto queryDto) throws CommonException {
        if (queryDto.getPage() == null) {
            queryDto.setPage(new Page(0, 10));
        }
        queryDto.getPage().setDesc("irl.CREATE_DATE");
        queryDto.setSite(UserUtils.getSite());
        try {
            return itemRuleLabelMapper.queryListBySO(queryDto.getPage(), queryDto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException(e.getMessage(), CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void delete(String[] bos) throws CommonException {
        try {
            LambdaQueryWrapper<ItemRuleLabelDetail> query = new QueryWrapper<ItemRuleLabelDetail>().lambda().in(ItemRuleLabelDetail::getIrlBo, bos);
            itemRuleLabelDetailService.remove(query);

            itemRuleLabelMapper.deleteBatchIds(Arrays.asList(bos));
        } catch (Exception e) {
            throw new CommonException(e.getMessage(), CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    @Override
    public List<ItemColumns> getItemColumns() {
        String site = UserUtils.getSite();
        return itemRuleLabelMapper.getItemColumns(site);
    }

    @Override
    public List<ItemColumns> getShopOrderColumns() {
        String site = UserUtils.getSite();
        return itemRuleLabelMapper.getShopOrderColumns(site);
    }

    @Override
    public List<LabelEntityParams> getLabelEntityParams(String id) {

        return itemRuleLabelMapper.getLabelEntityParams(id);
    }

    @Override
    public List<String> generatorCode(String bo, Integer number) {
        // 查询itemRuleLabel和itemRuleLabelDetail
        ItemRuleLabel itemRuleLabel = getById(bo);
        List<ItemRuleLabelDetail> list = itemRuleLabelDetailService.list(
                new QueryWrapper<ItemRuleLabelDetail>()
                        .lambda()
                        .eq(ItemRuleLabelDetail::getIrlBo, bo));

        if (CollectionUtil.isNotEmpty(list)) {
            // 字段 <--> 值Map
            Map<String, Object> params = this.getItemParams(itemRuleLabel, list, UserUtils.getSite());

            if (CollectionUtil.isNotEmpty(params)) {
                // 变量 <--> 值Map
                Map<String, Object> parameters = new HashMap<>();
                list.forEach(data -> {
                    parameters.put(data.getRuleVar(), params.get(data.getRuleVal()));
                });

                List<String> codes = feignService.generatorNextNumberList(new CodeGenerateDto(itemRuleLabel.getCodeRuleBo(), number, parameters));
                return codes;
            }
        } else {
            List<String> codes = feignService.generatorNextNumberList(new CodeGenerateDto(itemRuleLabel.getCodeRuleBo(), number, null));
        }

        return null;
    }

    @Override
    public ItemRuleLabelShowDto getByBo(String bo) {
        return itemRuleLabelMapper.getByBo(bo);
    }

    private void insertEntity(ItemRuleLabelSaveDto saveDto, String site, String userName, List<String> customs) {

        // 生成ID
        String bo = new GeneratorId().getSnowflake().nextIdStr();

        ItemRuleLabel toSave = new ItemRuleLabel();
        BeanUtil.copyProperties(saveDto,toSave);

        toSave.setBo(bo).setSite(site)
                .setCodeRuleBo(new CodeRuleHandleBO(site, saveDto.getCodeRuleType()).getBo())
                .setObjectSetBasicAttribute(userName, new Date());
        saveDto.getDetails().forEach(data -> data.setIrlBo(bo)
                .setIsCustom(customs.contains(data.getRuleVal()) ? "Y" : "N"));

        itemRuleLabelMapper.insert(toSave);

        saveDto.getDetails().forEach(x -> {
          if (StringUtils.isNotBlank(x.getRuleVar())){
              if (StringUtils.isNotBlank(x.getTemplateArg())){
                  try {
                      throw new CommonException("编码变量和模板变量不能都存在!", CommonExceptionDefinition.BASIC_EXCEPTION);
                  } catch (CommonException e) {
                      e.printStackTrace();
                  }
              }
          }else {
              if (x.getTemplateArg().isEmpty()){
                  try {
                      throw new CommonException("编码变量和模板变量不能都为空!", CommonExceptionDefinition.BASIC_EXCEPTION);
                  } catch (CommonException e) {
                      e.printStackTrace();
                  }
              }
          }
        });
        if (CollectionUtil.isNotEmpty(saveDto.getDetails())) {
            itemRuleLabelDetailService.saveBatch(saveDto.getDetails());
        }
    }

    private void updateEntity(ItemRuleLabelSaveDto saveDto, String site, String userName, List<String> customs) {
        ItemRuleLabel toUpdate = new ItemRuleLabel();
        BeanUtil.copyProperties(saveDto, toUpdate);

        toUpdate.setSite(site)
                .setCodeRuleBo(new CodeRuleHandleBO(site, saveDto.getCodeRuleType()).getBo())
                .setModifyUser(userName).setModifyDate(new Date());
        saveDto.getDetails().forEach(data -> data.setIrlBo(saveDto.getBo())
                .setIsCustom(customs.contains(data.getRuleVal()) ? "Y" : "N"));

        itemRuleLabelMapper.updateById(toUpdate);
        saveDto.getDetails().forEach(x -> {
            if (StringUtils.isNotBlank(x.getRuleVar())){
                if (StringUtils.isNotBlank(x.getTemplateArg())){
                    try {
                        throw new CommonException("编码变量和模板变量不能都存在!", CommonExceptionDefinition.BASIC_EXCEPTION);
                    } catch (CommonException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                if (x.getTemplateArg().isEmpty()){
                    try {
                        throw new CommonException("编码变量和模板变量不能都为空!", CommonExceptionDefinition.BASIC_EXCEPTION);
                    } catch (CommonException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        // 移除原有的明细
        itemRuleLabelDetailService.remove(
                new QueryWrapper<ItemRuleLabelDetail>().lambda().eq(ItemRuleLabelDetail::getIrlBo,saveDto.getBo())
        );
        if (CollectionUtil.isNotEmpty(saveDto.getDetails())) {
            itemRuleLabelDetailService.saveBatch(saveDto.getDetails());
        }
    }

    /**
     * 获取该模板涉及物料的字段及其取值
     * @param itemRuleLabel
     * @param list
     * @return
     */
    private Map<String, Object> getItemParams(ItemRuleLabel itemRuleLabel, List<ItemRuleLabelDetail> list, String site) {
        Map<String, Object> params = new HashMap<>();

        // 拼装涉及的字段
        ItemForParamQueryDto yQuery = new ItemForParamQueryDto()
                .setSite(site)
                .setIsCustom("Y")
                .setItemBo(itemRuleLabel.getItemBo());
        ItemForParamQueryDto nQuery = new ItemForParamQueryDto()
                .setSite(site)
                .setIsCustom("N")
                .setItemBo(itemRuleLabel.getItemBo());
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(data -> {
                if (StrUtil.isNotBlank(data.getRuleVal())) {
                    if ("Y".equals(data.getIsCustom())) {
                        yQuery.getColumns().add(data.getRuleVal());
                    } else if ("N".equals(data.getIsCustom())) {
                        nQuery.getColumns().add(data.getRuleVal());
                    }
                }
            });
        }

        // 查询涉及字段的取值
        Map<String, Object> yParams = feignService.getParams(yQuery);
        Map<String, Object> nParams = feignService.getParams(nQuery);

        params.putAll(yParams);
        params.putAll(nParams);

        return params;
    }
}
