package com.sinotrans.transport.common.exception;

/**
 * Created by emi on 2016/5/11.
 */
public class RestException extends RuntimeException {

    private int code;

    public RestException(int code,String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
