package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.RegisterCostInvoiceGnaDTO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.RegisterCostInvoiceGnaVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface RCostInvoiceGnaService {


	List<PartyMasterVO> getAllVendorFromPartyMaster(Long orgId, String partyType);

	List<Map<String, Object>> getChargeLedgerFromGroup(Long orgId);

	List<Map<String, Object>> getSectionNameFromTDSMaster(Long orgId, String section);

	List<Map<String, Object>> getGstTypeDetails(Long orgId, String branchCode, String stateCode);

	List<Map<String, Object>> getCurrencyAndExrates(Long orgId);

	List<Map<String, Object>> getStateFromPartyMaster(Long orgId, String partyCode);

	List<Map<String, Object>> getCityFromPartyMaster(Long orgId, String partyCode, String state, String addressType);

	List<Map<String, Object>> findByAddressTypeFromPartyAddress(Long orgId, String state, String partyCode);


	List<Map<String, Object>> getRegisterCostInvoiceReport(Long orgId, String branchCode, String fromDate,
			String toDate, String partyCode, String finYear);

	RegisterCostInvoiceGnaVO getRCostInvoiceGnaByDocIdandScreenCode(String ScreenCode, String docId);
	// screencode

	List<Map<String, Object>> getRCostInvoiceGnaCount(Long orgId, String finYear, String branchCode);
	
	List<RegisterCostInvoiceGnaVO> getAllRCostInvoiceGnaByOrgId(Long orgId, String finYear, String branchCode);

	List<RegisterCostInvoiceGnaVO> getAllRCostInvoiceGnaById(Long id);

	Map<String, Object> getRCostInvoiceGnaDocId(Long orgId, String finYear, String branch, String branchCode);

	Map<String, Object> updateCreateRCostInvoiceGna(RegisterCostInvoiceGnaDTO registerCostInvoiceGnaDTO)
			throws ApplicationException;

	RegisterCostInvoiceGnaVO approveRCostInvoiceGna(Long orgId, Long id, String docId, String action, String actionBy)
			throws ApplicationException;

}
