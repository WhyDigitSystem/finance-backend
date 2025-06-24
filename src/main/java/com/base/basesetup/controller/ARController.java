package com.base.basesetup.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.basesetup.common.CommonConstant;
import com.base.basesetup.common.UserConstants;
import com.base.basesetup.dto.ArBillBalanceDTO;
import com.base.basesetup.dto.ReceiptDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.ArBillBalanceVO;
import com.base.basesetup.entity.PaymentVO;
import com.base.basesetup.entity.ReceiptVO;
import com.base.basesetup.entity.TaxInvoiceVO;
import com.base.basesetup.service.ARService;

@CrossOrigin
@RestController
@RequestMapping("/api/arreceivable")
public class ARController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(ARController.class);

	@Autowired
	ARService arReceivableService;

	// Receipt

	@GetMapping("/getAllReceiptReceivableByOrgId")
	public ResponseEntity<ResponseDTO> getAllReceiptReceivableByOrgId(@RequestParam Long orgId,
			@RequestParam String finYear, @RequestParam String branchCode) {
		String methodName = "getAllReceiptReceivableByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ReceiptVO> receiptReceivableVO = new ArrayList<>();
		try {
			receiptReceivableVO = arReceivableService.getAllReceiptReceivableByOrgId(orgId, finYear, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ReceiptReceivable information get successfully By OrgId");
			responseObjectsMap.put("receiptReceivableVO", receiptReceivableVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ReceiptReceivable information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllReceiptById")
	public ResponseEntity<ResponseDTO> getAllReceiptReceivableById(@RequestParam Long id) {
		String methodName = "getAllReceiptReceivableById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ReceiptVO> receiptReceivableVO = new ArrayList<>();
		try {
			receiptReceivableVO = arReceivableService.getAllReceiptReceivableById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ReceiptReceivable information get successfully By id");
			responseObjectsMap.put("receiptReceivableVO", receiptReceivableVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ReceiptReceivable information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateReceipt")
	public ResponseEntity<ResponseDTO> updateCreateReceiptReceivable(
			@Valid @RequestBody ReceiptDTO receiptReceivableDTO) {
		String methodName = "updateCreateReceiptReceivable()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> receiptReceivableVO = arReceivableService
					.updateCreateReceiptReceivable(receiptReceivableDTO);
			boolean isUpdate = receiptReceivableDTO.getId() != null;

			if (receiptReceivableVO != null) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
						isUpdate ? "ReceiptReceivable updated successfully" : "ReceiptReceivable created successfully");
				responseObjectsMap.put("receiptReceivableVO", receiptReceivableVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = isUpdate ? "ReceiptReceivable not found for ID: " + receiptReceivableDTO.getId()
						: "ReceiptReceivable creation failed";
				responseDTO = createServiceResponseError(responseObjectsMap,
						isUpdate ? "ReceiptReceivable update failed" : "ReceiptReceivable creation failed", errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			boolean isUpdate = receiptReceivableDTO.getId() != null;
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap,
					isUpdate ? "ReceiptReceivable update failed" : "ReceiptReceivable creation failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getReceiptByActive")
	public ResponseEntity<ResponseDTO> getReceiptReceivableByActive() {
		String methodName = "getReceiptReceivableByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ReceiptVO> receiptReceivableVO = new ArrayList<>();
		try {
			receiptReceivableVO = arReceivableService.getReceiptReceivableByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ReceiptReceivable information get successfully By Active");
			responseObjectsMap.put("receiptReceivableVO", receiptReceivableVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ReceiptReceivable information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getCustomerNameAndCodeForReceipt")
	public ResponseEntity<ResponseDTO> getCustomerNameAndCodeForReceipt(@RequestParam Long orgId) {
		String methodName = "getCustomerNameAndCodeForReceipt()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> customer = new ArrayList<>();
		try {
			customer = arReceivableService.getCustomerNameAndCodeForReceipt(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"Customer name and code information get successfully");
			responseObjectsMap.put("PartyMasterVO", customer);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Customer name and code information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getReceiptDocId")
	public ResponseEntity<ResponseDTO> x(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getReceiptDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = arReceivableService.getReceiptDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Receipt DocId information retrieved successfully");
			responseObjectsMap.put("receiptDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve Receipt Docid information",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// ArBillBalance
	@GetMapping("/getAllArBillBalanceByOrgId")
	public ResponseEntity<ResponseDTO> getAllArBillBalanceByOrgId(@RequestParam Long orgId) {
		String methodName = "getAllArBillBalanceByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ArBillBalanceVO> arBillBalanceVO = new ArrayList<>();
		try {
			arBillBalanceVO = arReceivableService.getAllArBillBalanceByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ArBillBalance information get successfully By OrgId");
			responseObjectsMap.put("arApBillBalanceReceivableVO", arBillBalanceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ArBillBalance information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllArBillBalanceById")
	public ResponseEntity<ResponseDTO> getAllArBillBalanceById(@RequestParam Long id) {
		String methodName = "getAllArBillBalanceById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ArBillBalanceVO> arApBillBalanceReceivableVO = new ArrayList<>();
		try {
			arApBillBalanceReceivableVO = arReceivableService.getAllArBillBalanceById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ArBillBalance information get successfully By id");
			responseObjectsMap.put("arApBillBalanceReceivableVO", arApBillBalanceReceivableVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ArBillBalance information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateArBillBalance")
	public ResponseEntity<ResponseDTO> updateCreateArBillBalance(
			@Valid @RequestBody ArBillBalanceDTO arApBillBalanceReceivableDTO) {
		String methodName = "updateCreateArBillBalance()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			Map<String, Object> arApBillBalanceReceivableVO = arReceivableService
					.updateCreateArBillBalance(arApBillBalanceReceivableDTO);
			boolean isUpdate = arApBillBalanceReceivableDTO.getId() != null;

			if (arApBillBalanceReceivableVO != null) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
						isUpdate ? "ArBillBalance updated successfully" : "ArBillBalance created successfully");
				responseObjectsMap.put("arApBillBalanceReceivableVO", arApBillBalanceReceivableVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = isUpdate ? "ArBillBalance not found for ID: " + arApBillBalanceReceivableDTO.getId()
						: "ArBillBalance creation failed";
				responseDTO = createServiceResponseError(responseObjectsMap,
						isUpdate ? "ArBillBalance update failed" : "ArBillBalance creation failed", errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			boolean isUpdate = arApBillBalanceReceivableDTO.getId() != null;
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap,
					isUpdate ? "ArBillBalance update failed" : "ArBillBalance creation failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getArBillBalanceByActive")
	public ResponseEntity<ResponseDTO> getArBillBalanceByActive() {
		String methodName = "getArBillBalanceByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ArBillBalanceVO> arApBillBalanceReceivableVO = new ArrayList<>();
		try {
			arApBillBalanceReceivableVO = arReceivableService.getArBillBalanceByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ArBillBalance information get successfully By Active");
			responseObjectsMap.put("arApBillBalanceReceivableVO", arApBillBalanceReceivableVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "ArBillBalance receive failed By Active",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getArBillBalanceDocId")
	public ResponseEntity<ResponseDTO> getArBillBalanceDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getArBillBalanceDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = arReceivableService.getArBillBalanceDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ArBillBalance DocId information retrieved successfully");
			responseObjectsMap.put("arBillBalanceDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve ArBillBalance Docid information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getPartyNameAndCodeForArBillBalance")
	public ResponseEntity<ResponseDTO> getPartyNameAndCodeForArBillBalance(@RequestParam Long orgId) {
		String methodName = "getPartyNameAndCodeForArBillBalance()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> customer = new ArrayList<>();
		try {
			customer = arReceivableService.getPartyNameAndCodeForArBillBalance(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Party name and code information get successfully");
			responseObjectsMap.put("PartyMasterVO", customer);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Party name and code information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// Receipt Register
	@GetMapping("/getAllReceiptRegister")
	public ResponseEntity<ResponseDTO> getAllReceiptRegister(@RequestParam Long orgId, String fromDate, String toDate,
			@RequestParam String subLedgerName) {
		String methodName = "getAllReceiptRegister()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> customer = new ArrayList<>();
		try {
			customer = arReceivableService.getAllReceiptRegister(orgId, fromDate, toDate, subLedgerName);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Receipt Register information get successfully");
			responseObjectsMap.put("PartyMasterVO", customer);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Receipt Register information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// Receipt Register
	@GetMapping("/getReciptFillGrid")
	public ResponseEntity<ResponseDTO> getReciptFillGrid(@RequestParam Long orgId, @RequestParam String partyCode,
			@RequestParam String branchCode) {
		String methodName = "getReciptFillGrid()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> reciptFillGrid = new ArrayList<>();
		try {
			reciptFillGrid = arReceivableService.getReciptFillGrid(orgId, partyCode, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Receipt Fill Grid information get successfully");
			responseObjectsMap.put("reciptFillGrid", reciptFillGrid);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Receipt Fill Grid information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/approveReceipt")
	public ResponseEntity<ResponseDTO> approveReceipt(@RequestParam Long orgId, @RequestParam Long id,
			@RequestParam String docId, @RequestParam String action, @RequestParam String actionBy) {
		String methodName = "approveReceipt()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			ReceiptVO taxInvoiceVO = arReceivableService.approveReceipt(orgId, id, docId, action, actionBy);
			responseObjectsMap.put("taxInvoiceVO", taxInvoiceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

//report

	@GetMapping("/getReceiptDetails")
	public ResponseEntity<ResponseDTO> getReceiptDetails(@RequestParam Long orgId,
			@RequestParam(required = true) String partyname, String fromDate, String toDate,
			@RequestParam String branchCode) {
		String methodName = "getReceiptDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = arReceivableService.getReceiptDetails(orgId, partyname, fromDate, toDate, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Receipt Details retrieved successfully");
			responseObjectsMap.put("mapp", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve tax Receipt Details",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getReceiptSummary")
	public ResponseEntity<ResponseDTO> getReceiptSummary(@RequestParam Long orgId,
			@RequestParam(required = true) String partyname, String fromDate, String toDate,
			@RequestParam String branchCode) {
		String methodName = "getReceiptSummary()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = arReceivableService.getReceiptSummary(orgId, partyname, fromDate, toDate, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "tax Receipt summary retrieved successfully");
			responseObjectsMap.put("mapp", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve tax Receipt summary",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getReceiptByDocIdAndScreenCode")
	public ResponseEntity<ResponseDTO> getReceiptByDocIdAndScreenCode(@RequestParam String docId) {
		String methodName = "getReceiptByDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		ReceiptVO receiptVO = new ReceiptVO();
		try {
			receiptVO = arReceivableService.getReceiptByDocIdAndScreenCode(docId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "receiptVO information get successfully By docid");
			responseObjectsMap.put("receiptVO", receiptVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"receiptVO information receive failed By docid", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
}
