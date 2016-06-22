package com.sinotrans.transport.common.exception;

import com.sinotrans.transport.common.ErrorCode;
import com.sinotrans.transport.common.enums.DomainType;
import org.apache.log4j.Logger;

/**
 * Created by emi on 2016/5/20.
 */
public class DataException extends RestException {
    public DataException(String message, String detail, Logger logger) {
        super(ErrorCode.DATA_ERROR, message);
        logger.error(detail);
    }
}
