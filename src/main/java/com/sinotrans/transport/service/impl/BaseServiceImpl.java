package com.sinotrans.transport.service.impl;

import com.sinotrans.framework.service.mybatis.base.impl.MybatisManagerImpl;
import com.sinotrans.transport.common.CommonConstant;
import com.sinotrans.transport.dto.out.*;
import com.sinotrans.transport.dto.out.vo.BillVo;
import com.sinotrans.transport.model.Order;
import com.sinotrans.transport.service.BaseService;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by emi on 2016/5/20.
 */
@Service
public class BaseServiceImpl extends MybatisManagerImpl implements BaseService{

    Logger logger = Logger.getLogger(BaseServiceImpl.class);

    @Override
    public QueryCrmResponse queryCustomer(String codeOrName) {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<QueryCrmResponse> response = restTemplate.getForEntity(CommonConstant.GBS_PREFIX_URL + "/api/queryCrmTypeIdIsCU?codeOrName=" + codeOrName, QueryCrmResponse.class);
        return response.getBody();
    }

    @Override
    public QueryCrmResponse queryContainerOwner(String codeOrName) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<QueryCrmResponse> response = restTemplate.getForEntity(CommonConstant.GBS_PREFIX_URL + "/api/queryCrmTypeIdIsCW?codeOrName=" + codeOrName, QueryCrmResponse.class);
        return response.getBody();
    }

    @Override
    public QueryTypeCodeResponse queryTypeCode(String typeCode) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<QueryTypeCodeResponse> response = restTemplate.getForEntity(CommonConstant.GBS_PREFIX_URL + "/api/" + typeCode, QueryTypeCodeResponse.class);
        return response.getBody();
    }


    @Override
    public QueryVesselResponse queryVessel(String codeOrEnname) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<QueryVesselResponse> response = restTemplate.getForEntity(CommonConstant.GBS_PREFIX_URL + "/api/queryVessel?codeOrEnname=" + codeOrEnname, QueryVesselResponse.class);
        return response.getBody();
    }

    @Override
    public QueryVesselEtdResponse queryVesselEtd(String vesselCode, String voyage) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<QueryVesselEtdResponse> response = restTemplate.getForEntity(CommonConstant.GBS_PREFIX_URL + "/api/queryVesselEtd?vesselCode=" + vesselCode + "&voyage=" + voyage, QueryVesselEtdResponse.class);
        return response.getBody();
    }

    @Override
    public QueryPortCodeResponse queryPortCode(String codeOrEnname) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<QueryPortCodeResponse> response = restTemplate.getForEntity(CommonConstant.GBS_PREFIX_URL + "/api/queryPortCode?codeOrEnname=" + codeOrEnname, QueryPortCodeResponse.class);
        return response.getBody();
    }

    @Override
    public QueryDepotResponse queryDepot(String codeOrName) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<QueryDepotResponse> response = restTemplate.getForEntity(CommonConstant.GBS_PREFIX_URL + "/api/queryDepot?codeOrName=" + codeOrName, QueryDepotResponse.class);
        return response.getBody();
    }

    @Override
    public QueryBillResponse queryBillList(QueryBillRequest request) {//importExport  1：进口  0：出口
        String importExport = StringUtils.isEmpty(request.getOrderType()) ? "" : (1 == Integer.parseInt(request.getOrderType()) ? "1" : "0");//importExport  1：进口  0：出口
        String beginEtd = StringUtils.isEmpty(request.getBeginTime()) ? "" : request.getBeginTime();
        String endEtd = StringUtils.isEmpty(request.getEndTime()) ? "" : request.getEndTime();
        String claimNo = StringUtils.isEmpty(request.getOrderBillNo()) ? "" : request.getOrderBillNo();
        String containerNo = StringUtils.isEmpty(request.getContainerNo()) ? "" : request.getContainerNo();
        String url = CommonConstant.GBS_PREFIX_URL + "/api/queryBillList?pageSize=" + request.getPageSize() + "&currentPage=" + request.getCurrentPage() + "&beginEtd=" + beginEtd + "&endEtd=" + endEtd + "&importExport=" + importExport + "&claimNo=" + claimNo + "&containerNo=" + containerNo;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<QueryBillResponse> response = restTemplate.getForEntity(url, QueryBillResponse.class);
        QueryBillResponse billResponse = response.getBody();
        List<BillVo> billVoList = billResponse.getData();
        if (null == billVoList || billVoList.size() == 0) {
            return billResponse;
        }
        Map<String, Object> conditionMap = new HashMap<String, Object>(1);
        List<Map<String, Object>> mapList;
        for (BillVo b : billVoList) {
            String orderBillNo;
            try {
                orderBillNo = b.getBills().get(0).get("claimNo");
            } catch (Exception e) {
                e.printStackTrace();
                logger.equals("gbs提单号获取失败");
                continue;
            }

            conditionMap.clear();
            conditionMap.put("orderBillNo", orderBillNo);
            mapList = myBatisDao.queryByCondition("SearchOrderShipAgency", conditionMap, null, null , null);
            String orderShipAgency = null;
            try {
                orderShipAgency = (String)mapList.get(0).get("ordeShipAgency");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("提单号：" + orderBillNo + "不存在");
                continue;
            }
            for (Map<String, String> m : b.getBills()) {
                m.put("orderShipAgency", orderShipAgency);
            }
        }
        billResponse.setData(billVoList);
        return billResponse;
    }
}
