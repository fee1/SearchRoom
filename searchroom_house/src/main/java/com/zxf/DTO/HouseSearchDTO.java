package com.zxf.DTO;

import com.zxf.viewResult.ViewResult;

public class HouseSearchDTO extends ViewResult {

    private int draw;
    private long recordsTotal;//总数
    private long recordsFiltered;//总数

    public HouseSearchDTO() {
    }

    public HouseSearchDTO(int code, String message, Object data) {
        super(code, message, data);
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

}
