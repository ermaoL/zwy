package com.sinotrans.transport.service;


import com.sinotrans.transport.dto.out.*;

/**
 * Created by emi on 2016/5/20.
 */
public interface BaseService {

    QueryCrmResponse queryCustomer(String codeOrName);

    QueryCrmResponse queryContainerOwner(String codeOrName);

    QueryTypeCodeResponse queryTypeCode(String queryContainerType);

    QueryVesselResponse queryVessel(String codeOrEnname);

    QueryVesselEtdResponse queryVesselEtd(String vesselCode, String voyage);

    QueryPortCodeResponse queryPortCode(String codeOrEnname);

    QueryDepotResponse queryDepot(String codeOrName);
}
