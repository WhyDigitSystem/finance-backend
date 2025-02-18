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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.basesetup.common.CommonConstant;
import com.base.basesetup.common.UserConstants;
import com.base.basesetup.dto.RCostInvoiceGnaDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.RCostInvoiceGnaVO;
import com.base.basesetup.entity.TdsMasterVO;
import com.base.basesetup.service.RCostInvoiceGnaService;

@CrossOrigin
@RestController
@RequestMapping("/api/rCostInvoiceGna")
public class RCostInvoiceGnaController extends BaseController{

	public static final Logger LOGGER = LoggerFactory.getLogger(RCostInvoiceGnaController.class);

	@Autowired
	RCostInvoiceGnaService rCostInvoiceGnaService;
	
	@GetMapping("/getAllRCostInvoiceGnaByOrgId")
	public ResponseEntity<ResponseDTO> getAllRCostInvoiceGnaByOrgId(@RequestParam Long orgId) {
		String methodName = "getAllRCostInvoiceGnaByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<RCostInvoiceGnaVO> rCostInvoiceGnaVO = new ArrayList<>();
		try {
			rCostInvoiceGnaVO = rCostInvoiceGnaService.getAllRCostInvoiceGnaByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "RCostInvoiceGna information get successfully By OrgId");
			responseObjectsMap.put("rCostInvoiceGnaVO", rCostInvoiceGnaVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"RCostInvoiceGna information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	
	@GetMapping("/getAllRCostInvoiceGnaById")
	public ResponseEntity<ResponseDTO> getAllRCostInvoiceGnaById(@RequestParam Long id) {
		String methodName = "getAllRCostInvoiceGnaById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		RCostInvoiceGnaVO rCostInvoiceGnaVO = new RCostInvoiceGnaVO() ;
		try {
			rCostInvoiceGnaVO = rCostInvoiceGnaService.getAllRCostInvoiceGnaById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "RCostInvoiceGna information get successfully By id");
			responseObjectsMap.put("rCostInvoiceGnaVO", rCostInvoiceGnaVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"RCostInvoiceGna information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getRCostInvoiceGnaDocId")
	public ResponseEntity<ResponseDTO> getRCostInvoiceGnaDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getRCostInvoiceGnaDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = rCostInvoiceGnaService.getRCostInvoiceGnaDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"RCostInvoiceGnaDocId information retrieved successfully");
			responseObjectsMap.put("taxInvoiceDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve RCostInvoiceGna Docid information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	

	
	@PutMapping("/updateCreateRCostInvoiceGna")
	public ResponseEntity<ResponseDTO> updateCreateRCostInvoiceGna(
			@RequestBody RCostInvoiceGnaDTO rCostInvoiceGnaDTO) {
		String methodName = "updateCreateRCostInvoiceGna()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> rCostInvoiceGnaVO = rCostInvoiceGnaService
					.updateCreateRCostInvoiceGna(rCostInvoiceGnaDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, rCostInvoiceGnaVO.get("message"));
			responseObjectsMap.put("rCostInvoiceGnaVO", rCostInvoiceGnaVO.get("rCostInvoiceGnaVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAllVendorFromPartyMaster")
	public ResponseEntity<ResponseDTO> getAllVendorFromPartyMaster(@RequestParam Long orgId,
			@RequestParam String partyType) {
		String methodName = "getAllVendorFromPartyMaster()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<PartyMasterVO> partyMasterVO = new ArrayList<>();
		try {
			partyMasterVO = rCostInvoiceGnaService.getAllVendorFromPartyMaster(orgId, partyType);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Party information get successfully ByOrgId");
			responseObjectsMap.put("partyMasterVO", partyMasterVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Party information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}


	@GetMapping("/getCurrencyAndExrateDetails")
	public ResponseEntity<ResponseDTO> getCurrencyAndExrateDetails(@RequestParam Long orgId) {
		String methodName = "getCurrencyAndExrateDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = rCostInvoiceGnaService.getCurrencyAndExrates(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Currency Details retrieved successfully");
			responseObjectsMap.put("currencyVO", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve Currency Details",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getChargeLedgerFromGroup")
	public ResponseEntity<ResponseDTO> getChargeLedgerFromGroup(@RequestParam Long orgId) {
		String methodName = "getChargeLedgerFromGroup()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = rCostInvoiceGnaService.getChargeLedgerFromGroup(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ChargeLedger  retrieved successfully");
			responseObjectsMap.put("chargeCodeVO", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve ChargeLedger ", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
  
	
	@GetMapping("/getSectionNameFromTDSMaster")
	public ResponseEntity<ResponseDTO> getSectionNameFromTDSMaster(@RequestParam Long orgId,
			@RequestParam String section) {
		String methodName = "getSectionNameFromTDSMaster()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> tdsMasterVO = new ArrayList<>();
		try {
			tdsMasterVO = rCostInvoiceGnaService.getSectionNameFromTDSMaster(orgId, section);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Section information get successfully ByOrgId");
			responseObjectsMap.put("tdsMasterVO", tdsMasterVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Section information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	@GetMapping("/getGstType")
	public ResponseEntity<ResponseDTO> getGstType(@RequestParam Long orgId, @RequestParam String branchCode,
			@RequestParam String stateCode) {
		String methodName = "getGstType()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> gstTypeDetails = new ArrayList<>();
		try {
			gstTypeDetails = rCostInvoiceGnaService.getGstTypeDetails(orgId, branchCode, stateCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Gst Type information get successfully ByOrgId");
			responseObjectsMap.put("gstTypeDetails", gstTypeDetails);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Gst Type information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getStateFromPartyMaster")
	public ResponseEntity<ResponseDTO> getStateFromPartyMaster(@RequestParam Long orgId,String partyCode) {
		String methodName = "getChargeLedgerFromGroup()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = rCostInvoiceGnaService.getStateFromPartyMaster(orgId,partyCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "StateDetails  retrieved successfully");
			responseObjectsMap.put("partyMasterVO", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "StateDetails Failed to retrieve ChargeLedger ", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
  

}
