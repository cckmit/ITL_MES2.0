package com.itl.andon.core.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.andon.api.dto.CallTypeDTO;
import com.itl.mes.andon.api.entity.CallType;
import com.itl.mes.andon.api.entity.GradePush;
import com.itl.mes.andon.api.entity.Record;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "mes-andon-provider")
public interface AndonService {


    @GetMapping("/andon/findByLine")
    @ApiOperation(value = "根据产线查询所有的异常")
    public List<Record> findByLine(@RequestParam("line") String line);

    @PostMapping("/callType/query")
    @ApiOperation(("根据车间和安灯类型名称查询信息"))
    public ResponseData<Page<CallType>> query(@RequestBody CallTypeDTO callTypeDTO);

    @ApiOperation(value = "查看配置人员")
    @GetMapping("/callType/queryGradePush/{id}")
    public ResponseData<List<GradePush>> queryGradePush(@PathVariable("id") String id);

}
