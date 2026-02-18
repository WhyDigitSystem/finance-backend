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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.common.CommonConstant;
import com.base.basesetup.common.UserConstants;
import com.base.basesetup.dto.ElMfrDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.ElMfrVO;
import com.base.basesetup.service.ELReportService;

@CrossOrigin
@RestController
@RequestMapping("/api/eLReportController")
public class ELReportController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(ELReportController.class);

	@Autowired
	ELReportService elReportService;

	@PutMapping("/createUpdateElMfr")
	public ResponseEntity<ResponseDTO> createUpdateElMfr(@RequestBody ElMfrDTO elMfrDTO) {
		String methodName = "createUpdateElMfr()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> elMfrVO = elReportService.createUpdateElMfr(elMfrDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, elMfrVO.get("message"));
			responseObjectsMap.put("elMfrVO", elMfrVO.get("elMfrVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllElMfr")
	public ResponseEntity<ResponseDTO> getAllElMfr(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllCao()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ElMfrVO> elMfrVOs = new ArrayList<>();
		try {
			elMfrVOs = elReportService.getAllElMfr(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ELMFR information get successfully");
			responseObjectsMap.put("elMfrVO", elMfrVOs);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "ELMFR information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PostMapping("/excelUploadForElMfr")
	public ResponseEntity<ResponseDTO> excelUploadForElMfr(@RequestParam MultipartFile[] files,
			@RequestParam(required = false) String createdBy, @RequestParam(required = false) Long orgId) {
		String methodName = "excelUploadForElMfr()";
		int totalRows = 0;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		int successfulUploads = 0;
		ResponseDTO responseDTO = null;
		try {
			// Call service method to process Excel upload
			elReportService.excelUploadForElMfr(files, createdBy, orgId);

			// Retrieve the counts after processing
			totalRows = elReportService.getTotalRows(); // Get total rows processed
			successfulUploads = elReportService.getSuccessfulUploads(); // Get successful uploads count
			responseObjectsMap.put("statusFlag", "Ok");
			responseObjectsMap.put("status", true);
			responseObjectsMap.put("totalRows", totalRows);
			responseObjectsMap.put("successfulUploads", successfulUploads);
			responseObjectsMap.put("message", "Excel Upload For ElMfr successful"); // Directly include the message here
			responseDTO = createServiceResponse(responseObjectsMap);

		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(CommonConstant.EXCEPTION, methodName, e);
			responseObjectsMap.put("statusFlag", "Error");
			responseObjectsMap.put("status", false);
			responseObjectsMap.put("errorMessage", errorMsg);

			responseDTO = createServiceResponseError(responseObjectsMap, "Excel Upload For ElMfr Failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getMisMatchClientTb")
	public ResponseEntity<ResponseDTO> getMisMatchClientTb(@RequestParam Long orgId, @RequestParam String clientCode) {
		String methodName = "getMisMatchClientTb()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> coaVO = new ArrayList<Map<String, Object>>();
		try {
			coaVO = elReportService.getMisMatchClientTb(orgId, clientCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "MisMatch ClientTb information get successfully");
			responseObjectsMap.put("coaVO", coaVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "MisMatch ClientTb information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getClientBudgetDetails")
	public ResponseEntity<ResponseDTO> getClientBudgetDetails(@RequestParam Long orgId, @RequestParam String year,
			@RequestParam String client, @RequestParam String clientCode) {
		String methodName = "getClientBudgetDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> budgetVO = new ArrayList<Map<String, Object>>();
		try {
			budgetVO = elReportService.getClientBudgetDetails(orgId, year, client, clientCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Client Budget information get successfully");
			responseObjectsMap.put("budgetVO", budgetVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Client Budget information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getClientPreviousYearActualDetails")
	public ResponseEntity<ResponseDTO> getClientPreviousYearActualDetails(@RequestParam Long orgId,
			@RequestParam String year, @RequestParam String client, @RequestParam String clientCode) {
		String methodName = "getClientPreviousYearActualDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> actualVO = new ArrayList<Map<String, Object>>();
		try {
			actualVO = elReportService.getClientPreviousYearActualDetails(orgId, year, client, clientCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Client Actual information get successfully");
			responseObjectsMap.put("actualVO", actualVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Client Actual information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getElYtdTD")
	public ResponseEntity<ResponseDTO> getElYtdTD(@RequestParam Long orgId, @RequestParam String clientCode,
			@RequestParam String finyear, @RequestParam String month) {
		String methodName = "getElYtdTD()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elYtdDetails = new ArrayList<Map<String, Object>>();
		try {
			elYtdDetails = elReportService.getElevateYTDTBDetails(orgId, clientCode, finyear, month);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "El YTD information get successfully");
			responseObjectsMap.put("elYtdDetailsVO", elYtdDetails);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "El YTD information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getMonthlyProcessDetails")
	public ResponseEntity<ResponseDTO> getMonthlyProcessDetails(@RequestParam Long orgId,
			@RequestParam String clientCode, @RequestParam String finYear, @RequestParam String month,
			@RequestParam String yearType, String mainGroup, String subGroupCode) {
		String methodName = "getMonthlyProcessDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elYtdDetails = new ArrayList<Map<String, Object>>();
		try {
			elYtdDetails = elReportService.getMonthlyProcess(orgId, clientCode, finYear, month, yearType, mainGroup,
					subGroupCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Monthly Process information get successfully");
			responseObjectsMap.put("elYtdDetailsVO", elYtdDetails);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Monthly Process information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getElBudgetReport")
	public ResponseEntity<ResponseDTO> getElBudgetReport(@RequestParam Long orgId, @RequestParam String clientCode,
			@RequestParam String finyear, @RequestParam String yearType, @RequestParam String mainGroupName,
			@RequestParam String subGroupCode) {
		String methodName = "getElBudgetReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elBudgetReport = new ArrayList<Map<String, Object>>();
		try {
			elBudgetReport = elReportService.getELBudgetReport(orgId, clientCode, finyear, yearType, mainGroupName,
					subGroupCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EL Budget Report information get successfully");
			responseObjectsMap.put("elBudgetReport", elBudgetReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EL Budget Report information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getElPYReport")
	public ResponseEntity<ResponseDTO> getElPYReport(@RequestParam Long orgId, @RequestParam String finyear,
			@RequestParam String clientCode, @RequestParam String mainGroupName, @RequestParam String subGroupCode,
			@RequestParam String month) {
		String methodName = "getElPYReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elPYReport = new ArrayList<Map<String, Object>>();
		try {
			elPYReport = elReportService.getELPYReport(orgId, finyear, clientCode, mainGroupName, subGroupCode, month);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EL PY Report information get successfully");
			responseObjectsMap.put("elPYReport", elPYReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EL PY Report information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getElActualReport")
	public ResponseEntity<ResponseDTO> getElActualReport(@RequestParam Long orgId, @RequestParam String clientCode,
			@RequestParam String finyear, @RequestParam String yearType, @RequestParam String month,
			@RequestParam String mainGroupName, @RequestParam String subGroupCode) {
		String methodName = "getElActualReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elActualReport = new ArrayList<Map<String, Object>>();
		try {
			elActualReport = elReportService.getELActualReport(orgId, clientCode, finyear, yearType, month,
					mainGroupName, subGroupCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EL Actual Report information get successfully");
			responseObjectsMap.put("elActualReport", elActualReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EL Actual Report information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getElActualQuaterReport")
	public ResponseEntity<ResponseDTO> getElActualQuaterReport(@RequestParam Long orgId,
			@RequestParam String clientCode, @RequestParam String finyear, @RequestParam String yearType,
			@RequestParam String month, @RequestParam String mainGroupName, @RequestParam String subGroupCode) {
		String methodName = "getElActualQuaterReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elActualQuaterReport = new ArrayList<Map<String, Object>>();
		try {
			elActualQuaterReport = elReportService.getELActualQuaterReport(orgId, clientCode, finyear, yearType, month,
					mainGroupName, subGroupCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"EL Actual Quater Report information get successfully");
			responseObjectsMap.put("elActualQuaterReport", elActualQuaterReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"EL Actual Quater Report information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getElActualAutomaticReport")
	public ResponseEntity<ResponseDTO> getElActualAutomaticReport(@RequestParam Long orgId,
			@RequestParam String finyear, @RequestParam String clientCode, @RequestParam String mainGroupName,
			@RequestParam String month, @RequestParam String yearType) {
		String methodName = "getElActualAutomaticReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elActualReport = new ArrayList<Map<String, Object>>();
		try {
			elActualReport = elReportService.getELActualAutomaticReport(orgId, finyear, clientCode, mainGroupName,
					month, yearType);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EL Actual Report information get successfully");
			responseObjectsMap.put("elActualReport", elActualReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EL Actual Report information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getElActualIncrementalReport")
	public ResponseEntity<ResponseDTO> getELActualIncrementalProfitReport(@RequestParam Long orgId,
			@RequestParam String clientCode, @RequestParam String finyear, @RequestParam String yearType) {
		String methodName = "getELActualIncrementalProfitReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elActualReport = new ArrayList<Map<String, Object>>();
		try {
			elActualReport = elReportService.getELActualIncrementalProfitReport(orgId, clientCode, finyear, yearType);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EL Actual Report information get successfully");
			responseObjectsMap.put("elActualReport", elActualReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EL Actual Report information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getELActualHeadCountReport")
	public ResponseEntity<ResponseDTO> getELActualHeadCountReport(@RequestParam Long orgId,
			@RequestParam String clientCode, @RequestParam String finyear, @RequestParam String yearType,
			@RequestParam String month) {
		String methodName = "getELActualHeadCountReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elActualReport = new ArrayList<Map<String, Object>>();
		try {
			elActualReport = elReportService.getELActualHeadCountReport(orgId, clientCode, finyear, yearType, month);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EL Actual Report information get successfully");
			responseObjectsMap.put("elActualReport", elActualReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EL Actual Report information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getELActualARAPReport")
	public ResponseEntity<ResponseDTO> getELActualARAPReport(@RequestParam Long orgId, @RequestParam String clientCode,
			@RequestParam String finyear, @RequestParam String yearType, @RequestParam String month,
			@RequestParam String type) {
		String methodName = "getELActualARAPReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elActualReport = new ArrayList<Map<String, Object>>();
		try {
			elActualReport = elReportService.getELActualARAPReport(orgId, clientCode, finyear, yearType, month, type);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EL Actual Report information get successfully");
			responseObjectsMap.put("elActualReport", elActualReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EL Actual Report information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getELActualRatioAnalysisReport")
	public ResponseEntity<ResponseDTO> getELActualRatioAnalysisReport(@RequestParam Long orgId,
			@RequestParam String clientCode, @RequestParam String finyear, @RequestParam String mainGroupName,
			@RequestParam String month, @RequestParam String yearType, @RequestParam String subGroup) {
		String methodName = "getELActualRatioAnalysisReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elActualReport = new ArrayList<Map<String, Object>>();
		try {
			elActualReport = elReportService.getELActualRatioAnalysisReport(orgId, finyear, clientCode, mainGroupName,
					month, yearType, subGroup);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EL Actual Report information get successfully");
			responseObjectsMap.put("elActualReport", elActualReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EL Actual Report information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getELActualSalesPurchaseAnalysisReport")
	public ResponseEntity<ResponseDTO> getELActualSalesPurchaseAnalysisReport(@RequestParam Long orgId,
			@RequestParam String clientCode, @RequestParam String finyear, @RequestParam String type,
			@RequestParam String month, @RequestParam String yearType) {
		String methodName = "getELActualSalesPurchaseAnalysisReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elActualReport = new ArrayList<Map<String, Object>>();
		try {
			elActualReport = elReportService.getELActualSalesPurchaseAnalysisReport(orgId, finyear, clientCode, type,
					month, yearType);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EL Actual Report information get successfully");
			responseObjectsMap.put("elActualReport", elActualReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EL Actual Report information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getElBudgetAutomaticReport")
	public ResponseEntity<ResponseDTO> getElBudgetAutomaticReport(@RequestParam Long orgId,
			@RequestParam String clientCode, @RequestParam String finyear, @RequestParam String yearType,
			@RequestParam String mainGroupName) {
		String methodName = "getElBudgetAutomaticReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elBudgetReport = new ArrayList<Map<String, Object>>();
		try {
			elBudgetReport = elReportService.getELBudgetAutomaticReport(orgId, clientCode, finyear, yearType,
					mainGroupName);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"EL Budget Automatic Report information get successfully");
			responseObjectsMap.put("elBudgetReport", elBudgetReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"EL Budget Automatic Report information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getElPyAutomaticReport")
	public ResponseEntity<ResponseDTO> getElPyAutomaticReport(@RequestParam Long orgId, @RequestParam String clientCode,
			@RequestParam String finyear, @RequestParam String yearType, @RequestParam String mainGroupName,
			@RequestParam String month) {
		String methodName = "getElPyAutomaticReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elBudgetReport = new ArrayList<Map<String, Object>>();
		try {
			elBudgetReport = elReportService.getELPyAutomaticReport(orgId, clientCode, finyear, yearType, mainGroupName,
					month);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"EL Py Automatic Report information get successfully");
			responseObjectsMap.put("elBudgetReport", elBudgetReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"EL Py Automatic Report information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getElPyRatioAnalysisReport")
	public ResponseEntity<ResponseDTO> getElPyRatioAnalysisReport(@RequestParam Long orgId, @RequestParam String clientCode,
			@RequestParam String finyear, @RequestParam String yearType, @RequestParam String mainGroupName,@RequestParam String subGroupName, @RequestParam String month) {
		String methodName = "getElPyRatioAnalysisReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elBudgetReport = new ArrayList<Map<String, Object>>();
		try {
			elBudgetReport = elReportService.getELPyRatioAnalaysisReport(orgId, clientCode, finyear, yearType, mainGroupName, subGroupName, month);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EL Py Automatic Report information get successfully");
			responseObjectsMap.put("elBudgetReport", elBudgetReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EL Py Automatic Report information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getElBudgetHCReport")
	public ResponseEntity<ResponseDTO> getElBudgetHCReport(@RequestParam Long orgId,@RequestParam String finYear, @RequestParam String clientCode, @RequestParam String previousYear) {
		String methodName = "getElBudgetHCReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elBudgetReport = new ArrayList<Map<String, Object>>();
		try {
			elBudgetReport = elReportService.getELBudgetHCReport(orgId, finYear, clientCode, previousYear);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EL Budget HC Report information get successfully");
			responseObjectsMap.put("elBudgetReport", elBudgetReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EL Budget HC Report information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getElBudgetOBReport")
	public ResponseEntity<ResponseDTO> getElBudgetOBReport(@RequestParam Long orgId,@RequestParam String finYear, @RequestParam String clientCode, @RequestParam String previousYear) {
		String methodName = "getElBudgetOBReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elBudgetReport = new ArrayList<Map<String, Object>>();
		try {
			elBudgetReport = elReportService.getELBudgetOBReport(orgId, finYear, clientCode, previousYear);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EL Budget OB Report information get successfully");
			responseObjectsMap.put("elBudgetReport", elBudgetReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EL Budget OB Report information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getElBudgetRatioAnalysisReport")
	public ResponseEntity<ResponseDTO> getElBudgetRatioAnalysisReport(@RequestParam Long orgId,@RequestParam String finYear, @RequestParam String clientCode, @RequestParam String previousYear) {
		String methodName = "getElBudgetRatioAnalysisReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elBudgetReport = new ArrayList<Map<String, Object>>();
		try {
			elBudgetReport = elReportService.getELBudgetRatioAnalysisDetails(orgId, finYear, clientCode, previousYear);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EL Budget Sales Analysis Report information get successfully");
			responseObjectsMap.put("elBudgetReport", elBudgetReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EL Budget Sales Analysis information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getElBudgetSalesPurchaseAnalysisReport")
	public ResponseEntity<ResponseDTO> getElBudgetSalesPurchaseAnalysisReport(@RequestParam Long orgId,@RequestParam String finYear, @RequestParam String clientCode, @RequestParam String previousYear,@RequestParam String type) {
		String methodName = "getElBudgetSalesPurchaseAnalysisReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elBudgetReport = new ArrayList<Map<String, Object>>();
		try {
			elBudgetReport = elReportService.getELBudgetSalesPurchaseAnalysisDetails(orgId, finYear, clientCode, previousYear, type);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EL Budget Ratio Analysis Report information get successfully");
			responseObjectsMap.put("elBudgetReport", elBudgetReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EL Budget Ratio Analysis information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getElPyOBReport")
	public ResponseEntity<ResponseDTO> getElPyOBReport(@RequestParam Long orgId,@RequestParam String previousYear, @RequestParam String clientCode,@RequestParam String month) {
		String methodName = "getElPyOBReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elBudgetReport = new ArrayList<Map<String, Object>>();
		try {
			elBudgetReport = elReportService.getELPyOBReport(orgId, previousYear, clientCode,month);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EL Py OB Report information get successfully");
			responseObjectsMap.put("elBudgetReport", elBudgetReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EL Py OB Report information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getElPyHCReport")
	public ResponseEntity<ResponseDTO> getElPyHCReport(@RequestParam Long orgId,@RequestParam String previousYear, @RequestParam String clientCode,@RequestParam String month) {
		String methodName = "getElPyHCReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elBudgetReport = new ArrayList<Map<String, Object>>();
		try {
			elBudgetReport = elReportService.getELPyHCReport(orgId, previousYear, clientCode,month);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EL Budget HC Report information get successfully");
			responseObjectsMap.put("elBudgetReport", elBudgetReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EL Budget HC Report information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getElPySalesPurchaseAnalysisReport")
	public ResponseEntity<ResponseDTO> getElPySalesPurchaseAnalysisReport(@RequestParam Long orgId,@RequestParam String previousYear, @RequestParam String clientCode, @RequestParam String type,@RequestParam String month) {
		String methodName = "getElPySalesPurchaseAnalysisReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elBudgetReport = new ArrayList<Map<String, Object>>();
		try {
			elBudgetReport = elReportService.getELPySalesPurchaseAnalysisDetails(orgId, previousYear, clientCode,type,month);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EL Budget Ratio Analysis Report information get successfully");
			responseObjectsMap.put("elBudgetReport", elBudgetReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EL Budget Ratio Analysis information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getElActualOBReport")
	public ResponseEntity<ResponseDTO> getElActualOBReport(@RequestParam Long orgId,@RequestParam String clientCode,@RequestParam String finYear,@RequestParam String month, @RequestParam String previousYear,@RequestParam String type) {
		String methodName = "getElActualOBReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> elBudgetReport = new ArrayList<Map<String, Object>>();
		try {
			elBudgetReport = elReportService.getELActualOBReport(orgId,clientCode,finYear,month,previousYear,type);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EL Actual OB Report information get successfully");
			responseObjectsMap.put("elBudgetReport", elBudgetReport);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EL Actual OB Report information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
}

