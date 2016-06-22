package com.sinotrans.transport.dto;

import com.sinotrans.transport.dto.common.RestResponse;

/**
 * Created by emi on 2016/5/27.
 */
public class UserAlterPassResponse extends RestResponse {

    public UserAlterPassResponse(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public UserAlterPassResponse() {
        super();
    }
}
