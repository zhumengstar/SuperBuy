package com.duckdream.superbuy.redis;

public class MsKey extends BasePrefix {

    public MsKey(String prefix) {
        super(prefix);
    }

    public static MsKey isGoodsOver = new MsKey("go");

}
