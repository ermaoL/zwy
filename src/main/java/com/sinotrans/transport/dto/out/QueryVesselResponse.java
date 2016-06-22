package com.sinotrans.transport.dto.out;


import com.sinotrans.transport.dto.common.RestResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 2016/5/25.
 */
public class QueryVesselResponse extends RestResponse {

    List<Map<String,Object>> vessels = new ArrayList<Map<String,Object>>();

    public List<Map<String, Object>> getVessels() {
        return vessels;
    }

    public void setVessels(List<Map<String, Object>> vessels) {
        this.vessels = vessels;
    }

    public QueryVesselResponse() {
        super();
    }

    public QueryVesselResponse(List<Map<String, Object>> vessels) {
        this();
        this.vessels = vessels;
    }

    public QueryVesselResponse(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }


}
