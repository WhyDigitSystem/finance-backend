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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.basesetup.common.CommonConstant;
import com.base.basesetup.common.UserConstants;
import com.base.basesetup.dto.InvoiceDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.InvoiceVO;
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
	
	
}