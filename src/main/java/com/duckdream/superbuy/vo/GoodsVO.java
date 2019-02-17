package com.duckdream.superbuy.vo;

import com.duckdream.superbuy.entity.Goods;

import java.util.Date;

public class GoodsVO extends Goods {
    private Double msPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

    public Double getMsPrice() {
        return msPrice;
    }

    public void setMsPrice(Double msPrice) {
        this.msPrice = msPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
