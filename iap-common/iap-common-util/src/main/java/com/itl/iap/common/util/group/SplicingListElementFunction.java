package com.itl.iap.common.util.group;

import java.util.List;

@FunctionalInterface
public interface SplicingListElementFunction {
    String format(String delimiter, List list);
}
