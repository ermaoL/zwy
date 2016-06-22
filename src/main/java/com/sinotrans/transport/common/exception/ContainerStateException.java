package com.sinotrans.transport.common.exception;

import com.sinotrans.transport.common.ErrorCode;
import com.sinotrans.transport.common.enums.ContainerStateType;
import org.apache.log4j.Logger;

/**
 * Created by emi on 2016/5/30.
 */
public class ContainerStateException extends RestException {

    public ContainerStateException(ContainerStateType stateType, Long contId, Logger logger) {
        super(ErrorCode.CONTAINER_STATE_EXCEPTION, String.format("容器状态为%s，无法执行该操作", stateType.getDesc()));
        logger.info(String.format("容器[%d]状态为%s, 无法执行当前操作", contId, stateType.getDesc()));
    }
}
