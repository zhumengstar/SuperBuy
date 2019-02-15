package com.duckdream.superbuy.controller;

import com.duckdream.superbuy.result.CodeMsg;
import com.duckdream.superbuy.result.Result;
import com.duckdream.superbuy.service.UserService;
import com.duckdream.superbuy.util.ValidatorUtil;
import com.duckdream.superbuy.vo.LoginVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(LoginVO loginVO) {
        log.info(loginVO.toString());
        //参数校验
        String inputPass = loginVO.getPassword();
        String mobile = loginVO.getMobile();
        if(StringUtils.isEmpty(inputPass)) {
            return Result.error(CodeMsg.PASSWORD_EMPTY);
        }
        if(StringUtils.isEmpty(mobile)) {
            return Result.error(CodeMsg.MOBILE_EMPTY);
        }
        if(!ValidatorUtil.isMobile(mobile)) {
            return Result.error(CodeMsg.MOBILE_ERROR);
        }
        //登录
        CodeMsg cm = userService.login(loginVO);
        if(cm.getCode() == 0) {
            return Result.success(true);
        } else {
            return Result.error(cm);
        }
    }


}
