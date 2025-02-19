package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.RCostInvoiceGnaDTO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.RCostInvoiceGnaVO;
import com.base.basesetup.entity.TdsMasterVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface RCostInvoiceGnaService {

	List<RCostInvoiceGnaVO> getAllRCostInvoiceGnaByOrgId(Long orgId);

	RCostInvoiceGnaVO getAllRCostInvoiceGnaById(Long id);

	String getRCostInvoiceGnaDocId(Long orgId, String finYear, String branch, String branchCode);

	Map<String, Object> updateCreateRCostInvoiceGna( RCostInvoiceGnaDTO rCostInvoiceGnaDTO) throws ApplicationException;

	List<PartyMasterVO> getAllVendorFromPartyMaster(Long orgId, String partyType);

	List<Map<String, Object>> getChargeLedgerFromGroup(Long orgId);

	List<Map<String, Object>> getSectionNameFromTDSMaster(Long orgId, String section);

	List<Map<String, Object>> getGstTypeDetails(Long orgId, String branchCode, String stateCode);

	List<Map<String, Object>> getCurrencyAndExrates(Long orgId);

	List<Map<String, Object>> getStateFromPartyMaster(Long orgId, String partyCode);


}
