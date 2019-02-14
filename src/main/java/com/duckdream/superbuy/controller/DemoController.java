package com.duckdream.superbuy.controller;

import com.duckdream.superbuy.result.CodeMsg;
import com.duckdream.superbuy.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/")
    @ResponseBody
    public String home() {
        return "hello duck~";
    }

    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello() {
        return Result.success("hi, duck");
    }

    @RequestMapping("/error")
    @ResponseBody
    public Result<String> error() {
        return Result.error(CodeMsg.SERVER_ERROR);
    }
    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "duck");
        return "hello";
    }
}
