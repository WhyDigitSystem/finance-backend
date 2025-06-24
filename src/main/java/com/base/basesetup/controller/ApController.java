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
import com.base.basesetup.dto.ApBillBalanceDTO;
import com.base.basesetup.dto.PaymentDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.ApBillBalanceVO;
import com.base.basesetup.entity.PaymentVO;
import com.base.basesetup.service.APService;

@CrossOrigin
@RestController
@RequestMapping("/api/payable")
public class ApController extends BaseController {

	@Autowired
	APService apService;

	public static final Logger LOGGER = LoggerFactory.getLogger(ApController.class);

	@GetMapping("/getAllPaymentByOrgId")
	public ResponseEntity<ResponseDTO> getAllPaymentByOrgId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branchCode) {
		String methodName = "getAllPaymentByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<PaymentVO> paymentVO = new ArrayList<>();
		try {
			paymentVO = apService.getAllPaymentByOrgId(orgId, finYear, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Payment information get successfully By OrgId");
			responseObjectsMap.put("paymentVO", paymentVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Payment information receive failed By OrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getPaymentById")
	public ResponseEntity<ResponseDTO> getPaymentById(@RequestParam Long id) {
		String methodName = "getPaymentById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<PaymentVO> paymentVO = new ArrayList<>();
		try {
			paymentVO = apService.getPaymentById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Payment information get successfully By id");
			responseObjectsMap.put("paymentVO", paymentVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Payment information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreatePayment")
	public ResponseEntity<ResponseDTO> updateCreatePayment(@RequestBody PaymentDTO paymentDTO) {
		String methodName = "updateCreatePayment()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			Map<String, Object> paymentVO = apService.updateCreatePayment(paymentDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, paymentVO.get("message"));
			responseObjectsMap.put("paymentVO", paymentVO.get("paymentVO")); // Corrected key
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// ApBillBalance
	@GetMapping("/getAllApBillBalanceByOrgId")
	public ResponseEntity<ResponseDTO> getAllApBillBalanceByOrgId(@RequestParam Long orgId) {
		String methodName = "getAllApBillBalanceByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ApBillBalanceVO> arBillBalanceVO = new ArrayList<>();
		try {
			arBillBalanceVO = apService.getAllApBillBalanceByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ApBillBalance information get successfully By OrgId");
			responseObjectsMap.put("apBillBalanceVO", arBillBalanceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ApBillBalance information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllApBillBalanceById")
	public ResponseEntity<ResponseDTO> getAllApBillBalanceById(@RequestParam Long id) {
		String methodName = "getAllApBillBalanceById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ApBillBalanceVO> apBillBalanceVO = new ArrayList<>();
		try {
			apBillBalanceVO = apService.getAllApBillBalanceById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ApBillBalance information get successfully By id");
			responseObjectsMap.put("apBillBalanceVO", apBillBalanceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ApBillBalance information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateApBillBalance")
	public ResponseEntity<ResponseDTO> updateCreateApBillBalance(
			@Valid @RequestBody ApBillBalanceDTO apBillBalanceDTO) {
		String methodName = "updateCreateApBillBalance()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			Map<String, Object> apBillBalanceVO = apService.updateCreateApBillBalance(apBillBalanceDTO);
			boolean isUpdate = apBillBalanceDTO.getId() != null;

			if (apBillBalanceVO != null) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
						isUpdate ? "ApBillBalance updated successfully" : "ApBillBalance created successfully");
				responseObjectsMap.put("apBillBalanceVO", apBillBalanceVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = isUpdate ? "ApBillBalance not found for ID: " + apBillBalanceDTO.getId()
						: "ApBillBalance creation failed";
				responseDTO = createServiceResponseError(responseObjectsMap,
						isUpdate ? "ApBillBalance update failed" : "ApBillBalance creation failed", errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			boolean isUpdate = apBillBalanceDTO.getId() != null;
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap,
					isUpdate ? "ApBillBalance update failed" : "ApBillBalance creation failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getApBillBalanceByActive")
	public ResponseEntity<ResponseDTO> getApBillBalanceByActive() {
		String methodName = "getApBillBalanceByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ApBillBalanceVO> apBillBalanceVO = new ArrayList<>();
		try {
			apBillBalanceVO = apService.getApBillBalanceByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ApBillBalance information get successfully By Active");
			responseObjectsMap.put("apBillBalanceVO", apBillBalanceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "ApBillBalance receive failed By Active",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getPartyNameAndCodeForApBillBalance")
	public ResponseEntity<ResponseDTO> getPartyNameAndCodeForApBillBalance(@RequestParam Long orgId) {
		String methodName = "getPartyNameAndCodeForApBillBalance()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> customer = new ArrayList<>();
		try {
			customer = apService.getPartyNameAndCodeForApBillBalance(orgId);
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

	// PaymentRegister
	@GetMapping("/getAllPaymentRegister")
	public ResponseEntity<ResponseDTO> getAllPaymentRegister(@RequestParam Long orgId, @RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String subLedgerName) {
		String methodName = "getAllPaymentRegister()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> customer = new ArrayList<>();
		try {
			customer = apService.getAllPaymentRegister(orgId, fromDate, toDate, subLedgerName);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Payment Register information get successfully");
			responseObjectsMap.put("PartyMasterVO", customer);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Payment Register information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getPartyNameAndCodeForPayment")
	public ResponseEntity<ResponseDTO> getPartyNameAndCodeForPayment(@RequestParam Long orgId,
			@RequestParam String partyName) {
		String methodName = "getPartyNameAndCodeForPayment()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> party = new ArrayList<>();
		try {
			party = apService.getPartyNameAndCodeForPayment(orgId, partyName);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Party name and code information get successfully");
			responseObjectsMap.put("PartyMasterVO", party);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Party name and code information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getCurrencyAndTransCurrencyForPayment")
	public ResponseEntity<ResponseDTO> getCurrencyAndTransCurrencyForPayment(@RequestParam Long orgId,
			@RequestParam String branch, @RequestParam String branchCode, @RequestParam String finYear,
			@RequestParam String partyName) {
		String methodName = "getCurrencyAndTransCurrencyForPayment()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> payment = new ArrayList<>();
		try {
			payment = apService.getCurrencyAndTransCurrencyForPayment(orgId, branch, branchCode, finYear, partyName);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Currency information get successfully");
			responseObjectsMap.put("PaymentVO", payment);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Currency information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getStateCodeByOrgIdForPayment")
	public ResponseEntity<ResponseDTO> getStateCodeByOrgIdForPayment(@RequestParam Long orgId) {
		String methodName = "getStateCodeByOrgIdForPayment()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> payment = new ArrayList<>();
		try {
			payment = apService.getStateCodeByOrgIdForPayment(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"StateCode from statemaster information get successfully");
			responseObjectsMap.put("PaymentVO", payment);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"StateCode from statemaster information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAccountGroupNameByOrgIdForPayment")
	public ResponseEntity<ResponseDTO> getAccountGroupNameByOrgIdForPayment(@RequestParam Long orgId) {
		String methodName = "getAccountGroupNameByOrgIdForPayment()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> payment = new ArrayList<>();
		try {
			payment = apService.getAccountGroupNameByOrgIdForPayment(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"AccountGroupName from Group information get successfully");
			responseObjectsMap.put("PaymentVO", payment);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"AccountGroupName from Group information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getPaymentDocId")
	public ResponseEntity<ResponseDTO> getPaymentDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getPaymentDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = apService.getPaymentDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Payment DocId information retrieved successfully");
			responseObjectsMap.put("paymentDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve Payment Docid information",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

//	@GetMapping("/getApBillBalanceDocId")
//	public ResponseEntity<ResponseDTO> getApBillBalanceDocId(@RequestParam Long orgId, @RequestParam String finYear,
//			@RequestParam String branch, @RequestParam String branchCode) {
//
//		String methodName = "getApBillBalanceDocId()";
//		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
//		String errorMsg = null;
//		Map<String, Object> responseObjectsMap = new HashMap<>();
//		ResponseDTO responseDTO = null;
//		String mapp = "";
//
//		try {
//			mapp = apService.getApBillBalanceDocId(orgId, finYear, branch, branchCode);
//		} catch (Exception e) {
//			errorMsg = e.getMessage();
//			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
//		}
//
//		if (StringUtils.isBlank(errorMsg)) {
//			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ApBillBalance DocId information retrieved successfully");
//			responseObjectsMap.put("apBillBalanceDocId", mapp);
//			responseDTO = createServiceResponse(responseObjectsMap);
//		} else {
//			responseDTO = createServiceResponseError(responseObjectsMap,
//					"Failed to retrieve ApBillBalance Docid information", errorMsg);
//		}
//
//		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
//		return ResponseEntity.ok().body(responseDTO);
//	}

	@GetMapping("/getPartyNameAndPartyCode")
	public ResponseEntity<ResponseDTO> getPartyNameAndPartyCode(@RequestParam Long orgId) {
		String methodName = "getPartyNameAndPartyCode()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> party = new ArrayList<>();
		try {
			party = apService.getPartyNameAndPartyCode(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Party name and code information get successfully");
			responseObjectsMap.put("PartyMasterVO", party);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Party name and code information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getPaymentFillGrid")
	public ResponseEntity<ResponseDTO> getPaymentFillGrid(Long orgId, String partyCode, String branchCode) {
		String methodName = "getPaymentFillGrid()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> paymentfillgrid = new ArrayList<>();
		try {
			paymentfillgrid = apService.getPaymentFillGrid(orgId, partyCode, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, " Payment fillgrid information get successfully");
			responseObjectsMap.put("paymentfillgrid", paymentfillgrid);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Payment fillgrid  information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

//	@GetMapping("/getarapoffsetadjustmentFillGrid")
//	public ResponseEntity<ResponseDTO> getarapoffsetadjustmentFillGrid(Long orgId, String partyCode,String branchCode, String docDate,String docId) {
//		String methodName = "getarapoffsetadjustmentFillGrid()";
//		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
//		String errorMsg = null;
//		Map<String, Object> responseObjectsMap = new HashMap<>();
//		ResponseDTO responseDTO = null;
//		List<Map<String, Object>> arapoffsetadjustmentFillGrid = new ArrayList<>();
//		try {
//			arapoffsetadjustmentFillGrid = apService.getarapoffsetadjustmentFillGrid(orgId,partyCode, branchCode, docDate,docId);
//		} catch (Exception e) {
//			errorMsg = e.getMessage();
//			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
//		}
//		if (StringUtils.isBlank(errorMsg)) {
//			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
//					" ARAP offset adjustment fillgrid information get successfully");
//			responseObjectsMap.put("arapoffsetadjustmentFillGrid", arapoffsetadjustmentFillGrid);
//			responseDTO = createServiceResponse(responseObjectsMap);
//		} else {
//			responseDTO = createServiceResponseError(responseObjectsMap,
//					"ARAP offset adjustment fillgrid  information receive failed", errorMsg);
//		}
//		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
//		return ResponseEntity.ok().body(responseDTO);
//	}

// ap ageing

	@GetMapping("/getAPAgeing")
	public ResponseEntity<ResponseDTO> getAPAgeing(@RequestParam(required = true) String Asondate,
			@RequestParam(required = true) String partyname, @RequestParam(required = false) String pdate,
			@RequestParam(required = true) Long orgId) {
		String methodName = "getAPAgeing()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> APAgeing = new ArrayList<>();
		try {
			APAgeing = apService.getAPAgeing(Asondate, partyname, pdate, orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Ap ageing information get successfully");
			responseObjectsMap.put("APAgeing", APAgeing);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Ap ageing information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// Ap Outstanding

	@GetMapping("/getAPOutstanding")
	public ResponseEntity<ResponseDTO> getAPOutstanding(@RequestParam(required = true) String Asondate, 
			@RequestParam(required = true) String partyname,@RequestParam(required = true) String branch,  
			@RequestParam(required = true)  Long orgId, 
			@RequestParam(required = false) String pdate
		) {
		String methodName = "getAPOutstanding()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> APOutstanding = new ArrayList<>();
		try {
			APOutstanding = apService.getAPOutstanding(Asondate, partyname,branch,orgId,pdate);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "AP Outstanding information get successfully");
			responseObjectsMap.put("APOutstanding", APOutstanding);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "AP Outstanding information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllPaymentByOrgIdAndBranchCode")
	public ResponseEntity<ResponseDTO> getAllPaymentByOrgIdAndBranchCode(@RequestParam Long orgId,
			@RequestParam String branchCode, String partyName) {
		String methodName = "getAllPaymentByOrgIdAndBranchCode()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> paymentVO = new ArrayList<>();
		try {
			paymentVO = apService.getAllPaymentByOrgIdAndBranchCode(orgId, branchCode, partyName);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Payment information get successfully By OrgId");
			responseObjectsMap.put("paymentVO", paymentVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Payment information receive failed By OrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@PutMapping("/approvePayment")
	public ResponseEntity<ResponseDTO> approvePayment(@RequestParam Long orgId, @RequestParam Long id,
			@RequestParam String docId, @RequestParam String action, @RequestParam String actionBy) {
		String methodName = "approvePayment()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			PaymentVO taxInvoiceVO = apService.approvePayment(orgId, id, docId, action, actionBy);
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

	@GetMapping("/getPaymentDetails")
	public ResponseEntity<ResponseDTO> getPaymentDetails(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String partyname, String fromDate, String toDate, @RequestParam String branchCode) {
		String methodName = "getPaymentDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> paymentVO = new ArrayList<>();
		try {
			paymentVO = apService.getPaymentDetails(orgId, finYear, partyname, fromDate, toDate, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Payment Details  information get successfully ");
			responseObjectsMap.put("paymentVO", paymentVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Payment Details information receive failed ",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getPaymentSummary")
	public ResponseEntity<ResponseDTO> getPaymentSummary(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String partyname, String fromDate, String toDate, @RequestParam String branchCode) {
		String methodName = "getPaymentSummary()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> paymentVO = new ArrayList<>();
		try {
			paymentVO = apService.getPaymentSummary(orgId, finYear, partyname, fromDate, toDate, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Payment Summary information get successfully ");
			responseObjectsMap.put("paymentVO", paymentVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Payment Summary information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getPaymentByDocId")
	public ResponseEntity<ResponseDTO> getPaymentByDocId(@RequestParam Long orgId, @RequestParam String docId) {
		String methodName = "getPaymentByDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		PaymentVO taxInvoiceVO = new PaymentVO();
		try {
			taxInvoiceVO = apService.getPaymentByDocId(orgId, docId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "payment information get successfully By docid");
			responseObjectsMap.put("PaymentVO", taxInvoiceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "payment information receive failed By docid",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

}
