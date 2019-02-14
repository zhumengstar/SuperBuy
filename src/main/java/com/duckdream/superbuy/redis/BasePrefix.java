package com.duckdream.superbuy.redis;

public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;

    private String prefix;

    public BasePrefix(String prefix) {
        this(0, prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    public int expireSeconds() {
        //默认0 代表永不过期
        return 0;
    }

    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }

}
