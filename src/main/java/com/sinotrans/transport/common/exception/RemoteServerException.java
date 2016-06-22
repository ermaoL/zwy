package com.sinotrans.transport.common.exception;

import com.sinotrans.transport.common.ErrorCode;
import com.sinotrans.transport.common.enums.OperateType;
import org.apache.log4j.Logger;

/**
 * Created by emi on 2016/6/2.
 */
public class RemoteServerException extends RestException {
    public RemoteServerException(OperateType remoteOperate, Logger logger) {
        super(ErrorCode.REMOTE_SERVER_ERROR, remoteOperate.getDesc() + "远程服务器异常");
        if (null != logger) {
            logger.error(remoteOperate.getDesc() + "远程服务器异常");
        }
    }
}
