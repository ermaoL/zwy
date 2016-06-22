package com.sinotrans.transport.common.exception;

import com.sinotrans.transport.common.ErrorCode;
import com.sinotrans.transport.common.enums.OrderStateType;
import org.apache.log4j.Logger;

/**
 * Created by emi on 2016/5/30.
 */
public class OrderStateException extends RestException {

    public OrderStateException(OrderStateType orderStateType, Long orderId, Logger logger) {

        super(ErrorCode.ORDER_STATE_EXCEPTION, String.format("订单状态为%s, 无法执行当前操作", orderStateType.getDesc()));
        logger.info(String.format("订单[%d]状态为%s, 无法执行当前操作", orderId, orderStateType.getDesc()));
    }
}
