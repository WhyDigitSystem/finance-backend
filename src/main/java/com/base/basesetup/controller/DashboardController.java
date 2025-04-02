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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.basesetup.common.CommonConstant;
import com.base.basesetup.common.UserConstants;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.service.DashboardService;

@CrossOrigin
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController extends BaseController{
	
	@Autowired
	DashboardService dashboardService;

	public static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);
	
	
	@GetMapping("/getReceiptAmont")
	public ResponseEntity<ResponseDTO> getReceiptAmont(@RequestParam(required = true) Long orgId,@RequestParam(required = false) String month,@RequestParam(required = false) String year) {
		String methodName = "getReceiptAmont()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> receiptAmont = new ArrayList<>();

		try {
			receiptAmont = dashboardService.getReceiptAmont(orgId,month,year);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Receipt Amont   retrieved successfully");
			responseObjectsMap.put("receiptAmont", receiptAmont);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Receipt Amont Reterive Failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@GetMapping("/getPaymentAmont")
	public ResponseEntity<ResponseDTO> getPaymentAmont(@RequestParam(required = true) Long orgId,@RequestParam(required = false) String month,@RequestParam(required = false) String year) {
		String methodName = "getPaymentAmont()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> receiptAmont = new ArrayList<>();

		try {
			receiptAmont = dashboardService.getPaymentAmont(orgId,month,year);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Payment Amont   retrieved successfully");
			responseObjectsMap.put("receiptAmont", receiptAmont);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Payment Amont Reterive Failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getTdsSummary")
	public ResponseEntity<ResponseDTO> getTdsSummary(@RequestParam(required = true) Long orgId,@RequestParam(required = false) String month,@RequestParam(required = true) String finYear) {
		String methodName = "getTdsSummary()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> receiptAmont = new ArrayList<>();

		try {
			receiptAmont = dashboardService.getTdsSummary(orgId,month,finYear);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Tds Summary  retrieved successfully");
			responseObjectsMap.put("receiptAmont", receiptAmont);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Tds Summary Reterive Failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
}
