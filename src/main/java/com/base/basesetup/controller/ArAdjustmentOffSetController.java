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
import com.base.basesetup.dto.ApAdjustmentOffSetDTO;
import com.base.basesetup.dto.ArAdjustmentOffSetDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.ApAdjustmentOffSetVO;
import com.base.basesetup.entity.ArAdjustmentOffSetVO;
import com.base.basesetup.entity.PaymentVO;
import com.base.basesetup.service.ArAdjustmentOffSetService;

@CrossOrigin
@RestController
@RequestMapping("/api/aradjustmentoffset")
public class ArAdjustmentOffSetController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(ArAdjustmentOffSetController.class);

	@Autowired
	ArAdjustmentOffSetService arAdjustmentOffSetService;

	@GetMapping("/getAllArAdjustmentOffSetByOrgId")
	public ResponseEntity<ResponseDTO> getAllArAdjustmentOffSetByOrgId(@RequestParam Long orgId) {
		String methodName = "getAllArAdjustmentOffSetByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ArAdjustmentOffSetVO> arAdjustmentOffSetVO = new ArrayList<>();
		try {
			arAdjustmentOffSetVO = arAdjustmentOffSetService.getAllArAdjustmentOffSetByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ArAdjustmentOffSet information get successfully By OrgId");
			responseObjectsMap.put("arAdjustmentOffSetVO", arAdjustmentOffSetVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ArAdjustmentOffSet information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getArAdjustmentOffSetById")
	public ResponseEntity<ResponseDTO> getArAdjustmentOffSetById(@RequestParam(required = false) Long id) {
		String methodName = "getArAdjustmentOffSetById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ArAdjustmentOffSetVO> arAdjustmentOffSetVO = new ArrayList<>();
		try {
			arAdjustmentOffSetVO = arAdjustmentOffSetService.getArAdjustmentOffSetById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ArAdjustmentOffSet information get successfully By id");
			responseObjectsMap.put("arAdjustmentOffSetVO", arAdjustmentOffSetVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ArAdjustmentOffSet information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateArAdjustmentOffSet")
	public ResponseEntity<ResponseDTO> updateCreateArAdjustmentOffSet(
			@Valid @RequestBody ArAdjustmentOffSetDTO arAdjustmentOffSetDTO) {
		String methodName = "updateCreateArAdjustmentOffSet()";

		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			Map<String, Object> arAdjustmentOffSetVO = arAdjustmentOffSetService
					.updateCreateArAdjustmentOffSet(arAdjustmentOffSetDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, arAdjustmentOffSetVO.get("message"));
			responseObjectsMap.put("arAdjustmentOffSetVO", arAdjustmentOffSetVO.get("arAdjustmentOffSetVO")); // Corrected
																												// key
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

//	@GetMapping("/getArAdjustmentOffSetDocId")
//	public ResponseEntity<ResponseDTO> getArAdjustmentOffSetDocId(@RequestParam Long orgId, @RequestParam String finYear,
//			@RequestParam String branch, @RequestParam String branchCode) {
//
//		String methodName = "getArAdjustmentOffSetDocId()";
//		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
//		String errorMsg = null;
//		Map<String, Object> responseObjectsMap = new HashMap<>();
//		ResponseDTO responseDTO = null;
//		String mapp = "";
//
//		try {
//			mapp = arAdjustmentOffSetService.getArAdjustmentOffSetDocId(orgId, finYear, branch, branchCode);
//		} catch (Exception e) {
//			errorMsg = e.getMessage();
//			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
//		}
//
//		if (StringUtils.isBlank(errorMsg)) {
//			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ArAdjustmentOffSet information retrieved successfully");
//			responseObjectsMap.put("arAdjustmentOffSetDocId", mapp);
//			responseDTO = createServiceResponse(responseObjectsMap);
//		} else {
//			responseDTO = createServiceResponseError(responseObjectsMap,
//					"Failed to retrieve ArAdjustmentOffSet Docid information", errorMsg);
//		}
//
//		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
//		return ResponseEntity.ok().body(responseDTO);
//	}
	@GetMapping("/getArAdjustmentOffSetDocId")
	public ResponseEntity<ResponseDTO> getArAdjustmentOffSetDocId(@RequestParam Long orgId,
			@RequestParam String finYear, @RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getArAdjustmentOffSetDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		Map<String, Object> resultMap = new HashMap<>();

		try {
			resultMap = arAdjustmentOffSetService.getArAdjustmentOffSetDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"arAdjustmentOffSetDocId information retrieved successfully");

			responseObjectsMap.put("arAdjustmentOffSetDocId", resultMap.get("docId"));
			responseObjectsMap.put("docDate", resultMap.get("docDate"));

			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve arAdjustmentOffSetDocId information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// DropDown Api ARAdjustmentOffSet

	@GetMapping("/getAllCustomerReceiptByOrgIdAndBranchCode")
	public ResponseEntity<ResponseDTO> getAllCustomerReceiptByOrgIdAndBranchCode(@RequestParam Long orgId,
			@RequestParam String branchCode, String customerName) {
		String methodName = "getAllCustomerReceiptByOrgIdAndBranchCode()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> receiptVO = new ArrayList<>();
		try {
			receiptVO = arAdjustmentOffSetService.getAllCustomerReceiptByOrgIdAndBranchCode(orgId, branchCode,
					customerName);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ArAdjustmentOffSet information get successfully By OrgId");
			responseObjectsMap.put("receiptVO", receiptVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ArAdjustmentOffSet information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// AP ADJUSTMENT OFFSET

	@GetMapping("/getAllApAdjustmentOffSetByOrgId")
	public ResponseEntity<ResponseDTO> getAllApAdjustmentOffSetByOrgId(@RequestParam Long orgId) {
		String methodName = "getAllApAdjustmentOffSetByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ApAdjustmentOffSetVO> apAdjustmentOffSetVO = new ArrayList<>();
		try {
			apAdjustmentOffSetVO = arAdjustmentOffSetService.getAllApAdjustmentOffSetByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ApAdjustmentOffSet information get successfully By OrgId");
			responseObjectsMap.put("apAdjustmentOffSetVO", apAdjustmentOffSetVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ApAdjustmentOffSet information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getApAdjustmentOffSetById")
	public ResponseEntity<ResponseDTO> getApAdjustmentOffSetById(@RequestParam(required = false) Long id) {
		String methodName = "getApAdjustmentOffSetById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ApAdjustmentOffSetVO> apAdjustmentOffSetVO = new ArrayList<>();
		try {
			apAdjustmentOffSetVO = arAdjustmentOffSetService.getApAdjustmentOffSetById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ApAdjustmentOffSet information get successfully By id");
			responseObjectsMap.put("apAdjustmentOffSetVO", apAdjustmentOffSetVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ApAdjustmentOffSet information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getApAdjustmentOffSetDocId")
	public ResponseEntity<ResponseDTO> getApAdjustmentOffSetDocId(@RequestParam Long orgId,
			@RequestParam String finYear, @RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getApAdjustmentOffSetDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = arAdjustmentOffSetService.getApAdjustmentOffSetDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ApAdjustmentOffSet information retrieved successfully");
			responseObjectsMap.put("apAdjustmentOffSetDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve ApAdjustmentOffSet Docid information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// DropDown Api ARAdjustmentOffSet

	@GetMapping("/getAllVendorPaymentByOrgIdAndBranchCode")
	public ResponseEntity<ResponseDTO> getAllVendorPaymentByOrgIdAndBranchCode(@RequestParam Long orgId,
			@RequestParam String branchCode) {
		String methodName = "getAllVendorPaymentByOrgIdAndBranchCode()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<PaymentVO> paymentVO = new ArrayList<>();
		try {
			paymentVO = arAdjustmentOffSetService.getAllVendorPaymentByOrgIdAndBranchCode(orgId, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"VendorPayment information get successfully By OrgId");
			responseObjectsMap.put("paymentVO", paymentVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"VendorPayment information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@PutMapping("/updateCreateApAdjustmentOffSet")
	public ResponseEntity<ResponseDTO> updateCreateApAdjustmentOffSet(
			@Valid @RequestBody ApAdjustmentOffSetDTO apAdjustmentOffSetDTO) {
		String methodName = "updateCreateApAdjustmentOffSet()";

		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			Map<String, Object> apAdjustmentOffSetVO = arAdjustmentOffSetService
					.updateCreateApAdjustmentOffSet(apAdjustmentOffSetDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, apAdjustmentOffSetVO.get("message"));
			responseObjectsMap.put("apAdjustmentOffSetVO", apAdjustmentOffSetVO.get("apAdjustmentOffSetVO")); // Corrected
																												// key
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getArOffsetFillgrid")
	public ResponseEntity<ResponseDTO> getArOffsetFillgrid(@RequestParam Long orgId, @RequestParam String subLedgerCode,
			@RequestParam String docId, @RequestParam String branch, @RequestParam String docDate) {
		String methodName = "getReciptFillGrid()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> reciptFillGrid = new ArrayList<>();
		try {
			reciptFillGrid = arAdjustmentOffSetService.getArOffsetFillgrid(orgId, subLedgerCode, docId, branch,
					docDate);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ArOffsetFillgrid Fill Grid information get successfully");
			responseObjectsMap.put("reciptFillGrid", reciptFillGrid);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ArOffsetFillgrid Fill Grid information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/approveApAdjustmentOffSet")
	public ResponseEntity<ResponseDTO> approveApAdjustmentOffSet(@RequestParam Long orgId, @RequestParam Long id,
			@RequestParam String docId, @RequestParam String action, @RequestParam String actionBy) {
		String methodName = "approveApAdjustmentOffSet()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			ApAdjustmentOffSetVO taxInvoiceVO = arAdjustmentOffSetService.approveApAdjustmentOffSet(orgId, id, docId,
					action, actionBy);
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

	@PutMapping("/approveArAdjustmentOffSet")
	public ResponseEntity<ResponseDTO> approveArAdjustmentOffSet(@RequestParam Long orgId, @RequestParam Long id,
			@RequestParam String docId, @RequestParam String action, @RequestParam String actionBy) {
		String methodName = "approveArAdjustmentOffSet()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			ArAdjustmentOffSetVO taxInvoiceVO = arAdjustmentOffSetService.approveArAdjustmentOffSet(orgId, id, docId,
					action, actionBy);
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

}
