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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.basesetup.common.CommonConstant;
import com.base.basesetup.common.UserConstants;
import com.base.basesetup.dto.MonthlyProcessDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.MonthlyProcessVO;
import com.base.basesetup.service.MonthlyProcessService;

@RestController
@RequestMapping("/api/MonthlyProcess")
public class MonthlyProcessController extends BaseController {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(MonthlyProcessController.class);
	
	@Autowired
	MonthlyProcessService monthlyProcessService;
	
	@PutMapping("/createUpdateMonthlyProcess")
	public ResponseEntity<ResponseDTO> createUpdateMonthlyProcess(@RequestBody MonthlyProcessDTO monthlyProcessDTO) {
		String methodName = "createUpdateMonthlyProcess()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> monthlyProcessVO = monthlyProcessService.createUpdateMonthlyProcess(monthlyProcessDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, monthlyProcessVO.get("message"));
			responseObjectsMap.put("monthlyProcessVO", monthlyProcessVO.get("monthlyProcessVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getMonthlyProcessByClient")
	public ResponseEntity<ResponseDTO> getMonthlyProcessByClient(@RequestParam Long orgId,@RequestParam String clientCode,@RequestParam String mainGroup,@RequestParam String subGroupCode,@RequestParam String finYear) {
		String methodName = "getMonthlyProcessByClient()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<MonthlyProcessVO> monthlyProcessVO = new ArrayList<>();
		try {
			monthlyProcessVO = monthlyProcessService.getAllMonthlyProcessByClientCode(orgId, clientCode, mainGroup, subGroupCode,finYear);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Monthly ProcessVO information get successfully");
			responseObjectsMap.put("monthlyProcessVO", monthlyProcessVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Monthly ProcessVO information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getMonthlyProcessById")
	public ResponseEntity<ResponseDTO> getMonthlyProcessById(@RequestParam Long id) {
		String methodName = "getMonthlyProcessById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		MonthlyProcessVO monthlyProcessVO = new MonthlyProcessVO();
		try {
			monthlyProcessVO = monthlyProcessService.getAllMonthlyProcessById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Monthly ProcessVO information get successfully");
			responseObjectsMap.put("monthlyProcessVO", monthlyProcessVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Monthly ProcessVO information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	
	

}
