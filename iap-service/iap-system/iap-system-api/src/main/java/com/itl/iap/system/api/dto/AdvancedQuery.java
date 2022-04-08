package com.itl.iap.system.api.dto;

import lombok.Data;

/**
 * @author 崔翀赫
 * @date 2021/3/3$
 * @since JDK1.8
 */
@Data
public class AdvancedQuery {

        private String column;

        private String condition;

        private String value;

        private String connection;

}
