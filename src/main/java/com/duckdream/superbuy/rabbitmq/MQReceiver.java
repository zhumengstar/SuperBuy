package com.duckdream.superbuy.rabbitmq;

import com.duckdream.superbuy.entity.MsOrder;
import com.duckdream.superbuy.entity.User;
import com.duckdream.superbuy.redis.RedisService;
import com.duckdream.superbuy.service.GoodsService;
import com.duckdream.superbuy.service.MsService;
import com.duckdream.superbuy.service.OrderService;
import com.duckdream.superbuy.vo.GoodsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);


    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MsService msService;

    @RabbitListener(queues=MQConfig.MS_QUEUE)
    public void receive(String message) {
        log.info("receive message:" + message);
        MsMessage mm = RedisService.stringToBean(message, MsMessage.class);
        User user = mm.getUser();
        long goodsId = mm.getGoodsId();
        //判断库存
        GoodsVO goods = goodsService.getGoodsVOByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0) {
            return;
        }
        //判断是否已经秒杀到了
        MsOrder order = orderService.getMsOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return;
        }
        //减库存 下订单 写入秒杀订单
        msService.ms(user, goods);
    }
//    @RabbitListener(queues=MQConfig.QUEUE)
//    public void receive(String message) {
//        log.info("receive message:" + message);
//    }
//
//    @RabbitListener(queues=MQConfig.TOPIC_QUEUE1)
//    public void receiveTopic1(String message) {
//        log.info("topic queue1 message:" + message);
//    }
//
//    @RabbitListener(queues=MQConfig.TOPIC_QUEUE2)
//    public void receiveTopic2(String message) {
//        log.info("topic queue2 message:" + message);
//    }
//
//    @RabbitListener(queues=MQConfig.HEADER_QUEUE)
//    public void receiveHeaderQueue(byte[] message) {
//        log.info("header queue message:" + new String(message));
//    }
}
