package com.duckdream.superbuy.redis;

public interface KeyPrefix {

    public int expireSeconds();

    public String getPrefix();
}
