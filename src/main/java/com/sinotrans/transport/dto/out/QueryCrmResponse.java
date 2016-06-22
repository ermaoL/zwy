package com.sinotrans.transport.dto.out;

import com.sinotrans.transport.dto.common.RestResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 2016/5/18.
 */
public class QueryCrmResponse extends RestResponse {

    List<Map<String,Object>> crms = new ArrayList<Map<String,Object>>();

    public List<Map<String, Object>> getCrms() {
        return crms;
    }
    public void setCrms(List<Map<String, Object>> crms) {
        this.crms = crms;
    }

    public QueryCrmResponse() {
        super();
    }

    public QueryCrmResponse(List<Map<String, Object>> crms) {
        this();
        this.crms = crms;
    }

    public QueryCrmResponse(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }


}
