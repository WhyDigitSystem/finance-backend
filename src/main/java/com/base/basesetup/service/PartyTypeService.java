package com.base.basesetup.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.CustomersDTO;
import com.base.basesetup.dto.PartyTypeDTO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.PartyTypeVO;
import com.base.basesetup.entity.VendorDTO;
import com.base.basesetup.exception.ApplicationException;

import io.jsonwebtoken.io.IOException;

@Service
public interface PartyTypeService {

	// PartyType

	PartyTypeVO createUpdatePartyType(@Valid PartyTypeDTO partyTypeDTO) throws ApplicationException;

	List<PartyTypeVO> getAllPartyTypeByOrgId(Long orgid);

	List<PartyTypeVO> getPartyTypeById(Long id);

	List<Map<String, Object>> getPartyCodeByOrgIdAndPartyType(Long orgid, String partytype);

	void uploadCustomerData(MultipartFile files, Long orgId, String createdBy) throws Exception;

	Map<String, Object> createUpdateCustomer(@Valid CustomersDTO customersDTO) throws ApplicationException;

	Optional<PartyMasterVO> getCustomersById(Long id);

	List<PartyMasterVO> getAllCustomers(Long orgId);
	
	List<Map<String, Object>> getAllTransporters(Long orgId);

	// VENDORS

	Map<String, Object> createUpdateVendor(@Valid VendorDTO vendorDTO) throws ApplicationException;

	Optional<PartyMasterVO> getVendorsById(Long id);

	List<PartyMasterVO> getAllVendors(Long orgId);

	void vendorUpload(MultipartFile files, Long orgId, String createdBy) throws Exception;

	List<Map<String, Object>> getSectionNameFromTds(Long orgId, String section);

	List<Map<String, Object>> getAccountNameFromGroup(Long orgId);
	
	List<Map<String, Object>> getAllPartyLedgerReport(Long orgId,String partyName,String partyType,String branch,String fromDate,String toDate);
	
	List<Map<String, Object>> getAllLedgerReport(Long orgId,String accountName,String branchCode,String fromDate,String toDate);

	List<Map<String, Object>> getMonthlyAndYearWiseData(Long orgId, String month,String finYear,String branchCode);

	List<Map<String, Object>> getSalesDistributionData(Long orgId, String month, String finYear, String branchCode);

	
}
