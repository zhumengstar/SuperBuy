package com.duckdream.superbuy.entity;

import java.util.Date;

public class MsGoods {
	private Long id;
	private Long goodsId;
	private Double msPrice;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
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
