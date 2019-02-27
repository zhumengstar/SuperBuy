package com.duckdream.superbuy.redis;

public class MsKey extends BasePrefix {


    public MsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MsKey isGoodsOver = new MsKey(0,"go");
    public static MsKey getMsPath = new MsKey(60,"mp");
    public static MsKey getMsVerifyCode = new MsKey(300, "vc");



}
