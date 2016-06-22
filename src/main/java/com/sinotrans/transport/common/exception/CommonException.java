package com.sinotrans.transport.common.exception;


import org.apache.log4j.Logger;

/**
 * Created by emi on 2016/5/11.
 */
public class CommonException extends RestException {

    public CommonException(int code, String message, String detailInfo, Logger logger) {
        super(code, message);
        if (null != logger) {
            logger.error(detailInfo);
        }

    }
}
