package com.duckdream.superbuy.controller;

import com.duckdream.superbuy.entity.User;
import com.duckdream.superbuy.redis.RedisService;
import com.duckdream.superbuy.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/to_list")
    public String toList(Model model,
            @CookieValue(value = UserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
            @RequestParam(value = UserService.COOKIE_NAME_TOKEN, required = false) String paramToken) {
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return "login";
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken:paramToken;
        User user = userService.getByToken(token);
        model.addAttribute("user", user);
        return "goods_list";
    }
}
