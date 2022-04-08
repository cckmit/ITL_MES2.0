package com.itl.iap.report.provider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.report.api.dto.SfcNcLogReportDto;
import com.itl.iap.report.api.dto.SfcScrapDto;
import com.itl.iap.report.api.dto.SfcScrapListDto;
import com.itl.iap.report.api.entity.SfcNcLog;
import com.itl.iap.report.api.entity.SfcScrap;
import com.itl.iap.report.api.service.SfcNcLogReportService;
import com.itl.iap.report.provider.mapper.SfcNcLogReportMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.ws.Action;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class SfcNcLogReportServiceImpl extends ServiceImpl<SfcNcLogReportMapper, SfcNcLog> implements SfcNcLogReportService {

    @Autowired
    private SfcNcLogReportMapper sfcNcLogReportMapper;

    @Autowired
    private UserUtil userUtil;

    @Override
    public IPage<SfcNcLog> queryList(SfcNcLogReportDto sfcNcLogReportDto) {
        IPage<SfcNcLog> page = sfcNcLogReportMapper.queryList(sfcNcLogReportDto.getPage(), sfcNcLogReportDto.getShopOrder(), sfcNcLogReportDto.getOperationOrder(), sfcNcLogReportDto.getOperationName(), sfcNcLogReportDto.getState(), sfcNcLogReportDto.getItem(), sfcNcLogReportDto.getSfc(), sfcNcLogReportDto.getNcName(), sfcNcLogReportDto.getDutyOperation());
        return page;
    }

    @Override
    public IPage<SfcScrap> querySfcScrap(SfcScrapDto sfcScrapDto) {
        IPage<SfcScrap> page = sfcNcLogReportMapper.querySfcScrap(sfcScrapDto.getPage(), sfcScrapDto);
        return page;
    }

    /**
     * 获取退料单号
     */
    private String getSfdadocno(String bo) {
        //通过BO获取退料单号,存在时直接获取退料单号
        String orderNumber = sfcNcLogReportMapper.selectOrderNumber(bo);
        if (null == orderNumber || StringUtils.isBlank(orderNumber)) {
            //不存在时生成默认退料单号并更新数据库
            orderNumber = "D-";
            orderNumber = orderNumber + "JCTL";
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat();
            // 格式化时间
            sdf.applyPattern("yyMMdd");
            String formatDate = sdf.format(date);
            orderNumber = orderNumber + formatDate;// D-JCTL210902
            // 通过D-JCTL210902模糊查询整个退料单字段
            String number = sfcNcLogReportMapper.selectMaxOrderNumber(orderNumber);
            if (StringUtils.isBlank(number) || null == number) {
                number = "1";
            } else {
                //转换成int + 1
                int i = Integer.parseInt(number);
                i = i + 1;
                number = i + "";
            }
            //  "001" 循环3次拼接
            while (number.length() < 3) {
                number = "0" + number;
            }

            orderNumber = orderNumber + number;
            // 将当前的默认退料单更新到数据库
            sfcNcLogReportMapper.updateOrderNumber(orderNumber, bo);
        }

        return orderNumber;
    }

    /**
     * 获取用户真名
     *
     * @return
     */
    private String getReallyName(String dutyUser) {
        return sfcNcLogReportMapper.findTruename(dutyUser);
    }


    private String getDutyUser(String bo) {
        return sfcNcLogReportMapper.catchUsers(bo);
    }

    @Override
    public Set<String> insScrap(List<SfcScrapListDto> sfcScrapListDto) {
        String bo = "";
        String shopOrder = "";
        Double scrapQty = null;
        String ncName = "";
        String json = null;
        String sfdadocno = null;
        String contents = "";
        String authentic = "";
        // 接收报废失败原因结果集问题不重复(前端传的报废数据在同一时间点产生同种问题)
        Set<String> result = new HashSet<>();
        for (SfcScrapListDto scrapListDto : sfcScrapListDto) {
            bo = scrapListDto.getBo();
            shopOrder = scrapListDto.getShopOrder();
            scrapQty = scrapListDto.getScrapQty();
            ncName = scrapListDto.getNcName();
            if (StringUtils.isEmpty(ncName) || ncName.equals("null")) {
                ncName = "";
            }
            contents = scrapListDto.getContents();
            if (StringUtils.isEmpty(contents) || contents.equals("null")) {
                contents = "";
            }
            sfdadocno = getSfdadocno(bo);
            String dutyUser = getDutyUser(bo);
            if (StringUtils.isEmpty(dutyUser) || dutyUser.equals("null")) {
                // 如果为空设置空字符串
                dutyUser = "";
            }
            // 获取用户真实姓名
            authentic = getReallyName(dutyUser);
            if (StringUtils.isEmpty(authentic) || authentic.equals("null")) {
                // 如果为空设置空字符串
                authentic = "";
            }

            String employeeCode = userUtil.getUser().getEmployeeCode();
            if (StringUtils.isEmpty(employeeCode) || employeeCode.equals("null")) {
                employeeCode = "";
            }

            String realName = userUtil.getUser().getRealName();
            if (StringUtils.isEmpty(realName) || realName.equals("null")) {
                realName = "";
            }
            // 根据bo查询出ERP_SUCCESS_FLAG状态 列名无效是因为nacos配置的数据库为正式没有该字段
            // 根据bo查询同步状态(bo在controller层已做判断必不为空)
            String syncState = sfcNcLogReportMapper.selectSyncState(bo);
            boolean flag = true;
            // 为1时即成功(不允许ERP调用)
            if (StringUtils.isNotBlank(syncState) && syncState.equals("1")) {
                flag = false;
                // 该bo是当前条件不满足情况下的唯一字段(用于前端匹配对应索引序号(唯一性))
                result.add(bo);
            }
            if (StringUtils.isEmpty(shopOrder) || null == shopOrder) {
                flag = false;
                // 工单为空
                result.add(bo);
            }
            if (null == scrapQty || scrapQty < 0) {
                flag = false;
                // 报废数量为空
                result.add(bo);
            }

//            String tstUrl = "http://192.168.1.181/wtoptst/ws/r/awsp900?WSDL";
             String prdUrl = "http://192.168.1.181/wtopprd/ws/r/awsp900?WSDL"; //正式区地址

            // 满足上述三个条件即可调用ERP接口进行自动退料申请
            if (flag) {
                // 创建动态客户端
                JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
                //对方的wsdl地址
                Client client = dcf.createClient(prdUrl);

                try {
                    String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
                    str += "<request type=\"sync\" key=\"76CFB4D975B57C166A5F93C79E19D933\">";
                    str += "<host prod=\"EDI\" ver=\"1.0\" ip=\"192.168.6.69\" lang=\"zh_CN\" timezone=\"8\" timestamp=\"20170309085642517\" acct=\"tiptop\"/>";
                    str += "<service prod=\"T100\" name=\"insTLD\" srvver=\"1.0\" id=\"00000\"/>";
                    str += "<datakey type=\"FOM\">";
                    str += "<key name=\"EntId\">1</key>";
                    str += "<key name=\"CompanyId\">DY</key>";
                    str += "</datakey>";
                    str += "<payload>";
                    str += "<param key=\"data\" type=\"XML\"><![CDATA[";
                    str += "<Request>";
                    str += "<RequestContent>";
                    str += "<Parameter/>";
                    str += "<Document>";
                    str += "<RecordSet id=\"1\">";
                    str += "<Master name=\"sfda_t\" node_id=\"1\">";
                    str += "<Record>";
                    str += "<Field name=\"sfdadocno\" value=" + "\"" + sfdadocno + "\"" + "/>";
                    // 退料员工编号
                    str += "<Field name=\"sfda004\" value=" + "\"" + employeeCode + "\"" + "/>";
                    // 查询车间产生退料的员工名字
                    str += "<Field name=\"sfdaud004\" value=" + "\"" + authentic + "\"" + "/>";
                    // 当前登录的退料人员真名
                    str += "<Field name=\"sfdaud005\" value=" + "\"" + realName + "\"" + "/>";
                    str += "<Detail name=\"sfdb_t\" >";
                    str += "<Record>";
                    str += "<Field name=\"sfdb001\" value=" + "\"" + shopOrder + "\"" + "/>";
                    str += "<Field name=\"sfdb007\" value=" + "\"" + scrapQty + "\"" + "/>";
                    str += "<Field name=\"sfdc015\" value=" + "\"" + ncName + "\"" + "/>";
                    str += "<Field name=\"ooff013\" value=" + "\"" + contents + "\"" + "/>";
                    str += "</Record>";
                    str += "</Detail>";
                    str += "</Record>";
                    str += "</Master>";
                    str += "</RecordSet>";
                    str += "</Document>";
                    str += "</RequestContent>";
                    str += "</Request>]]>";
                    str += "</param>";
                    str += "</payload>";
                    str += "</request>";
                    System.out.println(str);
                    Object[] objects1 = client.invoke("invokeSrv", str);
                    json = JSONObject.toJSONString(objects1[0]);
                    System.out.println("返回数据:" + json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 退料接口返回的结果包含成功
                if (json.contains("SUCCESS！")) {
                    // 更新ERP_SUCCESS_FLAG状态为1
                    sfcNcLogReportMapper.updateSyncStatus(bo);
                    // 当成功后清空失败原因(假设第一次失败而后续成功)

                } else {
                    // 更新ERP_FAILED_REASON为json(此时无需考虑状态问题)
                    result.add(bo);
                    // 添加原因
                    sfcNcLogReportMapper.updateFailReason(json, bo);
                }
            }
        }
        return result;
    }

}


