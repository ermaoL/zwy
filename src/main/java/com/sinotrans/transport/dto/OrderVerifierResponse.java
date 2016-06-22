package com.sinotrans.transport.dto;

import com.sinotrans.transport.dto.common.RestResponse;

/**
 * Created by emi on 2016/6/1.
 */
public class OrderVerifierResponse extends RestResponse {

    public OrderVerifierResponse() {
        super();
    }

    public OrderVerifierResponse(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }
}
