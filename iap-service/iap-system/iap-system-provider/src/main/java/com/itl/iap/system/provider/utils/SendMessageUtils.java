/*
package com.itl.iap.system.provider.utils;

import com.itl.notice.client.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

*/
/**
 * @description: 发送短信工具类
 * @author: LiShuaiPeng
 * @datetime 2020/5/27 16:28
 * @version: v1.0
 * @since JDK 1.8
 *//*

@Component
@Slf4j
public class SendMessageUtils {
    */
/**
     * 给多个人发送短信时的分隔符
     *//*

    private static final String STR = ",";

    @Resource
    private NoticeService noticeService;



    */
/**
     * 根据业务对象发送短信
     *
     * @param businessId      主ID  比如标书ID  询报价ID
     * @param sendMessageCode 模板编码
     * @param phones          电话
     * @param toAddress       地址
     * @param ccAddress       业务对象
     * @param userId          用户登录账号
     * @param userName        用户名称
     * @param obj             业务对象
     * @throws Exception
     *//*

    public void sendMessage(String businessId,
                            String sendMessageCode,
                            String phones,
                            String toAddress,
                            String ccAddress,
                            String userId,
                            String userName,
                            Object obj) throws Exception{
        //发送消息,调用消息中心
        if (this.sendMessageIsParam(businessId, sendMessageCode, phones,
                toAddress, null, userId, userName, obj) != null) {
            if (noticeService.sendMessage(this.sendMessageIsParam(
                    businessId,
                    sendMessageCode,
                    phones,
                    toAddress,
                    null,
                    userId,
                    userName,
                    obj)).getCode().equals("1001")) {
                log.error("发送信息失败!");
                //throw new CommonException(PurException.MISSING_PARAMETER_EXCEPTION);
            }
        }
    }

    */
/**
     * @param businessId      必输* 业务单据id
     * @param sendMessageCode 必输*信息模板编码
     * @param phones          短信必输*   //接收人手机号
     * @param toAddress       *邮件收件人 ,发邮件必输  多个以英文的，分割  //接收人邮箱，
     * @param ccAddress       可不输   //抄送人邮箱，
     * @param userId          站内信必输* //接收人账号
     * @param userName        站内信必输* //接收人姓名
     * @param obj             业务实体对象
     * @return
     * @throws Exception
     *//*

    public Map<String, Object> sendMessageIsParam(
            String businessId,
            String sendMessageCode,
            String phones,
            String toAddress,
            String ccAddress,
            String userId,
            String userName,
            Object obj) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("businessId", businessId);
        returnMap.put("code", sendMessageCode);
        returnMap.put("phones", phones);
        returnMap.put("toAddress", toAddress);
        if (StringUtils.isEmpty(ccAddress)) {
            returnMap.put("ccAddress", ccAddress);
        }
        returnMap.put("userId", userId);
        returnMap.put("userName", userName);
        returnMap.put("params", obj);
        return returnMap;
    }

}
*/
