package com.sinotrans.transport.service;

import com.sinotrans.transport.common.enums.CompanyType;
import com.sinotrans.transport.dto.*;
import com.sinotrans.transport.dto.common.CommonListResponse;
import com.sinotrans.transport.dto.common.CommonNoPageListResponse;
import com.sinotrans.transport.model.Order;

import java.util.List;

/**
 * Created by emi on 2016/5/31.
 */
public interface AdminService {


    AdminLoginResponse login(String adminName, String adminPass, String adminType, String ipAddress);

    CommonListResponse searchImport(CompanyType companyType, OrderImportSearchRequest searchRequest);

    CommonListResponse searchExport(CompanyType companyType, OrderExportSearchRequest searchRequest);

    OrderImportAllResponse searchImportAll(CompanyType companyType, Long orderId);

    OrderExportAllResponse searchExportAll(CompanyType companyType, Long orderId);

    OrderVerifierResponse verifierOrder(String adminCode, String adminName, CompanyType companyType, Long orderId, OrderVerifierRequest verifierRequest);

    CommonNoPageListResponse importAddress(Long containerId);

    CommonNoPageListResponse exportAddress(Long containerId);

    CommonListResponse checkList(String adminCode, String adminName, CompanyType companyType, UserSearchRequest searchRequest);

    UserVerifierResponse verifierUser(String adminCode, String adminName, CompanyType companyType, long userId, UserVerifierRequest verifierRequest);

    OrderImportAlterResponse importModify(String adminCode, CompanyType companyType, OrderImportAlterRequest alterRequest);

    CommonNoPageListResponse modifyImportContainers(String adminCode, CompanyType companyType, ContainerImportAlterRequest alterRequest);

    ContainerDropResponse dropImportContainers(String adminCode, CompanyType companyType, DomainsDropRequest dropRequest);

    CommonNoPageListResponse modifyImportAddress(String adminCode, CompanyType companyType, AddressImportAlterRequest alterRequest);

    CommonNoPageListResponse dropImportAddresses(String adminCode, CompanyType companyType, DomainsDropRequest dropRequest);

    OrderExportAlterResponse exportModify(String adminCode, CompanyType companyType, OrderExportAlterRequest alterRequest);

    CommonNoPageListResponse modifyExportContainer(String adminCode, CompanyType companyType, ContainerExportAlterRequest alterRequest);

    ContainerDropResponse dropExportContainers(String adminCode, CompanyType companyType, DomainsDropRequest dropRequest);

    CommonNoPageListResponse modifyExportAddress(String adminCode, CompanyType companyType, AddressExportAlterRequest alterRequest);

    CommonNoPageListResponse dropExportAddresses(String adminCode, CompanyType companyType, DomainsDropRequest dropRequest);

}
