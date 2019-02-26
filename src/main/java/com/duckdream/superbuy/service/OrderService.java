package com.duckdream.superbuy.service;

import com.duckdream.superbuy.dao.OrderDao;
import com.duckdream.superbuy.entity.MsOrder;
import com.duckdream.superbuy.entity.OrderInfo;
import com.duckdream.superbuy.entity.User;
import com.duckdream.superbuy.redis.OrderKey;
import com.duckdream.superbuy.redis.RedisService;
import com.duckdream.superbuy.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    RedisService redisService;

    public MsOrder getMsOrderByUserIdGoodsId(long userId, long goodsId) {
        //return orderDao.getMsOrderByUserIdGoodsId(userId, goodsId);
        return redisService.get(OrderKey.getMsOrderByUidGid, ""+userId+"_"+goodsId, MsOrder.class);
    }

    @Transactional
    public OrderInfo createOrder(User user, GoodsVO goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMsPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0); //未支付
        orderInfo.setUserId(user.getId());
        orderDao.insertOrderInfo(orderInfo);
        MsOrder msOrder = new MsOrder();
        msOrder.setOrderId(orderInfo.getId());
        msOrder.setGoodsId(goods.getId());
        msOrder.setUserId(user.getId());
        orderDao.insertMsOrder(msOrder);

        redisService.set(OrderKey.getMsOrderByUidGid, ""+user.getId()+"_"+goods.getId(), msOrder);

        return orderInfo;
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }

    public void deleteOrders() {
        orderDao.deleteOrders();
        orderDao.deleteMsOrders();
    }
}
