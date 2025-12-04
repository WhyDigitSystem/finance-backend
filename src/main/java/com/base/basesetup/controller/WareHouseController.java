package com.base.basesetup.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.base.basesetup.dto.StockBranchDTO;
import com.base.basesetup.dto.WarehouseDTO;
import com.base.basesetup.entity.CityVO;
import com.base.basesetup.entity.StateVO;
import com.base.basesetup.entity.StockBranchVO;
import com.base.basesetup.entity.WarehouseVO;
import com.base.basesetup.service.WareHouseService;

@CrossOrigin
@RestController
@RequestMapping("/api/warehouser")
public class WareHouseController extends BaseController {

	@Autowired
	WareHouseService wareHouseService;

	@GetMapping("/getAllStockBranchByOrgId")
	public ResponseEntity<ResponseDTO> getAllStockBranchByOrgId(@RequestParam Long orgId) {
		String methodName = "getAllStockBranchByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<StockBranchVO> stockBranchVO = new ArrayList<>();
		try {
			stockBranchVO = wareHouseService.getAllStockBranchByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "StockBranch information get successfully");
			responseObjectsMap.put("stockBranchVO", stockBranchVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "StockBranch information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getStockBranchById")
	public ResponseEntity<ResponseDTO> getStockBranchById(@RequestParam Long id) {
		String methodName = "getStockBranchById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		StockBranchVO stockBranchVO = null;
		try {
			stockBranchVO = wareHouseService.getStockBranchById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "StockBranch found by ID");
			responseObjectsMap.put("stockBranchVO", stockBranchVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "StockBranch not found for ID: " + id;
			responseDTO = createServiceResponseError(responseObjectsMap, "StockBranch not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/createupdateStockBranch")
	public ResponseEntity<ResponseDTO> createupdateStockBranch(@RequestBody StockBranchDTO stockBranchDTO) {
		String methodName = "createupdateStockBranch()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> stockBranchVO = wareHouseService.createupdateStockBranch(stockBranchDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, stockBranchVO.get("message"));
			responseObjectsMap.put("stockBranchVO", stockBranchVO.get("stockBranchVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// WareHouse

	@GetMapping("/getAllWarehouseByOrgId")
	public ResponseEntity<ResponseDTO> getAllWarehouseByOrgId(@RequestParam Long orgId) {
		String methodName = "getAllWarehouseByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<WarehouseVO> warehouseVO = new ArrayList<>();
		try {
			warehouseVO = wareHouseService.getAllWarehouseByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Warehouse information get successfully");
			responseObjectsMap.put("warehouseVO", warehouseVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Warehouse information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getWarehouseById")
	public ResponseEntity<ResponseDTO> getWarehouseById(@RequestParam Long id) {
		String methodName = "getWarehouseById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		WarehouseVO warehouseVO = null;
		try {
			warehouseVO = wareHouseService.getWarehouseById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "wareHouse found by ID");
			responseObjectsMap.put("warehouseVO", warehouseVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "Warehouse not found for ID: " + id;
			responseDTO = createServiceResponseError(responseObjectsMap, "Warehouse not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/createupdateWarehouse")
	public ResponseEntity<ResponseDTO> createupdateWarehouse(@RequestBody WarehouseDTO warehouseDTO) {
		String methodName = "createupdateWarehouse()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> warehouseVO = wareHouseService.createupdateWarehouse(warehouseDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, warehouseVO.get("message"));
			responseObjectsMap.put("warehouseVO", warehouseVO.get("warehouseVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getStockBranchName")
	public ResponseEntity<ResponseDTO> getStockBranchName(@RequestParam Long orgId) {
		String methodName = "getStockBranchName()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<StockBranchVO> taxInvoiceVO = new ArrayList<>();
		try {	
			taxInvoiceVO = wareHouseService.getStockBranchName(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "StockBranch information get successfully ByOrgId");
			responseObjectsMap.put("taxInvoiceVO", taxInvoiceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "StockBranch information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	
	@PostMapping("/excelUploadForWarehouse")
	public ResponseEntity<ResponseDTO> excelUploadForWarehouse(@RequestParam MultipartFile[] files,
			@RequestParam(required = false) String createdBy, @RequestParam Long orgId) {
		String methodName = "excelUploadForWarehouse()";
		int totalRows = 0;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		int successfulUploads = 0;
		ResponseDTO responseDTO = null;
		try {

			wareHouseService.excelUploadForWarehouse(files, createdBy, orgId);

			totalRows = wareHouseService.getTotalRows(); 
			successfulUploads = wareHouseService.getSuccessfulUploads(); 
			responseObjectsMap.put("statusFlag", "Ok");
			responseObjectsMap.put("status", true);
			responseObjectsMap.put("totalRows", totalRows);
			responseObjectsMap.put("successfulUploads", successfulUploads);
			responseObjectsMap.put("message", "Excel Upload For Warehouse successful"); 
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(CommonConstant.EXCEPTION, methodName, e);
			responseObjectsMap.put("statusFlag", "Error");
			responseObjectsMap.put("status", false);
			responseObjectsMap.put("errorMessage", errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, "Excel Upload For Warehouse Failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllCitiesByStateAndCountry")
	public ResponseEntity<ResponseDTO> getAllCitiesByStateAndCountry(@RequestParam String state,@RequestParam String country,@RequestParam Long orgId) {
		String methodName = "getAllCitiesByStateAndCountry()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CityVO> cityVO = new ArrayList<>();
		try {
			cityVO = wareHouseService.getAllCitiesByStateAndCountry(state,country,orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "city information get successfully");
			responseObjectsMap.put("cityVO", cityVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "city information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("getAllStatesByCountry")
	public ResponseEntity<ResponseDTO> getAllStatesByCountry(@RequestParam String country,@RequestParam Long orgId) {
		String methodName = "getAllStatesByCountry()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<StateVO> stateVO = new ArrayList<>();
		try {
			stateVO = wareHouseService.getAllStatesByCountry(country,orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "state information get successfully");
			responseObjectsMap.put("stateVO", stateVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "states information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	//ASSET 
	

	@PostMapping("/excelUploadForAsset")
	public ResponseEntity<ResponseDTO> excelUploadForAsset(@RequestParam MultipartFile[] files,
			@RequestParam(required = false) String createdBy, @RequestParam Long orgId) {
		String methodName = "excelUploadForWarehouse()";
		int totalRows = 0;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		int successfulUploads = 0;
		ResponseDTO responseDTO = null;
		try {

			wareHouseService.excelUploadForAsset(files, createdBy, orgId);

			totalRows = wareHouseService.getTotalRows(); 
			successfulUploads = wareHouseService.getSuccessfulUploads(); 
			responseObjectsMap.put("statusFlag", "Ok");
			responseObjectsMap.put("status", true);
			responseObjectsMap.put("totalRows", totalRows);
			responseObjectsMap.put("successfulUploads", successfulUploads);
			responseObjectsMap.put("message", "Excel Upload For Warehouse successful"); 
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(CommonConstant.EXCEPTION, methodName, e);
			responseObjectsMap.put("statusFlag", "Error");
			responseObjectsMap.put("status", false);
			responseObjectsMap.put("errorMessage", errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, "Excel Upload For Warehouse Failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@GetMapping("/getAllWarehouseNames")
	public ResponseEntity<ResponseDTO> getAllWarehouseNames(@RequestParam Long orgId) {
		String methodName = "getAllWarehouseNames()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> warehouseVO = new ArrayList<>();
		try {
			warehouseVO = wareHouseService.getAllWarehouseNames(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "WarehouseNames get successfully ");
			responseObjectsMap.put("warehouseVO", warehouseVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "information receive failedBy WarehouseNames",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

}
