package com.sinotrans.transport.common.cache;

import com.sinotrans.transport.common.enums.CompanyType;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by emi on 2016/5/31.
 */
public class TokenCacheAdmin {

    private String adminCode;//对应gbs的 employeeCode（登录名）, 对应管理员的登录名

    private String adminName;//对应gbs的 employeeName, 对应"Admin"

    private CompanyType companyType;

    private String token;

    private String ipStr;

    private long createTime;

    private long lastLoginTime;

    private AtomicInteger count;//访问计数

    public TokenCacheAdmin(String adminCode, String adminName, CompanyType companyType, String token, String ipStr) {
        this();
        this.adminCode = adminCode;
        this.adminName = adminName;
        this.companyType = companyType;
        this.token = token;
        this.ipStr = ipStr;
    }

    public TokenCacheAdmin() {
        super();
        this.createTime = System.currentTimeMillis();
        this.lastLoginTime = this.createTime;
        this.count = new AtomicInteger(0);
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public CompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(CompanyType companyType) {
        this.companyType = companyType;
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

    public void updateSelf(String adminCode, String adminName, CompanyType companyType, String md5Token, String ipAddress) {
        this.adminCode = adminCode;
        this.adminName = adminName;
        this.companyType = companyType;
        this.token = md5Token;
        this.ipStr = ipAddress;
        this.createTime = System.currentTimeMillis();
        this.lastLoginTime = this.createTime;
        this.count.getAndIncrement();
    }
}
