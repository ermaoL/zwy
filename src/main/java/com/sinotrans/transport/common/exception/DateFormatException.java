package com.sinotrans.transport.common.exception;

import com.sinotrans.transport.common.ErrorCode;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;

/**
 * Created by emi on 2016/5/17.
 */
public class DateFormatException extends RestException {
    public DateFormatException(SimpleDateFormat sdf, Logger logger) {
        super(ErrorCode.DATE_FORMAT_EXCEPTION, "日期格式错误");
        if (null != logger) {
            logger.error("日期格式错误,请传入" + sdf.toPattern() + "格式的日期字符串");
        }
    }
}
