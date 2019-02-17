package com.duckdream.superbuy.service;

import com.duckdream.superbuy.dao.GoodsDao;
import com.duckdream.superbuy.entity.Goods;
import com.duckdream.superbuy.entity.Order;
import com.duckdream.superbuy.entity.User;
import com.duckdream.superbuy.vo.GoodsVO;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class MsService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;


    @Transactional
    public Order ms(User user, GoodsVO goods) {
        //减库存 下订单 写入秒杀订单
        goodsService.reduceStock(goods);
        //order msorder
        return orderService.createOrder(user, goods);
    }
}
