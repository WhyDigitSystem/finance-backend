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
import com.base.basesetup.dto.BudgetACPDTO;
import com.base.basesetup.dto.BudgetDTO;
import com.base.basesetup.dto.BudgetHeadCountDTO;
import com.base.basesetup.dto.BudgetRatioAnalysisDTO;
import com.base.basesetup.dto.BudgetUnitWiseDTO;
import com.base.basesetup.dto.IncrementalProfitDTO;
import com.base.basesetup.dto.LoanOutstandingDTO;
import com.base.basesetup.dto.OrderBookingDTO;
import com.base.basesetup.dto.PreviousYearDTO;
import com.base.basesetup.dto.PyAdvancePaymentReceiptDTO;
import com.base.basesetup.dto.PyHeadCountDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.dto.SalesPurchaseDTO;
import com.base.basesetup.dto.SalesPurchaseItemDTO;
import com.base.basesetup.entity.BudgetLoansOutStandingVO;
import com.base.basesetup.entity.PyLoansOutStandingVO;
import com.base.basesetup.service.BudgetService;

@RestController
@RequestMapping("/api/Budget")
public class BudgetController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(BudgetController.class);
	
	@Autowired
	BudgetService budgetService;
	
	
	@GetMapping("/getSubGroup")
	public ResponseEntity<ResponseDTO> getSubGroup(@RequestParam Long orgId,@RequestParam String mainGroup) {
		String methodName = "getSubGroup()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> subGroup = new ArrayList<>();
		try {
			subGroup = budgetService.getSubGroupDetails(orgId, mainGroup);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Sub Group information get successfully");
			responseObjectsMap.put("subGroup", subGroup);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Sub Group information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getGroupLedgers")
	public ResponseEntity<ResponseDTO> getGroupLedgers(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode,@RequestParam String mainGroup,@RequestParam String subGroupCode) {
		String methodName = "getGroupLedgers()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> groupLedgers = new ArrayList<>();
		try {
			groupLedgers = budgetService.getGroupLedgersDetails(orgId, year, clientCode, mainGroup, subGroupCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Group Ledgers information get successfully");
			responseObjectsMap.put("groupLedgers", groupLedgers);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Group Ledgers information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdateBudget")
	public ResponseEntity<ResponseDTO> createUpdateBudget(@RequestBody List<BudgetDTO> budgetDTO) {
		String methodName = "createUpdateBudget()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> BudgetDetails = budgetService.createUpdateBudget(budgetDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, BudgetDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getPreviousYearGroupLedgers")
	public ResponseEntity<ResponseDTO> getPreviousYearGroupLedgers(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode,@RequestParam String mainGroup,@RequestParam String subGroupCode) {
		String methodName = "getPreviousYearGroupLedgers()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> groupLedgers = new ArrayList<>();
		try {
			groupLedgers = budgetService.getPreviousYearGroupLedgersDetails(orgId, year, clientCode, mainGroup, subGroupCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Group Ledgers information get successfully");
			responseObjectsMap.put("groupLedgers", groupLedgers);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Group Ledgers information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getActualGroupLedgers")
	public ResponseEntity<ResponseDTO> getActualGroupLedgers(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode,@RequestParam String mainGroup,@RequestParam String subGroupCode) {
		String methodName = "getActualGroupLedgers()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> groupLedgers = new ArrayList<>();
		try {
			groupLedgers = budgetService.getActualGroupLedgersDetails(orgId, year, clientCode, mainGroup, subGroupCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Group Ledgers information get successfully");
			responseObjectsMap.put("groupLedgers", groupLedgers);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Group Ledgers information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdatePreviousYear")
	public ResponseEntity<ResponseDTO> createUpdatePreviousYear(@RequestBody List<PreviousYearDTO> previousYearDTO) {
		String methodName = "createUpdatePreviousYear()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> BudgetDetails = budgetService.createUpdatePreviousYear(previousYearDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, BudgetDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@PutMapping("/createUpdateBudgetOB")
	public ResponseEntity<ResponseDTO> createUpdateBudgetOB(@RequestBody List<OrderBookingDTO> orderBookingDTO) {
		String methodName = "createUpdateBudgetOB()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> BudgetDetails = budgetService.createUpdateBudgetOB(orderBookingDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, BudgetDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getBudgetOBDetails")
	public ResponseEntity<ResponseDTO> getBudgetOBDetails(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode,@RequestParam String type) {
		String methodName = "getBudgetOBDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> budgetOBDetails = new ArrayList<>();
		try {
			budgetOBDetails = budgetService.getOrderBookingBudgetDetail(orgId, year, clientCode, type);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Budget OB information get successfully");
			responseObjectsMap.put("budgetOBDetails", budgetOBDetails);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Budget OB information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdatePYActulaOB")
	public ResponseEntity<ResponseDTO> createUpdatePYActulaOB(@RequestBody List<OrderBookingDTO> orderBookingDTO) {
		String methodName = "createUpdatePYActulaOB()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> BudgetDetails = budgetService.createUpdatePYActulaOB(orderBookingDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, BudgetDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getPYActualOBDetails")
	public ResponseEntity<ResponseDTO> getPYActualOBDetails(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode,@RequestParam String type) {
		String methodName = "getPYActualOBDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> budgetOBDetails = new ArrayList<>();
		try {
			budgetOBDetails = budgetService.getPYActualOBDetails(orgId, year, clientCode, type);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Budget OB information get successfully");
			responseObjectsMap.put("budgetOBDetails", budgetOBDetails);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Budget OB information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@GetMapping("/getBudgetAutomatic")
	public ResponseEntity<ResponseDTO> getBudgetAutomatic(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode, @RequestParam String mainGroup,@RequestParam String clientYear) {
		String methodName = "getBudgetAutomatic()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> subGroup = new ArrayList<>();
		try {
			subGroup = budgetService.getBudgetDetailsAutomatic(orgId, year, clientCode, mainGroup,clientYear);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Budget "+mainGroup+" information get successfully");
			responseObjectsMap.put("subGroup", subGroup);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Budget "+mainGroup+" information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getGroupLedgersDetailsForHC")
	public ResponseEntity<ResponseDTO> getGroupLedgersDetailsForHC(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode) {
		String methodName = "getGroupLedgersDetailsForHC()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> groupLedgers = new ArrayList<>();
		try {
			groupLedgers = budgetService.getGroupLedgersDetailsForHeadCount(orgId, year, clientCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Group Ledgers information get successfully");
			responseObjectsMap.put("groupLedgers", groupLedgers);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Group Ledgers information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getGroupLedgersDetailsForPYHC")
	public ResponseEntity<ResponseDTO> getGroupLedgersDetailsForPYHC(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode) {
		String methodName = "getGroupLedgersDetailsForPYHC()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> groupLedgers = new ArrayList<>();
		try {
			groupLedgers = budgetService.getGroupLedgersDetailsPYForHeadCount(orgId, year, clientCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Group Ledgers information get successfully");
			responseObjectsMap.put("groupLedgers", groupLedgers);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Group Ledgers information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdateBudgetHC")
	public ResponseEntity<ResponseDTO> createUpdateBudgetHC(@RequestBody List<BudgetHeadCountDTO> budgetHeadCountDTO) {
		String methodName = "createUpdateBudgetHC()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> BudgetDetails = budgetService.createUpdateBudgetHeadCount(budgetHeadCountDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, BudgetDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdatePYHC")
	public ResponseEntity<ResponseDTO> createUpdatePYHC(@RequestBody List<PyHeadCountDTO> pyHeadCountDTO) {
		String methodName = "createUpdatePYHC()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> BudgetDetails = budgetService.createUpdatePreviousYearHeadCount(pyHeadCountDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, BudgetDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdateBudgetACP")
	public ResponseEntity<ResponseDTO> createUpdateBudgetACP(@RequestBody List<BudgetACPDTO> budgetACPDTO) {
		String methodName = "createUpdateBudgetACP()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> BudgetACPDetails = budgetService.createUpdateBudgetAccountPayable(budgetACPDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, BudgetACPDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdatePYACP")
	public ResponseEntity<ResponseDTO> createUpdatePYACP(@RequestBody List<BudgetACPDTO> budgetACPDTO) {
		String methodName = "createUpdatePYACP()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> BudgetACPDetails = budgetService.createUpdatePYAccountPayable(budgetACPDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, BudgetACPDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@GetMapping("/getACPDetails")
	public ResponseEntity<ResponseDTO> getACPDetails(@RequestParam Long orgId,@RequestParam String year,@RequestParam String month,@RequestParam String clientCode,@RequestParam String type) {
		String methodName = "getACPDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> acpDetails = new ArrayList<>();
		try {
			acpDetails = budgetService.getBudgetACPDetails(orgId, year, month, clientCode,type);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Account Payable Details information get successfully");
			responseObjectsMap.put("acpDetails", acpDetails);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Account Payable Details information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getPYACPDetails")
	public ResponseEntity<ResponseDTO> getPYACPDetails(@RequestParam Long orgId,@RequestParam String year,@RequestParam String month,@RequestParam String clientCode,@RequestParam String type) {
		String methodName = "getPYACPDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> acpDetails = new ArrayList<>();
		try {
			acpDetails = budgetService.getPYACPDetails(orgId, year, month, clientCode, type);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Account Payable PY Details information get successfully");
			responseObjectsMap.put("acpDetails", acpDetails);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Account Payable PY Details information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getUnitDetails")
	public ResponseEntity<ResponseDTO> getUnitDetails(@RequestParam Long orgId,@RequestParam String clientCode) {
		String methodName = "getUnitDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> unitDetails = new ArrayList<>();
		try {
			unitDetails = budgetService.getUnitDetails(orgId, clientCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Unit Details information get successfully");
			responseObjectsMap.put("unitDetails", unitDetails);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Unit Details information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdateBudgetUnitWise")
	public ResponseEntity<ResponseDTO> createUpdateBudgetUnitWise(@RequestBody List<BudgetUnitWiseDTO> budgetUnitWiseDTO) {
		String methodName = "createUpdateBudgetUnitWise()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> budgetUnitWise = budgetService.createUpdateBudgetUnitWise(budgetUnitWiseDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, budgetUnitWise.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdatePYUnitWise")
	public ResponseEntity<ResponseDTO> createUpdatePYUnitWise(@RequestBody List<BudgetUnitWiseDTO> budgetUnitWiseDTO) {
		String methodName = "createUpdatePYUnitWise()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> budgetUnitWise = budgetService.createUpdatePYUnitWise(budgetUnitWiseDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, budgetUnitWise.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getUnitLedgers")
	public ResponseEntity<ResponseDTO> getGroupLedgers(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode,@RequestParam String mainGroup,@RequestParam String subGroupCode,@RequestParam String unit) {
		String methodName = "getGroupLedgers()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> unitLedgerDetails = new ArrayList<>();
		try {
			unitLedgerDetails = budgetService.getUnitLedgerDetails(orgId, year, clientCode, mainGroup, subGroupCode,unit);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Unit Ledgers information get successfully");
			responseObjectsMap.put("unitLedgerDetails", unitLedgerDetails);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Unit Ledgers information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getPYUnitLedgers")
	public ResponseEntity<ResponseDTO> getPYUnitLedgers(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode,@RequestParam String mainGroup,@RequestParam String subGroupCode,@RequestParam String unit) {
		String methodName = "getPYUnitLedgers()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> unitLedgerDetails = new ArrayList<>();
		try {
			unitLedgerDetails = budgetService.getPYUnitLedgerDetails(orgId, year, clientCode, mainGroup, subGroupCode, unit);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "PY Unit Ledgers information get successfully");
			responseObjectsMap.put("unitLedgerDetails", unitLedgerDetails);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "PY Unit Ledgers information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getSegmentDetails")
	public ResponseEntity<ResponseDTO> getSegmentDetails(@RequestParam Long orgId,@RequestParam String clientCode,@RequestParam String type) {
		String methodName = "getSegmentDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> segmentDetails = new ArrayList<>();
		try {
			segmentDetails = budgetService.getSegmentDetails(orgId, clientCode,type);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Segment Details information get successfully");
			responseObjectsMap.put("segmentDetails", segmentDetails);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Segment Details information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getRatioAnalisysGroupLedgers")
	public ResponseEntity<ResponseDTO> getRatioAnalisysGroupLedgers(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode,@RequestParam String mainGroup,@RequestParam String subGroupCode) {
		String methodName = "getRatioAnalisysGroupLedgers()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> groupLedgers = new ArrayList<>();
		try {
			groupLedgers = budgetService.getRatioAnalysisBudgetGroupLedgersDetails(orgId, year, clientCode, mainGroup, subGroupCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Ratio Analysis Group Ledgers information get successfully");
			responseObjectsMap.put("groupLedgers", groupLedgers);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Ratio Analysis Group Ledgers information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdateBudgetRationAnalysis")
	public ResponseEntity<ResponseDTO> createUpdateBudgetRationAnalysis(@RequestBody List<BudgetRatioAnalysisDTO> budgetRatioAnalysisDTO) {
		String methodName = "createUpdateBudgetRationAnalysis()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> BudgetDetails = budgetService.createUpdateBudgetRatioAnalysis(budgetRatioAnalysisDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, BudgetDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getRatioAnalisysPYGroupLedgers")
	public ResponseEntity<ResponseDTO> getRatioAnalisysPYGroupLedgers(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode,@RequestParam String mainGroup,@RequestParam String subGroupCode) {
		String methodName = "getRatioAnalisysPYGroupLedgers()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> groupLedgers = new ArrayList<>();
		try {
			groupLedgers = budgetService.getRatioAnalysisPYGroupLedgersDetails(orgId, year, clientCode, mainGroup, subGroupCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Ratio Analysis PY Group Ledgers information get successfully");
			responseObjectsMap.put("groupLedgers", groupLedgers);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Ratio Analysis PY Group Ledgers information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdatePYRationAnalysis")
	public ResponseEntity<ResponseDTO> createUpdatePYRationAnalysis(@RequestBody List<BudgetRatioAnalysisDTO> budgetRatioAnalysisDTO) {
		String methodName = "createUpdatePYRationAnalysis()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> BudgetDetails = budgetService.createUpdatePYRatioAnalysis(budgetRatioAnalysisDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, BudgetDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getProfitLossLedgerDetails")
	public ResponseEntity<ResponseDTO> getProfitLossLedgerDetails(@RequestParam Long orgId,@RequestParam String mainGroup) {
		String methodName = "getProfitLossLedgerDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> groupLedgers = new ArrayList<>();
		try {
			groupLedgers = budgetService.getLedgerDetailsForPL(orgId, mainGroup);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "P&L Ledgers information get successfully");
			responseObjectsMap.put("groupLedgers", groupLedgers);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "P&L Ledgers information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getSubGroupForProfitLossGroupmapping")
	public ResponseEntity<ResponseDTO> getSubGroupForProfitLossGroupmapping(@RequestParam Long orgId,@RequestParam String mainGroup) {
		String methodName = "getSubGroupForProfitLossGroupmapping()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> groupLedgers = new ArrayList<>();
		try {
			groupLedgers = budgetService.getSubGroupDetailsForPL(orgId, mainGroup);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "P&L Ledgers information get successfully");
			responseObjectsMap.put("groupLedgers", groupLedgers);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "P&L Ledgers information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getLedgerDetailsForProfitLossGroupmapping")
	public ResponseEntity<ResponseDTO> getLedgerDetailsForProfitLossGroupmapping(@RequestParam Long orgId,@RequestParam String mainGroup,@RequestParam String subGroup) {
		String methodName = "getLedgerDetailsForProfitLossGroupmapping()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> groupLedgers = new ArrayList<>();
		try {
			groupLedgers = budgetService.getLedgerDetailsForSubGroupPL(orgId, mainGroup, subGroup);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "P&L Ledgers information get successfully");
			responseObjectsMap.put("groupLedgers", groupLedgers);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "P&L Ledgers information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getPYDetailsAutomatic")
	public ResponseEntity<ResponseDTO> getPYDetailsAutomatic(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode,@RequestParam String mainGroup) {
		String methodName = "getPYDetailsAutomatic()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> pyDetails = new ArrayList<>();
		try {
			pyDetails = budgetService.getPYDetailsAutomatic(orgId, year, clientCode, mainGroup);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "PY Automation information get successfully");
			responseObjectsMap.put("pyDetails", pyDetails);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "PY Automation information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdateBudgetIncrementalProfit")
	public ResponseEntity<ResponseDTO> createUpdateBudgetIncrementalProfit(@RequestBody List<IncrementalProfitDTO> incrementalProfitDTO) {
		String methodName = "createUpdateBudgetIncrementalProfit()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> BudgetIncrementalProfitDetails = budgetService.createUpdateIncrementalProfitBudget(incrementalProfitDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, BudgetIncrementalProfitDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdatePYIncrementalProfit")
	public ResponseEntity<ResponseDTO> createUpdatePYIncrementalProfit(@RequestBody List<IncrementalProfitDTO> incrementalProfitDTO) {
		String methodName = "createUpdatePYIncrementalProfit()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> PYIncrementalProfitDetails = budgetService.createUpdateIncrementalProfitPY(incrementalProfitDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, PYIncrementalProfitDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getBudgetIncrementalProfitLedgers")
	public ResponseEntity<ResponseDTO> getBudgetIncrementalProfitLedgers(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode,@RequestParam String mainGroup,@RequestParam String subGroup) {
		String methodName = "getBudgetIncrementalProfitLedgers()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> budgetIncrementalProfitLedgers = new ArrayList<>();
		try {
			budgetIncrementalProfitLedgers = budgetService.getBudgetIncrementalGroupLedgersDetails(orgId, year, clientCode, mainGroup,subGroup);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Budget Incremental Profit Ledgers information get successfully");
			responseObjectsMap.put("budgetIncrementalProfitLedgers", budgetIncrementalProfitLedgers);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Budget Incremental Profit Ledgers information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getPYIncrementalProfitLedgers")
	public ResponseEntity<ResponseDTO> getPYIncrementalProfitLedgers(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode,@RequestParam String mainGroup,@RequestParam String subGroup) {
		String methodName = "getPYIncrementalProfitLedgers()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> pyIncrementalProfitLedgers = new ArrayList<>();
		try {
			pyIncrementalProfitLedgers = budgetService.getPYIncrementalGroupLedgersDetails(orgId, year, clientCode, mainGroup,subGroup);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "PY Incremental Profit Ledgers information get successfully");
			responseObjectsMap.put("pyIncrementalProfitLedgers", pyIncrementalProfitLedgers);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "PY Incremental Profit Ledgers information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getActualIncrementalProfitLedgers")
	public ResponseEntity<ResponseDTO> getActualIncrementalProfitLedgers(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode,@RequestParam String mainGroup,@RequestParam String subGroup,@RequestParam String month) {
		String methodName = "getActualIncrementalProfitLedgers()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> pyIncrementalProfitLedgers = new ArrayList<>();
		try {
			pyIncrementalProfitLedgers = budgetService.getActualIncrementalGroupLedgersDetails(orgId, year, clientCode, mainGroup,subGroup,month);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "PY Incremental Profit Ledgers information get successfully");
			responseObjectsMap.put("pyIncrementalProfitLedgers", pyIncrementalProfitLedgers);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "PY Incremental Profit Ledgers information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@GetMapping("/getAdvancePaymentReceiptDetails")
	public ResponseEntity<ResponseDTO> getAdvancePaymentReceiptDetails(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode,@RequestParam String type) {
		String methodName = "getAdvancePaymentReceiptDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> paymentReceiptDetails = new ArrayList<>();
		try {
			paymentReceiptDetails = budgetService.getAdvancePaymentReceiptDetails(orgId, year, clientCode, type);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Payment Receipt Details get successfully");
			responseObjectsMap.put("paymentReceiptDetails", paymentReceiptDetails);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Payment Receipt Details receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdateAdvancePaymentReceipt")
	public ResponseEntity<ResponseDTO> createUpdateAdvancePaymentReceipt(@RequestBody List<PyAdvancePaymentReceiptDTO> pyAdvancePaymentReceiptDTO) {
		String methodName = "createUpdateAdvancePaymentReceipt()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> paymentReceiptDetails = budgetService.createUpdateAdvancePaymentPY(pyAdvancePaymentReceiptDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, paymentReceiptDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getBudgetLoanOutStandingLedgers")
	public ResponseEntity<ResponseDTO> getBudgetLoanOutStandingLedgers(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode) {
		String methodName = "getBudgetLoanOutStandingLedgers()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<BudgetLoansOutStandingVO> groupLedgers = new ArrayList<>();
		try {
			groupLedgers = budgetService.BudgetLoanOutStandingLedger(orgId, year, clientCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Group Ledgers information get successfully");
			responseObjectsMap.put("groupLedgers", groupLedgers);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Group Ledgers information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdateBudgetLoanOutStanding")
	public ResponseEntity<ResponseDTO> createUpdateBudgetLoanOutStanding(@RequestBody List<LoanOutstandingDTO> loanOutstandingDTO) {
		String methodName = "createUpdateBudget()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> BudgetDetails = budgetService.createUpdateBudgetLoanOutStanding(loanOutstandingDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, BudgetDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getPyLoanOutStandingLedgers")
	public ResponseEntity<ResponseDTO> getPyLoanOutStandingLedgers(@RequestParam Long orgId,@RequestParam String year,@RequestParam String clientCode) {
		String methodName = "getPyLoanOutStandingLedgers()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<PyLoansOutStandingVO> groupLedgers = new ArrayList<>();
		try {
			groupLedgers = budgetService.PyLoanOutStandingLedger(orgId, year, clientCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Group Ledgers information get successfully");
			responseObjectsMap.put("groupLedgers", groupLedgers);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Group Ledgers information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdatePyLoanOutStanding")
	public ResponseEntity<ResponseDTO> createUpdatePyLoanOutStanding(@RequestBody List<LoanOutstandingDTO> loanOutstandingDTO) {
		String methodName = "createUpdateBudget()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> BudgetDetails = budgetService.createUpdatePyLoanOutStanding(loanOutstandingDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, BudgetDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdateBudgetSalesPurchaseAnalysis")
	public ResponseEntity<ResponseDTO> createUpdateBudgetSalesPurchaseAnalysis(@RequestBody List<SalesPurchaseDTO> salesPurchaseDTO) {
		String methodName = "createUpdateBudgetSalesPurchaseAnalysis()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> BudgetDetails = budgetService.createUpdateBudgetSalesPurchaseAnalysis(salesPurchaseDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, BudgetDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdatePySalesPurchaseAnalysis")
	public ResponseEntity<ResponseDTO> createUpdatePySalesPurchaseAnalysis(@RequestBody List<SalesPurchaseDTO> salesPurchaseDTO) {
		String methodName = "createUpdatePySalesPurchaseAnalysis()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> BudgetDetails = budgetService.createUpdatePySalesPurchaseAnalysis(salesPurchaseDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, BudgetDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdateBudgetSalesPurchaseItemAnalysis")
	public ResponseEntity<ResponseDTO> createUpdateBudgetSalesPurchaseItemAnalysis(@RequestBody List<SalesPurchaseItemDTO> salesPurchaseItemDTO) {
		String methodName = "createUpdateBudgetSalesPurchaseItemAnalysis()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> BudgetDetails = budgetService.createUpdateBudgetSalesPurchaseItemAnalysis(salesPurchaseItemDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, BudgetDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();  
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdatePySalesPurchaseItemAnalysis")
	public ResponseEntity<ResponseDTO> createUpdatePySalesPurchaseItemAnalysis(@RequestBody List<SalesPurchaseItemDTO> salesPurchaseItemDTO) {
		String methodName = "createUpdatePySalesPurchaseItemAnalysis()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> BudgetDetails = budgetService.createUpdatePySalesPurchaseItemAnalysis(salesPurchaseItemDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, BudgetDetails.get("message"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getBudgetSalesPurchaseAnalysisDetails")
	public ResponseEntity<ResponseDTO> getBudgetSalesPurchaseAnalysisDetails(@RequestParam Long orgId,@RequestParam String finYear,@RequestParam String clientCode,@RequestParam String type) {
		String methodName = "getBudgetSalesPurchaseAnalysisDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> budgetSalesPurchase = new ArrayList<>();
		try {
			budgetSalesPurchase = budgetService.getBudgetSalesPurchaseDetails(orgId, finYear, clientCode, type);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Budget SalesPurchase information get successfully");
			responseObjectsMap.put("budgetSalesPurchase", budgetSalesPurchase);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Budget Sales Purchase information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getPySalesPurchaseAnalysisDetails")
	public ResponseEntity<ResponseDTO> getPySalesPurchaseAnalysisDetails(@RequestParam Long orgId,@RequestParam String finYear,@RequestParam String clientCode,@RequestParam String type) {
		String methodName = "getPySalesPurchaseAnalysisDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> pySalesPurchase = new ArrayList<>();
		try {
			pySalesPurchase = budgetService.getPySalesPurchaseDetails(orgId, finYear, clientCode, type);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Py SalesPurchase information get successfully");
			responseObjectsMap.put("pySalesPurchase", pySalesPurchase);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Py Sales Purchase information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getBudgetSalesPurchaseAnalysisItemDetails")
	public ResponseEntity<ResponseDTO> getBudgetSalesPurchaseAnalysisItemDetails(@RequestParam Long orgId,@RequestParam String finYear,@RequestParam String clientCode,@RequestParam String type) {
		String methodName = "getBudgetSalesPurchaseAnalysisItemDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> budgetSalesPurchaseItem = new ArrayList<>();
		try {
			budgetSalesPurchaseItem = budgetService.getBudgetSalesPurchaseItemDetails(orgId, finYear, clientCode, type);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Budget Sales Purchase Item information get successfully");
			responseObjectsMap.put("budgetSalesPurchaseItem", budgetSalesPurchaseItem);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Budget Sales Purchase Item information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getPySalesPurchaseAnalysisItemDetails")
	public ResponseEntity<ResponseDTO> getPySalesPurchaseAnalysisItemDetails(@RequestParam Long orgId,@RequestParam String finYear,@RequestParam String clientCode,@RequestParam String type) {
		String methodName = "getPySalesPurchaseAnalysisItemDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> pySalesPurchaseItem = new ArrayList<>();
		try {
			pySalesPurchaseItem = budgetService.getPySalesPurchaseItemDetails(orgId, finYear, clientCode, type);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Py Sales Purchase Item information get successfully");
			responseObjectsMap.put("pySalesPurchaseItem", pySalesPurchaseItem);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Py Sales Purchase Item information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
}
