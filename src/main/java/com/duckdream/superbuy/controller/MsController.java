package com.duckdream.superbuy.controller;

import com.duckdream.superbuy.entity.MsOrder;
import com.duckdream.superbuy.entity.User;
import com.duckdream.superbuy.rabbitmq.MQSender;
import com.duckdream.superbuy.rabbitmq.MsMessage;
import com.duckdream.superbuy.redis.GoodsKey;
import com.duckdream.superbuy.redis.RedisService;
import com.duckdream.superbuy.result.CodeMsg;
import com.duckdream.superbuy.result.Result;
import com.duckdream.superbuy.service.GoodsService;
import com.duckdream.superbuy.service.MsService;
import com.duckdream.superbuy.service.OrderService;
import com.duckdream.superbuy.vo.GoodsVO;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ms")
public class  MsController implements InitializingBean {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MsService msService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender mqSender;
    

    //系统初始化
    public void afterPropertiesSet() throws Exception {
        List<GoodsVO> goodsList = goodsService.listGoodsVO();
        if(goodsList == null) {
            return;
        }
        for (GoodsVO goods : goodsList) {
            redisService.set(GoodsKey.getMsGoodsStock, ""+goods.getId(), goods.getStockCount());
        }
    }

    @RequestMapping(value = "/do_ms", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> list(Model model, User user, @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getMsGoodsStock, ""+goodsId);
        if(stock < 0) {
            return Result.error(CodeMsg.MS_OVER);
        }
        //判断是否已经秒杀到了
        MsOrder order = orderService.getMsOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return Result.error(CodeMsg.REPEATE_MS);
        }
        //入队
        MsMessage mm = new MsMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        mqSender.sendMsMessage(mm);
        return Result.success(0); //0代表排队中


//        //判断库存
//        GoodsVO goods = goodsService.getGoodsVOByGoodsId(goodsId);
//        int stock = goods.getStockCount();
//        if(stock <= 0) {
//            return Result.error(CodeMsg.MS_OVER);
//        }
//        //判断是否已经秒杀到了
//        MsOrder order = orderService.getMsOrderByUserIdGoodsId(user.getId(), goodsId);
//        if(order != null) {
//            return Result.error(CodeMsg.REPEATE_MS);
//        }
//        //减库存 下订单 写入秒杀订单
//        OrderInfo orderInfo = msService.ms(user, goods);
//        return Result.success(orderInfo);

    }

    //orderId：成功
    //-1：秒杀失败
    //0：排队中
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> msResult(Model model, User user, @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = msService.getMsResult(user.getId(), goodsId);
        return Result.success(result);
    }
}
