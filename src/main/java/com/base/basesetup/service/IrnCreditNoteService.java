package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.IrnCreditNoteDTO;
import com.base.basesetup.entity.IrnCreditNoteVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.TaxInvoiceVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface IrnCreditNoteService {
	
//	IrnCredit
	List<IrnCreditNoteVO> getAllIrnCreditByOrgId(Long orgId);
	
	List<IrnCreditNoteVO> getAllIrnCreditById(Long id);

	Map<String, Object> updateCreateIrnCreditNote(IrnCreditNoteDTO irnCreditDTO) throws ApplicationException;
	
	String getIrnCreditNoteDocId(Long orgId, String finYear, String branch, String branchCode);

	List<PartyMasterVO> getAllPartyByPartyType(Long orgId, String partyType);
	
	List<TaxInvoiceVO>getOriginBillNofromTaxInvoiceByParty(Long orgId,String party,String branchCode);
	
	IrnCreditNoteVO approveIrnCreditNote(Long orgId,Long id,String docId,String action,String actionBy) throws ApplicationException;
}
