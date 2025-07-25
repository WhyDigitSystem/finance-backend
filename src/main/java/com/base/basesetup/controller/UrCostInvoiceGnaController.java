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
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.dto.UrCostInvoiceGnaDTO;
import com.base.basesetup.entity.IrnCreditNoteVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.UrCostInvoiceGnaVO;
import com.base.basesetup.service.UrCostInvoiceGnaService;

@RestController
@RequestMapping("/api/UrCostInvoiceGna")
public class UrCostInvoiceGnaController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(UrCostInvoiceGnaController.class);

	@Autowired
	UrCostInvoiceGnaService urCostInvoiceGnaService;

	@GetMapping("/getAllUrCostInvoiceGnaByOrgId")
	public ResponseEntity<ResponseDTO> getAllUrCostInvoiceGnaByOrgId(@RequestParam Long orgId,
			@RequestParam String finYear, @RequestParam String branchCode) {
		String methodName = "getAllUrCostInvoiceGnaByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<UrCostInvoiceGnaVO> urCostInvoiceGnaVO = new ArrayList<>();
		try {
			urCostInvoiceGnaVO = urCostInvoiceGnaService.getAllUrCostInvoiceGnaByOrgId(orgId, finYear, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"UrCostInvoiceGnaVO information get successfully By OrgId");
			responseObjectsMap.put("urCostInvoiceGnaVO", urCostInvoiceGnaVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"UrCostInvoiceGnaVO information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getUrCostInvoiceGnaById")
	public ResponseEntity<ResponseDTO> getUrCostInvoiceGnaById(@RequestParam Long id) {
		String methodName = "getUrCostInvoiceGnaById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<UrCostInvoiceGnaVO> urCostInvoiceGnaVO =   new ArrayList<>();
		try {
			urCostInvoiceGnaVO = urCostInvoiceGnaService.getUrCostInvoiceGnaById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"UrCostInvoiceGnaVO information get successfully By id");
			responseObjectsMap.put("urCostInvoiceGnaVO", urCostInvoiceGnaVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"UrCostInvoiceGnaVO information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateUrCostInvoiceGna")
	public ResponseEntity<ResponseDTO> updateCreateUrCostInvoiceGna(
			@RequestBody UrCostInvoiceGnaDTO urCostInvoiceGnaDTO) {
		String methodName = "updateCreateUrCostInvoiceGna()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> urCostInvoiceGnaVO = urCostInvoiceGnaService
					.updateCreateUrCostInvoiceGna(urCostInvoiceGnaDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, urCostInvoiceGnaVO.get("message"));
			responseObjectsMap.put("urCostInvoiceGnaVO", urCostInvoiceGnaVO.get("urCostInvoiceGnaVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getUrCostInvoiceGnaDocId")
	public ResponseEntity<ResponseDTO> getUrCostInvoiceGnaDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getUrCostInvoiceGnaDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = urCostInvoiceGnaService.getUrCostInvoiceGnaDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"UrCostInvoiceGnaDocId information retrieved successfully");
			responseObjectsMap.put("urCostInvoiceGnaDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve UrCostInvoiceGnaDocId information", errorMsg);
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
			partyMasterVO = urCostInvoiceGnaService.getAllVendorFromPartyMaster(orgId, partyType);
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
	
	
	

	@GetMapping("/getVendorAddressFromPartyMaster")
	public ResponseEntity<ResponseDTO> getVendorAddressFromPartyMaster(@RequestParam Long orgId,
			@RequestParam String supplierCode) {
		String methodName = "getVendorAddressFromPartyMaster()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = urCostInvoiceGnaService.getVendorAddressFromPartyMaster(orgId,supplierCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Address  retrieved successfully");
			responseObjectsMap.put("partyMasterVO", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "StateDetails Failed to retrieve Address ", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getCurrencyAndExrateFromParty")
	public ResponseEntity<ResponseDTO> getCurrencyAndExrateFromParty(@RequestParam Long orgId,@RequestParam String supplierCode) {
		String methodName = "getCurrencyAndExrateFromParty()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = urCostInvoiceGnaService.getCurrencyAndExrateFromParty(orgId, supplierCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Currency retrieved successfully");
			responseObjectsMap.put("exrateVO", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve Currency", errorMsg);
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
			mapp = urCostInvoiceGnaService.getChargeLedgerFromGroup(orgId);
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
	
	@GetMapping("/getSectionNameFromMaster")
	public ResponseEntity<ResponseDTO> getSectionNameFromMaster(@RequestParam Long orgId,
			@RequestParam String section) {
		String methodName = "getSectionNameFromMaster()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> tdsMasterVO = new ArrayList<>();
		try {
			tdsMasterVO = urCostInvoiceGnaService.getSectionNameFromMaster(orgId, section);
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
	
	
	
	@PutMapping("/approveUrCostInvoiceGnaVO")
	public ResponseEntity<ResponseDTO> approveUrCostInvoiceGnaVO(@RequestParam Long orgId,@RequestParam Long id,@RequestParam String docId,@RequestParam String action,@RequestParam String actionBy) {
		String methodName = "approveTaxInvoice()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			UrCostInvoiceGnaVO urCostInvoiceGnaVO = urCostInvoiceGnaService.approveUrCostInvoiceGnaVO(orgId, id, docId, action, actionBy);
			responseObjectsMap.put("urCostInvoiceGnaVO", urCostInvoiceGnaVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getUrCostInvoiceByDocIdandScreenCode")
	public ResponseEntity<ResponseDTO> getUrCostInvoiceByDocIdandScreenCode(@RequestParam String ScreenCode , @RequestParam String docId) {
		String methodName = "getUrCostInvoiceByDocIdandScreenCode()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		UrCostInvoiceGnaVO urCostInvoiceGnaVO = new UrCostInvoiceGnaVO();
		try {
			urCostInvoiceGnaVO = urCostInvoiceGnaService.getUrCostInvoiceByDocIdandScreenCode( ScreenCode, docId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "UR Cost Invoice information get successfully By docid");
			responseObjectsMap.put("urCostInvoiceGnaVO", urCostInvoiceGnaVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"UR Cost Invoice information receive failed By docid", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	@GetMapping("/getChargeAccountFromChargeLedger")
	public ResponseEntity<ResponseDTO> getChargeAccountFromChargeLedger(@RequestParam Long orgId,@RequestParam String chargeLedger) {
		String methodName = "getChargeAccountFromChargeLedger()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = urCostInvoiceGnaService.getChargeAccountFromChargeLedger(orgId,chargeLedger);
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
	
	
}
