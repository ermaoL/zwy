package com.sinotrans.transport.common.exception;

import com.sinotrans.transport.common.ErrorCode;
import com.sinotrans.transport.common.enums.DomainType;
import org.apache.log4j.Logger;

/**
 * Created by emi on 2016/5/30.
 */
public class DomainNotBelongException extends RestException {

    public DomainNotBelongException(DomainType original, Long originalId, String originalCreator, Long userId, Logger logger) {
        super(ErrorCode.NOT_BELONG, "用户无权操作该" + original.getDesc());
        logger.error(String.format("该%s[%d]属于用户[%s],当前用户[%d]无权进行操作", original.getDesc(), originalId, originalCreator,  userId));
    }
}
