package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.UrCostInvoiceGnaDTO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.UrCostInvoiceGnaVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface UrCostInvoiceGnaService {

	List<UrCostInvoiceGnaVO> getAllUrCostInvoiceGnaByOrgId(Long orgId, String finYear, String branchCode);

	Map<String, Object> updateCreateUrCostInvoiceGna(UrCostInvoiceGnaDTO urCostInvoiceGnaDTO)
			throws ApplicationException;

	List<UrCostInvoiceGnaVO> getUrCostInvoiceGnaById(Long id);

	String getUrCostInvoiceGnaDocId(Long orgId, String finYear, String branch, String branchCode);

	List<PartyMasterVO> getAllVendorFromPartyMaster(Long orgId, String partyType);

	List<Map<String, Object>> getChargeLedgerFromGroup(Long orgId);

	List<Map<String, Object>> getCurrencyAndExrateFromParty(Long orgId,String supplierCode);

	List<Map<String, Object>> getVendorAddressFromPartyMaster(Long orgId, String supplierCode);

	List<Map<String, Object>> getSectionNameFromMaster(Long orgId, String section);
}
