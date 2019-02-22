package com.duckdream.superbuy.controller;

import com.duckdream.superbuy.entity.User;
import com.duckdream.superbuy.redis.GoodsKey;
import com.duckdream.superbuy.redis.RedisService;
import com.duckdream.superbuy.result.Result;
import com.duckdream.superbuy.service.GoodsService;
import com.duckdream.superbuy.vo.GoodsDetailVO;
import com.duckdream.superbuy.vo.GoodsVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, User user) {
        model.addAttribute("user", user);

        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }

        //查询商品列表
        List<GoodsVO> goodsList = goodsService.listGoodsVO();
        model.addAttribute("goodsList", goodsList);
        //return "goods_list";

        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if(!StringUtils.isEmpty(html)) { //如果模版不是空,就保存到缓存中
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    @RequestMapping(value = "/to_detail2/{goodsId}", produces = "text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request, HttpServletResponse response, Model model, User user, @PathVariable("goodsId")long goodsId) {
        model.addAttribute("user", user);

        //取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }

        GoodsVO goods = goodsService.getGoodsVOByGoodsId(goodsId);
        model.addAttribute("goods", goods);

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
        //return "goods_detail";

        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if(!StringUtils.isEmpty(html)) { //如果模版不是空,就保存到缓存中
            redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
        }
        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVO> detail(HttpServletRequest request, HttpServletResponse response, Model model, User user, @PathVariable("goodsId")long goodsId) {

        GoodsVO goods = goodsService.getGoodsVOByGoodsId(goodsId);

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

        GoodsDetailVO vo = new GoodsDetailVO();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMsStatus(msStatus);
        return Result.success(vo);
    }
}
