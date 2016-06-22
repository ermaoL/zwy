package com.sinotrans.transport.dto.out;

import com.sinotrans.transport.dto.common.RestResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 2016/5/25.
 */
public class QueryVesselEtdResponse extends RestResponse {

    List<Map<String,Object>> vesselEtds = new ArrayList<Map<String,Object>>();

    public List<Map<String, Object>> getVesselEtds() {
        return vesselEtds;
    }

    public void setVesselEtds(List<Map<String, Object>> vesselEtds) {
        this.vesselEtds = vesselEtds;
    }

    public QueryVesselEtdResponse() {
        super();
    }

    public QueryVesselEtdResponse(List<Map<String, Object>> vesselEtds) {
        this();
        this.vesselEtds = vesselEtds;
    }

    public QueryVesselEtdResponse(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }


}
