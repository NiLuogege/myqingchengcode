package com.qingcheng.entity;

import java.io.Serializable;
import java.util.List;

public class PageResult<T> implements Serializable {

    private Long total;
    private List<T> list;

    public PageResult(Long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
