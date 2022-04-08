package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.bo.DeviceHandleBO;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.dto.FeedInDTO;
import com.itl.mes.core.api.dto.OutStationDto;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.BomService;
import com.itl.mes.core.api.service.FeedInService;
import com.itl.mes.core.api.vo.BomComponnetVo;
import com.itl.mes.core.api.vo.BomVo;
import com.itl.mes.core.api.vo.FeedInVo;
import com.itl.mes.core.provider.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class FeedInServiceImpl extends ServiceImpl<FeedInMapper, FeedIn> implements FeedInService {

    @Autowired
    private ShopOrderMapper shopOrderMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private SfcMapper sfcMapper;

    @Autowired
    private FeedInMapper feedInMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private BomService bomService;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private AssySfcMapper assySfcMapper;

    @Autowired
    private BomComponnetMapper bomComponnetMapper;

    @Override
    public FeedInVo query(FeedInDTO feedInDTO) throws CommonException {
        FeedIn feedIn = new FeedIn();
        ShopOrder shopOrder = new ShopOrder();
        FeedInVo feedInVo = new FeedInVo();
        Device device = new Device();
        Stock stock = stockMapper.selectOne(new QueryWrapper<Stock>().eq("SFC",feedInDTO.getSfc()));
        if(stock ==null) {
            throw new CommonException("该批次条码没有生成入库申请单",30002);
        }
        int qty = stock.getOkQty();
        Sfc sfc = new Sfc();
        int bomQty = 0;
        if (StrUtil.isNotEmpty(feedInDTO.getShopOrderBo())){
            shopOrder = shopOrderMapper.selectById(feedInDTO.getShopOrderBo());
            feedInVo.setShopOrderBo(shopOrder.getBo());
            feedInVo.setShopOrder(shopOrder.getShopOrder());
            feedInVo.setItemBo(shopOrder.getItemBo());
            feedInVo.setItem(new ItemHandleBO(shopOrder.getItemBo()).getItem());
        }
        if (StrUtil.isNotEmpty(feedInDTO.getDeviceBo())){
            device = deviceMapper.selectById(feedInDTO.getDeviceBo());
            feedInVo.setDevice(device.getDevice());
            feedInVo.setDeviceBo(device.getBo());
        }
        if (StrUtil.isNotEmpty(feedInDTO.getSfc())){
            sfc = sfcMapper.selectBySfc(feedInDTO.getSfc());
            if (!sfc.getState().equals("已完成")){
                return null; //批次未完成
            }
            Item item = itemMapper.selectById(sfc.getItemBo());
            feedInVo.setSfc(sfc.getSfc());
            feedInVo.setSfcItem(item.getItem());
            feedInVo.setSfcItemBo(item.getBo());
            feedInVo.setSfcItemName(item.getItemName());
            feedInVo.setSfcItemDesc(item.getItemDesc());
            BomVo bomVoByBo = bomService.getBomVoByBo(shopOrder.getBomBo());
            feedInVo.setBomBo(shopOrder.getBomBo());
            List<BomComponnetVo> componnetVos = bomVoByBo.getBomComponnetVoList();
            Boolean result = false;
            for (BomComponnetVo componnetVo : componnetVos) {
                if (componnetVo.getComponent().equals(item.getItem())){
                    bomQty = componnetVo.getQty().intValue();
                    feedIn.setComponentBo(componnetVo.getBo());
                    feedInVo.setBomQty(bomQty);
                    result = true;
                }
            }
            if (!result){
                return null;
            }
            int sfcOutQty = sfcMapper.getOutQty(sfc.getSfc());
            int sfcQty = sfc.getSfcQty().intValue();
            feedInVo.setSfcOutQty(sfcOutQty);
            feedInVo.setSfcQty(sfc.getDoneQty().intValue());
            feedInVo.setSurplusQty(sfcQty);
            feedIn.setBo(UUID.uuid32());
            feedIn.setDeviceBo(device.getBo());
            feedIn.setItemBo(item.getBo());
            feedIn.setShopOrderBo(shopOrder.getBo());
            feedIn.setAssySfc(sfc.getSfc());
            feedIn.setQty(sfc.getDoneQty().intValue());
            feedIn.setFeedingUser(userUtil.getUser().getUserName());
            feedIn.setFeedingDate(new Date());
            feedIn.setState("0");
            feedIn.setSurplusQty(sfcQty);
            feedInMapper.insert(feedIn);
        }
        return feedInVo;
    }

    @Override
    public List<FeedInVo> queryList(FeedInDTO feedInDTO) throws CommonException {
        List<FeedIn> feedIns = feedInMapper.selectList(new QueryWrapper<FeedIn>().eq("DEVICE_BO",feedInDTO.getDeviceBo()).eq("STATE","0"));
        List<FeedInVo> feedInVos = new ArrayList<>();
        for (FeedIn feedIn : feedIns) {
            FeedInVo feedInVo = new FeedInVo();
            BeanUtils.copyProperties(feedIn,feedInVo);
            ShopOrder shopOrder = shopOrderMapper.selectById(feedIn.getShopOrderBo());
            feedInVo.setShopOrder(shopOrder.getShopOrder());
            feedInVo.setItem(new ItemHandleBO(shopOrder.getItemBo()).getItem());
            Sfc sfcEntity = sfcMapper.selectBySfc(feedIn.getAssySfc());
            Item item = itemMapper.selectById(sfcEntity.getItemBo());
            feedInVo.setSfc(feedIn.getAssySfc());
            feedInVo.setSfcItem(item.getItem());
            feedInVo.setSfcItemBo(item.getBo());
            feedInVo.setSfcItemName(item.getItemName());
            feedInVo.setSfcItemDesc(item.getItemDesc());
            int sfcOutQty = sfcMapper.getOutQty(sfcEntity.getSfc());
            int sfcQty = sfcEntity.getSfcQty().intValue();
            BomVo bomVoByBo = bomService.getBomVoByBo(shopOrder.getBomBo());
            feedInVo.setBomBo(shopOrder.getBomBo());
            List<BomComponnetVo> componnetVos = bomVoByBo.getBomComponnetVoList();
            for (BomComponnetVo componnetVo : componnetVos) {
                if (componnetVo.getComponent().equals(item.getItem())){
                    int bomQty = componnetVo.getQty().intValue();
                    feedInVo.setBomQty(bomQty);
                }
            }
            feedInVo.setSfcOutQty(sfcOutQty);
            feedInVo.setSfcQty(sfcQty);
            feedInVo.setSurplusQty(feedIn.getSurplusQty());
            feedInVos.add(feedInVo);
        }
        return feedInVos;
    }

    /*出站扣料*/
    @Override
    public void outStation(OutStationDto outStationDto) throws CommonException {
        String sfc = outStationDto.getSfc();
        Sfc sfcEntity = sfcMapper.selectBySfc(sfc);
        AssySfc assySfc = new AssySfc();
        List<OutStationDevice> outStationDevices = outStationDto.getOutStationDevices();
        for (OutStationDevice outStationDevice : outStationDevices) {
            int outStationQty = outStationDevice.getOutStationQty().intValue();
            String deviceBo = outStationDevice.getDeviceBo();

            QueryWrapper<FeedIn> feedInQueryWrapper = new QueryWrapper<>();
            feedInQueryWrapper.eq("DEVICE_BO",deviceBo);
            feedInQueryWrapper.eq("STATE","0");
            feedInQueryWrapper.eq("SHOP_ORDER_BO",sfcEntity.getShopOrderBo());

            List<FeedIn> feedIns = feedInMapper.selectList(feedInQueryWrapper);
            String currentComponentBo = "";//当前组件BO，设置此值是因为同一个组件BO只能扣料一次
            for (FeedIn feed : feedIns) {
                if (!currentComponentBo.equals(feed.getComponentBo())){
                    BomComponnet bomComponnet = bomComponnetMapper.selectById(feed.getComponentBo());
                    if (ObjectUtil.isNotEmpty(bomComponnet)) {
                        int bomQty = bomComponnet.getQty().intValue();
                        if (bomQty!=0){
                            int surplusQty;
                            assySfc.setComponentBo(feed.getComponentBo());
                            assySfc.setAssyTime(new Date());
                            assySfc.setAssyUser(userUtil.getUser().getUserName());
                            assySfc.setAssembledSn(feed.getAssySfc());
                            assySfc.setSfc(sfc);
                            assySfc.setId(UUID.uuid32());
                            assySfc.setSfcQty(outStationQty);
                            assySfc.setSite(UserUtils.getSite());
                            if (feed.getSurplusQty()<(outStationQty*bomQty)){
                                feed.setSurplusQty(0);
                                feedInMapper.updateById(feed);
                            }else {
                                surplusQty = feed.getSurplusQty()-(outStationQty*bomQty);
                                feed.setSurplusQty(surplusQty);
                                feedInMapper.updateById(feed);
                            }
                            assySfc.setQty(outStationQty*bomQty);
                            assySfcMapper.insert(assySfc);
                        }
                    }
                    currentComponentBo = feed.getComponentBo();
                }
            }
        }
    }
}
