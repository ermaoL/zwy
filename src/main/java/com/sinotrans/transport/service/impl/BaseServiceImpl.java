package com.sinotrans.transport.service.impl;

import com.sinotrans.framework.service.mybatis.base.impl.MybatisManagerImpl;
import com.sinotrans.transport.dto.out.*;
import com.sinotrans.transport.service.BaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by emi on 2016/5/20.
 */
@Service
public class BaseServiceImpl extends MybatisManagerImpl implements BaseService{


    @Override
    public QueryCrmResponse queryCustomer(String codeOrName) {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<QueryCrmResponse> response = restTemplate.getForEntity("http://172.21.180.151:8080/gbs/api/queryCrmTypeIdIsCU?codeOrName=" + codeOrName, QueryCrmResponse.class);
        return response.getBody();
    }

    @Override
    public QueryCrmResponse queryContainerOwner(String codeOrName) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<QueryCrmResponse> response = restTemplate.getForEntity("http://172.21.180.151:8080/gbs/api/queryCrmTypeIdIsCW?codeOrName=" + codeOrName, QueryCrmResponse.class);
        return response.getBody();
    }

    @Override
    public QueryTypeCodeResponse queryTypeCode(String typeCode) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<QueryTypeCodeResponse> response = restTemplate.getForEntity("http://172.21.180.151:8080/gbs/api/" + typeCode, QueryTypeCodeResponse.class);
        return response.getBody();
    }


    @Override
    public QueryVesselResponse queryVessel(String codeOrEnname) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<QueryVesselResponse> response = restTemplate.getForEntity("http://172.21.180.151:8080/gbs/api/queryVessel?codeOrEnname=" + codeOrEnname, QueryVesselResponse.class);
        return response.getBody();
    }

    @Override
    public QueryVesselEtdResponse queryVesselEtd(String vesselCode, String voyage) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<QueryVesselEtdResponse> response = restTemplate.getForEntity("http://172.21.180.151:8080/gbs/api/queryVesselEtd?vesselCode=" + vesselCode + "&voyage=" + voyage, QueryVesselEtdResponse.class);
        return response.getBody();
    }

    @Override
    public QueryPortCodeResponse queryPortCode(String codeOrEnname) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<QueryPortCodeResponse> response = restTemplate.getForEntity("http://172.21.180.151:8080/gbs/api/queryPortCode?codeOrEnname=" + codeOrEnname, QueryPortCodeResponse.class);
        return response.getBody();
    }

    @Override
    public QueryDepotResponse queryDepot(String codeOrName) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<QueryDepotResponse> response = restTemplate.getForEntity("http://172.21.180.151:8080/gbs/api/queryDepot?codeOrName=" + codeOrName, QueryDepotResponse.class);
        return response.getBody();
    }
}
