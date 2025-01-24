package com.base.basesetup.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.base.basesetup.dto.CostDebitNoteDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.CostDebitNoteVO;
import com.base.basesetup.service.CostDebitNoteService;

@CrossOrigin
@RestController
@RequestMapping("/api/costdebitnote")
public class CostDebitNoteController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(CostDebitNoteController.class);

	@Autowired
	CostDebitNoteService costDebitNoteService;

	@PutMapping("/updateCreateCostDebitNote")
	public ResponseEntity<ResponseDTO> updateCreateCostDebitNote(
			@Valid @RequestBody CostDebitNoteDTO CostDebitNoteDTO) {
		String methodName = "updateCreateCostDebitNote()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			Map<String, Object> costDebitNote = costDebitNoteService.updateCreateCostDebitNote(CostDebitNoteDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, costDebitNote.get("message"));
			responseObjectsMap.put("costDebitNote", costDebitNote.get("costDebitNoteVO")); // Corrected key
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getCostDebitNoteByOrgId")
	public ResponseEntity<ResponseDTO> getCostDebitNoteByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getCostDebitNoteByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CostDebitNoteVO> costDebitNoteVOs = new ArrayList<>();
		try {
			costDebitNoteVOs = costDebitNoteService.getCostDebitNoteByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"Cost DebitNote information get successfully By OrgId");
			responseObjectsMap.put("costDebitNoteVOs", costDebitNoteVOs);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Cost DebitNote information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getCostDebitNoteById")
	public ResponseEntity<ResponseDTO> getCostDebitNoteById(@RequestParam(required = false) Long id) {
		String methodName = "getCostDebitNoteById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CostDebitNoteVO> costDebitNoteVOs = new ArrayList<>();
		try {
			costDebitNoteVOs = costDebitNoteService.getCostDebitNoteById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Cost DebitNote information get successfully By Id");
			responseObjectsMap.put("costDebitNoteVOs", costDebitNoteVOs);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Cost DebitNote information receive failed By Id", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getActiveCostDebitNote")
	public ResponseEntity<ResponseDTO> getActiveCostDebitNote() {
		String methodName = "getActiveCostDebitNote()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CostDebitNoteVO> costDebitNoteVOs = new ArrayList<>();
		try {
			costDebitNoteVOs = costDebitNoteService.getActiveCostDebitNote();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					" Active Cost DebitNote information get successfully");
			responseObjectsMap.put("costDebitNoteVOs", costDebitNoteVOs);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Active Cost DebitNote information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllCostDebitNoteByDocId")
	public ResponseEntity<ResponseDTO> getAllCostDebitNoteByDocId(@RequestParam Long orgId,
			@RequestParam String docId) {
		String methodName = "getAllCostDebitNoteByDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		CostDebitNoteVO costDebitNoteVO = new CostDebitNoteVO();
		try {
			costDebitNoteVO = costDebitNoteService.getAllCostDebitNoteByDocId(orgId, docId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Cost Debit Note get successfully By docid");
			responseObjectsMap.put("costDebitNoteVO", costDebitNoteVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Cost Debit Note information receive failed By docid", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getCostDebitNoteDocId")
	public ResponseEntity<ResponseDTO> getCostDebitNoteDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getCostDebitNoteDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = costDebitNoteService.getCostDebitNoteDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Cost Debit Note information retrieved successfully");
			responseObjectsMap.put("costDebitNoteDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve Cost Debit Note Docid information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/partyDetailsForCostDebitNote")
	public ResponseEntity<ResponseDTO> partyDetailsForCostDebitNote(@RequestParam Long orgId,
			@RequestParam String branch, @RequestParam String finYear) {
		String methodName = "partyDetailsForCostDebitNote()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> partyMaster = new ArrayList<>();
		try {
			partyMaster = costDebitNoteService.partyDetailsForCostDebitNote(orgId, branch, finYear);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Party Master information get successfully");
			responseObjectsMap.put("partyMaster", partyMaster);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Party Master information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/chargeTypeDetailsForCostDebitNote")
	public ResponseEntity<ResponseDTO> chargeTypeDetailsForCostDebitNote(@RequestParam Long orgId) {
		String methodName = "chargeTypeDetailsForCostDebitNote()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> chargeType = new ArrayList<>();
		try {
			chargeType = costDebitNoteService.chargeTypeDetailsForCostDebitNote(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "charge Type Details information get successfully");
			responseObjectsMap.put("chargeType", chargeType);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"charge Type Details information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllDocIdForCostInvoice")
	public ResponseEntity<ResponseDTO> getAllDocIdForCostInvoice(@RequestParam Long orgId) {
		String methodName = "getAllDocIdForCostInvoice()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> docId = new ArrayList<>();
		try {
			docId = costDebitNoteService.getAllDocIdForCostInvoice(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"All DocId For CostInvoice information get successfully");
			responseObjectsMap.put("costInvoiceDocId", docId);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"All DocId For CostInvoice Details information receive failed", errorMsg);
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
			mapp = costDebitNoteService.getCurrencyAndExrates(orgId);
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

	@GetMapping("/getOrginBillNoByParty")
	public ResponseEntity<ResponseDTO> getOrginBillNoByParty(@RequestParam Long orgId, @RequestParam String party,
			@RequestParam String branchCode) {
		String methodName = "getOrginBillNoByParty()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CostDebitNoteVO> costDebitNoteVO = new ArrayList<>();
		try {
			costDebitNoteVO = costDebitNoteService.getOrginBillNoByParty(orgId, party, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "OrginBillNo information get successfully By OrgId");
			responseObjectsMap.put("costDebitNoteVO", costDebitNoteVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"OrginBillNo information receive failed  By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/partyTypeForCostDebitNote")
	public ResponseEntity<ResponseDTO> partyTypeForCostDebitNote(@RequestParam Long orgId, @RequestParam String branch,
			@RequestParam String finYear) {
		String methodName = "partyTypeForCostDebitNote()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> partyMaster = new ArrayList<>();
		try {
			partyMaster = costDebitNoteService.partyTypeForCostDebitNote(orgId, branch, finYear);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"PartyType from Party Master information get successfully");
			responseObjectsMap.put("partyMaster", partyMaster);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"PartyType from Party Master information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/approveCostDebitNote")
	public ResponseEntity<ResponseDTO> approveCostDebitNote(@RequestParam Long orgId, @RequestParam Long id,
			@RequestParam String docId, @RequestParam String action, @RequestParam String actionBy) {
		String methodName = "approveCostDebitNote()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			CostDebitNoteVO costDebitNoteVO = costDebitNoteService.approveCostDebitNote(orgId, id, docId, action,
					actionBy);
			responseObjectsMap.put("costDebitNoteVO", costDebitNoteVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getInterAndIntraDetailsForCostInvoice")
	public ResponseEntity<ResponseDTO> getInterAndIntraDetailsForCostInvoice(@RequestParam Long orgId,
			@RequestParam String gstType, @RequestParam List<String> gstPercent) {
		String methodName = "getInterAndIntraDetailsForCostInvoice()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> charge = new ArrayList<>();
		try {
			charge = costDebitNoteService.getInterAndIntraDetailsForCostInvoice(orgId, gstType, gstPercent);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"Tax Details get successfully By OrgId, GstType And GstPercent");
			responseObjectsMap.put("chargeDetails", charge);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Tax Detials receive failed By OrgId, GstType And GstPercent", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
}
