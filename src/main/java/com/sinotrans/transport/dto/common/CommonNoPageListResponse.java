package com.sinotrans.transport.dto.common;

import java.util.List;

/**
 * Created by emi on 2016/5/17.
 */
public class CommonNoPageListResponse extends RestResponse{

    private List data;

    public CommonNoPageListResponse(List data) {
        this();
        this.data = data;
    }

    public CommonNoPageListResponse() {
        super();
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
