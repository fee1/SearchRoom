package com.zxf.result;

public class PageResult {

    Integer total;

    Object result;

    Boolean more;

    public PageResult() {
    }

    public PageResult(Boolean more) {
        this.more = more;
    }

    public Boolean getMore() {
        return more;
    }

    public void setMore(Boolean more) {
        this.more = more;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
