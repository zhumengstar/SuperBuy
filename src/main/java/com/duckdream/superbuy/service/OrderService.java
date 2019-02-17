package com.duckdream.superbuy.service;

import com.duckdream.superbuy.entity.MsOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    public MsOrder getMsOrderByUserIdGoods(long userId, long goodsId) {
        return orderDao.getMsOrderByUserIdGoods(userId, goodsId);
    }
}
