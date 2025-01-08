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
import com.base.basesetup.dto.MultipleDocIdGenerationDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.MultipleDocIdGenerationVO;
import com.base.basesetup.service.MultipleDocIdGenerationService;

@RestController
@RequestMapping("/api/multipleDocIdGeneration")
public class MultipleDocIdGenerationController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(MultipleDocIdGenerationController.class);

	@Autowired
	MultipleDocIdGenerationService multipleDocIdGenerationService;
	
	// Document type
	
			@PutMapping("/createUpdateMultipleDocIdGeneration")
			public ResponseEntity<ResponseDTO> createUpdateMultipleDocIdGeneration(@RequestBody MultipleDocIdGenerationDTO multipleDocIdGenerationDTO) {
			    String methodName = "createUpdateMultipleDocIdGeneration()";
			    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			    String errorMsg = null;
			    Map<String, Object> responseObjectsMap = new HashMap<>();
			    ResponseDTO responseDTO = null;
			    try {
			        Map<String, Object> multipleDocIdGeneration1 = multipleDocIdGenerationService.createUpdateMultipleDocIdGeneration(multipleDocIdGenerationDTO);
			        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, multipleDocIdGeneration1.get("message"));
			        responseObjectsMap.put("multipleDocIdGenerationVO", multipleDocIdGeneration1.get("multipleDocIdGenerationVO"));
			        responseDTO = createServiceResponse(responseObjectsMap);
			    } catch (Exception e) {
			        errorMsg = e.getMessage();
			        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
			    }
			    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			    return ResponseEntity.ok().body(responseDTO);
			}
			
			@GetMapping("/getMultipleDocIdGenerationById")
			public ResponseEntity<ResponseDTO> getMultipleDocIdGenerationById(@RequestParam Long id) {
				String methodName = "getMultipleDocIdGenerationById()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				MultipleDocIdGenerationVO multipleDocIdGenerationVO = new MultipleDocIdGenerationVO();
				try {
					multipleDocIdGenerationVO = multipleDocIdGenerationService.getMultipleDocIdGenerationById(id);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				}
				if (StringUtils.isBlank(errorMsg)) {
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
							"MultipleDocIdGeneration information get Id successfully");
					responseObjectsMap.put("multipleDocIdGenerationVO", multipleDocIdGenerationVO);
					responseDTO = createServiceResponse(responseObjectsMap);
				} else {
					responseDTO = createServiceResponseError(responseObjectsMap,
							"MultipleDocIdGeneration information receive Id failed", errorMsg);
				}
				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);
			}
			
			@GetMapping("/getMultipleDocIdGenerationByOrgId")
			public ResponseEntity<ResponseDTO> getMultipleDocIdGenerationByOrgId(@RequestParam Long orgId) {
				String methodName = "getMultipleDocIdGenerationByOrgId()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				List<MultipleDocIdGenerationVO> multipleDocIdGenerationVO = new ArrayList<>();
				try {
					multipleDocIdGenerationVO = multipleDocIdGenerationService.getMultipleDocIdGenerationByOrgId(orgId);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				}
				if (StringUtils.isBlank(errorMsg)) {
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
							"MultipleDocIdGeneration information get OrgId successfully");
					responseObjectsMap.put("multipleDocIdGenerationVO", multipleDocIdGenerationVO);
					responseDTO = createServiceResponse(responseObjectsMap);
				} else {
					responseDTO = createServiceResponseError(responseObjectsMap,
							"MultipleDocIdGeneration information OrgId receive failed", errorMsg);
				}
				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);
			}
			
			@GetMapping("/getPendingMultipleDocIdGeneration")
			public ResponseEntity<ResponseDTO> getPendingMultipleDocIdGeneration(@RequestParam Long orgId,@RequestParam String branch,@RequestParam String branchCode,
					@RequestParam String finYear,@RequestParam String finYearIdentifier,@RequestParam String docCode) {
				String methodName = "getPendingMultipleDocIdGeneration()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				List<Map<String, Object>> multipleDocIdGenerationVO = new ArrayList<>();
				try {
					multipleDocIdGenerationVO = multipleDocIdGenerationService.getPendingMultipleDocIdGeneration(orgId, branch, branchCode, finYear, finYearIdentifier,docCode);
					} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				}
				if (StringUtils.isBlank(errorMsg)) {
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "MultipleDocIdGeneration information get successfully");
					responseObjectsMap.put("multipleDocIdGenerationVO", multipleDocIdGenerationVO);
					responseDTO = createServiceResponse(responseObjectsMap);
				} else {
					responseDTO = createServiceResponseError(responseObjectsMap, "MultipleDocIdGeneration information receive failed",
							errorMsg);
				}
				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);
			}
}
