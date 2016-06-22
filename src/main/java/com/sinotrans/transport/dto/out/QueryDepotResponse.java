package com.sinotrans.transport.dto.out;


import com.sinotrans.transport.dto.common.RestResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 2016/6/16.
 */
public class QueryDepotResponse extends RestResponse {

    List<Map<String,Object>> depots = new ArrayList<Map<String,Object>>();

    public List<Map<String, Object>> getDepots() {
        return depots;
    }
    public void setDepots(List<Map<String, Object>> depots) {
        this.depots = depots;
    }

    public QueryDepotResponse() {
        super();
    }

    public QueryDepotResponse(List<Map<String, Object>> depots) {
        this();
        this.depots = depots;
    }

    public QueryDepotResponse(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }
}
