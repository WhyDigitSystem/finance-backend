package com.base.basesetup.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

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

	void processExcelFile(MultipartFile excelFile, String creatdBy) throws IOException, java.io.IOException;

	void uploadCustomerData(MultipartFile files, Long orgId, String createdBy) throws Exception;

	Map<String, Object> createUpdateCustomer(@Valid CustomersDTO customersDTO) throws ApplicationException;

	Optional<PartyMasterVO> getCustomersById(Long id);

	List<PartyMasterVO> getAllCustomers(Long orgId);
	
	//VENDORS

	Map<String, Object> createUpdateVendor(@Valid VendorDTO vendorDTO) throws ApplicationException;

	Optional<PartyMasterVO> getVendorsById(Long id);

	List<PartyMasterVO> getAllVendors(Long orgId);

	void vendorUpload(MultipartFile files, Long orgId, String createdBy) throws Exception;

}
