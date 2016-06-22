package com.sinotrans.transport.common.cache;

import com.sinotrans.transport.common.enums.CompanyType;

/**
 * Created by emi on 2016/5/31.
 */
public class CheckResultAdminVo {

    private boolean success;

    private String adminCode;

    private String adminName;

    private CompanyType companyType;

    private int errorCode;

    private String errorMsg;

    public CheckResultAdminVo() {
        super();
    }

    public CheckResultAdminVo(String adminCode, String adminName, CompanyType companyType) {
        this();
        this.success = true;
        this.adminCode = adminCode;
        this.adminName = adminName;
        this.companyType = companyType;
    }

    public CheckResultAdminVo(int errorCode, String errorMsg) {
        this();
        this.success = false;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
