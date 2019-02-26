package com.duckdream.superbuy.service;

import com.duckdream.superbuy.entity.MsOrder;
import com.duckdream.superbuy.entity.OrderInfo;
import com.duckdream.superbuy.entity.User;
import com.duckdream.superbuy.redis.MsKey;
import com.duckdream.superbuy.redis.RedisService;
import com.duckdream.superbuy.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MsService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo ms(User user, GoodsVO goods) {
        //减库存 下订单 写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
        if(success) {
            return orderService.createOrder(user, goods);
        } else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    public long getMsResult(long userId, long goodsId) {
        MsOrder msOrder = orderService.getMsOrderByUserIdGoodsId(userId, goodsId);
        if(msOrder != null) { //秒杀成功
            return msOrder.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public void setGoodsOver(long goodsId) {
        redisService.set(MsKey.isGoodsOver, ""+goodsId, true);
    }

    public boolean getGoodsOver(long goodsId) {
        return redisService.exists(MsKey.isGoodsOver, ""+goodsId);
    }

    public void reset(List<GoodsVO> goodsList) {
        goodsService.resetStock(goodsList);
        orderService.deleteOrders();
    }
}
