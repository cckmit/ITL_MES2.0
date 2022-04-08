package com.itl.iap.mes.provider.common;

import lombok.Data;

/**
 * @author 胡广
 * @version 1.0
 * @name PaginationData
 * @description
 * @date 2019-07-08
 */
@Data
public class PaginationResponse extends BaseResponse {
    PageableData pageableData;
}
