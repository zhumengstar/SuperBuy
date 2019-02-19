package com.duckdream.superbuy.service;

import com.duckdream.superbuy.entity.OrderInfo;
import com.duckdream.superbuy.entity.User;
import com.duckdream.superbuy.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MsService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;


    @Transactional
    public OrderInfo ms(User user, GoodsVO goods) {
        //减库存 下订单 写入秒杀订单
        goodsService.reduceStock(goods);
        //orderinfo order_ms
        return orderService.createOrder(user, goods);
    }
}
