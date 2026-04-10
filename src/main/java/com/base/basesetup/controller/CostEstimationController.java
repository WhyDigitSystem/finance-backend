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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.common.CommonConstant;
import com.base.basesetup.common.UserConstants;
import com.base.basesetup.dto.CostEstimationDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.CostEstimationVO;
import com.base.basesetup.service.CostEstimationService;

@RestController
@RequestMapping("/api/costEstimation")
public class CostEstimationController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(CostEstimationController.class);

	@Autowired
	CostEstimationService costEstimationService;

	// TaxInvoice

	@GetMapping("/getAllCostEstimationByOrgId")
	public ResponseEntity<ResponseDTO> getAllCostEstimationByOrgId(@RequestParam Long orgId,@RequestParam String finYear,@RequestParam String branchCode) {
		String methodName = "getAllCostEstimationByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CostEstimationVO> costEstimationVO = new ArrayList<>();
		try {
			costEstimationVO = costEstimationService.getAllCostEstimationByOrgId(orgId, finYear,  branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"CostEstimation information get successfully ByOrgId");
			responseObjectsMap.put("costEstimationVO", costEstimationVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"CostEstimation information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllCostEstimationById")
	public ResponseEntity<ResponseDTO> getAllCostEstimationById(@RequestParam Long id) {
		String methodName = "getAllCostEstimationById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		CostEstimationVO costEstimationVO = new CostEstimationVO();
		try {
			costEstimationVO = costEstimationService.getAllCostEstimationById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "CostEstimation information get successfully By id");
			responseObjectsMap.put("costEstimationVO", costEstimationVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"CostEstimation information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateCostEstimation")
	public ResponseEntity<ResponseDTO> updateCreateCostEstimation(@RequestBody CostEstimationDTO costEstimationDTO) {
		String methodName = "updateCreateCostEstimation()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> costEstimationVO = costEstimationService.updateCreateCostEstimation(costEstimationDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, costEstimationVO.get("message"));
			responseObjectsMap.put("costEstimationVO", costEstimationVO.get("costEstimationVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllEmployees")
	public ResponseEntity<ResponseDTO> getAllEmployees(@RequestParam Long orgId) {
		String methodName = "getAllEmployees()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = costEstimationService.getAllEmployees(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee Type retrieved successfully");
			responseObjectsMap.put("EmployeeVO", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve Employee", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getCostEstimationDocId")
	public ResponseEntity<ResponseDTO> getCostEstimationDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getCostEstimationDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = costEstimationService.getCostEstimationDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"costEstimationDocid information retrieved successfully");
			responseObjectsMap.put("costEstimationDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve costEstimationDocid information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/approveCostEstimation")
	public ResponseEntity<ResponseDTO> approveCostEstimation(@RequestParam Long orgId, @RequestParam Long id,
			@RequestParam String docId, @RequestParam String action, @RequestParam String actionBy) {
		String methodName = "approveCostEstimation()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			CostEstimationVO costEstimationVO = costEstimationService.approveCostEstimation(orgId, id, docId, action,
					actionBy);
			responseObjectsMap.put("costEstimationVO", costEstimationVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PostMapping("/uploadImageCostEstimationDetail")
	public ResponseEntity<ResponseDTO> uploadImageCostEstimationDetail(
	        @RequestParam List<MultipartFile> file,
	        @RequestParam Long costEstimationId,
	        @RequestParam List<Long> costEstimationDetailsId) {

	    String methodName = "uploadImageCostEstimationDetail()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    String errorMsg = null;

	    try {
	        String costEstimationVO = costEstimationService
	                .uploadImageCostEstimationDetail(file, costEstimationId, costEstimationDetailsId);

	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "CostEstimation Successfully Uploaded");
	        responseObjectsMap.put("costEstimationVO", costEstimationVO);
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error("Unable To Upload PartImage", methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, "CostEstimation Upload Failed", errorMsg);
	    }

	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getCostEstimationDetails")
	public ResponseEntity<ResponseDTO> getCostEstimationDetails(@RequestParam Long orgId, @RequestParam(required = true) String finYear, @RequestParam String employeeName, String fromDate, String toDate,@RequestParam String branchCode,@RequestParam String category) {
		String methodName = "getCostEstimationDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();
		try {
			mapp = costEstimationService.getCostEstimationDetails(orgId, finYear, employeeName, fromDate, toDate,branchCode,category);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "tax CostEstimationDetails retrieved successfully");
			responseObjectsMap.put("mapp", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve CostEstimationDetails", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	
	@GetMapping("/getCostEstimationSummary")
	public ResponseEntity<ResponseDTO> getCostEstimationSummary(@RequestParam Long orgId, @RequestParam(required = true) String finYear,@RequestParam
			String employeeName, String fromDate, String toDate,@RequestParam String branchCode,@RequestParam String category) {
		String methodName = "getCostEstimationSummary()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = costEstimationService.getCostEstimationSummary(orgId, finYear, employeeName, fromDate, toDate,branchCode,category);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "CostEstimation summary retrieved successfully");
			responseObjectsMap.put("mapp", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve CostEstimation summary", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	
	@GetMapping("/getCostEstimationCount")
	public ResponseEntity<ResponseDTO> getCostEstimationCount(@RequestParam Long orgId,
			@RequestParam String finYear, @RequestParam String branchCode) {
		String methodName = "getCostEstimationCount()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = costEstimationService.getCostEstimationCount(orgId, finYear, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Count  retrieved successfully");
			responseObjectsMap.put("mapp", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve  Count", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
}
