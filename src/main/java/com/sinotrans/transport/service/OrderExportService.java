package com.sinotrans.transport.service;

import com.sinotrans.transport.dto.*;
import com.sinotrans.transport.dto.common.CommonListResponse;
import com.sinotrans.transport.dto.common.CommonNoPageListResponse;

/**
 * Created by emi on 2016/5/13.
 */
public interface OrderExportService {

    CommonListResponse search(Long userId, OrderExportSearchRequest searchRequest);

    OrderExportAlterResponse create(Long userId, OrderExportAlterRequest request);

    OrderExportAlterResponse alter(Long userId, Long orderId, OrderExportAlterRequest request);

    CommonNoPageListResponse searchContainer(Long userId, Long orderId);

    CommonNoPageListResponse modifyContainer(Long userId, ContainerExportAlterRequest alterRequest);

    CommonNoPageListResponse searchAddress(Long userId, Long containerId);

    CommonNoPageListResponse modifyAddress(Long userId, AddressExportAlterRequest alterRequest);

    OrderExportAllResponse searchAll(Long userId, Long orderId);

    ContainerDropResponse dropContainers(Long userId, DomainsDropRequest dropRequest);

    CommonNoPageListResponse dropAddresses(Long userId, DomainsDropRequest dropRequest);

    OrderExportAllResponse sendOrder(Long userId, Long orderId);
}
