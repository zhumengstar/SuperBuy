package com.duckdream.superbuy.result;

public class CodeMsg {
    private int code;
    private String msg;

    //通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(50100, "服务端异常");

    //登录模块 502XX

    //商品模块 503XX

    //订单模块 504XX

    //秒杀模块 505XX


    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
