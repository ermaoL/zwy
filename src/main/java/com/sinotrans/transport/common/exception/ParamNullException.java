package com.sinotrans.transport.common.exception;

import com.sinotrans.transport.common.ErrorCode;
import org.apache.log4j.Logger;

/**
 * Created by emi on 2016/6/17.
 */
public class ParamNullException extends RestException {
    public ParamNullException(String param, Logger logger) {
        super(ErrorCode.PARAM_NULL, "请求参数：" + param + "不能为空");
        if (null != logger) {
            logger.info("请求参数：" + param + "不能为空");
        }
    }
}
