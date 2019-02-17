package com.duckdream.superbuy.controller;

import com.duckdream.superbuy.entity.User;
import com.duckdream.superbuy.service.GoodsService;
import com.duckdream.superbuy.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/to_list")
    public String list(Model model, User user) {
        model.addAttribute("user", user);
        //查询商品列表
        List<GoodsVO> goodsList = goodsService.listGoodsVO();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, User user, @PathVariable("goodsId")long goodsId) {
        model.addAttribute("user", user);

        GoodsVO goods = goodsService.getGoodsVOByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        //
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int msStatus = 0;  //当前状态
        int remainSeconds = 0;  //距离秒杀开始剩余时间

        if(now < startAt) { //秒杀没开始，倒计时
            msStatus = 0;
            remainSeconds = (int)((startAt - now)/1000);
        } else if (now > endAt){ //秒杀已结束
            msStatus = 2;
            remainSeconds = -1;
        } else { //秒杀进行中
            msStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("msStatus", msStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }
}
