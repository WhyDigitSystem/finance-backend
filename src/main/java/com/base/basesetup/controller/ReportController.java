package com.base.basesetup.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.basesetup.common.CommonConstant;
import com.base.basesetup.common.UserConstants;
import com.base.basesetup.dto.InvoiceDTO;
import com.base.basesetup.dto.IssueManifestProviderDTO;
import com.base.basesetup.dto.QuotationDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.dto.RetrievalManifestProviderDTO;
import com.base.basesetup.entity.DeclarationAndNotesVO;
import com.base.basesetup.entity.InvoiceVO;
import com.base.basesetup.entity.IssueManifestProviderVO;
import com.base.basesetup.entity.QuotationVO;
import com.base.basesetup.entity.RetrievalManifestProviderVO;
import com.base.basesetup.service.ReportService;

@CrossOrigin
@RestController
@RequestMapping("/api/reportController")
public class ReportController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

	@Autowired
	ReportService reportService;

	@PutMapping("/createUpdateInvoice")
	public ResponseEntity<ResponseDTO> createUpdateInvocie(@RequestBody InvoiceDTO invoiceDTO) {
		String methodName = "createUpdateInvocie()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> invoiceVO = reportService.createUpdateInvoice(invoiceDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, invoiceVO.get("message"));
			responseObjectsMap.put("invoiceVO", invoiceVO.get("invoiceVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllInvoiceByOrgId")
	public ResponseEntity<ResponseDTO> getAllInvoiceByOrgId(@RequestParam(required = true) Long orgId) {
		String methodName = "getAllInvoiceByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<InvoiceVO> invoiceVO = new ArrayList<>();
		try {
			invoiceVO = reportService.getAllInvoice(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Invoice Information get successfully");
			responseObjectsMap.put("invoiceVO", invoiceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Invoice Information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getInvoiceById")
	public ResponseEntity<ResponseDTO> getInvoiceById(@RequestParam(required = true) Long id) {
		String methodName = "getInvoiceById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		InvoiceVO invoiceVO = new InvoiceVO();
		try {
			invoiceVO = reportService.getInvoiceById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Invoice Information get successfully");
			responseObjectsMap.put("invoiceVO", invoiceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Invoice Information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/createUpdateIssuemanifest")
	public ResponseEntity<ResponseDTO> createUpdateIssuemanifest(
			@RequestBody IssueManifestProviderDTO issueManifestProviderDTO) {
		String methodName = "createUpdateIssuemanifest()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> issueManifestProviderVO = reportService
					.createUpdateIssuemanifest(issueManifestProviderDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, issueManifestProviderVO.get("message"));
			responseObjectsMap.put("issueManifestProviderVO", issueManifestProviderVO.get("issueManifestProviderVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllIssueManifestProvider")
	public ResponseEntity<ResponseDTO> getAllIssueManifestProvider(@RequestParam(required = true) Long orgId,
			@RequestParam(required = true) Long finYear) {
		String methodName = "getAllIssueManifestProvider()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<IssueManifestProviderVO> IssueManifestProviderVO = new ArrayList<IssueManifestProviderVO>();
		try {
			IssueManifestProviderVO = reportService.getAllIssueManifestProvider(orgId, finYear);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "IssueManifestProvider information get successfully");
			responseObjectsMap.put("IssueManifestProviderVO", IssueManifestProviderVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"IssueManifestProvider information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllIssueManifestProviderForPendingIR")
	public ResponseEntity<ResponseDTO> getAllIssueManifestProviderForPendingIR(@RequestParam Long orgId) {
		String methodName = "getAllIssueManifestProviderForPendingIR()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<IssueManifestProviderVO> IssueManifestProviderVO = new ArrayList<IssueManifestProviderVO>();
		try {
			IssueManifestProviderVO = reportService.getAllIssueManifestProviderForPendingIssueRequest(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "IssueManifestProvider information get successfully");
			responseObjectsMap.put("IssueManifestProviderVO", IssueManifestProviderVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"IssueManifestProvider information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllIssueManifestProviderById")
	public ResponseEntity<ResponseDTO> getAllIssueManifestProviderById(Long id) {
		String methodName = "getAllIssueManifestProvider()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		IssueManifestProviderVO IssueManifestProviderVO = null;
		try {
			IssueManifestProviderVO = reportService.getAllIssueManifestProviderById(id).orElse(null);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "IssueManifestProvider information get successfully");
			responseObjectsMap.put("IssueManifestProviderVO", IssueManifestProviderVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"IssueManifestProvider information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// RETRIEVALMANIFEST

	@PutMapping("/createUpdateRetrievalManifest")
	public ResponseEntity<ResponseDTO> createUpdateRetrievalManifest(
			@RequestBody RetrievalManifestProviderDTO retrievalManifestProviderDTO) {
		String methodName = "createUpdateRetrievalManifest()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> retrievalManifestProviderVO = reportService
					.createUpdateRetrievalManifest(retrievalManifestProviderDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, retrievalManifestProviderVO.get("message"));
			responseObjectsMap.put("issueManifestProviderVO",
					retrievalManifestProviderVO.get("retrievalManifestProviderVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllRetrievalManifestProvider")
	public ResponseEntity<ResponseDTO> getAllRetrievalManifestProvider() {
		String methodName = "getAllRetrievalManifestProvider()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<RetrievalManifestProviderVO> retrievalManifestProviderVOs = new ArrayList<RetrievalManifestProviderVO>();
		try {
			retrievalManifestProviderVOs = reportService.getAllRetrievalManifestProvider();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "RetrievalManifest information get successfully");
			responseObjectsMap.put("retrievalManifestProviderVOs", retrievalManifestProviderVOs);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "RetrievalManifest information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getRetrievalManifestProviderById")
	public ResponseEntity<ResponseDTO> getRetrievalManifestProviderById(Long id) {
		String methodName = "getRetrievalManifestProviderById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		RetrievalManifestProviderVO retrievalManifestProviderVO = null;
		try {
			retrievalManifestProviderVO = reportService.getRetrievalManifestProviderById(id).orElse(null);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"RetrievalManifest information get successfully By Id");
			responseObjectsMap.put("retrievalManifestProviderVO", retrievalManifestProviderVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "RetrievalManifest information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// DECLARATION AND NOTES

	@PostMapping("/createDeclarationAndNotes")
	public ResponseEntity<ResponseDTO> createDeclarationAndNotes(
			@RequestBody DeclarationAndNotesVO declarationAndNotesVO) {
		String methodName = "createDeclarationAndNotes()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		DeclarationAndNotesVO declarationAndNotesVO1 = new DeclarationAndNotesVO();
		try {
			declarationAndNotesVO1 = reportService.createDeclarationAndNotes(declarationAndNotesVO);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "DeclarationAndNotes Created Successfully");
			responseObjectsMap.put("declarationAndNotesVO1", declarationAndNotesVO1);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Oem Bin Inward Failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllDeclarationAndNotes")
	public ResponseEntity<ResponseDTO> getAllDeclarationAndNotes() {
		String methodName = "getAllDeclarationAndNotesById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<DeclarationAndNotesVO> declarationAndNotesVO = new ArrayList<DeclarationAndNotesVO>();
		try {
			declarationAndNotesVO = reportService.getAllDeclarationAndNotes();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"DeclarationAndNotes information get successfully By Id");
			responseObjectsMap.put("declarationAndNotesVO", declarationAndNotesVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"DeclarationAndNotes information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// Receipt Register
	@GetMapping("/getReceiptRegisterReport")
	public ResponseEntity<ResponseDTO> getReceiptRegisterReport(@RequestParam Long orgId,
			@RequestParam String partyName, @RequestParam String branchCode, @RequestParam String finYear,
			@RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate) {
		String methodName = "getReceiptRegisterReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> reciptReport = new ArrayList<>();
		try {
			reciptReport = reportService.getReceiptRegisterReport(orgId, partyName, branchCode, finYear, fromDate,
					toDate);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Receipt information get successfully");
			responseObjectsMap.put("reciptReport", reciptReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Receipt information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// PAYMENTREPORT

	@GetMapping("/getPaymentRegisterReport")
	public ResponseEntity<ResponseDTO> getPaymentRegisterReport(@RequestParam Long orgId,
			@RequestParam String partyCode, @RequestParam String branchCode, @RequestParam String finYear,
			@RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate) {
		String methodName = "getPaymentRegisterReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> paymentReport = new ArrayList<>();
		try {
			paymentReport = reportService.getPaymentRegisterReport(orgId, partyCode, branchCode, finYear, fromDate,
					toDate);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Payment information get successfully");
			responseObjectsMap.put("paymentReport", paymentReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Payment information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// QUATATION

	@PutMapping("/createUpdateQuotatio")
	public ResponseEntity<ResponseDTO> createUpdateQuotatio(@RequestBody QuotationDTO quotationDTO) {
		String methodName = "createUpdateQuotatio()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> quotationVO = reportService.createUpdateQuotatio(quotationDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, quotationVO.get("message"));
			responseObjectsMap.put("quotationVO", quotationVO.get("quotationVO")); // Corrected key
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getQuotationByorgId")
	public ResponseEntity<ResponseDTO> getQuotationByorgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getQuotationByorgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<QuotationVO> quotationVO = new ArrayList<>();
		try {
			quotationVO = reportService.getQuotationByorgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "quotation found by ID");
			responseObjectsMap.put("quotationVO", quotationVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "quotation not found for ID: " + orgId;
			responseDTO = createServiceResponseError(responseObjectsMap, "quotation not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getQutationById")
	public ResponseEntity<ResponseDTO> getQutationById(@RequestParam Long id) {
		String methodName = "getQutationById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<QuotationVO> quotationVO = null;
		try {
			quotationVO = reportService.getQutationById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Qutation information get successfully");
			responseObjectsMap.put("quotationVO", quotationVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Qutation information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getFillGridForTaxInvoice")
	public ResponseEntity<ResponseDTO> getFillGridForTaxInvoice(@RequestParam(required = true) Long orgId) {
		String methodName = "getFillGridForTaxInvoice()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> quotationVO = new ArrayList<>();
		try {
			quotationVO = reportService.getFillGridForTaxInvoice(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "quotation found by ID");
			responseObjectsMap.put("issueDetails", quotationVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "issueDetails fillgrid not found for ID: " + orgId;
			responseDTO = createServiceResponseError(responseObjectsMap, "issueDetails not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getMimFillGridgettransaction")
	public ResponseEntity<ResponseDTO> getMimFillGridgettransaction(@RequestParam Long orgId, String Receiver,@RequestParam(required = false) String docId) {
		String methodName = "getMimFillGridgettransaction()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> department = new ArrayList<>();
		try {
			department = reportService.getMimFillGridgettransaction(orgId, Receiver,docId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, " TransactionNo get successfully");
			responseObjectsMap.put("TransactionNo", department);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "TransactionNo receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getMimFillGridgetKitDetails")
	public ResponseEntity<ResponseDTO> getMimFillGridgetKitDetails(@RequestParam Long orgId, String TransactionNo) {
		String methodName = "getMimFillGridgetKitDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> department = new ArrayList<>();
		try {
			department = reportService.getMimFillGridgetKitDetails(orgId, TransactionNo);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, " MIM Fillgrid get successfully");
			responseObjectsMap.put("MIM Fillgrid", department);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "MIM Fillgrid  receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getOrginBillNoBased")
	public ResponseEntity<ResponseDTO> getOrginBillNoBased(@RequestParam Long orgId, @RequestParam String orginBillNo) {
		String methodName = "getOrginBillNoBased()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> department = new ArrayList<>();
		try {
			department = reportService.getOrginBillNoBased(orgId, orginBillNo);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, " IrnCreditNote successfully");
			responseObjectsMap.put("MIM Fillgrid", department);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "IrnCreditNot  receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getMimReportDetails")
	public ResponseEntity<ResponseDTO> getMimReportDetails(@RequestParam String type, @RequestParam Long orgId,
			@RequestParam String customerName, @RequestParam String finYear,
			@RequestParam(required = false) String toDate, @RequestParam(required = false) String fromDate) {
		String methodName = "getOrginBillNoBased()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> department = new ArrayList<>();
		try {
			department = reportService.getMimReportDetails(type, orgId, customerName, finYear, toDate, fromDate);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Report get successfully");
			responseObjectsMap.put("MIMFillgrid", department);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Report  receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	@GetMapping("/findMIMReports")
	public ResponseEntity<ResponseDTO> findMIMReports(@RequestParam String type, @RequestParam Long orgId,
			@RequestParam(required = false) String customerName, @RequestParam String finYear,
			@RequestParam(required = false) String toDate, @RequestParam(required = false) String fromDate) {
		String methodName = "findMIMReports()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<IssueManifestProviderVO> mimReportFillGrid = new ArrayList<>();
		try {
			mimReportFillGrid  = reportService.findMIMReports(type, orgId, customerName, finYear, toDate, fromDate);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "MimReports information get successfully ByOrgId");
			responseObjectsMap.put("mimReportFillGrid", mimReportFillGrid );
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "MimReports information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	@GetMapping("/findRIMReports")
	public ResponseEntity<ResponseDTO> findRIMReports(@RequestParam String type,@RequestParam Long orgId,
			@RequestParam(required = false) String customerName, @RequestParam String finYear,
			@RequestParam(required = false) String toDate, @RequestParam(required = false) String fromDate) {
		String methodName = "findRIMReports()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<RetrievalManifestProviderVO> rimReportFillGrid = new ArrayList<>();
		try {
			rimReportFillGrid  = reportService.findRIMReports(type,orgId, customerName, finYear, toDate, fromDate);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "RimReports information get successfully ByOrgId");
			responseObjectsMap.put("rimReportFillGrid", rimReportFillGrid );
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "RimReports information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	
	@GetMapping("/findMimSummaryReport")
	public ResponseEntity<ResponseDTO> findMimSummaryReport(@RequestParam String type, @RequestParam Long orgId,
			@RequestParam String customerName, @RequestParam String finYear, @RequestParam(required = false) String fromDate,@RequestParam(required = false) String toDate) {
		String methodName = "findMimSummaryReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> department = new ArrayList<>();
		try {
			department = reportService.findMimSummaryReport(type, orgId, customerName, finYear, fromDate, toDate);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Report get successfully");
			responseObjectsMap.put("mimReportFillGrid", department);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Report  receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	
	@GetMapping("/findRimSummaryReport")
	public ResponseEntity<ResponseDTO> findRimSummaryReport(@RequestParam String type, @RequestParam Long orgId,
			@RequestParam String customerName, @RequestParam String finYear, @RequestParam(required = false) String fromDate,@RequestParam(required = false) String toDate) {
		String methodName = "findMimSummaryReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> department = new ArrayList<>();
		try {
			department = reportService.findRimSummaryReport(type, orgId, customerName, finYear, fromDate, toDate);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Report get successfully");
			responseObjectsMap.put("rimReportFillGrid", department);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Report  receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
@GetMapping("/getARAgeingReport")
	public ResponseEntity<ResponseDTO> getARAgeingReport(@RequestParam Long orgId,
			@RequestParam String branch, @RequestParam String partyName, @RequestParam(required = false) String asOnDate,@RequestParam(required = false) String base) {
		String methodName = "getARAgeingReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> department = new ArrayList<>();
		try {
			department = reportService.getARAgeingReport(orgId, branch, partyName, asOnDate,base);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ArAgeing Report get successfully");
			responseObjectsMap.put("rimReportFillGrid", department);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "ArAgeing Report  receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
  
  
  @GetMapping("/getApAgeing")
	public ResponseEntity<ResponseDTO> getApAgeing(@RequestParam Long orgId,
			@RequestParam String branch, @RequestParam String partyname,
			@RequestParam String asdate,  @RequestParam String baseType ) {
		String methodName = "getApAgeing()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = reportService.getApAgeing(orgId, branch, partyname, asdate,baseType);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ApAgeing Details retrieved successfully");
			responseObjectsMap.put("mapp", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve tax ApAgeing Details",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
//	@GetMapping("/findRIMMIMReports")
//	public ResponseEntity<ResponseDTO> findRIMMIMReports(@RequestParam String type,@RequestParam Long orgId,
//			@RequestParam(required = false) String customerName, @RequestParam String finYear,
//			@RequestParam(required = false) String toDate, @RequestParam(required = false) String fromDate) {
//		String methodName = "findRIMMIMReports()";
//		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
//		String errorMsg = null;
//		Map<String, Object> responseObjectsMap = new HashMap<>();
//		ResponseDTO responseDTO = null;
//		List<RetrievalManifestProviderVO> rimReportFillGrid = new ArrayList<>();
//		try {
//			rimReportFillGrid  = reportService.findRIMMIMReports(type,orgId, customerName, finYear, toDate, fromDate);
//		} catch (Exception e) {
//			errorMsg = e.getMessage();
//			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
//		}
//		if (StringUtils.isBlank(errorMsg)) {
//			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "RimReports information get successfully ByOrgId");
//			responseObjectsMap.put("rimReportFillGrid", rimReportFillGrid );
//			responseDTO = createServiceResponse(responseObjectsMap);
//		} else {
//			responseDTO = createServiceResponseError(responseObjectsMap, "RimReports information receive failedByOrgId",
//					errorMsg);
//		}
//		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
//		return ResponseEntity.ok().body(responseDTO);
//
//	}

}