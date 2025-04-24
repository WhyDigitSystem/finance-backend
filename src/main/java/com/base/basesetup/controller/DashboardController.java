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
	public ResponseEntity<ResponseDTO> getReceiptAmont(@RequestParam(required = true) Long orgId,@RequestParam(required = false) String month,@RequestParam String year) {
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
	public ResponseEntity<ResponseDTO> getTdsSummary(@RequestParam(required = true) Long orgId,@RequestParam(required = false) String month,
			@RequestParam(required = true) Long finYear) {
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
	
	@GetMapping("/getPercentageDiffFromRevenue")

	public ResponseEntity<ResponseDTO> getPercentageDiffFromRevenue(@RequestParam(required = true) Long orgId,@RequestParam(required = true) Long finYear,
			@RequestParam(required = true) String Month,@RequestParam(required = true) String Year,@RequestParam(required = true) String branchCode) {
	String methodName = "getPercentageDiffFromRevenue()";

		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> receiptAmont = new ArrayList<>();

		try {
			receiptAmont = dashboardService.getPercentageDiffFromRevenue(orgId, finYear, Month, Year,branchCode);

		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Revenue Information  retrieved successfully");
			responseObjectsMap.put("Revenue", receiptAmont);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Revenue Information Reterive Failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getPercentageDiffFromYear")
	public ResponseEntity<ResponseDTO> getPercentageDiffFromYear(@RequestParam(required = true) Long orgId,
			@RequestParam(required = false) Long finYear
			) {
		String methodName = "getPercentageDiffFromYear()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> receiptAmont = new ArrayList<>();

		try {
			receiptAmont = dashboardService.getPercentageDiffFromYear(orgId,finYear);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Revenue Information  retrieved successfully");
			responseObjectsMap.put("Revenue", receiptAmont);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Revenue Information Reterive Failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getPercentageDiffFromCost")
	public ResponseEntity<ResponseDTO> getPercentageDiffFromCost(@RequestParam(required = true) Long orgId,
			@RequestParam(required = true) Long finYear,
			@RequestParam(required = false) String month,
			@RequestParam(required = false) String year
			) {
		String methodName = "getPercentageDiffFromCost()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> receiptAmont = new ArrayList<>();

		try {
			receiptAmont = dashboardService.getPercentageDiffFromCost(orgId,finYear,month,year);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Cost Information  retrieved successfully");
			responseObjectsMap.put("cost", receiptAmont);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Cost Information Reterive Failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@GetMapping("/getPercentageFromReceipt")
	public ResponseEntity<ResponseDTO> getPercentageFromReceipt(@RequestParam(required = true) Long orgId,
			@RequestParam(required = true) Long finYear,
			@RequestParam(required = false) String month
			) {
		String methodName = "getPercentageFromReceipt()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> receiptAmont = new ArrayList<>();

		try {
			receiptAmont = dashboardService.getPercentageFromReceipt(orgId,finYear,month);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Receipt Information  retrieved successfully");
			responseObjectsMap.put("receipt", receiptAmont);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Receipt Information Reterive Failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getPercentageFromPayment")
	public ResponseEntity<ResponseDTO> getPercentageFromPayment(@RequestParam(required = true) Long orgId,
			@RequestParam(required = true) Long finYear,
			@RequestParam(required = false) String month
			) {
		String methodName = "getPercentageFromPayment()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> receiptAmont = new ArrayList<>();

		try {
			receiptAmont = dashboardService.getPercentageFromPayment(orgId,finYear,month);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Payment Information  retrieved successfully");
			responseObjectsMap.put("Payment", receiptAmont);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Payment Information Reterive Failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getSalesMonthWiseData")
	public ResponseEntity<ResponseDTO> getSalesMonthWiseData(@RequestParam(required = true) Long orgId,
			@RequestParam(required = true) Long finYear,
			@RequestParam(required = false) String branchCode
			) {
		String methodName = "getSalesMonthWiseData()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> receiptAmont = new ArrayList<>();

		try {
			receiptAmont = dashboardService.getSalesMonthWiseData(orgId,finYear,branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Sales MonthWise Data Information  retrieved successfully");
			responseObjectsMap.put("Payment", receiptAmont);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Sales MonthWise Data Information Reterive Failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getTotaltdsFromCustomer")
	public ResponseEntity<ResponseDTO> getTotaltdsFromCustomer(@RequestParam(required = true) Long orgId,
			@RequestParam(required = true) Long finYear,
			@RequestParam(required = false) String branchCode
			) {
		String methodName = "getTotaltdsFromCustomer()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> receiptAmont = new ArrayList<>();

		try {
			receiptAmont = dashboardService.getTotaltdsFromCustomer(orgId,finYear,branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Customer Tds Information  retrieved successfully");
			responseObjectsMap.put("Payment", receiptAmont);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Customer Tds Information Reterive Failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getTotaltdsFromCustomerBillWise")
	public ResponseEntity<ResponseDTO> getTotaltdsFromCustomerBillWise(@RequestParam(required = true) Long orgId,
			@RequestParam(required = true) Long finYear,
			@RequestParam(required = false) String branchCode,@RequestParam(required = false) String partyName
			) {
		String methodName = "getTotaltdsFromCustomerBillWise()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> receiptAmont = new ArrayList<>();

		try {
			receiptAmont = dashboardService.getTotaltdsFromCustomerBillWise(orgId,finYear,branchCode,partyName);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Customer Tds Information  retrieved successfully");
			responseObjectsMap.put("Payment", receiptAmont);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Customer Tds Information Reterive Failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
}
