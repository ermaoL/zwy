package com.sinotrans.transport.dto.out;

import com.sinotrans.transport.dto.common.RestResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 2016/5/25.
 */
public class QueryPortCodeResponse extends RestResponse {

    List<Map<String,Object>> portCodes = new ArrayList<Map<String,Object>>();

    public List<Map<String, Object>> getPortCodes() {
        return portCodes;
    }

    public void setPortCodes(List<Map<String, Object>> portCodes) {
        this.portCodes = portCodes;
    }

    public QueryPortCodeResponse() {
        super();
    }

    public QueryPortCodeResponse(List<Map<String, Object>> portCodes) {
        this();
        this.portCodes = portCodes;
    }

    public QueryPortCodeResponse(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }


}
