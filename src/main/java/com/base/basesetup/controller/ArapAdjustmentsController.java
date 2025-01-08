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
import com.base.basesetup.dto.ArapAdjustmentsDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.ArapAdjustmentsVO;
import com.base.basesetup.service.ArapAdjustmentsService;

@CrossOrigin
@RestController
@RequestMapping("/api/arapAdjustments")
public class ArapAdjustmentsController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(ArapAdjustmentsController.class);

	@Autowired
	ArapAdjustmentsService arapAdjustmentsService;

	// ArapAdjustments

	@GetMapping("/getAllArapAdjustmentsByOrgId")
	public ResponseEntity<ResponseDTO> getAllArapAdjustmentsByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllArapAdjustmentsByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ArapAdjustmentsVO> arapAdjustmentsVO = new ArrayList<>();
		try {
			arapAdjustmentsVO = arapAdjustmentsService.getAllArapAdjustmentsByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ArapAdjustments information get successfully By OrgId");
			responseObjectsMap.put("arapAdjustmentsVO", arapAdjustmentsVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ArapAdjustments information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllArapAdjustmentsById")
	public ResponseEntity<ResponseDTO> getAllArapAdjustmentsById(@RequestParam(required = false) Long id) {
		String methodName = "getAllArapAdjustmentsById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ArapAdjustmentsVO> arapAdjustmentsVO = new ArrayList<>();
		try {
			arapAdjustmentsVO = arapAdjustmentsService.getAllArapAdjustmentsById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ArapAdjustments information get successfully By id");
			responseObjectsMap.put("arapAdjustmentsVO", arapAdjustmentsVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ArapAdjustments information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateArapAdjustments")
	public ResponseEntity<ResponseDTO> createUpdateArapAdjustments(
			@Valid @RequestBody ArapAdjustmentsDTO arapAdjustmentsDTO) {
		String methodName = "createUpdateArapAdjustments()";

		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			Map<String, Object> arapAdjustmentsVO = arapAdjustmentsService
					.createUpdateArapAdjustments(arapAdjustmentsDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, arapAdjustmentsVO.get("message"));
			responseObjectsMap.put("arapAdjustmentsVO", arapAdjustmentsVO.get("arapAdjustmentsVO")); // Corrected key
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getArapAdjustmentsByActive")
	public ResponseEntity<ResponseDTO> getArapAdjustmentsByActive() {
		String methodName = "getArapAdjustmentsByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ArapAdjustmentsVO> arapAdjustmentsVO = new ArrayList<>();
		try {
			arapAdjustmentsVO = arapAdjustmentsService.getArapAdjustmentsByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ArapAdjustments information get successfully By Active");
			responseObjectsMap.put("arapAdjustmentsVO", arapAdjustmentsVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ArapAdjustments information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	@GetMapping("/getArapAdjustmentsByDocId")
	public ResponseEntity<ResponseDTO> getArapAdjustmentsByDocId(@RequestParam Long orgId, @RequestParam String docId) {
		String methodName = "getArapAdjustmentsByDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		ArapAdjustmentsVO adjustmentsVO = new ArapAdjustmentsVO();
		try {
			adjustmentsVO = arapAdjustmentsService.getArapAdjustmentsByDocId(orgId, docId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ArapAdjustments information get successfully By docid");
			responseObjectsMap.put("adjustmentsVO", adjustmentsVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ArapAdjustments information receive failed By docid", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	@GetMapping("/getArapAdjustmentsDocId")
	public ResponseEntity<ResponseDTO> getArapAdjustmentsDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getArapAdjustmentsDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = arapAdjustmentsService.getArapAdjustmentsDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ArapAdjustments information retrieved successfully");
			responseObjectsMap.put("taxInvoiceDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve ArapAdjustments Docid information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

}
