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
import com.base.basesetup.dto.CostInvoiceDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.CostInvoiceVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.TaxInvoiceVO;
import com.base.basesetup.service.CostInvoiceService;

@CrossOrigin
@RestController
@RequestMapping("/api/costInvoice")
public class CostInvoiceController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(CostInvoiceController.class);

	@Autowired
	CostInvoiceService costInvoiceService;

	// CostInvoice

	@GetMapping("/getAllCostInvoiceByOrgId")
	public ResponseEntity<ResponseDTO> getAllCostInvoiceByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllCostInvoiceByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CostInvoiceVO> costInvoiceVO = new ArrayList<>();
		try {
			costInvoiceVO = costInvoiceService.getAllCostInvoiceByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "CostInvoice information get successfully By OrgId");
			responseObjectsMap.put("costInvoiceVO", costInvoiceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"CostInvoice information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllCostInvoiceById")
	public ResponseEntity<ResponseDTO> getAllCostInvoiceById(@RequestParam(required = false) Long id) {
		String methodName = "getAllCostInvoiceById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CostInvoiceVO> costInvoiceVO = new ArrayList<>();
		try {
			costInvoiceVO = costInvoiceService.getAllCostInvoiceById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "CostInvoice information get successfully By id");
			responseObjectsMap.put("costInvoiceVO", costInvoiceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"CostInvoice information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateCostInvoice")
	public ResponseEntity<ResponseDTO> updateCreateCostInvoice(@Valid @RequestBody CostInvoiceDTO costInvoiceDTO) {
		String methodName = "updateCreateCostInvoice()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			Map<String, Object> costInvoiceVO = costInvoiceService.updateCreateCostInvoice(costInvoiceDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, costInvoiceVO.get("message"));
			responseObjectsMap.put("costInvoiceVO", costInvoiceVO.get("costInvoiceVO")); // Corrected key
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getCostInvoiceByActive")
	public ResponseEntity<ResponseDTO> getCostInvoiceByActive() {
		String methodName = "getCostInvoiceByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CostInvoiceVO> costInvoiceVO = new ArrayList<>();
		try {
			costInvoiceVO = costInvoiceService.getCostInvoiceByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "CostInvoice information get successfully By Active");
			responseObjectsMap.put("costInvoiceVO", costInvoiceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"CostInvoice information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getCostInvoiceByDocId")
	public ResponseEntity<ResponseDTO> getCostInvoiceByDocId(@RequestParam Long orgId, @RequestParam String docId) {
		String methodName = "getCostInvoiceByDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		CostInvoiceVO costInvoiceVO = new CostInvoiceVO();
		try {
			costInvoiceVO = costInvoiceService.getCostInvoiceByDocId(orgId, docId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "OriginBill information get successfully By docid");
			responseObjectsMap.put("costInvoiceVO", costInvoiceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"OriginBill information receive failed By docid", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getCostInvoiceDocId")
	public ResponseEntity<ResponseDTO> getCostInvoiceDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getCostInvoiceDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = costInvoiceService.getCostInvoiceDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"CostInvoiceDocId information retrieved successfully");
			responseObjectsMap.put("taxInvoiceDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve CostInvoice Docid information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getChargeType")
	public ResponseEntity<ResponseDTO> getChargeType(@RequestParam Long orgId) {
		String methodName = "getChargeType()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = costInvoiceService.getChargeType(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Charge Type retrieved successfully");
			responseObjectsMap.put("chargeTypeVO", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve Charge Type", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getChargeCodeDetailsByChargeType")
	public ResponseEntity<ResponseDTO> getChargeCodeDetailsByChargeType(@RequestParam Long orgId,
			@RequestParam String chargeType) {
		String methodName = "getChargeCodeDetailsByChargeType()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = costInvoiceService.getChargeCodeByChargeType(orgId, chargeType);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Charge Code retrieved successfully");
			responseObjectsMap.put("chargeCodeVO", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve Charge Code", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getCurrencyAndExratesForMatchingParties")
	public ResponseEntity<ResponseDTO> getCurrencyAndExratesForMatchingParties(@RequestParam Long orgId,
			@RequestParam String partyCode) {
		String methodName = "getCurrencyAndExratesForMatchingParties()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = costInvoiceService.getCurrencyAndExratesForMatchingParties(orgId, partyCode);
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

	@GetMapping("/getPartyNameByPartyType")
	public ResponseEntity<ResponseDTO> getPartyNameByPartyType(@RequestParam Long orgId,
			@RequestParam String partyType) {
		String methodName = "getPartyNameByPartyType()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<PartyMasterVO> partyMasterVO = new ArrayList<>();
		try {
			partyMasterVO = costInvoiceService.getAllPartyByPartyType(orgId, partyType);
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

	@GetMapping("/getPartyStateDetails")
	public ResponseEntity<ResponseDTO> getPartyStateDetails(@RequestParam Long orgId, @RequestParam Long id) {
		String methodName = "getPartyStateDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> partyStateVO = new ArrayList<>();
		try {
			partyStateVO = costInvoiceService.getPartyStateCodeDetails(orgId, id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Party State information get successfully ByOrgId");
			responseObjectsMap.put("partyStateVO", partyStateVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Party State information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getPartyAddress")
	public ResponseEntity<ResponseDTO> getPartyAddress(@RequestParam Long orgId, @RequestParam Long id,
			@RequestParam String stateCode, @RequestParam String placeOfSupply) {
		String methodName = "getPartyAddress()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> partyAddress = new ArrayList<>();
		try {
			partyAddress = costInvoiceService.getPartyAddressDetails(orgId, id, stateCode, placeOfSupply);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Party Address information get successfully ByOrgId");
			responseObjectsMap.put("partyAddress", partyAddress);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Party Address information receive failedByOrgId", errorMsg);
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
			gstTypeDetails = costInvoiceService.getGstTypeDetails(orgId, branchCode, stateCode);
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

	@GetMapping("/getPlaceOfSupply")
	public ResponseEntity<ResponseDTO> getPlaceOfSupply(@RequestParam Long orgId, @RequestParam Long id,
			@RequestParam String stateCode) {
		String methodName = "getPlaceOfSupply()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> placeOfSupplyDetails = new ArrayList<>();
		try {
			placeOfSupplyDetails = costInvoiceService.getPlaceOfSupplyDetails(orgId, id, stateCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"Place Of Supply information get successfully ByOrgId");
			responseObjectsMap.put("placeOfSupplyDetails", placeOfSupplyDetails);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Place Of Supply information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getJobNoFromTmsJobCard")
	public ResponseEntity<ResponseDTO> getJobNoFromTmsJobCard(@RequestParam Long orgId) {
		String methodName = "getJobNoFromTmsJobCard()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> jobNo = new ArrayList<>();
		try {
			jobNo = costInvoiceService.getJobNoFromTmsJobCard(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Job No get successfully By OrgId");
			responseObjectsMap.put("jobNo", jobNo);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Job No receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getChargeDetailsFromChargeType")
	public ResponseEntity<ResponseDTO> getChargeDetailsFromChargeType(@RequestParam Long orgId) {
		String methodName = "getChargeDetailsFromChargeType()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> chargeDetails = new ArrayList<>();
		try {
			chargeDetails = costInvoiceService.getChargeDetailsFromChargeType(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Charge Details get successfully By OrgId");
			responseObjectsMap.put("chargeDetails", chargeDetails);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Charge Details receive failed By OrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getTdsDetailsFromPartyMasterSpecialTDS")
	public ResponseEntity<ResponseDTO> getTdsDetailsFromPartyMasterSpecialTDS(@RequestParam Long orgId,
			@RequestParam String partyCode) {
		String methodName = "getTdsDetailsFromPartyMasterSpecialTDS()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> tds = new ArrayList<>();
		try {
			tds = costInvoiceService.getTdsDetailsFromPartyMasterSpecialTDS(orgId, partyCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"TDS get successfully By OrgId And Party Code from Party Special TDS");
			responseObjectsMap.put("tds", tds);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"TDS receive failed By OrgId And Party Code from Party Special TDS", errorMsg);
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
			charge = costInvoiceService.getInterAndIntraDetailsForCostInvoice(orgId, gstType, gstPercent);
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

	@PutMapping("/approveCostInvoice")
	public ResponseEntity<ResponseDTO> approveCostInvoice(@RequestParam Long orgId, @RequestParam Long id,
			@RequestParam String docId, @RequestParam String action, @RequestParam String actionBy) {
		String methodName = "approveCostInvoice()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			CostInvoiceVO costInvoiceVO = costInvoiceService.approveCostInvoice(orgId, id, docId, action, actionBy);
			responseObjectsMap.put("costInvoiceVO", costInvoiceVO);
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
