package com.duckdream.superbuy.controller;

import com.duckdream.superbuy.entity.MsOrder;
import com.duckdream.superbuy.entity.OrderInfo;
import com.duckdream.superbuy.entity.User;
import com.duckdream.superbuy.result.CodeMsg;
import com.duckdream.superbuy.result.Result;
import com.duckdream.superbuy.service.GoodsService;
import com.duckdream.superbuy.service.MsService;
import com.duckdream.superbuy.service.OrderService;
import com.duckdream.superbuy.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/ms")
public class MsController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MsService msService;

    @RequestMapping(value = "/do_ms", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> list(Model model, User user, @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        //判断库存
        GoodsVO goods = goodsService.getGoodsVOByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0) {
            return Result.error(CodeMsg.MS_OVER);
        }
        //判断是否已经秒杀到了
        MsOrder order = orderService.getMsOrderByUserIdGoods(user.getId(), goodsId);
        if(order != null) {
            return Result.error(CodeMsg.REPEATE_MS);
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = msService.ms(user, goods);
        return Result.success(orderInfo);
    }
}
