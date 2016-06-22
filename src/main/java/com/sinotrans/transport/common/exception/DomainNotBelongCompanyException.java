package com.sinotrans.transport.common.exception;

import com.sinotrans.transport.common.ErrorCode;
import com.sinotrans.transport.common.enums.DomainType;
import org.apache.log4j.Logger;

/**
 * Created by emi on 2016/5/31.
 */
public class DomainNotBelongCompanyException extends RestException {
    public DomainNotBelongCompanyException(DomainType original, Long originalId, String originalCompany, String company, Logger logger) {
        super(ErrorCode.NOT_BELONG_COMPANY, String.format("该%s属于公司[%s],当前账号无权操作", original.getDesc(), originalCompany) );
        logger.error(String.format("该%s[%d]属于公司[%s],当前公司[%s]账号无权进行操作", original.getDesc(), originalId, originalCompany, company));
    }
}
