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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.basesetup.common.CommonConstant;
import com.base.basesetup.common.UserConstants;
import com.base.basesetup.dto.QuotationNewDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.QuotationNewVO;
import com.base.basesetup.service.QuotationService;

@RestController
@CrossOrigin
@RequestMapping("/api/quotation")
public class QuotationController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(QuotationController.class);

	@Autowired
	QuotationService quotationService;

	@PutMapping("/createUpdateQuotation")
	public ResponseEntity<ResponseDTO> createUpdateQuotation(@RequestBody QuotationNewDTO quotationDTO) {
		String methodName = "createUpdateQuotation()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> quotationVO = quotationService.createUpdateQuotation(quotationDTO);
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
		List<QuotationNewVO> quotationVO = new ArrayList<>();
		try {
			quotationVO = quotationService.getQuotationByorgId(orgId);
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
		Optional<QuotationNewVO> quotationVO = null;
		try {
			quotationVO = quotationService.getQutationById(id);
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

}
