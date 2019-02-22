package com.duckdream.superbuy.controller;


import com.duckdream.superbuy.entity.OrderInfo;
import com.duckdream.superbuy.entity.User;
import com.duckdream.superbuy.result.CodeMsg;
import com.duckdream.superbuy.result.Result;
import com.duckdream.superbuy.service.GoodsService;
import com.duckdream.superbuy.service.OrderService;
import com.duckdream.superbuy.vo.GoodsVO;
import com.duckdream.superbuy.vo.OrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVO> info(Model model, User user, @RequestParam("orderId")long orderId) {
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if(orderInfo == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }

        long goodsId = orderInfo.getGoodsId();
        GoodsVO goods = goodsService.getGoodsVOByGoodsId(goodsId);
        OrderDetailVO vo = new OrderDetailVO();
        vo.setOrderInfo(orderInfo);
        vo.setGoods(goods);
        return Result.success(vo);
    }
}
