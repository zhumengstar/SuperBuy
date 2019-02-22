package com.duckdream.superbuy.vo;

import com.duckdream.superbuy.entity.User;

public class GoodsDetailVO {

    private int msStatus = 0;
    private int remainSeconds = 0;
    private GoodsVO goods;
    private User user;

    public int getMsStatus() {
        return msStatus;
    }

    public void setMsStatus(int msStatus) {
        this.msStatus = msStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVO getGoods() {
        return goods;
    }

    public void setGoods(GoodsVO goods) {
        this.goods = goods;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
