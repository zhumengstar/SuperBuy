package com.duckdream.superbuy.redis;

public class OrderKey extends BasePrefix {

    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getMsOrderByUidGid = new OrderKey("moug");

}
