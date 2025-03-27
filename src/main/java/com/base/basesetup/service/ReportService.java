package com.base.basesetup.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.InvoiceDTO;
import com.base.basesetup.dto.IssueManifestProviderDTO;
import com.base.basesetup.dto.QuotationDTO;
import com.base.basesetup.dto.RetrievalManifestProviderDTO;
import com.base.basesetup.entity.DeclarationAndNotesVO;
import com.base.basesetup.entity.InvoiceVO;
import com.base.basesetup.entity.IssueManifestProviderVO;
import com.base.basesetup.entity.QuotationVO;
import com.base.basesetup.entity.RetrievalManifestProviderVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface ReportService {

	Map<String, Object> createUpdateInvoice(InvoiceDTO invoiceDTO) throws ApplicationException;

	InvoiceVO getInvoiceById(Long id);

	List<InvoiceVO> getAllInvoice(Long orgId);

	
	//ISSUEMANIFEST
	
		Map<String, Object> createUpdateIssuemanifest(IssueManifestProviderDTO issueManifestProviderDTO) throws ApplicationException;
		
		List<IssueManifestProviderVO> getAllIssueManifestProvider();
		
		Optional<IssueManifestProviderVO> getAllIssueManifestProviderById(Long id);

		List<IssueManifestProviderVO> getAllIssueManifestProviderForPendingIssueRequest(Long orgId);
		
		//RETERIVELMANIFEST
		
	    Map<String, Object> createUpdateRetrievalManifest(RetrievalManifestProviderDTO retrievalManifestProviderDTO) throws ApplicationException;
		
		List<RetrievalManifestProviderVO> getAllRetrievalManifestProvider();
		
		Optional<RetrievalManifestProviderVO> getRetrievalManifestProviderById(Long id);
		
		//DECLARATION PART
		
		DeclarationAndNotesVO createDeclarationAndNotes(DeclarationAndNotesVO declarationAndNotesVO);

		List<DeclarationAndNotesVO>  getAllDeclarationAndNotes();

//		List<Map<String, Object>> getReceiptRegisterReport(Long orgId, String partyName, String branchCode,
//				String finYear, String fromDate, String toDate);
		
		//quation
		
		List<Map<String, Object>> getQuotationByorgId(Long orgId);

		Optional<QuotationVO> getQutationById(Long id);

		Map<String, Object> createUpdateQuotatio(QuotationDTO quotationDTO) throws ApplicationException;

		
	
}
