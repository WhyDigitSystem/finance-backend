package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.CostDebitNoteDTO;
import com.base.basesetup.entity.CostDebitNoteVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface CostDebitNoteService {

	Map<String, Object> updateCreateCostDebitNote(@Valid CostDebitNoteDTO costDebitNoteDTO) throws ApplicationException;

	List<CostDebitNoteVO> getCostDebitNoteByOrgId(Long orgId);

	List<CostDebitNoteVO> getCostDebitNoteById(Long id);

	List<CostDebitNoteVO> getActiveCostDebitNote();

	CostDebitNoteVO getAllCostDebitNoteByDocId(Long orgId, String docId);

	String getCostDebitNoteDocId(Long orgId, String finYear, String branch, String branchCode);

	List<Map<String, Object>> partyDetailsForCostDebitNote(Long orgId, String branch, String finYear);

	List<Map<String, Object>> chargeTypeDetailsForCostDebitNote(Long orgId);

	List<Map<String, Object>> getAllDocIdForCostInvoice(Long orgId);

	List<Map<String, Object>> getCurrencyAndExrates(Long orgId);

	List<CostDebitNoteVO> getOrginBillNoByParty(Long orgId, String party, String branchCode);

	List<Map<String, Object>> partyTypeForCostDebitNote(Long orgId, String branch, String finYear);

	List<Map<String, Object>> getInterAndIntraDetailsForCostInvoice(Long orgId, String gstType,
			List<String> gstPercent);

	CostDebitNoteVO approveCostDebitNote(Long orgId, Long id, String docId, String action, String actionBy)
			throws ApplicationException;

}
