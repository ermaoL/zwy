package com.sinotrans.transport.dto.out;

import com.sinotrans.transport.dto.common.RestResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 2016/5/18.
 */
public class QueryTypeCodeResponse extends RestResponse {

    List<Map<String,Object>> typeCodes = new ArrayList<Map<String,Object>>();

    public List<Map<String, Object>> getTypeCodes() {
        return typeCodes;
    }
    public void setTypeCodes(List<Map<String, Object>> typeCodes) {
        this.typeCodes = typeCodes;
    }

    public QueryTypeCodeResponse() {
        super();
    }

    public QueryTypeCodeResponse(List<Map<String, Object>> typeCodes) {
        this();
        this.typeCodes = typeCodes;
    }

    public QueryTypeCodeResponse(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }


}
