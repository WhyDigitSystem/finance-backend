package com.base.basesetup.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

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
import com.base.basesetup.dto.FirstDataDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.FirstDataVO;
import com.base.basesetup.service.ExcelFileUploadService;

@CrossOrigin
@RestController
@RequestMapping("/api/excelfileupload")
public class ExcelFileUploadController extends BaseController{
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ExcelFileUploadController.class);

	@Autowired
	ExcelFileUploadService excelFileUploadService;
	
	@PostMapping("/excelUploadForSample")
	public ResponseEntity<ResponseDTO> ExcelUploadForSample(@RequestParam MultipartFile[] files,@RequestParam(required = false) String createdBy) {
		String methodName = "ExcelUploadForSample()";
		int totalRows = 0;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		int successfulUploads = 0;
		ResponseDTO responseDTO = null;
		try {
			// Call service method to process Excel upload
			excelFileUploadService.ExcelUploadForSample(files,createdBy);

			// Retrieve the counts after processing
			totalRows = excelFileUploadService.getTotalRows(); // Get total rows processed
			successfulUploads = excelFileUploadService.getSuccessfulUploads(); // Get successful uploads count
			responseObjectsMap.put("statusFlag", "Ok");
	        responseObjectsMap.put("status", true);
	        responseObjectsMap.put("totalRows", totalRows);
	        responseObjectsMap.put("successfulUploads", successfulUploads);
	        responseObjectsMap.put("message", "Excel Upload For FirstData successful"); // Directly include the message here
	        responseDTO = createServiceResponse(responseObjectsMap);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(CommonConstant.EXCEPTION, methodName, e);
	        responseObjectsMap.put("statusFlag", "Error");
	        responseObjectsMap.put("status", false);
	        responseObjectsMap.put("errorMessage", errorMsg);

	        responseDTO = createServiceResponseError(responseObjectsMap, "Excel Upload For BuyerOrder Failed", errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@GetMapping("/getAllFirstData")
	public ResponseEntity<ResponseDTO> getAllFirstData() {
		String methodName = "getAllFirstData()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<FirstDataVO> firstDataVOs = new ArrayList<>();
		try {
			firstDataVOs = excelFileUploadService.getAllFirstData();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "First Data  information get successfully By OrgId");
			responseObjectsMap.put("firstDataVO", firstDataVOs);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"FirstDataVO information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	
	@PutMapping("/updateCreateFirstData")
	public ResponseEntity<ResponseDTO> updateCreateFirstData(@Valid @RequestBody FirstDataDTO firstDataDTO) {
		String methodName = "updateCreateCostInvoice()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
	        Map<String, Object> firstDataVO = excelFileUploadService.updateCreateFirstData(firstDataDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, firstDataVO.get("message"));
	        responseObjectsMap.put("firstDataVO", firstDataVO.get("firstDataVO")); // Corrected key
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}

	
	@GetMapping("/getFirstDataById")
	public ResponseEntity<ResponseDTO> getFirstDataById(@RequestParam(required =true) Long id) {
		String methodName = "getFirstDataById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<FirstDataVO> firstDataVOs =null;
		try {
			firstDataVOs = excelFileUploadService.getFirstDataById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "First Data information get successfully By Id");
			responseObjectsMap.put("firstDataVO", firstDataVOs);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"FirstDataVO information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	
}
