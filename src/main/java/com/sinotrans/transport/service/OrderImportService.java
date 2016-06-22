package com.sinotrans.transport.service;

import com.sinotrans.transport.dto.*;
import com.sinotrans.transport.dto.common.CommonListResponse;
import com.sinotrans.transport.dto.common.CommonNoPageListResponse;
import com.sinotrans.transport.dto.common.RestResponse;

/**
 * Created by emi on 2016/5/13.
 */
public interface OrderImportService {

    CommonListResponse search(Long userId, OrderImportSearchRequest searchRequest);

    OrderImportAlterResponse create(Long userId, OrderImportAlterRequest alterRequest);

    CommonNoPageListResponse searchContainer(Long userId, Long orderId);

    CommonNoPageListResponse searchAddress(Long userId, Long containerId);

    OrderImportAlterResponse alter(Long userId, Long orderId, OrderImportAlterRequest alterRequest);

    CommonNoPageListResponse modifyContainers(Long userId, ContainerImportAlterRequest alterRequest);

    CommonNoPageListResponse modifyAddress(Long userId, AddressImportAlterRequest alterRequest);

    OrderImportAllResponse searchAll(Long userId, Long orderId);

    ContainerDropResponse dropContainers(Long userId, DomainsDropRequest dropRequest);

    CommonNoPageListResponse dropAddresses(Long userId, DomainsDropRequest dropRequest);

    RestResponse textXml(String s);

    OrderImportAllResponse sendOrder(Long userId, Long orderId);
}