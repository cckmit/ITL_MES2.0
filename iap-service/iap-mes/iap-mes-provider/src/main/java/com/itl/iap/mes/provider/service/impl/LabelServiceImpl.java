package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.label.LabelQueryDTO;
import com.itl.iap.mes.api.dto.label.LabelSaveDto;
import com.itl.iap.mes.api.entity.MesFiles;
import com.itl.iap.mes.api.entity.Printer;
import com.itl.iap.mes.api.entity.label.LabelEntity;
import com.itl.iap.mes.api.entity.label.LabelEntityParams;
import com.itl.iap.mes.api.entity.label.LabelTypeEntity;
import com.itl.iap.mes.api.service.LabelService;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.common.ResultCode;
import com.itl.iap.mes.provider.config.Constant;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.LabelEntityParamsMapper;
import com.itl.iap.mes.provider.mapper.LabelMapper;
import com.itl.iap.mes.provider.mapper.MesFilesMapper;
import com.itl.iap.mes.provider.report.JasperReport;
import com.itl.iap.mes.provider.utils.BarCodeUtil;
import com.itl.iap.mes.provider.utils.FileUtils;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.client.service.CustomDataValService;
import net.sf.jasperreports.engine.*;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


/**
 * @author liuchenghao
 * @date 2020/11/3 17:59
 */
@Service
public class LabelServiceImpl extends JasperReport implements LabelService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    LabelMapper labelMapper;

    @Autowired
    CustomDataValService customDataValService;


    public IPage<LabelEntity> findList(LabelQueryDTO labelQueryDTO) {

        if(ObjectUtil.isEmpty(labelQueryDTO.getPage())){
            labelQueryDTO.setPage(new Page(0, 10));
        }

        QueryWrapper queryWrapper = new QueryWrapper<LabelEntity>();
        if(StrUtil.isNotEmpty(labelQueryDTO.getSite())){
            queryWrapper .eq("site",labelQueryDTO.getSite());
        }
        if(StrUtil.isNotEmpty(labelQueryDTO.getLabelTypeId())){
            queryWrapper.like("label_type_id",labelQueryDTO.getLabelTypeId());
        }
        if(StrUtil.isNotEmpty(labelQueryDTO.getLabel())){
            queryWrapper.like("label",labelQueryDTO.getLabel());
        }
        labelQueryDTO.getPage().setDesc("CREATION_DATE");
        queryWrapper.orderByDesc("CREATION_DATE");
        return labelMapper.selectPage(labelQueryDTO.getPage(),queryWrapper);
    }

    public IPage<LabelEntity> findListByState(LabelQueryDTO labelQueryDTO) {

        if(ObjectUtil.isEmpty(labelQueryDTO.getPage())){
            labelQueryDTO.setPage(new Page(0, 10));
        }

        QueryWrapper queryWrapper = new QueryWrapper<LabelEntity>();
        if(StrUtil.isNotEmpty(labelQueryDTO.getSite())){
            queryWrapper .eq("site",labelQueryDTO.getSite());
        }
        if(StrUtil.isNotEmpty(labelQueryDTO.getLabelTypeId())){
            queryWrapper.eq("label_type_id",labelQueryDTO.getLabelTypeId());
        }
        if(StrUtil.isNotEmpty(labelQueryDTO.getLabel())){
            queryWrapper.like("label",labelQueryDTO.getLabel());
        }
        labelQueryDTO.getPage().setDesc("CREATION_DATE");
        queryWrapper.orderByDesc("CREATION_DATE");
        queryWrapper.eq("STATE", 1);
        return labelMapper.selectPage(labelQueryDTO.getPage(),queryWrapper);
    }

    @Autowired
    LabelEntityParamsMapper labelEntityParamsMapper;
    @Transactional( rollbackFor = {Exception.class,RuntimeException.class} )
    public void save(LabelSaveDto labelSaveDto) throws CommonException {
        LambdaQueryWrapper<LabelEntity> query = new QueryWrapper<LabelEntity>()
                .lambda()
                .eq(LabelEntity::getLabelTypeId, labelSaveDto.getLabelTypeId())
                .eq(LabelEntity::getLabel, labelSaveDto.getLabel());
        if (ObjectUtil.isNotEmpty(labelSaveDto.getId())) {
            query.ne(LabelEntity::getId,labelSaveDto.getId());
        }
        List<LabelEntity> list = labelMapper.selectList(query);
        if (list != null && list.size() > 0) {
            throw new CustomException(CommonCode.LABEL_REPEAT);
        }
        LabelEntity labelEntity = new LabelEntity();
        BeanUtils.copyProperties(labelSaveDto, labelEntity);
        if(StrUtil.isNotEmpty(labelSaveDto.getId())){
            labelEntity.setLastUpdateDate(new Date());
            labelMapper.updateById(labelEntity);
        }else{
            labelEntity.setCreationDate(new Date());
            labelEntity.setId(UUID.randomUUID().toString().replace("-", ""));
            labelMapper.insert(labelEntity);
        }
        //保存自定义数据
        if( labelSaveDto.getCustomDataValVoList()!=null ){
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo( labelEntity.getId() );
            customDataValRequest.setSite( UserUtils.getSite() );
            customDataValRequest.setCustomDataType( CustomDataTypeEnum.LABEL.getDataType() );
            customDataValRequest.setCustomDataValVoList( labelSaveDto.getCustomDataValVoList() );
            customDataValService.saveCustomDataVal( customDataValRequest );
        }
        saveOrUpdateFile(labelEntity);
        saveOrUpdateParams(labelEntity);
    }

    private void saveOrUpdateParams(LabelEntity labelEntity) {
        if(labelEntity.getLabelEntityParamsList() != null && !labelEntity.getLabelEntityParamsList().isEmpty() ){
            labelEntityParamsMapper.delete(new QueryWrapper<LabelEntityParams>().eq("labelEntityId", labelEntity.getId()));

            labelEntity.getLabelEntityParamsList().forEach(labelEntityParams -> {
                labelEntityParams.setLabelEntityId(labelEntity.getId());
                labelEntityParamsMapper.insert(labelEntityParams);
            });

        }
    }

    @Autowired
    MesFilesMapper mesFilesMapper;

    private void saveOrUpdateFile(LabelEntity labelEntity) {
        MesFiles file = labelEntity.getJasperFile();
        if(file != null){
            file.setGroupId(labelEntity.getId());

            if(labelEntity.getId() != null){
                QueryWrapper<MesFiles> query = new QueryWrapper<MesFiles>().eq("groupId", labelEntity.getId());
                if(StringUtils.isNotBlank(file.getId())) query.ne("id",file.getId());
                List<MesFiles> mesFiles = mesFilesMapper.selectList(query);
                mesFiles.forEach(mes->{
                    mesFilesMapper.deleteById(mes.getId());
                });
            }

            if(StringUtils.isNotBlank(file.getId())){
                mesFilesMapper.updateById(file);
            }else {
                mesFilesMapper.insert(file);
            }
        }
    }


    @Transactional
    public void delete(List<String> ids) {
        ids.forEach(id ->{
            labelMapper.deleteById(id);
        });
    }

    @Value("${file.path}")
    private String filePath;
   //private String filePath="d:/";
    public LabelSaveDto findById(String id){
        LabelEntity labelEntity = labelMapper.selectById(id);

        List<MesFiles> mesFiles = mesFilesMapper.selectList(new QueryWrapper<MesFiles>().eq("groupId", labelEntity.getId()));
        if(mesFiles != null && !mesFiles.isEmpty()){
            labelEntity.setJasperFile(mesFiles.get(0));
        }

        List<LabelEntityParams> labelEntityParamsList = labelEntityParamsMapper.selectList(new QueryWrapper<LabelEntityParams>().eq("labelEntityId", id));
        labelEntity.setLabelEntityParamsList(labelEntityParamsList);
        LabelSaveDto labelSaveDto = new LabelSaveDto();
        BeanUtils.copyProperties(labelEntity, labelSaveDto);
        List<CustomDataAndValVo> customDataAndValVoList = customDataValService
                .selectCustomDataAndValByBoAndDataType( UserUtils.getSite(), id, CustomDataTypeEnum.LABEL.getDataType() );
        labelSaveDto.setCustomDataAndValVoList( customDataAndValVoList );
        return labelSaveDto;
    }

    public JasperPrint getJasper(Map<String, Object> params){
        LabelEntity labelEntity = labelMapper.selectById(params.get("id").toString());
        createSqlParams(labelEntity,params);
        if(labelEntity != null){
            MesFiles jasperFile = mesFilesMapper.selectOne(new QueryWrapper<MesFiles>().eq("groupId", labelEntity.getId()));
            try {
                FileInputStream fis = new FileInputStream(new File(jasperFile.getFilePath()));
               // FileInputStream fis = new FileInputStream(new File("D:mxy1.jasper"));
                JasperPrint jasperPrint = null;
                Integer useDataSource = labelEntity.getUseDataSource();
                if(useDataSource.equals(Constant.IsYesEnum.YES)){
                    Connection connection = getConnection();
                    jasperPrint = JasperFillManager.fillReport(fis, params, connection);
                }else{
                    jasperPrint = JasperFillManager.fillReport(fis, params, new JREmptyDataSource());
                }
               // boolean result=JasperPrintManager.printReport(jasperPrint, false);
                return jasperPrint;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
              //  throw new CustomException(CommonCode.JASPER_FAIL);
            }
        }
        return null;
    }
    private void convertImg(Map<String, Object> params){
        for (String key : params.keySet()) {
            if(params.get(key) != null && (params.get(key).toString().contains("png") || params.get(key).toString().contains("jpg"))){
                String imgPath = params.get(key).toString();
                try {
                    InputStream inputStream = new FileInputStream(new File(imgPath));
                    params.put(key,inputStream);
                } catch (FileNotFoundException e) {
                    throw new CustomException(CommonCode.IO_FAIL);
                }
            }
        }
    }

    private void createSqlParams(LabelEntity labelEntity, Map<String, Object> params) {
        List<LabelEntityParams> labelEntityParamsList = labelEntityParamsMapper.selectList(new QueryWrapper<LabelEntityParams>().eq("labelEntityId", labelEntity.getId()));
        StringBuffer sb = new StringBuffer();
        labelEntityParamsList.forEach(labelEntityParams -> {
            if((labelEntityParams.getIsFile().equals(Constant.BAR_CODE)|| labelEntityParams.getIsFile().equals(Constant.QR_CODE))&& labelEntityParams.getCode() != null){
                if(params.get(labelEntityParams.getCode().toString()) != null && StringUtils.isNotBlank(params.get(labelEntityParams.getCode().toString()).toString())){
                    String uuid = UUID.randomUUID().toString();
                    if(labelEntityParams.getIsFile().equals(Constant.BAR_CODE)) BarCodeUtil.getBarCode(params.get(labelEntityParams.getCode().toString()).toString(),filePath+uuid+".jpg");
                    if(labelEntityParams.getIsFile().equals(Constant.QR_CODE)) {
                        params.put(labelEntityParams.getCode().toString()+"_1",params.get(labelEntityParams.getCode().toString()));
                        BarCodeUtil.generateQRCodeImage(params.get(labelEntityParams.getCode().toString()).toString(),350, 350, filePath+uuid+".jpg");

                    }
                    try {
                        params.put(labelEntityParams.getCode().toString(),new FileInputStream(new File(filePath+uuid+".jpg")));
                    } catch (FileNotFoundException e) {
                        throw  new CustomException(CommonCode.IO_FAIL);
                    }
                }
            }

            if(labelEntityParams.getIsFile().equals(Constant.QUERY_PARAM) && params.get(labelEntityParams.getCode()) != null && StringUtils.isNotBlank(params.get(labelEntityParams.getCode()).toString())){
                sb.append(" and ").append(labelEntityParams.getCode()).append(" like '%").append(params.get(labelEntityParams.getCode()).toString()).append("%' ");
            }
        });
        if(StringUtils.isNotBlank(sb.toString()))
            params.put("params",sb.toString());
    }

    @Value("${html.path}")
    private String htmlPath;

    public String preview(Map<String, Object> params, HttpServletResponse response) {
        try {
            String fileType = params.get("fileType").toString();
            String uuid = UUID.randomUUID().toString();
            ServletOutputStream os = response.getOutputStream();
            JasperPrint jasperPrint = getJasper(params);
            if(fileType.contains("pdf")){
                JasperExportManager.exportReportToPdfStream(jasperPrint,os);
            }else if(fileType.contains("html")){
                JasperExportManager.exportReportToHtmlFile(jasperPrint,htmlPath+uuid+".html");
                return uuid+".html";
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }



    public String exportFile(Map params, HttpServletResponse response) {
        String fileType = params.get("fileType").toString();
        JasperPrint jasperPrint = getJasper(params);
        String uuid = UUID.randomUUID().toString();
        try {
            if(fileType.contains("pdf")){
                byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
                FileUtils.writeFile(filePath+uuid+".pdf",bytes);
                downFile(response,filePath+uuid+".pdf");
            }else if(fileType.contains("html")){
                JasperExportManager.exportReportToHtmlFile(jasperPrint,filePath+uuid+".html");
                return filePath+uuid+".html";
            }
        } catch (Exception e) {
            throw new CustomException(CommonCode.JASPER_FAIL);
        }
        return null;
    }

    public void downFile(HttpServletResponse resp, String path) {
        resp.setContentType("text/html");
        try {
            File file = new File(path);
            FileInputStream in = new FileInputStream(file);
            resp.setContentType("application/octet-stream");
            resp.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
            OutputStream out = resp.getOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            throw new CustomException(CommonCode.FILE_DOWN_FAIL);
        }

    }


    /*public void batchPrint(List<Map<String, Object>> list, String labelId) {

        LabelEntity labelEntity = labelMapper.selectById(labelId);

        MesFiles jasperFile = mesFilesMapper.selectOne(new QueryWrapper<MesFiles>().eq("groupId", labelEntity.getId()));

        labelEntity.setJasperFile(jasperFile);

        List<LabelEntityParams> labelEntityParamsList = labelEntityParamsMapper.selectList(new QueryWrapper<LabelEntityParams>().eq("labelEntityId", labelId));

        int n = list.size()/Constant.MAX_THREAD == 0 ? 1 : list.size() / Constant.MAX_THREAD;

        //解析list 将list数据拆分
        List<List<Map<String, Object>>> result = averageAssign(list, n);

        try {
            //将数据交给多个线程处理
            List<String> addressList = marginPdf(result, n, labelEntityParamsList, labelEntity);

            Printer instance = Printer.getInstance();
            addressList.forEach(address ->{
                instance.defaultPrintPDF(address,"");
            });

        } catch (Exception e) {
            throw new CustomException(CommonCode.PRINT_FAIL);
        }
    }*/


    public  List<String> marginPdf(List<String> sfcs, int nThreads, List<LabelEntityParams> labelEntityParamsList, LabelEntity labelEntity,Map<String, Object> params) throws Exception  {
        if (sfcs == null || sfcs.isEmpty()) {
            return null;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        List<Future<String>> futures = new ArrayList<Future<String>>(nThreads);

        for(int i = 0; i < nThreads; i++){
            Map<String, Object> params1 = new HashMap<>();
          //  List<Map<String, Object>> subList = result.get(i);
            String sfc=sfcs.get(i);

            //返回合并后pdf文件地址
            Callable<String> task = new Callable<String>() {

                public String call() throws Exception {

                    //生成多个pdf文件  并返回文件地址集合
                    List<String> anyPdfAddress = createPdfByJapser(sfc, labelEntityParamsList, labelEntity,params1);

                    //将多个pdf合并成一个pdf
                    return marginAnyPdf(anyPdfAddress);

                }
            };
            futures.add(executorService.submit(task));
        }

        List<String> pathAddressList = new ArrayList<>();

        for (Future<String> future : futures) {
            pathAddressList.add(future.get());
        }
        executorService.shutdown();

        // 等待子线程结束，再继续执行下面的代码
        executorService.awaitTermination(180, TimeUnit.SECONDS);

        return pathAddressList;

    }


    private String marginAnyPdf(List<String> anyPdfAddress) {
        PDFMergerUtility mergePdf = new PDFMergerUtility();

        anyPdfAddress.forEach(address->{
            try {
                mergePdf.addSource(address);
            } catch (FileNotFoundException e) {
                throw new CustomException(CommonCode.PRINT_FAIL);
            }
        });
        String uuid = UUID.randomUUID().toString();
        mergePdf.setDestinationFileName(filePath+uuid+".pdf");
        try {
            mergePdf.mergeDocuments();
        } catch (IOException e) {
            throw new CustomException(CommonCode.PRINT_FAIL);
        }
        return filePath+uuid+".pdf";
    }

    private List<String> createPdfByJapser(String sfc, List<LabelEntityParams> labelEntityParamsList, LabelEntity labelEntity,Map<String, Object> params) {
        for (LabelEntityParams labelEntityParams : labelEntityParamsList) {
            // 条码表
            if (labelEntityParams.getTableName().toLowerCase().equals("me_sfc")) {
                params.put(labelEntityParams.getCode(), labelMapper.selectSfcByFiled("t." + labelEntityParams.getFieldName(), sfc));
                // 工序工单表
            } else if (labelEntityParams.getTableName().toLowerCase().equals("m_operation_order")) {
                params.put(labelEntityParams.getCode(), labelMapper.selectOperationOrderByFiled("t." + labelEntityParams.getFieldName(), sfc));
                // 如果是物料表
            } else if (labelEntityParams.getTableName().toLowerCase().equals("m_item")) {
                params.put(labelEntityParams.getCode(), labelMapper.selectItemByFiled("t." + labelEntityParams.getFieldName(), sfc));
                // 若是工艺表(加t是为了统一别名关联表间字段)
            } else if (labelEntityParams.getTableName().toLowerCase().equals("m_router")) {
                if (labelEntityParams.getFieldName().toLowerCase().equals("router")) {
                    // 取工艺路线编码
                    params.put(labelEntityParams.getCode(), labelMapper.selectRouterByFiled("t." + labelEntityParams.getFieldName(), sfc));
                }
                if (labelEntityParams.getFieldName().toLowerCase().equals("router_name")) {
                    // 取工艺路线名称
                    params.put(labelEntityParams.getCode(), labelMapper.selectRouterNameByFiled("t." + labelEntityParams.getFieldName(), sfc));
                }
                // 设备表
            } else if (labelEntityParams.getTableName().toLowerCase().equals("m_dispatch")) {
                params.put(labelEntityParams.getCode(), labelMapper.selectDeviceByFiled("t." + labelEntityParams.getFieldName(), sfc));
                // 工序表
            } else if (labelEntityParams.getTableName().toLowerCase().equals("m_operation")) {
                // 关联工艺流程信息表(应用新表(已将json解析完毕存放)非json格式(json工艺流程是无序的))
                // 取出的是每个条码关联的工艺流线流程信息的工序中的工序名称
                List<String> operationNameList = labelMapper.selectOperationNameByFiled("n." + labelEntityParams.getFieldName(), sfc);

                StringBuffer sb = new StringBuffer();
                String stringIndex = "";
                for (String operationName : operationNameList) {
                    // 使用StringBuffer拼接工序名称便于前端解析生产工序流程
                    sb.append(operationName).append("->");
                    stringIndex = sb.toString().substring(0, sb.toString().length() - 2);
                }
                // 存入params
                params.put(labelEntityParams.getCode(), stringIndex);
            }
        }
        List<String> addressList = new ArrayList<>();
        //进行生成pdf文件
        String singlePdfAddress = createSinglePdf(params, labelEntity);

        if(StringUtils.isNotBlank(singlePdfAddress)) addressList.add(singlePdfAddress);

        return addressList;
    }

    private String createSinglePdf(Map<String, Object> params, LabelEntity labelEntity) {
        if(labelEntity != null){
            MesFiles jasperFile = labelEntity.getJasperFile();
            try {
                FileInputStream fis = new FileInputStream(new File(jasperFile.getFilePath()));
                createSqlParams(labelEntity,params);
                JasperPrint jasperPrint = null;
                Integer useDataSource = labelEntity.getUseDataSource();
                if(useDataSource.equals(Constant.IsYesEnum.YES)){
                    Connection connection = getConnection();
                    jasperPrint = JasperFillManager.fillReport(fis, params, connection);
                }else{
                    jasperPrint = JasperFillManager.fillReport(fis, params, new JREmptyDataSource());
                }
                String uuid = UUID.randomUUID().toString();
                byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
                FileUtils.writeFile(filePath+uuid+".pdf",bytes);
                return filePath+uuid+".pdf";
            } catch (Exception e) {
                throw new CustomException(CommonCode.JASPER_FAIL);
            }
        }
        return null;
    }


    /**
     * 将一组数据平均分成n组
     */
    public  <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remainder = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remainder > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remainder--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }
    public static void main(String[] args) throws Exception{
//        List<String> list = new ArrayList<>();
//        list.add("D:aaa.pdf");
//        list.add("D:ccc.pdf");
//        marginAnyPdf(list);
    }

    @Override
    public List<String> selectFieldByTableName(String tableName) throws CommonException{
        //String tableStr="object_id('"+tableName+"')";
        List<String> list=new ArrayList<>();
        try {
            list=labelMapper.selectFieldByTableName(tableName);
        }catch (Exception e){
            e.printStackTrace();
            throw new CommonException(e.getMessage(), CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return list;
    }

    public List<String> sfcPrint(List<String> sfcs, String labelId){

        if(StringUtils.isBlank(labelId)){
            throw new CustomException(CommonCode.LABEL_CHOOSE);
        }

        LabelEntity labelEntity = labelMapper.selectById(labelId);

        MesFiles jasperFile = mesFilesMapper.selectOne(new QueryWrapper<MesFiles>().eq("groupId", labelEntity.getId()));

        labelEntity.setJasperFile(jasperFile);

        List<LabelEntityParams> labelEntityParamsList = labelEntityParamsMapper.selectList(new QueryWrapper<LabelEntityParams>().eq("labelEntityId", labelId));
        int n=sfcs.size();
        try {
            Map<String, Object> params = new HashMap<>();
            /*params.put("fileType","pdf");
            params.put("id",labelId);
            for(int i=0;i<n;i++){
                String sfc=sfcs.get(i);

                for(LabelEntityParams labelEntityParams:labelEntityParamsList){
                    if(labelEntityParams.getTableName().toLowerCase().equals("me_sfc")){
                        params.put(labelEntityParams.getCode(),labelMapper.selectSfcByFiled("t."+labelEntityParams.getFieldName(),sfc));
                    }else if(labelEntityParams.getTableName().toLowerCase().equals("m_operation_order")){
                        params.put(labelEntityParams.getCode(),labelMapper.selectOperationOrderByFiled("t."+labelEntityParams.getFieldName(),sfc));
                    }else{
                        params.put(labelEntityParams.getCode(),labelMapper.selectItemByFiled("t."+labelEntityParams.getFieldName(),sfc));
                    }

                }
                preview(params,response);
            }*/

           List<String> addressList = marginPdf(sfcs, n, labelEntityParamsList, labelEntity,params);
          //  preview(params,response);
           /* Printer instance = Printer.getInstance();
            addressList.forEach(address ->{
                instance.sfcPrintPDF(address,"",params);
            });*/
            return addressList;
        } catch (Exception e) {
            throw new CustomException(CommonCode.PRINT_FAIL);
        }
    }

}
