package com.duckdream.superbuy.controller;

import com.duckdream.superbuy.rabbitmq.MQSender;
import com.duckdream.superbuy.result.CodeMsg;
import com.duckdream.superbuy.result.Result;
import org.apache.catalina.startup.RealmRuleSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.plugin2.message.Message;

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

//    @Autowired
//    MQSender sender;

//    @RequestMapping("/mq")
//    @ResponseBody
//    public Result<String> mq() {
//        sender.send("hello~");
//        return Result.success("Hi");
//    }
//
//    @RequestMapping("/mq/topic")
//    @ResponseBody
//    public Result<String> mqTopic() {
//        sender.sendTopic("hello~");
//        return Result.success("Hi");
//    }
//
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public Result<String> mqFanout() {
//        sender.sendFanout("hello~");
//        return Result.success("Hi");
//    }
//
//    @RequestMapping("/mq/header")
//    @ResponseBody
//    public Result<String> mqHeader() {
//        sender.sendHeader("hello~");
//        return Result.success("Hi");
//    }
}
