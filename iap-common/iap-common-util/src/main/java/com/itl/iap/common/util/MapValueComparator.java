package com.itl.iap.common.util;

import java.util.Comparator;
import java.util.Map;

public class MapValueComparator<T extends Comparable<T>> implements Comparator<String> {
    private Map<String, T> map = null;

    public MapValueComparator(Map<String, T> map) {
        this.map = map;
    }

    public int compare(String o1, String o2) {
        int r = map.get(o2).compareTo(map.get(o1));
        if (r == 0) return 1; // 不这样写，值相同的会被删掉；但是这样写，get会返回null。看自己的需求写吧。
        return r;
    }
}
