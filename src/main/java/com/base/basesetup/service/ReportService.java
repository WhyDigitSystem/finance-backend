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
		
		List<IssueManifestProviderVO> getAllIssueManifestProvider(Long orgId, Long finYear);
		
		Optional<IssueManifestProviderVO> getAllIssueManifestProviderById(Long id);

		List<IssueManifestProviderVO> getAllIssueManifestProviderForPendingIssueRequest(Long orgId);
		
		List<Map<String, Object>> getFillGridForTaxInvoice(Long orgId);
		
		List<Map<String, Object>> getMimFillGridgettransaction(Long orgId,String Receiver,String docId);
		
		List<Map<String, Object>> getMimFillGridgetKitDetails(Long orgId, String TransactionNo);
		
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
		
		List<QuotationVO> getQuotationByorgId(Long orgId);

		Optional<QuotationVO> getQutationById(Long id);

		Map<String, Object> createUpdateQuotatio(QuotationDTO quotationDTO) throws ApplicationException;

		List<Map<String, Object>> getReceiptRegisterReport(Long orgId, String partyName, String branchCode,
				String finYear, String fromDate, String toDate);

		List<Map<String, Object>> getPaymentRegisterReport(Long orgId, String partyCode, String branchCode,
				String finYear, String fromDate, String toDate);

		List<Map<String, Object>> getOrginBillNoBased(Long orgId, String orginBillNo);

		List<Map<String, Object>> getMimReportDetails(String type, Long orgId, String customerName, String finYear,
				String toDate, String fromDate);

		List<IssueManifestProviderVO> findMIMReports(String type, Long orgId, String customerName, String finYear,
				String toDate, String fromDate);

		List<RetrievalManifestProviderVO> findRIMReports(String type, Long orgId, String customerName, String finYear,
				String toDate, String fromDate);

		List<Map<String, Object>> findMimSummaryReport(String type, Long orgId, String customerName, String finYear,
				String fromDate, String toDate);

		List<Map<String, Object>> findRimSummaryReport(String type, Long orgId, String customerName, String finYear,
				String fromDate, String toDate);

		List<Map<String, Object>> getApAgeing(Long orgId, String branch, String partyname, String asdate,
				String baseType);

//		List<RetrievalManifestProviderVO> findRIMMIMReports(String type, Long orgId, String customerName,
//				String finYear, String toDate, String fromDate);

		

		
	
}
