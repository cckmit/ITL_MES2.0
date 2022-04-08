package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.mes.core.api.entity.Device;
import java.util.List;

public class Page implements IPage<Device> {
    private List<Device> records;
    private long total;
    private long size;
    private long current;
    private long pages;

    @Override
    public long getPages() {
        return pages;
    }

    @Override
    public IPage<Device> setPages(long pages) {
        this.pages=pages;
        return null;
    }

    @Override
    public List<Device> getRecords() {
        return records;
    }

    @Override
    public IPage<Device> setRecords(List<Device> list) {
        this.records=list;
        return null;
    }

    @Override
    public long getTotal() {
        return total;
    }

    @Override
    public IPage<Device> setTotal(long l) {
        this.total=l;
        return null;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public IPage<Device> setSize(long l) {
        this.size=l;
        return null;
    }

    @Override
    public long getCurrent() {
        return current;
    }

    @Override
    public IPage<Device> setCurrent(long l) {
        this.current=l;
        return null;
    }
}
