package com.sinotrans.transport.common.cache;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by emi on 2016/5/10.
 */
public class TokenCache {

    private long userId;

    private String userName;

    private String token;

    private String ipStr;

    private long createTime;

    private long lastLoginTime;

    private AtomicInteger count;//访问计数

    public TokenCache() {
        super();
        this.createTime = System.currentTimeMillis();
        this.lastLoginTime = this.createTime;
        this.count = new AtomicInteger(0);
    }

    public TokenCache(long userId,String userName, String token, String ipStr) {
        this();
        this.userId = userId;
        this.userName = userName;
        this.token = token;
        this.ipStr = ipStr;

    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIpStr() {
        return ipStr;
    }

    public void setIpStr(String ipStr) {
        this.ipStr = ipStr;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public AtomicInteger getCount() {
        return count;
    }

    public void setCount(AtomicInteger count) {
        this.count = count;
    }

    public void updateSelf(long userId, String userName, String md5Token, String ipAddress) {
        this.userId = userId;
        this.userName = userName;
        this.token = md5Token;
        this.ipStr = ipAddress;
        this.createTime = System.currentTimeMillis();
        this.lastLoginTime = this.createTime;
        this.count.getAndIncrement();
    }
}
