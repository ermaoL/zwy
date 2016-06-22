package com.sinotrans.transport.common.exception;

import com.sinotrans.transport.common.ErrorCode;
import com.sinotrans.transport.common.enums.DomainType;
import org.apache.log4j.Logger;

/**
 * Created by emi on 2016/5/20.
 */
public class RecordHasDeleteException extends RestException {
    public RecordHasDeleteException(DomainType domainType, Long domainId, Logger logger) {
        super(ErrorCode.DOMAIN_HAS_DELETE, "目标已被删除，无法操作");
        if (null != logger) {
            logger.info(String.format("%s[%d]已被删除,无法操作", domainType.getDesc(), domainId));
        }
    }
}
