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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.common.CommonConstant;
import com.base.basesetup.common.UserConstants;
import com.base.basesetup.dto.CCoaDTO;
import com.base.basesetup.dto.CoaDTO;
import com.base.basesetup.dto.ExcelUploadResultDTO;
import com.base.basesetup.dto.LedgerMappingDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.dto.ServiceLevelDTO;
import com.base.basesetup.entity.CCoaVO;
import com.base.basesetup.entity.CoaVO;
import com.base.basesetup.entity.LedgerMappingVO;
import com.base.basesetup.entity.ServiceLevelVO;
import com.base.basesetup.service.BusinessService;

@CrossOrigin
@RestController
@RequestMapping("/api/businesscontroller")
public class BusinessController extends BaseController {

	@Autowired
	BusinessService businessService;

	public static final Logger LOGGER = LoggerFactory.getLogger(BusinessController.class);

	// COA

	@PutMapping("/createUpdateCoa")
	public ResponseEntity<ResponseDTO> createUpdateCoa(@RequestBody CoaDTO coaDTO) {
		String methodName = "createUpdateCoa()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> coaVO = businessService.createUpdateCoa(coaDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, coaVO.get("message"));
			responseObjectsMap.put("coaVO", coaVO.get("coaVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllCao")
	public ResponseEntity<ResponseDTO> getAllCao(@RequestParam Long orgId) {
		String methodName = "getAllCao()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CoaVO> coaVO = new ArrayList<>();
		try {
			coaVO = businessService.getAllCao(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "coa information get successfully");
			responseObjectsMap.put("coaVO", coaVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "coa information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getCoaById")
	public ResponseEntity<ResponseDTO> getCoaById(@RequestParam Long id) {
		String methodName = "getCoaById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<CoaVO> coaVO = null;
		try {
			coaVO = businessService.getCaoById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "coa information get successfully By Id");
			responseObjectsMap.put("coaVO", coaVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "coa information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getCoaByCode")
	public ResponseEntity<ResponseDTO> getCoaByCode(@RequestParam String code) {
		String methodName = "getCoaByCode()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<CoaVO> coaVO = null;
		try {
			coaVO = businessService.getCaoByCode(code);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "coa information get successfully By Id");
			responseObjectsMap.put("coaVO", coaVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "coa information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getGroupName")
	public ResponseEntity<ResponseDTO> getGroupName(@RequestParam Long orgId) {
		String methodName = "getGroupName()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> coaVO = new ArrayList<Map<String, Object>>();
		try {
			coaVO = businessService.getGroupName(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Group information get successfully");
			responseObjectsMap.put("coaVO", coaVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Group information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PostMapping("/excelUploadForCoa")
	public ResponseEntity<ResponseDTO> excelUploadForCoa(@RequestParam MultipartFile[] files,
			@RequestParam(required = false) String createdBy,@RequestParam Long orgId) {
		String methodName = "excelUploadForCoa()";
		
		Map<String, Object> responseObjectsMap = new HashMap<>();
		int totalRows = 0;
		int successfulUploads = 0;
		ResponseDTO responseDTO = null;
		try {
			// Call service method to process Excel upload
			businessService.excelUploadForCoa(files, createdBy,orgId);

			// Retrieve the counts after processing
			totalRows = businessService.getTotalRows(); // Get total rows processed
			successfulUploads = businessService.getSuccessfulUploads(); // Get successful uploads count
			responseObjectsMap.put("statusFlag", "Ok");
			responseObjectsMap.put("status", true);
			responseObjectsMap.put("totalRows", totalRows);
			responseObjectsMap.put("successfulUploads", successfulUploads);
			responseObjectsMap.put("message", "Excel Upload For Coa successful"); // Directly include the message here
			responseDTO = createServiceResponse(responseObjectsMap);

		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(CommonConstant.EXCEPTION, methodName, e);
			responseObjectsMap.put("statusFlag", "Error");
			responseObjectsMap.put("status", false);
			responseObjectsMap.put("errorMessage", errorMsg);

			responseDTO = createServiceResponseError(responseObjectsMap, "Excel Upload For Coa Failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// CCoaVO

	@PutMapping("/createUpdateCCoa")
	public ResponseEntity<ResponseDTO> createUpdateCCoa(@RequestBody CCoaDTO cCoaDTO) {
		String methodName = "createUpdateCCoa()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> cCoaVO = businessService.createUpdateCCoa(cCoaDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, cCoaVO.get("message"));
			responseObjectsMap.put("cCoaVO", cCoaVO.get("cCoaVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllCCao")
	public ResponseEntity<ResponseDTO> getAllCCao(@RequestParam Long orgId,@RequestParam String clientCode) {
		String methodName = "getAllCCao()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CCoaVO> cCoaVO = new ArrayList<>();
		try {
			cCoaVO = businessService.getAllCCao(orgId,clientCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "CCoa information get successfully");
			responseObjectsMap.put("cCoaVO", cCoaVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "CCoa information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getCCaoById")
	public ResponseEntity<ResponseDTO> getCCaoById(@RequestParam Long id) {
		String methodName = "getCCaoById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<CCoaVO> cCoaVO = null;
		try {
			cCoaVO = businessService.getCCaoById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "CCoa information get successfully By Id");
			responseObjectsMap.put("cCoaVO", cCoaVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "CCoa information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getGroupNameForCCoa")
	public ResponseEntity<ResponseDTO> getGroupNameForCCoa() {
		String methodName = "getGroupNameForCCoa()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> cCoaVO = new ArrayList<Map<String, Object>>();
		try {
			cCoaVO = businessService.getGroupNameForCCoa();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Group information get successfully For CCoa");
			responseObjectsMap.put("cCoaVO", cCoaVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Group information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PostMapping("/excelUploadForCCoa")
	public ResponseEntity<ResponseDTO> excelUploadForCCoa(@RequestParam MultipartFile[] files,
			@RequestParam(required = false) String createdBy, @RequestParam(required = false) String clientCode,
			@RequestParam String clientName, @RequestParam Long orgId) {

		int totalRows = 0;
		int successfulUploads = 0;
		String methodName = "excelUploadForCCoa()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

		ResponseDTO responseDTO;
		Map<String, Object> responseObjectsMap = new HashMap<>();

		try {
			// Call the service method and get the result
			ExcelUploadResultDTO uploadResult = businessService.excelUploadForCCoa(files, createdBy, clientCode, clientName, orgId);

			totalRows = businessService.getTotalRows(); // Get total rows processed
			successfulUploads = businessService.getSuccessfulUploads();
			responseObjectsMap.put("statusFlag", "Ok");
			responseObjectsMap.put("status", true);
			responseObjectsMap.put("totalRows", totalRows);
			responseObjectsMap.put("successfulUploads", successfulUploads);
			responseObjectsMap.put("message", "Excel Upload For CCoa successful"); 
//			// Populate success response
//			responseObjectsMap.put("statusFlag", "Ok");
//			responseObjectsMap.put("status", true);
//			responseObjectsMap.put("uploadResult", uploadResult);
			responseDTO = createServiceResponse(responseObjectsMap);

		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(CommonConstant.EXCEPTION, methodName, e);
			responseObjectsMap.put("statusFlag", "Error");
			responseObjectsMap.put("status", false);
			responseObjectsMap.put("errorMessage", errorMsg);

			responseDTO = createServiceResponseError(responseObjectsMap, "Excel Upload For CCoa Failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// LEDGER MAPPING

	@PutMapping("/createUpdateLedgerMapping")
	public ResponseEntity<ResponseDTO> createUpdateLedgerMapping(@RequestBody LedgerMappingDTO ledgerMappingDTO) {
		String methodName = "createUpdateLedgerMapping()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			// Call the service and get the response
			Map<String, Object> ledgerMappingVO = businessService.createUpdateLedgerMapping(ledgerMappingDTO);

			// Set response map values
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, ledgerMappingVO.get("message"));
			responseObjectsMap.put("ledgerMappingVOList", ledgerMappingVO.get("ledgerMappingVOList")); // Ensure correct
																										// key

			// Create success response
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getCOAForLedgerMapping")
	public ResponseEntity<ResponseDTO> getCOAForLedgerMapping(@RequestParam Long orgId) {
		String methodName = "getCOAForLedgerMapping()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> ledgerMappingVO = new ArrayList<Map<String, Object>>();
		try {
			ledgerMappingVO = businessService.getCOAForLedgerMapping(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"FullGridForLedgerMapping information get successfully");
			responseObjectsMap.put("ledgerMappingVO", ledgerMappingVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"FullGridForLedgerMapping information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getCOAMap")
	public ResponseEntity<ResponseDTO> getCOAMap(@RequestParam Long orgId) {
		String methodName = "getCOAMap()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> COA = null;
		try {
			// Call the method to get the ledger map (i.e., the required group data)
			COA = businessService.getLedgerMap(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		// If no errors, create the successful response
		if (errorMsg == null) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "COA information retrieved successfully for CCoa");
			responseObjectsMap.put("COA", COA);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			// If there was an error, create an error response
			responseDTO = createServiceResponseError(responseObjectsMap, "COA information retrieval failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getFillGridForLedgerMapping")
	public ResponseEntity<ResponseDTO> getFillGridForLedgerMapping(@RequestParam String clientCode,
			@RequestParam Long orgId) {
		String methodName = "getFillGridForLedgerMapping()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> COA = null;
		try {
			// Call the method to get the ledger map (i.e., the required group data)
			COA = businessService.getFillGridForLedgerMapping(clientCode, orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		// If no errors, create the successful response
		if (errorMsg == null) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "COA information retrieved successfully for CCoa");
			responseObjectsMap.put("COA", COA);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			// If there was an error, create an error response
			responseDTO = createServiceResponseError(responseObjectsMap, "COA information retrieval failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getLedgerMappingbyId")
	public ResponseEntity<ResponseDTO> getLedgerMappingbyId(@RequestParam(required = false) Long id) {
		String methodName = "getLedgerMappingbyId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<LedgerMappingVO> ledgerMappingVO = null;
		try {
			ledgerMappingVO = businessService.getLedgerMappingbyId(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "LedgerMapping information get successfully By Id");
			responseObjectsMap.put("ledgerMappingVO", ledgerMappingVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "LedgerMapping information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllLedgerMapping")
	public ResponseEntity<ResponseDTO> getAllLedgerMapping(@RequestParam(required = false) Long orgId,@RequestParam(required = false) String clientCode) {
		String methodName = "getAllLedgerMapping()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<LedgerMappingVO> ledgerMappingVO = new ArrayList<LedgerMappingVO>();
		try {
			ledgerMappingVO = businessService.getAllLedgerMapping(orgId,clientCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "LedgerMapping information get successfully");
			responseObjectsMap.put("ledgerMappingVO", ledgerMappingVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "LedgerMapping information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PostMapping("/excelUploadForLedgerMapping")
	public ResponseEntity<ResponseDTO> excelUploadForLedgerMapping(@RequestParam MultipartFile[] files,
			@RequestParam(required = false) String createdBy, @RequestParam(required = false) String clientCode,
			@RequestParam Long orgId, @RequestParam String clientName) {

		int totalRows = 0;
		int successfulUploads = 0;
		String methodName = "excelUploadForLedgerMapping()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

		ResponseDTO responseDTO;
		Map<String, Object> responseObjectsMap = new HashMap<>();

		try {
			// Call the service method and get the result
			ExcelUploadResultDTO uploadResult = businessService.excelUploadForLedgerMapping(files, createdBy,
					clientCode, orgId, clientName);

			totalRows = businessService.getTotalRows(); // Get total rows processed
			successfulUploads = businessService.getSuccessfulUploads();
			responseObjectsMap.put("statusFlag", "Ok");
			responseObjectsMap.put("status", true);
			responseObjectsMap.put("totalRows", totalRows);
			responseObjectsMap.put("successfulUploads", successfulUploads);
			responseObjectsMap.put("message", "Excel Upload For Ledger Mapping successful"); 
//			// Populate success response
//			responseObjectsMap.put("statusFlag", "Ok");
//			responseObjectsMap.put("status", true);
//			responseObjectsMap.put("uploadResult", uploadResult);
			responseDTO = createServiceResponse(responseObjectsMap);

		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(CommonConstant.EXCEPTION, methodName, e);
			responseObjectsMap.put("statusFlag", "Error");
			responseObjectsMap.put("status", false);
			responseObjectsMap.put("errorMessage", errorMsg);

			responseDTO = createServiceResponseError(responseObjectsMap, "Excel Upload For Ledger Mapping Failed",
					e.getMessage());
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	//SERVICE LEVEL
	
	@PutMapping("/createUpdateServiceLevel")
	public ResponseEntity<ResponseDTO> createUpdateServiceLevel(@RequestBody ServiceLevelDTO serviceLevelDTO) {
		String methodName = "createUpdateServiceLevel()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			// Call the service and get the response
			Map<String, Object> serviceLevelVO = businessService.createUpdateServiceLevel(serviceLevelDTO);

			// Set response map values
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, serviceLevelVO.get("message"));
			responseObjectsMap.put("ledgerMappingVOList", serviceLevelVO.get("serviceLevelVO")); // Ensure correct
																										// key

			// Create success response
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getServiceLevelbyId")
	public ResponseEntity<ResponseDTO> getServiceLevelbyId(@RequestParam(required = false) Long id) {
		String methodName = "getServiceLevelbyId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<ServiceLevelVO> serviceLevelVO = null;
		try {
			serviceLevelVO = businessService.getServiceLevelbyId(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ServiceLevel information get successfully By Id");
			responseObjectsMap.put("serviceLevelVO", serviceLevelVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "ServiceLevel information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAllServiceLevel")
	public ResponseEntity<ResponseDTO> getAllServiceLevel(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllServiceLevel()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ServiceLevelVO> serviceLevelVO = new ArrayList<ServiceLevelVO>();
		try {
			serviceLevelVO = businessService.getAllServiceLevel(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ServiceLevel information get successfully");
			responseObjectsMap.put("serviceLevelVO", serviceLevelVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "ServiceLevel information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
}
