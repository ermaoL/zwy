package com.sinotrans.transport.common.cache;

/**
 * Created by emi on 2016/5/10.
 */
public class CheckResultVo {

    private boolean success;

    private long userId;

    private int errorCode;

    private String errorMsg;

    public CheckResultVo() {
        super();
        this.success = true;
    }

    public CheckResultVo(long userId) {
        this();
        this.userId = userId;
    }

    public CheckResultVo(int errorCode, String errorMsg) {
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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
