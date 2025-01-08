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
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.dto.SampleExcelUploadDTO;
import com.base.basesetup.entity.SampleExcelUploadVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.service.SampleExcelUploadService;

@CrossOrigin
@RestController
@RequestMapping("/api/sampleExcel")
public class SampleExcelUploadController extends BaseController {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);
	
  @Autowired
  SampleExcelUploadService sampleExcelUploadService;
  
  
  @PutMapping("/updateCreateSampleExcelUpload")
	public ResponseEntity<ResponseDTO> updateCreateSampleExcelUpload(@RequestBody SampleExcelUploadDTO sampleExcelUploadDTO) {
		String methodName = "updateCreateSampleExcelUpload()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> sampleExcelUploadVO = sampleExcelUploadService.updateCreateSampleExcelUpload(sampleExcelUploadDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, sampleExcelUploadVO.get("message"));
			responseObjectsMap.put("sampleExcelUploadVO", sampleExcelUploadVO.get("sampleExcelUploadVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
  
  
  @GetMapping("/getAllSampleExcelById")
	public ResponseEntity<ResponseDTO> getAllSampleExcelById(@RequestParam Long id) {
		String methodName = "getAllSampleExcelById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<SampleExcelUploadVO> sampleExcelUploadVO = new ArrayList<>();
		try {
			sampleExcelUploadVO = sampleExcelUploadService.getAllSampleExcelById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "SampleExcel information get successfully By id");
			responseObjectsMap.put("sampleExcelUploadVO", sampleExcelUploadVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "SampleExcel information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
  
  @GetMapping("/getAllSampleExcelUpload")
 	public ResponseEntity<ResponseDTO> getAllSampleExcelUpload() {
 		String methodName = "getAllSampleExcelUpload()";
 		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
 		String errorMsg = null;
 		Map<String, Object> responseObjectsMap = new HashMap<>();
 		ResponseDTO responseDTO = null;
 		List<SampleExcelUploadVO> sampleExcelUploadVO = new ArrayList<>();
 		try {
 			sampleExcelUploadVO = sampleExcelUploadService.getAllSampleExcelUpload();
 		} catch (Exception e) {
 			errorMsg = e.getMessage();
 			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
 		}
 		if (StringUtils.isBlank(errorMsg)) {
 			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "SampleExcel information get successfully");
 			responseObjectsMap.put("sampleExcelUploadVO", sampleExcelUploadVO);
 			responseDTO = createServiceResponse(responseObjectsMap);
 		} else {
 			responseDTO = createServiceResponseError(responseObjectsMap, "SampleExcel information receive failed",
 					errorMsg);
 		}
 		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
 		return ResponseEntity.ok().body(responseDTO);
 	}
  
  @PostMapping("/ExcelUploadForSample")
	public ResponseEntity<ResponseDTO> ExcelUploadForSample(@RequestParam MultipartFile[] files) {
		String methodName = "ExcelUploadForSample()";
		int totalRows = 0;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		int successfulUploads = 0;
		ResponseDTO responseDTO = null;
		try {
			// Call service method to process Excel upload
			sampleExcelUploadService.ExcelUploadForSample(files);

			// Retrieve the counts after processing
			totalRows = sampleExcelUploadService.getTotalRows(); // Get total rows processed
			successfulUploads = sampleExcelUploadService.getSuccessfulUploads(); // Get successful uploads count
			// Construct success response
			responseObjectsMap.put("statusFlag", "Ok");
			responseObjectsMap.put("status", true);
			responseObjectsMap.put("totalRows", totalRows);
			responseObjectsMap.put("successfulUploads", successfulUploads);
			Map<String, Object> paramObjectsMap = new HashMap<>();
			paramObjectsMap.put("message", "Excel Upload  successful");
			responseObjectsMap.put("paramObjectsMap", paramObjectsMap);
			responseDTO = createServiceResponse(responseObjectsMap);

		} catch (Exception e) {

			String errorMsg = e.getMessage();
			LOGGER.error(CommonConstant.EXCEPTION, methodName, e);
			responseObjectsMap.put("statusFlag", "Error");
			responseObjectsMap.put("status", false);
			responseObjectsMap.put("errorMessage", errorMsg);

			responseDTO = createServiceResponseError(responseObjectsMap, "Excel Upload For Sample", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

}
