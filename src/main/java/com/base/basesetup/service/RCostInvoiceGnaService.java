package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.RCostInvoiceGnaDTO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.RCostInvoiceGnaVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface RCostInvoiceGnaService {

	List<RCostInvoiceGnaVO> getAllRCostInvoiceGnaByOrgId(Long orgId, String finYear, String branchCode);

	List<RCostInvoiceGnaVO> getAllRCostInvoiceGnaById(Long id);

	String getRCostInvoiceGnaDocId(Long orgId, String finYear, String branch, String branchCode);

	Map<String, Object> updateCreateRCostInvoiceGna(RCostInvoiceGnaDTO rCostInvoiceGnaDTO) throws ApplicationException;

	List<PartyMasterVO> getAllVendorFromPartyMaster(Long orgId, String partyType);

	List<Map<String, Object>> getChargeLedgerFromGroup(Long orgId);

	List<Map<String, Object>> getSectionNameFromTDSMaster(Long orgId, String section);

	List<Map<String, Object>> getGstTypeDetails(Long orgId, String branchCode, String stateCode);

	List<Map<String, Object>> getCurrencyAndExrates(Long orgId);

	List<Map<String, Object>> getStateFromPartyMaster(Long orgId, String partyCode);

	List<Map<String, Object>> getCityFromPartyMaster(Long orgId, String partyCode, String state, String addressType);

	List<Map<String, Object>> findByAddressTypeFromPartyAddress(Long orgId, String state, String partyCode);

	RCostInvoiceGnaVO approveRCostInvoiceGna(Long orgId, Long id, String docId, String action, String actionBy)
			throws ApplicationException;

//	List<Map<String, Object>> getRegisterCostInvoiceReport(Long orgId, String branchCode,
//			String fromDate, String toDate,String partyCode,String finYear);

	List<Map<String, Object>> getRegisterCostInvoiceReport(Long orgId, String branchCode, String fromDate,
			String toDate, String partyCode, String finYear);

	RCostInvoiceGnaVO getRCostInvoiceGnaByDocIdandScreenCode(String ScreenCode, String docId);
	// screencode

	List<Map<String, Object>> getRCostInvoiceGnaCount(Long orgId, String finYear, String branchCode);

}
