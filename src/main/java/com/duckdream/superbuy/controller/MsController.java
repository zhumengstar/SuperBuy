package com.duckdream.superbuy.controller;

import com.duckdream.superbuy.entity.MsOrder;
import com.duckdream.superbuy.entity.User;
import com.duckdream.superbuy.rabbitmq.MQSender;
import com.duckdream.superbuy.rabbitmq.MsMessage;
import com.duckdream.superbuy.redis.*;
import com.duckdream.superbuy.result.CodeMsg;
import com.duckdream.superbuy.result.Result;
import com.duckdream.superbuy.service.GoodsService;
import com.duckdream.superbuy.service.MsService;
import com.duckdream.superbuy.service.OrderService;
import com.duckdream.superbuy.util.MD5Util;
import com.duckdream.superbuy.util.UUIDUtil;
import com.duckdream.superbuy.vo.GoodsVO;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.security.krb5.internal.KdcErrException;
import sun.security.provider.MD5;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
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

    private Map<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();


    //系统初始化
    public void afterPropertiesSet() throws Exception {
        List<GoodsVO> goodsList = goodsService.listGoodsVO();
        if(goodsList == null) {
            return;
        }
        for (GoodsVO goods : goodsList) {
            redisService.set(GoodsKey.getMsGoodsStock, ""+goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false); //初始 没结束
        }
    }

    @RequestMapping(value = "/{path}/do_ms", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> ms(Model model, User user,
                              @RequestParam("goodsId")long goodsId,
                              @PathVariable("path")String path) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //验证path
        boolean check = msService.checkPath(user, goodsId, path);
        if(!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        //内存标记，减少Redis访问
        boolean over = localOverMap.get(goodsId); //false 没结束 true 结束
        if(over) {
            return Result.error(CodeMsg.MS_OVER);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getMsGoodsStock, ""+goodsId);
        if(stock < 0) {
            localOverMap.put(goodsId, true); //结束
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
    public Result<Long> msResult(Model model, User user,
                                 @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = msService.getMsResult(user.getId(), goodsId);
        return Result.success(result);
    }

    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMsPath(HttpServletRequest request, User user,
                                    @RequestParam("goodsId")long goodsId,
                                    @RequestParam("verifyCode")int verifyCode) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        boolean check = msService.checkVerifyCode(user, goodsId, verifyCode);
        if(!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path = msService.createMsPath(user, goodsId);
        return Result.success(path);
    }



    @RequestMapping(value="/reset", method=RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        List<GoodsVO> goodsList = goodsService.listGoodsVO();
        for(GoodsVO goods : goodsList) {
            goods.setStockCount(10);
            redisService.set(GoodsKey.getMsGoodsStock, ""+goods.getId(), 10);
            localOverMap.put(goods.getId(), false);
        }
        redisService.delete(OrderKey.getMsOrderByUidGid);
        redisService.delete(MsKey.isGoodsOver);
        msService.reset(goodsList);
        return Result.success(true);
    }

    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMsVerifyCode(HttpServletResponse response, User user, @RequestParam("goodsId")long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image = msService.createMsVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MS_FAIL);
        }
    }
}
