package com.duckdream.superbuy.result;

public class CodeMsg {
    private int code;
    private String msg;

    //ͨ���쳣
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(50100, "������쳣");

    //��¼ģ�� 502XX

    //��Ʒģ�� 503XX

    //����ģ�� 504XX

    //��ɱģ�� 505XX


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
