package com.duckdream.superbuy.controller;

import com.duckdream.superbuy.entity.MsOrder;
import com.duckdream.superbuy.entity.OrderInfo;
import com.duckdream.superbuy.entity.User;
import com.duckdream.superbuy.result.CodeMsg;
import com.duckdream.superbuy.service.GoodsService;
import com.duckdream.superbuy.service.MsService;
import com.duckdream.superbuy.service.OrderService;
import com.duckdream.superbuy.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ms")
public class MsController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MsService msService;

    @RequestMapping("/do_ms")
    public String list(Model model, User user, @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return "login";
        }
        //判断库存
        GoodsVO goods = goodsService.getGoodsVOByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0) {
            model.addAttribute("errmsg", CodeMsg.MS_OVER.getMsg());
            return "ms_fail";
        }
        //判断是否已经秒杀到了
        MsOrder order = orderService.getMsOrderByUserIdGoods(user.getId(), goodsId);
        if(order != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_MS.getMsg());
            return "ms_fail";
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = msService.ms(user, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }
}
