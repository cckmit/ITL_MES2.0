package com.itl.iap.report.api.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

@Data
public class UserProductVo {
    IPage<UserProduct> userProductIPage;
    int allQty; //加工总数
}
