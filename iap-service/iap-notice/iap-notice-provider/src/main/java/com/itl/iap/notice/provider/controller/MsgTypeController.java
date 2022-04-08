package com.itl.iap.notice.provider.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.UUID;
import com.itl.iap.notice.api.entity.MsgType;
import com.itl.iap.notice.api.pojo.ResponseCode;
import com.itl.iap.notice.api.service.MsgTypeService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 消息类型表(MsgType)表控制层
 *
 * @author liaochengdian
 * @date 2020-03-25
 * @since jdk1.8
 */
@RestController
@RequestMapping("/msgType")
public class MsgTypeController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 服务对象
     */
    @Autowired
    private MsgTypeService msgTypeService;


    @PostMapping("/add")
    @ApiOperation(value = "新增消息类型", notes = "新增消息类型")
    public ResponseData<MsgType> addRecord(@RequestBody MsgType record) {
        logger.info("新增消息类型");
        Assert.notNull(record.getCode(), "code is null!");
        //编码唯一性验证
        boolean flag = msgTypeService.checkOwn(record);
        if (flag) {
            logger.error("code have exist");
            return new ResponseData<>(ResponseCode.ERROR.getCode(), "code have exist");
        }
        try {
            record.setId(UUID.uuid32());
//            if(currentUser!=null){
//                record.setCreateName(currentUser.getUserName());
//            }
            record.setCreateTime(new Date());
            msgTypeService.save(record);
        } catch (Exception e) {
            logger.error("新增消息类型：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改消息类型", notes = "修改消息类型")
    public ResponseData<MsgType> updateRecord(@RequestBody MsgType record) {
        logger.info("修改消息类型");
        Assert.notNull(record.getId(), "id cannot null");
        //编码唯一性验证
        boolean flag = msgTypeService.checkOwn(record);
        if (flag) {
            logger.error("code have exist");
            return new ResponseData<>(ResponseCode.ERROR.getCode(), "code have exist");
        }
        try {
//            if(currentUser!=null){
//                record.setUpdateName(currentUser.getUserName());
//            }
            record.setUpdateTime(new Date());
            msgTypeService.updateById(record);
        } catch (Exception e) {
            logger.error("修改消息类型：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除消息类型", notes = "删除消息类型")
    public ResponseData<MsgType> deleteRecord(@RequestBody List<MsgType> record) {
        logger.info("删除消息类型");
        //如果下面有子类型，递归删除
        try {
            if (!(CollectionUtils.isEmpty(record))) {
                QueryWrapper<MsgType> queryWrapper = new QueryWrapper<>();
                record.stream().forEach(item -> {
                    queryWrapper.eq("parentId", item.getId());
                    msgTypeService.remove(queryWrapper);
                    msgTypeService.removeById(item.getId());
                });
            } else {
                logger.error("删除数据为空");
                return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
            }
        } catch (Exception e) {
            logger.error("删除删除消息类型失败：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 获取消息类型详情
     *
     * @return
     */
    @GetMapping("/getMsgTypeInfo/{id}")
    @ApiOperation(value = "获取消息类型详情", notes = "获取消息类型详情")
    public ResponseData<MsgType> getMsgTypeInfo(@PathVariable("id") final String id) {
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), msgTypeService.selectById(id));
    }

    @ApiOperation(value = "查询消息类型树", notes = "查询消息类型树")
    @PostMapping(value = "/tree")
    public ResponseData<List<MsgType>> msgTypeTree(@RequestBody MsgType msgType) {
        List<MsgType> list;
        try {
            list = msgTypeService.selectMsgTypeTree(msgType.getId());
        } catch (Exception e) {
            logger.error("查询消息类型树失败" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }
}