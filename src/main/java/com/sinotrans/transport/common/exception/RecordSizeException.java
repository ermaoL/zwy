package com.sinotrans.transport.common.exception;


import com.sinotrans.transport.common.ErrorCode;
import com.sinotrans.transport.common.enums.DomainType;
import org.apache.log4j.Logger;

/**
 * Created by emi on 2016/6/6.
 */
public class RecordSizeException extends RestException {
    public RecordSizeException(DomainType domain, int proposeSize, int realSize, String detailInfo, Logger logger) {
        super(ErrorCode.DATA_SIZE_ERROR, domain.getDesc() + "数量异常");
        if (null != logger) {
            logger.error(String.format("%s数量异常，期望%d条，实际%d条，搜索条件%s", domain.getDesc(), proposeSize, realSize, detailInfo));
        }
    }
}
