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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.basesetup.common.CommonConstant;
import com.base.basesetup.common.UserConstants;
import com.base.basesetup.dto.AssetCategoryDTO;
import com.base.basesetup.dto.AssetDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.AssetCategoryVO;
import com.base.basesetup.entity.AssetTypeDTO;
import com.base.basesetup.entity.AssetTypeVO;
import com.base.basesetup.service.KitControllerService;

@CrossOrigin
@RestController
@RequestMapping("/api/kitController")
public class KitController extends BaseController {
	public static final Logger LOGGER = LoggerFactory.getLogger(KitController.class);

	@Autowired
	KitControllerService kitControllerService;

	@PutMapping("/updateCreateAssetType")
	public ResponseEntity<ResponseDTO> updateCreateAssetType(@Valid @RequestBody AssetTypeDTO assetTypeDTO) {
		String methodName = "updateCreateAssetType()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> assetTypeVO = kitControllerService.updateCreateAssetType(assetTypeDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, assetTypeVO.get("message"));
			responseObjectsMap.put("assetTypeVO", assetTypeVO.get("assetTypeVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAssetTypeByOrgId")
	public ResponseEntity<ResponseDTO> getAssetTypeByOrgId(@RequestParam(required = true) Long orgid) {
		String methodName = "getAssetTypeByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<AssetTypeVO> assetTypeVO = new ArrayList<>();
		try {
			assetTypeVO = kitControllerService.getAssetTypeByOrgId(orgid);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "AssetType information get successfully By OrgId");
			responseObjectsMap.put("assetTypeVO", assetTypeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "AssetType information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAssetTypeById")
	public ResponseEntity<ResponseDTO> getAssetTypeById(@RequestParam(required = true) Long id) {
		String methodName = "getAssetTypeById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<AssetTypeVO> assetTypeVO = null;
		try {
			assetTypeVO = kitControllerService.getAssetTypeById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "AssetType information get successfully By Id");
			responseObjectsMap.put("assetTypeVO", assetTypeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "AssetType information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/updateCreateAssetCategory")
	public ResponseEntity<ResponseDTO> updateCreateAssetCategory(@Valid @RequestBody AssetCategoryDTO assetCategoryDTO) {
		String methodName = "updateCreateAssetCategory()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> assetCategoryVO = kitControllerService.updateCreateAssetCategory(assetCategoryDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, assetCategoryVO.get("message"));
			responseObjectsMap.put("assetCategoryVO", assetCategoryVO.get("assetCategoryVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAssetCategoryByOrgId")
	public ResponseEntity<ResponseDTO> getAssetCategoryByOrgId(@RequestParam(required = true) Long orgid) {
		String methodName = "getAssetCategoryByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<AssetCategoryVO> assetCategoryVO = new ArrayList<>();
		try {
			assetCategoryVO = kitControllerService.getAssetCategoryByOrgId(orgid);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "AssetCategory information get successfully By OrgId");
			responseObjectsMap.put("assetCategoryVO", assetCategoryVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "AssetCategory information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAssetCategoryById")
	public ResponseEntity<ResponseDTO> getAssetCategoryById(@RequestParam(required = true) Long id) {
		String methodName = "getAssetCategoryById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<AssetCategoryVO> assetCategoryVO = null;
		try {
			assetCategoryVO = kitControllerService.getAssetCategoryById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "AssetCategory information get successfully By Id");
			responseObjectsMap.put("assetCategoryVO", assetCategoryVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "AssetCategory information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	//ASSETS
	
	@PutMapping("/updateCreateAsset")
	public ResponseEntity<ResponseDTO> updateCreateAsset(@Valid @RequestBody AssetDTO assetDTO) {
		String methodName = "updateCreateAsset()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> assetVO = kitControllerService.updateCreateAsset(assetDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, assetVO.get("message"));
			responseObjectsMap.put("assetVO", assetVO.get("assetCategoryVO"));
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
