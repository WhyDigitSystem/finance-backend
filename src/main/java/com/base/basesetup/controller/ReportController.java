package com.base.basesetup.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.dto.RetrievalManifestProviderDTO;
import com.base.basesetup.entity.DeclarationAndNotesVO;
import com.base.basesetup.entity.InvoiceVO;
import com.base.basesetup.entity.IssueManifestProviderVO;
import com.base.basesetup.entity.RetrievalManifestProviderVO;
import com.base.basesetup.service.ReportService;

@CrossOrigin
@RestController
@RequestMapping("/api/reportController")
public class ReportController extends BaseController{
	
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
	public ResponseEntity<ResponseDTO> createUpdateIssuemanifest(@RequestBody IssueManifestProviderDTO issueManifestProviderDTO) {
	    String methodName = "createUpdateIssuemanifest()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = null;
	    try {
	        Map<String, Object> issueManifestProviderVO = reportService.createUpdateIssuemanifest(issueManifestProviderDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, issueManifestProviderVO.get("message"));
	        responseObjectsMap.put("issueManifestProviderVO", issueManifestProviderVO.get("issueManifestProviderVO"));
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	    	String errorMsg =  e.getMessage(); 
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}


	@GetMapping("/getAllIssueManifestProvider")
	public ResponseEntity<ResponseDTO> getAllIssueManifestProvider() {
		String methodName = "getAllIssueManifestProvider()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<IssueManifestProviderVO> IssueManifestProviderVO =new ArrayList<IssueManifestProviderVO>();
		try {
			IssueManifestProviderVO = reportService.getAllIssueManifestProvider();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "IssueManifestProvider information get successfully");
			responseObjectsMap.put("IssueManifestProviderVO", IssueManifestProviderVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "IssueManifestProvider information receive failed",
					errorMsg);
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
		List<IssueManifestProviderVO> IssueManifestProviderVO =new ArrayList<IssueManifestProviderVO>();
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
			responseDTO = createServiceResponseError(responseObjectsMap, "IssueManifestProvider information receive failed",
					errorMsg);
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
		IssueManifestProviderVO IssueManifestProviderVO =null;
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
			responseDTO = createServiceResponseError(responseObjectsMap, "IssueManifestProvider information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	
	//RETRIEVALMANIFEST

		@PutMapping("/createUpdateRetrievalManifest")
		public ResponseEntity<ResponseDTO> createUpdateRetrievalManifest(@RequestBody RetrievalManifestProviderDTO retrievalManifestProviderDTO) {
		    String methodName = "createUpdateRetrievalManifest()";
		    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		    Map<String, Object> responseObjectsMap = new HashMap<>();
		    ResponseDTO responseDTO = null;
		    try {
		        Map<String, Object> retrievalManifestProviderVO = reportService.createUpdateRetrievalManifest(retrievalManifestProviderDTO);
		        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, retrievalManifestProviderVO.get("message"));
		        responseObjectsMap.put("issueManifestProviderVO", retrievalManifestProviderVO.get("retrievalManifestProviderVO"));
		        responseDTO = createServiceResponse(responseObjectsMap);
		    } catch (Exception e) {
		    	String errorMsg =  e.getMessage();
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
			List<RetrievalManifestProviderVO> retrievalManifestProviderVOs =new ArrayList<RetrievalManifestProviderVO>();
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
			RetrievalManifestProviderVO retrievalManifestProviderVO =null;
			try {
				retrievalManifestProviderVO = reportService.getRetrievalManifestProviderById(id).orElse(null);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "RetrievalManifest information get successfully By Id");
				responseObjectsMap.put("retrievalManifestProviderVO", retrievalManifestProviderVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "RetrievalManifest information receive failed",
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);

		}
		
		//DECLARATION AND NOTES
		
		@PostMapping("/createDeclarationAndNotes")
		public ResponseEntity<ResponseDTO> createDeclarationAndNotes(@RequestBody DeclarationAndNotesVO declarationAndNotesVO) {
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
			List<DeclarationAndNotesVO> declarationAndNotesVO =new ArrayList<DeclarationAndNotesVO>();
			try {
				declarationAndNotesVO = reportService.getAllDeclarationAndNotes();
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "DeclarationAndNotes information get successfully By Id");
				responseObjectsMap.put("declarationAndNotesVO", declarationAndNotesVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "DeclarationAndNotes information receive failed",
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);

		}

	
}