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
import com.base.basesetup.dto.CustomerAttachmentType;
import com.base.basesetup.dto.QrBarCodeDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.dto.SingleQrBarCodeDTO;
import com.base.basesetup.entity.QrBarCodeVO;
import com.base.basesetup.entity.SingleQrBarCodeVO;
import com.base.basesetup.service.QrBarCodeService;

@RestController
@RequestMapping("/api/qrbarcode")
public class QrBarCodeController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(QrBarCodeController.class);

	@Autowired
	QrBarCodeService qrBarCodeService;

	// QrBarCode

	@PutMapping("/createUpdateQrBarCode")
	public ResponseEntity<ResponseDTO> createUpdateQrBarCode(@RequestBody QrBarCodeDTO qrBarCodeDTO) {
		String methodName = "createUpdateQrBarCode()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> qrBarCodeVO = qrBarCodeService.createUpdateQrBarCode(qrBarCodeDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, qrBarCodeVO.get("message"));
			responseObjectsMap.put("qrBarCodeVO", qrBarCodeVO.get("qrBarCodeVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllQrBarCodeByOrgId")
	public ResponseEntity<ResponseDTO> getAllQrBarCodeByOrgId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branchCode) {
		String methodName = "getAllQrBarCodeByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<QrBarCodeVO> qrBarCodeVO = new ArrayList<>();
		try {
			qrBarCodeVO = qrBarCodeService.getAllQrBarCodeByOrgId(orgId, finYear, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "QrBarCode information get successfully ByOrgId");
			responseObjectsMap.put("qrBarCodeVO", qrBarCodeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "QrBarCode information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getQrBarCodeById")
	public ResponseEntity<ResponseDTO> getQrBarCodeById(@RequestParam Long id) {
		String methodName = "getQrBarCodeById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		QrBarCodeVO qrBarCodeVO = new QrBarCodeVO();
		try {
			qrBarCodeVO = qrBarCodeService.getQrBarCodeById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "QrBarCode get successfully By id");
			responseObjectsMap.put("qrBarCodeVO", qrBarCodeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "QrBarCode information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getQrBarCodeDocId")
	public ResponseEntity<ResponseDTO> getQrBarCodeDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getQrBarCodeDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = qrBarCodeService.getQrBarCodeDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "QrBarCodeDocId information retrieved successfully");
			responseObjectsMap.put("qrBarCodeDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve QrBarCodeDocId information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PostMapping("/ExcelUploadForQrBarCode")
	public ResponseEntity<ResponseDTO> ExcelUploadForQrBarCode(@RequestParam MultipartFile[] files,
			CustomerAttachmentType type, @RequestParam(required = false) String createdBy) {
		String methodName = "ExcelUploadForQrBarCode()";
		int totalRows = 0;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		int successfulUploads = 0;
		ResponseDTO responseDTO = null;
		try {
			// Call service method to process Excel upload
			qrBarCodeService.ExcelUploadForQrBarCode(files, type, createdBy);

			// Retrieve the counts after processing
			totalRows = qrBarCodeService.getTotalRows(); // Get total rows processed
			successfulUploads = qrBarCodeService.getSuccessfulUploads(); // Get successful uploads count
			// Construct success response
			responseObjectsMap.put("statusFlag", "Ok");
			responseObjectsMap.put("status", true);
			responseObjectsMap.put("totalRows", totalRows);
			responseObjectsMap.put("successfulUploads", successfulUploads);
			Map<String, Object> paramObjectsMap = new HashMap<>();
			paramObjectsMap.put("message", "Excel Upload For QrBarCode successful");
			responseObjectsMap.put("paramObjectsMap", paramObjectsMap);
			responseDTO = createServiceResponse(responseObjectsMap);

		} catch (Exception e) {

			String errorMsg = e.getMessage();
			LOGGER.error(CommonConstant.EXCEPTION, methodName, e);
			responseObjectsMap.put("statusFlag", "Error");
			responseObjectsMap.put("status", false);
			responseObjectsMap.put("errorMessage", errorMsg);

			responseDTO = createServiceResponseError(responseObjectsMap, "Excel Upload For QrBarCode Failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getFillGridFromQrBarExcelUpload")
	public ResponseEntity<ResponseDTO> getFillGridFromQrBarExcelUpload(@RequestParam(required = false) String entryNo) {

		String methodName = "getFillGridFromQrBarExcelUpload()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mov = new ArrayList<>();
		try {
			mov = qrBarCodeService.getFillGridFromQrBarExcelUpload(entryNo);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"Al FillGrid from QrBarCode information retrieved successfully");
			responseObjectsMap.put("qrBarCodeVO", mov);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve FillGrid from QrBarCode information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// SingleQrBarCode

	@GetMapping("/getAllSingleQrBarCode")
	public ResponseEntity<ResponseDTO> getAllSingleQrBarCode(Long orgId) {
		String methodName = "getAllSingleQrBarCode()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<SingleQrBarCodeVO> singleQrBarCodeVO = new ArrayList<>();
		try {
			singleQrBarCodeVO = qrBarCodeService.getAllSingleQrBarCode(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "SingleQrBarCode information get successfully");
			responseObjectsMap.put("SingleQrBarCodeVO", singleQrBarCodeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "SingleQrBarCode information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getSingleQrBarCodeById")
	public ResponseEntity<ResponseDTO> getSingleQrBarCodeById(@RequestParam(required = false) Long id) {
		String methodName = "getSingleQrBarCodeById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		SingleQrBarCodeVO singleQrBarCodeVO = new SingleQrBarCodeVO();

		try {
			singleQrBarCodeVO = qrBarCodeService.getSingleQrBarCodeById(id);
			if (singleQrBarCodeVO != null) {
				responseObjectsMap.put("SingleQrBarCodeVO", singleQrBarCodeVO);
				responseDTO = createServiceResponseMsg(responseObjectsMap,
						" SingleQrBarCode information retrieved successfully.");
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, " SingleQrBarCode information not found.",
						errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap,
					" SingleQrBarCode information retrieval failed.", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/createUpdateSingleQrBarCode")
	public ResponseEntity<ResponseDTO> createUpdateSingleQrBarCode(@RequestBody SingleQrBarCodeDTO singleQrBarCodeDTO) {
		String methodName = "createUpdateSingleQrBarCode()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> singleQrBarCodeVO = qrBarCodeService.createUpdateSingleQrBarCode(singleQrBarCodeDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, singleQrBarCodeVO.get("message"));
			responseObjectsMap.put("singleQrBarCodeVO", singleQrBarCodeVO.get("singleQrBarCodeVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
}
