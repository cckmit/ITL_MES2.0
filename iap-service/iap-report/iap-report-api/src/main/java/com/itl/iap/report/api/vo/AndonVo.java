package com.itl.iap.report.api.vo;

import lombok.Data;

import java.util.List;

@Data
public class AndonVo {
    private String name;
    private int value;

    private List<String> names;
    private List<Integer> values;
}
