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
import com.base.basesetup.dto.ArAdjustmentOffSetDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.ArAdjustmentOffSetVO;
import com.base.basesetup.entity.ReceiptVO;
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
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ArAdjustmentOffSet information get successfully By OrgId");
			responseObjectsMap.put("arAdjustmentOffSetVO", arAdjustmentOffSetVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "ArAdjustmentOffSet information receive failed By OrgId",
					errorMsg);
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
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ArAdjustmentOffSet information get successfully By id");
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
			responseObjectsMap.put("arAdjustmentOffSetVO", arAdjustmentOffSetVO.get("arAdjustmentOffSetVO")); // Corrected key
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getArAdjustmentOffSetDocId")
	public ResponseEntity<ResponseDTO> getArAdjustmentOffSetDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getArAdjustmentOffSetDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = arAdjustmentOffSetService.getArAdjustmentOffSetDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ArAdjustmentOffSet information retrieved successfully");
			responseObjectsMap.put("arAdjustmentOffSetDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve ArAdjustmentOffSet Docid information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	//DropDown Api ARAdjustmentOffSet
	
	@GetMapping("/getAllCustomerReceiptByOrgIdAndBranchCode")
	public ResponseEntity<ResponseDTO> getAllCustomerReceiptByOrgIdAndBranchCode(@RequestParam Long orgId,@RequestParam String branchCode) {
		String methodName = "getAllCustomerReceiptByOrgIdAndBranchCode()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ReceiptVO> receiptVO = new ArrayList<>();
		try {
			receiptVO = arAdjustmentOffSetService.getAllCustomerReceiptByOrgIdAndBranchCode(orgId,branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ArAdjustmentOffSet information get successfully By OrgId");
			responseObjectsMap.put("receiptVO", receiptVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "ArAdjustmentOffSet information receive failed By OrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

}
