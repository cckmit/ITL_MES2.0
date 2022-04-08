package com.itl.iap.notice.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.UUID;
import com.itl.iap.notice.api.dto.MsgPlaceholderDto;
import com.itl.iap.notice.api.entity.MsgPlaceholder;
import com.itl.iap.notice.api.entity.MsgPlaceholderType;
import com.itl.iap.notice.api.pojo.ResponseCode;
import com.itl.iap.notice.api.service.MsgPlaceholderService;
import com.itl.iap.notice.api.service.MsgPlaceholderTypeService;
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
 * (MsgPlaceholder)表控制层
 *
 * @author liaochengdian
 * @date 2020-04-07
 * @since jdk1.8
 */
@RestController
@RequestMapping("/msgPlaceholder")
public class MsgPlaceholderController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 服务对象
     */
    @Autowired
    private MsgPlaceholderService msgPlaceholderService;
    @Autowired
    private MsgPlaceholderTypeService msgPlaceholderTypeService;

    @PostMapping("/add")
    @ApiOperation(value = "新增占位符", notes = "新增占位符")
    public ResponseData<MsgPlaceholder> addRecord(@RequestBody MsgPlaceholder record) {
        try {
            logger.info("新增占位符");
            Assert.notNull(record.getName(), "name is null!");
            Assert.notNull(record.getTypeName(), "typeName is null!");
            //占位符名称唯一性验证
            boolean flag = msgPlaceholderService.checkOwn(record);
            if (flag) {
                logger.error("same type below name have exist");
                return new ResponseData<>(ResponseCode.ERROR.getCode(), "same type below name have exist");
            }
            //检查和处理消息类型
            MsgPlaceholderType msgPlaceholderType = msgPlaceholderTypeService.updateTypeData(record.getTypeName());
            record.setId(UUID.uuid32());
            record.setCreateTime(new Date());
            record.setMsgPlaceholderTypeId(msgPlaceholderType.getId());
//            if(currentUser!=null){
//                String userName = currentUser.getUserName();
//                record.setCreateName(userName);
//            }
            record.setCreateTime(new Date());
            msgPlaceholderService.save(record);
        } catch (Exception e) {
            logger.error("新增占位符失败：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<MsgPlaceholder>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<MsgPlaceholder>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改占位符", notes = "修改占位符")
    public ResponseData<MsgPlaceholder> updateRecord(@RequestBody MsgPlaceholder record) {
        try {
            logger.info("修改占位符");
            Assert.notNull(record.getId(), "Id cannot be null");
            Assert.notNull(record.getName(), "name is null!");
            //编码唯一性验证
            boolean flag = msgPlaceholderService.checkOwn(record);
            if (flag) {
                logger.error("name have exist");
                return new ResponseData<>(ResponseCode.ERROR.getCode(), "name have exist");
            }
//            if(currentUser!=null){
//                String userName = currentUser.getUserName();
//                record.setUpdateName(userName);
//            }
            record.setUpdateTime(new Date());
            //检查和处理消息类型
            MsgPlaceholderType msgPlaceholderType = msgPlaceholderTypeService.updateTypeData(record.getTypeName());
            record.setMsgPlaceholderTypeId(msgPlaceholderType.getId());
            msgPlaceholderService.updateById(record);
        } catch (Exception e) {
            logger.error("修改占位符失败：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @PostMapping("/table")
    @ApiOperation(value = "分页查询占位符", notes = "分页查询占位符")
    public ResponseData<IPage<MsgPlaceholder>> queryRecord(@RequestBody MsgPlaceholderDto query) {

        if (query.getPageNum() == null) {
            query.setPageNum(1);
        }
        if (query.getPageSize() == null) {
            query.setPageSize(10);
        }
        IPage<MsgPlaceholder> msgSendRecordList = msgPlaceholderService.getMsgPlaceholderList(query);
        return ResponseData.success(msgSendRecordList);
    }

    /**
     * 根据id获取占位符详情
     *
     * @return
     */
    @GetMapping("/getMsgPlaceholderInf/id/{id}")
    @ApiOperation(value = "根据id获取占位符详情", notes = "根据id获取占位符详情")
    public ResponseData<MsgPlaceholder> getMsgPlaceholderInfById(@PathVariable("id") final String id) {
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), msgPlaceholderService.selectById(id));
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除占位符", notes = "删除占位符")
    public ResponseData<MsgPlaceholder> deleteRecord(@RequestBody List<MsgPlaceholder> record) {

        logger.info("删除占位符");
        try {
            if (!(CollectionUtils.isEmpty(record))) {
                record.stream().forEach(item -> {
                    msgPlaceholderService.removeById(item.getId());
                });
            } else {
                logger.error("删除数据为空");
                return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
            }
        } catch (Exception e) {
            logger.error("删除占位符失败：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @ApiOperation(value = "构造占位符树,按类型分组", notes = "构造占位符树,按类型分组")
    @PostMapping(value = "/tree")
    public ResponseData<List<MsgPlaceholderType>> msgPlaceholderTree(@RequestBody MsgPlaceholder msgPlaceholder) {
        List<MsgPlaceholderType> list;
        try {
            list = msgPlaceholderService.selectMsgPlaceholderTree(msgPlaceholder.getTypeName());
        } catch (Exception e) {
            logger.error("查询占位符树失败" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }
}