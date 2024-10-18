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
import com.base.basesetup.dto.ArApBillBalanceReceivableDTO;
import com.base.basesetup.dto.ReceiptDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.ArApBillBalanceReceivableVO;
import com.base.basesetup.entity.ReceiptVO;
import com.base.basesetup.service.ARService;

@CrossOrigin
@RestController
@RequestMapping("/api/arreceivable")
public class ARController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(ARController.class);

	@Autowired
	ARService arReceivableService;

	// Receipt

	@GetMapping("/getAllReceiptByOrgId")
	public ResponseEntity<ResponseDTO> getAllReceiptReceivableByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllReceiptReceivableByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ReceiptVO> receiptReceivableVO = new ArrayList<>();
		try {
			receiptReceivableVO = arReceivableService.getAllReceiptReceivableByOrgId(orgId);
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
	public ResponseEntity<ResponseDTO> getAllReceiptReceivableById(@RequestParam(required = false) Long id) {
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
			ReceiptVO receiptReceivableVO = arReceivableService
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
	public ResponseEntity<ResponseDTO> getCustomerNameAndCodeForReceipt(@RequestParam Long orgId,
			@RequestParam String branch, @RequestParam String branchCode, @RequestParam String finYear) {
		String methodName = "getCustomerNameAndCodeForReceipt()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> customer = new ArrayList<>();
		try {
			customer = arReceivableService.getCustomerNameAndCodeForReceipt(orgId, branch, branchCode, finYear);
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

	// ArApBillBalance
	@GetMapping("/getAllArApBillBalanceReceivableByOrgId")
	public ResponseEntity<ResponseDTO> getAllArApBillBalanceReceivableByOrgId(
			@RequestParam(required = false) Long orgId) {
		String methodName = "getAllArApBillBalanceReceivableByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ArApBillBalanceReceivableVO> arApBillBalanceVO = new ArrayList<>();
		try {
			arApBillBalanceVO = arReceivableService.getAllArApBillBalanceReceivableByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ArApBillBalance information get successfully By OrgId");
			responseObjectsMap.put("arApBillBalanceReceivableVO", arApBillBalanceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ArApBillBalance information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllArApBillBalanceReceivableById")
	public ResponseEntity<ResponseDTO> getAllArApBillBalanceReceivableById(@RequestParam(required = false) Long id) {
		String methodName = "getAllArApBillBalanceReceivableById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ArApBillBalanceReceivableVO> arApBillBalanceReceivableVO = new ArrayList<>();
		try {
			arApBillBalanceReceivableVO = arReceivableService.getAllArApBillBalanceReceivableById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ArApBillBalanceReceivable information get successfully By id");
			responseObjectsMap.put("arApBillBalanceReceivableVO", arApBillBalanceReceivableVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ArApBillBalanceReceivable information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateArApBillBalanceReceivable")
	public ResponseEntity<ResponseDTO> updateCreateArApBillBalanceReceivable(
			@Valid @RequestBody ArApBillBalanceReceivableDTO arApBillBalanceReceivableDTO) {
		String methodName = "updateCreateArApBillBalanceReceivable()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			ArApBillBalanceReceivableVO arApBillBalanceReceivableVO = arReceivableService
					.updateCreateArApBillBalanceReceivable(arApBillBalanceReceivableDTO);
			boolean isUpdate = arApBillBalanceReceivableDTO.getId() != null;

			if (arApBillBalanceReceivableVO != null) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
						isUpdate ? "ArApBillBalance updated successfully" : "ArApBillBalance created successfully");
				responseObjectsMap.put("arApBillBalanceReceivableVO", arApBillBalanceReceivableVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = isUpdate ? "ArApBillBalance not found for ID: " + arApBillBalanceReceivableDTO.getId()
						: "ArApBillBalance creation failed";
				responseDTO = createServiceResponseError(responseObjectsMap,
						isUpdate ? "ArApBillBalance update failed" : "ArApBillBalance creation failed", errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			boolean isUpdate = arApBillBalanceReceivableDTO.getId() != null;
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap,
					isUpdate ? "ArApBillBalance update failed" : "ArApBillBalance creation failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getArApBillBalanceReceivableByActive")
	public ResponseEntity<ResponseDTO> getArApBillBalanceReceivableByActive() {
		String methodName = "getArApBillBalanceReceivableByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ArApBillBalanceReceivableVO> arApBillBalanceReceivableVO = new ArrayList<>();
		try {
			arApBillBalanceReceivableVO = arReceivableService.getArApBillBalanceReceivableByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ArApBillBalance information get successfully By Active");
			responseObjectsMap.put("arApBillBalanceReceivableVO", arApBillBalanceReceivableVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ArApBillBalanceReceivable receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	//Receipt Register
	@GetMapping("/getAllReceiptRegister")
	public ResponseEntity<ResponseDTO> getAllReceiptRegister(@RequestParam Long orgId, @RequestParam String branch,
			@RequestParam String branchCode, @RequestParam String finYear, @RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String subLedgerName) {
		String methodName = "getAllReceiptRegister()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> customer = new ArrayList<>();
		try {
			customer = arReceivableService.getAllReceiptRegister(orgId, branch, branchCode, finYear, fromDate, toDate,
					subLedgerName);
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

}
