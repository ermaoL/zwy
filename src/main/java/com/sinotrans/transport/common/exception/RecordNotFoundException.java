package com.sinotrans.transport.common.exception;

import com.sinotrans.transport.common.ErrorCode;
import com.sinotrans.transport.common.enums.DomainType;
import org.apache.log4j.Logger;

/**
 * Created by emi on 2016/5/10.
 */
public class RecordNotFoundException extends RestException {

    public RecordNotFoundException(DomainType domainType, Long domainId, Logger logger) {
        super(ErrorCode.RECORD_NOT_FOUND, String.format("%s记录不存在", domainType.getDesc()));
        logger.error(String.format("%s[%d]记录不存在", domainType.getDesc(), domainId));
    }

    public RecordNotFoundException(DomainType fromDomainType, long fromDomainId, DomainType domainType, Logger logger) {
        super(ErrorCode.RECORD_NOT_FOUND, String.format("%s记录不存在", domainType.getDesc()));
        logger.error(String.format("由%s[%d]寻找的%s记录不存在", fromDomainType, fromDomainId, domainType.getDesc()));
    }
}
