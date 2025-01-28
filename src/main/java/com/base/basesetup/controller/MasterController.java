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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.common.CommonConstant;
import com.base.basesetup.common.UserConstants;
import com.base.basesetup.dto.AccountDTO;
import com.base.basesetup.dto.BranchDTO;
import com.base.basesetup.dto.ChargeTypeRequestDTO;
import com.base.basesetup.dto.ChequeBookDTO;
import com.base.basesetup.dto.CostCenterDTO;
import com.base.basesetup.dto.EmployeeDTO;
import com.base.basesetup.dto.GroupLedgerDTO;
import com.base.basesetup.dto.ListOfValuesDTO;
import com.base.basesetup.dto.PartyMasterDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.dto.SacCodeDTO;
import com.base.basesetup.dto.SetTaxRateDTO;
import com.base.basesetup.dto.SubLedgerAccountDTO;
import com.base.basesetup.dto.TaxMasterDTO;
import com.base.basesetup.dto.TcsMasterDTO;
import com.base.basesetup.dto.TdsMasterDTO;
import com.base.basesetup.entity.AccountVO;
import com.base.basesetup.entity.BranchVO;
import com.base.basesetup.entity.ChargeTypeRequestVO;
import com.base.basesetup.entity.ChequeBookVO;
import com.base.basesetup.entity.CostCenterVO;
import com.base.basesetup.entity.EmployeeVO;
import com.base.basesetup.entity.GroupLedgerVO;
import com.base.basesetup.entity.ListOfValuesVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.SacCodeVO;
import com.base.basesetup.entity.SetTaxRateVO;
import com.base.basesetup.entity.SubLedgerAccountVO;
import com.base.basesetup.entity.TaxMasterVO;
import com.base.basesetup.entity.TcsMasterVO;
import com.base.basesetup.entity.TdsMasterVO;
import com.base.basesetup.service.MasterService;

@CrossOrigin
@RestController
@RequestMapping("/api/master")
public class MasterController extends BaseController {

	@Autowired
	MasterService masterService;

	public static final Logger LOGGER = LoggerFactory.getLogger(MasterController.class);

	// Branch
		@GetMapping("/branch")
		public ResponseEntity<ResponseDTO> getAllBranch(@RequestParam Long orgid) {
			String methodName = "getAllBranch()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<BranchVO> branchVO = new ArrayList<>();
			try {
				branchVO = masterService.getAllBranch(orgid);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Branch information get successfully");
				responseObjectsMap.put("branchVO", branchVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "Branch information receive failed", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@GetMapping("/branch/{branchid}")
		public ResponseEntity<ResponseDTO> getBranchById(@PathVariable Long branchid) {
			String methodName = "getBranchById()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			BranchVO branchVO = null;
			try {
				branchVO = masterService.getBranchById(branchid).orElse(null);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isEmpty(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Branch found by ID");
				responseObjectsMap.put("Branch", branchVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = "Branch not found for ID: " + branchid;
				responseDTO = createServiceResponseError(responseObjectsMap, "Branch not found", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
	 
		@PutMapping("/createUpdateBranch")
		public ResponseEntity<ResponseDTO> createUpdateBranch(@RequestBody BranchDTO branchDTO) {
			String methodName = "createBranch()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			try {
				Map<String, Object> createdBranchVO = masterService.createUpdateBranch(branchDTO);
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,createdBranchVO.get("message"));
				responseObjectsMap.put("branchVO", createdBranchVO.get("branchVO"));
				responseDTO = createServiceResponse(responseObjectsMap);
			} catch (Exception e) {
		        errorMsg = e.getMessage();
		        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		    }
		    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		    return ResponseEntity.ok().body(responseDTO);
		}
	
		// Employee

		@GetMapping("/getAllEmployeeByOrgId")
		public ResponseEntity<ResponseDTO> getAllEmployeeByOrgId(@RequestParam Long orgId) {
			String methodName = "getAllEmployeeByOrgId()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<EmployeeVO> employeeVO = new ArrayList<>();
			try {
				employeeVO = masterService.getAllEmployeeByOrgId(orgId);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee information get successfully");
				responseObjectsMap.put("employeeVO", employeeVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "Employee information receive failed",
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@GetMapping("/getAllEmployee")
		public ResponseEntity<ResponseDTO> getAllEmployee() {
			String methodName = "getAllEmployeeByOrgId()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<EmployeeVO> employeeVO = new ArrayList<>();
			try {
				employeeVO = masterService.getAllEmployee();
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee information get successfully");
				responseObjectsMap.put("employeeVO", employeeVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "Employee information receive failed",
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@GetMapping("/employee/{employeeid}")
		public ResponseEntity<ResponseDTO> getEmployeeById(@PathVariable Long employeeid) {
			String methodName = "getEmployeeById()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			EmployeeVO employeeVO = null;
			try {
				employeeVO = masterService.getEmployeeById(employeeid).orElse(null);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isEmpty(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee found by ID");
				responseObjectsMap.put("Employee", employeeVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = "Employee not found for ID: " + employeeid;
				responseDTO = createServiceResponseError(responseObjectsMap, "Employee not found", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@PutMapping("/createUpdateEmployee")
		public ResponseEntity<ResponseDTO> createUpdateEmployee(@RequestBody EmployeeDTO employeeDTO) {
			String methodName = "createEmployee()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			try {
				Map<String, Object> createdEmployeeVO = masterService.createEmployee(employeeDTO);
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,createdEmployeeVO.get("message") );
				responseObjectsMap.put("employeeVO", createdEmployeeVO.get("createdEmployeeVO"));
				responseDTO = createServiceResponse(responseObjectsMap);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				responseDTO = createServiceResponseError(responseObjectsMap, errorMsg,
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

	
	// SetTaxRate

	@GetMapping("/getAllSetTaxRateByOrgId")
	public ResponseEntity<ResponseDTO> getAllSetTaxRateByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllSetTaxRateByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<SetTaxRateVO> setTaxRateVO = new ArrayList<>();
		try {
			setTaxRateVO = masterService.getAllSetTaxRateByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "SetTaxRate information get successfullyByOrgId");
			responseObjectsMap.put("setTaxRateVO", setTaxRateVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "SetTaxRate information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllSetTaxRateById")
	public ResponseEntity<ResponseDTO> getAllSetTaxRateById(@RequestParam(required = false) Long id) {
		String methodName = "getAllSetTaxRateById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<SetTaxRateVO> setTaxRateVO = new ArrayList<>();
		try {
			setTaxRateVO = masterService.getAllSetTaxRateById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "SetTaxRate information get successfullyById");
			responseObjectsMap.put("setTaxRateVO", setTaxRateVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "SetTaxRate information receive failedById",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateSetTaxRate")
	public ResponseEntity<ResponseDTO> updateCreateSetTaxRate(@Valid @RequestBody SetTaxRateDTO setTaxRateDTO) {
	    String methodName = "updateCreateSetTaxRate()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    String errorMsg = null;
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = null;
	    
	    try {
	        SetTaxRateVO setTaxRateVO = masterService.updateCreateSetTaxRate(setTaxRateDTO);
	        boolean isUpdate = setTaxRateDTO.getId() != null;
	        if (setTaxRateVO != null) {
	            responseObjectsMap.put(CommonConstant.STRING_MESSAGE, isUpdate ? "SetTaxRate updated successfully" : "SetTaxRate created successfully");
	            responseObjectsMap.put("setTaxRateVO", setTaxRateVO);
	            responseDTO = createServiceResponse(responseObjectsMap);
	        } else {
	            errorMsg = isUpdate ? "SetTaxRate not found for ID: " + setTaxRateDTO.getId() : "SetTaxRate creation failed";
	            responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "SetTaxRate update failed" : "SetTaxRate creation failed", errorMsg);
	        }
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        boolean isUpdate = setTaxRateDTO.getId() != null;
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "SetTaxRate update failed" : "SetTaxRate creation failed", errorMsg);
	    }
	    
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}


	@GetMapping("/getSetTaxRateByActive")
	public ResponseEntity<ResponseDTO> getSetTaxRateByActive() {
		String methodName = "getSetTaxRateByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<SetTaxRateVO> setTaxRateVO = new ArrayList<>();
		try {
			setTaxRateVO = masterService.getSetTaxRateByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "SetTaxRate information get successfully By Active");
			responseObjectsMap.put("setTaxRateVO", setTaxRateVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"SetTaxRate information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// TAXMASTER

	@GetMapping("/getAllTaxMasterByOrgId")
	public ResponseEntity<ResponseDTO> getAllTaxMasterByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllTaxMasterByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<TaxMasterVO> taxMasterVO = new ArrayList<>();
		try {
			taxMasterVO = masterService.getAllTaxMasterByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TaxMaster information getByOrgId successfully");
			responseObjectsMap.put("taxMasterVO", taxMasterVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "TaxMaster information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllTaxMasterById")
	public ResponseEntity<ResponseDTO> getAllTaxMasterById(@RequestParam(required = false) Long id) {
		String methodName = "getAllTaxMasterById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<TaxMasterVO> taxMasterVO = new ArrayList<>();
		try {
			taxMasterVO = masterService.getAllTaxMasterById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TaxMaster information getById successfully");
			responseObjectsMap.put("taxMasterVO", taxMasterVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "TaxMaster information receive failedById",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@PutMapping("updateCreateTaxMaster")
	public ResponseEntity<ResponseDTO> updateCreateTaxMaster(@Valid @RequestBody TaxMasterDTO taxMasterDTO) {
	    String methodName = "updateCreateTaxMaster()";

	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    String errorMsg = null;
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = null;

	    try {
	        TaxMasterVO taxMasterVO = masterService.updateCreateTaxMaster(taxMasterDTO);
	        boolean isUpdate = taxMasterDTO.getId() != null;
	        if (taxMasterVO != null) {
	            responseObjectsMap.put(CommonConstant.STRING_MESSAGE, isUpdate ? "Tax Master updated successfully" : "Tax Master created successfully");
	            responseObjectsMap.put("taxMasterVO", taxMasterVO);
	            responseDTO = createServiceResponse(responseObjectsMap);
	        } else {
	            errorMsg = isUpdate ? "Tax Master not found for ID: " + taxMasterDTO.getId() : "Tax Master creation failed";
	            responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "Tax Master update failed" : "Tax Master creation failed", errorMsg);
	        }
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        boolean isUpdate = taxMasterDTO.getId() != null;
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "Tax Master update failed" : "Tax Master creation failed", errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}


	@GetMapping("/getTaxMasterByActive")
	public ResponseEntity<ResponseDTO> getTaxMasterByActive() {
		String methodName = "getTaxMasterByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<TaxMasterVO> taxMasterVO = new ArrayList<>();
		try {
			taxMasterVO = masterService.getTaxMasterByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TaxMaster information get successfully By Active");
			responseObjectsMap.put("taxMasterVO", taxMasterVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"TaxMaster information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// TCSMASTER

	@GetMapping("/getAllTcsMasterByOrgId")
	public ResponseEntity<ResponseDTO> getAllTcsMasterByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllTcsMasterByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<TcsMasterVO> tcsMasterVO = new ArrayList<>();
		try {
			tcsMasterVO = masterService.getAllTcsMasterByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TcsMaster information getByOrgId successfully");
			responseObjectsMap.put("tcsMasterVO", tcsMasterVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "TcsMaster information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllTcsMasterById")
	public ResponseEntity<ResponseDTO> getAllTcsMasterById(@RequestParam(required = false) Long id) {
		String methodName = "getAllTcsMasterById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<TcsMasterVO> tcsMasterVO = new ArrayList<>();
		try {
			tcsMasterVO = masterService.getAllTcsMasterById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TcsMaster information getById successfully");
			responseObjectsMap.put("tcsMasterVO", tcsMasterVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "TcsMaster information receive failedById",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@PutMapping("/updateCreateTcsMaster")
	public ResponseEntity<ResponseDTO> updateCreateTcsMaster(@Valid @RequestBody TcsMasterDTO tcsMasterDTO) {
	    String methodName = "updateCreateTcsMaster()";

	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    String errorMsg = null;
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = null;

	    try {
	        TcsMasterVO tcsMasterVO = masterService.updateCreateTcsMaster(tcsMasterDTO);
	        boolean isUpdate = tcsMasterDTO.getId() != null;
	        if (tcsMasterVO != null) {
	            responseObjectsMap.put(CommonConstant.STRING_MESSAGE, isUpdate ? "Tcs Master updated successfully" : "Tcs Master created successfully");
	            responseObjectsMap.put("tcsMasterVO", tcsMasterVO);
	            responseDTO = createServiceResponse(responseObjectsMap);
	        } else {
	            errorMsg = isUpdate ? "Tcs Master not found for ID: " + tcsMasterDTO.getId() : "Tcs Master creation failed";
	            responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "Tcs Master update failed" : "Tcs Master creation failed", errorMsg);
	        }
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        boolean isUpdate = tcsMasterDTO.getId() != null;
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "Tcs Master update failed" : "Tcs Master creation failed", errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}


	@GetMapping("/getTcsMasterByActive")
	public ResponseEntity<ResponseDTO> getTcsMasterByActive() {
		String methodName = "getTcsMasterByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<TcsMasterVO> tcsMasterVO = new ArrayList<>();
		try {
			tcsMasterVO = masterService.getTcsMasterByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TcsMaster information get successfully By Active");
			responseObjectsMap.put("tcsMasterVO", tcsMasterVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"TcsMaster information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// TDSMASTER

	@GetMapping("/getAllTdsMasterByOrgId")
	public ResponseEntity<ResponseDTO> getAllTdsMasterByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllTdsMasterByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<TdsMasterVO> tdsMasterVO = new ArrayList<>();
		try {
			tdsMasterVO = masterService.getAllTdsMasterByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TdsMaster information getByOrgId successfully");
			responseObjectsMap.put("tdsMasterVO", tdsMasterVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "TdsMaster information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllTdsMasterById")
	public ResponseEntity<ResponseDTO> getAllTdsMasterById(@RequestParam(required = false) Long id) {
		String methodName = "getAllTdsMasterById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<TdsMasterVO> tdsMasterVO = new ArrayList<>();
		try {
			tdsMasterVO = masterService.getAllTdsMasterById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TdsMaster information getById successfully");
			responseObjectsMap.put("tdsMasterVO", tdsMasterVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "TdsMaster information receive failedById",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@PutMapping("/updateCreateTdsMaster")
	public ResponseEntity<ResponseDTO> updateCreateTdsMaster(@Valid @RequestBody TdsMasterDTO tdsMasterDTO) {
	    String methodName = "updateCreateTdsMaster()";

	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    String errorMsg = null;
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = null;

	    try {
	        TdsMasterVO tdsMasterVO = masterService.updateCreateTdsMaster(tdsMasterDTO);
	        boolean isUpdate = tdsMasterDTO.getId() != null;
	        if (tdsMasterVO != null) {
	            responseObjectsMap.put(CommonConstant.STRING_MESSAGE, isUpdate ? "Tds Master updated successfully" : "Tds Master created successfully");
	            responseObjectsMap.put("tdsMasterVO", tdsMasterVO);
	            responseDTO = createServiceResponse(responseObjectsMap);
	        } else {
	            errorMsg = isUpdate ? "Tds Master not found for ID: " + tdsMasterDTO.getId() : "Tds Master creation failed";
	            responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "Tds Master update failed" : "Tds Master creation failed", errorMsg);
	        }
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        boolean isUpdate = tdsMasterDTO.getId() != null;
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "Tds Master update failed" : "Tds Master creation failed", errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}


	@GetMapping("/getTdsMasterByActive")
	public ResponseEntity<ResponseDTO> getTdsMasterByActive() {
		String methodName = "getTdsMasterByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<TdsMasterVO> tdsMasterVO = new ArrayList<>();
		try {
			tdsMasterVO = masterService.getTdsMasterByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TdsMaster information get successfully By Active");
			responseObjectsMap.put("tdsMasterVO", tdsMasterVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"TdsMaster information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// Account

	@GetMapping("/getAllAccountByOrgId")
	public ResponseEntity<ResponseDTO> getAllAccountByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllAccountByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<AccountVO> accountVO = new ArrayList<>();
		try {
			accountVO = masterService.getAllAccountByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Account information get successfully ByOrgId");
			responseObjectsMap.put("accountVO", accountVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Account information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllAccountById")
	public ResponseEntity<ResponseDTO> getAllAccountById(@RequestParam(required = false) Long id) {
		String methodName = "getAllAccountById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<AccountVO> accountVO = new ArrayList<>();
		try {
			accountVO = masterService.getAllAccountById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Account information get successfully By id");
			responseObjectsMap.put("accountVO", accountVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Account information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateAccount")
	public ResponseEntity<ResponseDTO> updateCreateAccount(@Valid @RequestBody AccountDTO accountDTO) {
	    String methodName = "updateCreateAccount()";

	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    String errorMsg = null;
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = null;

	    try {
	        AccountVO accountVO = masterService.updateCreateAccount(accountDTO);
	        boolean isUpdate = accountDTO.getId() != null;
	        if (accountVO != null) {
	            responseObjectsMap.put(CommonConstant.STRING_MESSAGE, isUpdate ? "Account updated successfully" : "Account created successfully");
	            responseObjectsMap.put("accountVO", accountVO);
	            responseDTO = createServiceResponse(responseObjectsMap);
	        } else {
	            errorMsg = isUpdate ? "Account not found for ID: " + accountDTO.getId() : "Account creation failed";
	            responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "Account update failed" : "Account creation failed", errorMsg);
	        }
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        boolean isUpdate = accountDTO.getId() != null;
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "Account update failed" : "Account creation failed", errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAccountByActive")
	public ResponseEntity<ResponseDTO> getAccountByActive() {
		String methodName = "getAccountByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<AccountVO> accountVO = new ArrayList<>();
		try {
			accountVO = masterService.getAccountByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Account information get successfully By Active");
			responseObjectsMap.put("accountVO", accountVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Account information receive failed By Active",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// Group Ledger
	@GetMapping("/getAllGroupLedgerByOrgId")
	public ResponseEntity<ResponseDTO> getAllGroupLedgerByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllGroupLedgerByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<GroupLedgerVO> groupLedgerVO = new ArrayList<>();
		try {
			groupLedgerVO = masterService.getAllGroupLedgerByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Group Ledger information get successfully ByOrgId");
			responseObjectsMap.put("groupLedgerVO", groupLedgerVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Group Ledger information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	@GetMapping("/getGroupNameByOrgId")
	public ResponseEntity<ResponseDTO> getGroupNameByOrgId(
			@RequestParam Long orgId) {

		String methodName = "getGroupNameByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> groupName = new ArrayList<>();
		try {
			groupName = masterService.getGroupName(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"Group Name information get  successfully By OrgId");
			responseObjectsMap.put("groupNameDetails", groupName);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Group Name information receive PartyMaster failed By OrgId", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllGroupLedgerById")
	public ResponseEntity<ResponseDTO> getAllGroupLedgerById(@RequestParam(required = false) Long id) {
		String methodName = "getAllGroupLedgerById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<GroupLedgerVO> groupLedgerVO = new ArrayList<>();
		try {
			groupLedgerVO = masterService.getAllGroupLedgerById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Group Ledger information get successfully ByOrgId");
			responseObjectsMap.put("groupLedgerVO", groupLedgerVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Group Ledger information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@PutMapping("/updateCreateGroupLedger")
	public ResponseEntity<ResponseDTO> updateCreateGroupLedger(@Valid @RequestBody GroupLedgerDTO groupLedgerDTO) {
	    String methodName = "updateCreateGroupLedger()";

	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    String errorMsg = null;
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = null;

	    try {
	        GroupLedgerVO groupLedgerVO = masterService.updateCreateGroupLedger(groupLedgerDTO);
	        boolean isUpdate = groupLedgerDTO.getId() != null;
	        if (groupLedgerVO != null) {
	            responseObjectsMap.put(CommonConstant.STRING_MESSAGE, isUpdate ? "Group Ledger updated successfully" : "Group Ledger created successfully");
	            responseObjectsMap.put("groupLedgerVO", groupLedgerVO);
	            responseDTO = createServiceResponse(responseObjectsMap);
	        } else {
	            errorMsg = isUpdate ? "Group Ledger not found for ID: " + groupLedgerDTO.getId() : "Group Ledger creation failed";
	            responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "Group Ledger update failed" : "Group Ledger creation failed", errorMsg);
	        }
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        boolean isUpdate = groupLedgerDTO.getId() != null;
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "Group Ledger update failed" : "Group Ledger creation failed", errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}



	@GetMapping("/getGroupLedgerByActive")
	public ResponseEntity<ResponseDTO> getGroupLedgerByActive() {
		String methodName = "getGroupLedgerByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<GroupLedgerVO> groupLedgerVO = new ArrayList<>();
		try {
			groupLedgerVO = masterService.getGroupLedgerByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "GroupLedger information get successfully By Active");
			responseObjectsMap.put("groupLedgerVO", groupLedgerVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"GroupLedger information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// SacCode
	@GetMapping("/getAllSacCodeByOrgId")
	public ResponseEntity<ResponseDTO> getAllSapCodeByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllSacCodeByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<SacCodeVO> sacCodeVO = new ArrayList<>();
		try {
			sacCodeVO = masterService.getAllSacCodeByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "SacCode information get successfully ByOrgId");
			responseObjectsMap.put("sacCodeVO", sacCodeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "SacCode information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	@GetMapping("/getAllActiveSacCodeByOrgId")
	public ResponseEntity<ResponseDTO> getAllActiveSacCodeByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllActiveSacCodeByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<SacCodeVO> sacCodeVO = new ArrayList<>();
		try {
			sacCodeVO = masterService.getAllActiveSacCodeByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "SacCode information get successfully ByOrgId");
			responseObjectsMap.put("sacCodeVO", sacCodeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "SacCode information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllSacCodeById")
	public ResponseEntity<ResponseDTO> getAllSacCodeById(@RequestParam(required = false) Long id) {
		String methodName = "getAllSacCodeById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<SacCodeVO> sacCodeVO = new ArrayList<>();
		try {
			sacCodeVO = masterService.getAllSacCodeById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "SacCode information get successfully By OrgId");
			responseObjectsMap.put("sacCodeVO", sacCodeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"SacCode information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@PutMapping("/updateCreateSacCode")
	public ResponseEntity<ResponseDTO> updateCreateSacCode(@Valid @RequestBody SacCodeDTO sacCodeDTO) {
	    String methodName = "updateCreateSacCode()";

	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    String errorMsg = null;
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = null;

	    try {
	        SacCodeVO sacCodeVO = masterService.updateCreateSacCode(sacCodeDTO);
	        boolean isUpdate = sacCodeDTO.getId() != null;
	        if (sacCodeVO != null) {
	            responseObjectsMap.put(CommonConstant.STRING_MESSAGE, isUpdate ? "SacCode updated successfully" : "SacCode created successfully");
	            responseObjectsMap.put("sacCodeVO", sacCodeVO);
	            responseDTO = createServiceResponse(responseObjectsMap);
	        } else {
	            errorMsg = isUpdate ? "SacCode not found for ID: " + sacCodeDTO.getId() : "SacCode creation failed";
	            responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "SacCode update failed" : "SacCode creation failed", errorMsg);
	        }
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        boolean isUpdate = sacCodeDTO.getId() != null;
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "SacCode update failed" : "SacCode creation failed", errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}

//	@GetMapping("/getSacCodeByActive")
//	public ResponseEntity<ResponseDTO> getSacCodeByActive() {
//		String methodName = "getSacCodeByActive()";
//		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
//		String errorMsg = null;
//		Map<String, Object> responseObjectsMap = new HashMap<>();
//		ResponseDTO responseDTO = null;
//		List<SacCodeVO> sacCodeVO = new ArrayList<>();
//		try {
//			sacCodeVO = masterService.getSacCodeByActive();
//		} catch (Exception e) {
//			errorMsg = e.getMessage();
//			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
//		}
//		if (StringUtils.isBlank(errorMsg)) {
//			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "SacCode information get successfully By Active");
//			responseObjectsMap.put("sacCodeVO", sacCodeVO);
//			responseDTO = createServiceResponse(responseObjectsMap);
//		} else {
//			responseDTO = createServiceResponseError(responseObjectsMap,
//					"SacCode information receive failed By Active", errorMsg);
//		}
//		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
//		return ResponseEntity.ok().body(responseDTO);
//
//	}

	// SubLedgerAccount

	@GetMapping("/getAllSubLedgerAccountByOrgId")
	public ResponseEntity<ResponseDTO> getAllSubLedgerAccountByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllSubLedgerAccountByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<SubLedgerAccountVO> subLedgerAccountVO = new ArrayList<>();
		try {
			subLedgerAccountVO = masterService.getAllSubLedgerAccountByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"SubLedgerAccount information get successfully ByOrgId");
			responseObjectsMap.put("subLedgerAccountVO", subLedgerAccountVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"SubLedgerAccount information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllSubLedgerAccountById")
	public ResponseEntity<ResponseDTO> getAllSubLedgerAccountById(@RequestParam(required = false) Long id) {
		String methodName = "getAllSubLedgerAccountById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<SubLedgerAccountVO> subLedgerAccountVO = new ArrayList<>();
		try {
			subLedgerAccountVO = masterService.getAllSubLedgerAccountById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"SubLedgerAccount information get successfully By Id");
			responseObjectsMap.put("subLedgerAccountVO", subLedgerAccountVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"SubLedgerAccount information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateSubLedgerAccount")
	public ResponseEntity<ResponseDTO> updateCreateSubLedgerAccount(
	        @Valid @RequestBody SubLedgerAccountDTO subLedgerAccountDTO) {
	    String methodName = "updateCreateSubLedgerAccount()";

	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    String errorMsg = null;
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = null;

	    try {
	        SubLedgerAccountVO subLedgerAccountVO = masterService.updateCreateSubLedgerAccount(subLedgerAccountDTO);
	        boolean isUpdate = subLedgerAccountDTO.getId() != null;
	        if (subLedgerAccountVO != null) {
	            responseObjectsMap.put(CommonConstant.STRING_MESSAGE, isUpdate ? "SubLedgerAccount updated successfully" : "SubLedgerAccount created successfully");
	            responseObjectsMap.put("subLedgerAccountVO", subLedgerAccountVO);
	            responseDTO = createServiceResponse(responseObjectsMap);
	        } else {
	            errorMsg = isUpdate ? "SubLedgerAccount not found for ID: " + subLedgerAccountDTO.getId() : "SubLedgerAccount creation failed";
	            responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "SubLedgerAccount update failed" : "SubLedgerAccount creation failed", errorMsg);
	        }
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        boolean isUpdate = subLedgerAccountDTO.getId() != null;
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "SubLedgerAccount update failed" : "SubLedgerAccount creation failed", errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}


	@GetMapping("/getSubLedgerAccountByActive")
	public ResponseEntity<ResponseDTO> getSubLedgerAccountByActive() {
		String methodName = "getSubLedgerAccountByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<SubLedgerAccountVO> subLedgerAccountVO = new ArrayList<>();
		try {
			subLedgerAccountVO = masterService.getSubLedgerAccountByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"SubLedgerAccount information get successfully By Active");
			responseObjectsMap.put("subLedgerAccountVO", subLedgerAccountVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"SubLedgerAccount information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// ChequeBook

	@GetMapping("/getAllChequeBookByOrgId")
	public ResponseEntity<ResponseDTO> getAllChequeBookByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllChequeBookByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ChequeBookVO> chequeBookVO = new ArrayList<>();
		try {
			chequeBookVO = masterService.getAllChequeBookByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ChequeBook information get successfully ByOrgId");
			responseObjectsMap.put("chequeBookVO", chequeBookVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "ChequeBook information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllChequeBookById")
	public ResponseEntity<ResponseDTO> getAllChequeBookById(@RequestParam(required = false) Long id) {
		String methodName = "getAllChequeBookById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ChequeBookVO> chequeBookVO = new ArrayList<>();
		try {
			chequeBookVO = masterService.getAllChequeBookById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ChequeBook information get successfully By id");
			responseObjectsMap.put("chequeBookVO", chequeBookVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "ChequeBook information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateChequeBook")
	public ResponseEntity<ResponseDTO> updateCreateChequeBook(@Valid @RequestBody ChequeBookDTO chequeBookDTO) {
	    String methodName = "updateCreateChequeBook()";

	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    String errorMsg = null;
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = null;

	    try {
	        ChequeBookVO chequeBookVO = masterService.updateCreateChequeBook(chequeBookDTO);
	        boolean isUpdate = chequeBookDTO.getId() != null;
	        if (chequeBookVO != null) {
	            responseObjectsMap.put(CommonConstant.STRING_MESSAGE, isUpdate ? "ChequeBook updated successfully" : "ChequeBook created successfully");
	            responseObjectsMap.put("chequeBookVO", chequeBookVO);
	            responseDTO = createServiceResponse(responseObjectsMap);
	        } else {
	            errorMsg = isUpdate ? "ChequeBook not found for ID: " + chequeBookDTO.getId() : "ChequeBook creation failed";
	            responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "ChequeBook update failed" : "ChequeBook creation failed", errorMsg);
	        }
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        boolean isUpdate = chequeBookDTO.getId() != null;
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "ChequeBook update failed" : "ChequeBook creation failed", errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getChequeBookByActive")
	public ResponseEntity<ResponseDTO> getChequeBookByActive() {
		String methodName = "getChequeBookByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ChequeBookVO> chequeBookVO = new ArrayList<>();
		try {
			chequeBookVO = masterService.getChequeBookByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ChequeBook information get successfully By Active");
			responseObjectsMap.put("chequeBookVO", chequeBookVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ChequeBook information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// ChargeTypeRequest
	
	@GetMapping("/getChargeType")
	public ResponseEntity<ResponseDTO> getChargeTypeFromListOfValues(@RequestParam Long orgId) {
		String methodName = "getChargeTypeFromListOfValues()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> chargeTypeDetails = new ArrayList<>();
		try {
			chargeTypeDetails = masterService.getChargeType(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Charge Type information get successfully");
			responseObjectsMap.put("chargeTypeDetails", chargeTypeDetails);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Charge Type information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllChargeTypeRequestByOrgId")
	public ResponseEntity<ResponseDTO> getAllChargeTypeRequestByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllChargeTypeRequestByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ChargeTypeRequestVO> chargeTypeRequestVO = new ArrayList<>();
		try {
			chargeTypeRequestVO = masterService.getAllChargeTypeRequestByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ChargeTypeRequest information get successfully ByOrgId");
			responseObjectsMap.put("chargeTypeRequestVO", chargeTypeRequestVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ChargeTypeRequest information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllChargeTypeRequestById")
	public ResponseEntity<ResponseDTO> getAllChargeTypeRequestById(@RequestParam(required = false) Long id) {
		String methodName = "getAllChargeTypeRequestById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ChargeTypeRequestVO> chargeTypeRequestVO = new ArrayList<>();
		try {
			chargeTypeRequestVO = masterService.getAllChargeTypeRequestById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ChargeTypeRequest information get successfully By id");
			responseObjectsMap.put("chargeTypeRequestVO", chargeTypeRequestVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ChargeTypeRequest information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateChargeTypeRequest")
	public ResponseEntity<ResponseDTO> updateCreateChargeTypeRequest(
	        @Valid @RequestBody ChargeTypeRequestDTO chargeTypeRequestDTO) {
	    String methodName = "updateCreateChargeTypeRequest()";

	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    String errorMsg = null;
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = null;

	    try {
	        ChargeTypeRequestVO chargeTypeRequestVO = masterService.updateCreateChargeTypeRequest(chargeTypeRequestDTO);
	        boolean isUpdate = chargeTypeRequestDTO.getId() != null;
	        if (chargeTypeRequestVO != null) {
	            responseObjectsMap.put(CommonConstant.STRING_MESSAGE, isUpdate ? "ChargeTypeRequest updated successfully" : "ChargeTypeRequest created successfully");
	            responseObjectsMap.put("chargeTypeRequestVO", chargeTypeRequestVO);
	            responseDTO = createServiceResponse(responseObjectsMap);
	        } else {
	            errorMsg = isUpdate ? "ChargeTypeRequest not found for ID: " + chargeTypeRequestDTO.getId() : "ChargeTypeRequest creation failed";
	            responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "ChargeTypeRequest update failed" : "ChargeTypeRequest creation failed", errorMsg);
	        }
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        boolean isUpdate = chargeTypeRequestDTO.getId() != null;
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "ChargeTypeRequest update failed" : "ChargeTypeRequest creation failed", errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}


	@GetMapping("/getChargeTypeRequestByActive")
	public ResponseEntity<ResponseDTO> getChargeTypeRequestByActive() {
		String methodName = "getChargeTypeRequestByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ChargeTypeRequestVO> chargeTypeRequestVO = new ArrayList<>();
		try {
			chargeTypeRequestVO = masterService.getChargeTypeRequestByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ChargeTypeRequest information get successfully By Active");
			responseObjectsMap.put("chargeTypeRequestVO", chargeTypeRequestVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ChargeTypeRequest information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	@GetMapping("/getSalesAccountFromGroup")
	public ResponseEntity<ResponseDTO> getSalesAccountFromGroup(@RequestParam Long orgId) {
		String methodName = "getSalesAccountFromGroup()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> salesAccount = new ArrayList<>();
		try {
			salesAccount = masterService.getSalesAccountFromGroup(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"SalesAccount information get successfully");
			responseObjectsMap.put("salesAccount", salesAccount);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"SalesAccount information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	
	@GetMapping("/getPurchaseAccountFromGroup")
	public ResponseEntity<ResponseDTO> getPurchaseAccountFromGroup(@RequestParam Long orgId) {
		String methodName = "getPurchaseAccountFromGroup()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> purchaseAccount = new ArrayList<>();
		try {
			purchaseAccount = masterService.getPaymentAccountFromGroup(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"Payment Account information get successfully");
			responseObjectsMap.put("purchaseAccount", purchaseAccount);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"PaymentAccount information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}


	// ListOfValues

	@GetMapping("/getListOfValuesById")
	public ResponseEntity<ResponseDTO> getListOfValuesById(@RequestParam(required = false) Long id) {
		String methodName = "getListOfValuesById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ListOfValuesVO> listOfValuesVO = new ArrayList<>();
		try {
			listOfValuesVO = masterService.getListOfValuesById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "listOfValues information get successfully By Id");
			responseObjectsMap.put("listOfValuesVO", listOfValuesVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"listOfValues information receive failed By Id", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getListOfValuesByOrgId")
	public ResponseEntity<ResponseDTO> getListOfValuesByOrgId(@RequestParam(required = false) Long orgid) {
		String methodName = "getListOfValuesByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ListOfValuesVO> listOfValuesVO = new ArrayList<>();
		try {
			listOfValuesVO = masterService.getListOfValuesByOrgId(orgid);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "listOfValues information get successfully By OrgId");
			responseObjectsMap.put("listOfValuesVO", listOfValuesVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"listOfValues information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateListOfValues")
	public ResponseEntity<ResponseDTO> updateCreateListOfValues(@Valid @RequestBody ListOfValuesDTO listOfValuesDTO) {
	    String methodName = "updateCreateListOfValues()";

	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    String errorMsg = null;
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = null;

	    try {
	        ListOfValuesVO listOfValuesVO = masterService.updateCreateListOfValues(listOfValuesDTO);
	        boolean isUpdate = listOfValuesDTO.getId() != null;
	        if (listOfValuesVO != null) {
	            responseObjectsMap.put(CommonConstant.STRING_MESSAGE, isUpdate ? "ListOfValues updated successfully" : "ListOfValues created successfully");
	            responseObjectsMap.put("listOfValuesVO", listOfValuesVO);
	            responseDTO = createServiceResponse(responseObjectsMap);
	        } else {
	            errorMsg = isUpdate ? "ListOfValues not found for ID: " + listOfValuesDTO.getId() : "ListOfValues creation failed";
	            responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "ListOfValues update failed" : "ListOfValues creation failed", errorMsg);
	        }
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        boolean isUpdate = listOfValuesDTO.getId() != null;
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "ListOfValues update failed" : "ListOfValues creation failed", errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}

	// CostCenter

	@GetMapping("/getCostCenterById")
	public ResponseEntity<ResponseDTO> getCostCenterById(@RequestParam(required = false) Long id) {
		String methodName = "getCostCenterById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CostCenterVO> costCenterVO = new ArrayList<>();
		try {
			costCenterVO = masterService.getAllCostCenterById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "CostCenter information get successfully By Id");
			responseObjectsMap.put("costCenterVO", costCenterVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "CostCenter information receive failed By Id",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllCostCenterByOrgId")
	public ResponseEntity<ResponseDTO> getAllCostCenterByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllCostCenterByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CostCenterVO> costCenterVO = new ArrayList<>();
		try {
			costCenterVO = masterService.getAllCostCenterByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "CostCenter information get successfully ByOrgId");
			responseObjectsMap.put("costCenterVO", costCenterVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "CostCenter information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateCostCenter")
	public ResponseEntity<ResponseDTO> updateCreateCostCenter(@Valid @RequestBody CostCenterDTO costCenterDTO) {
	    String methodName = "updateCreateCostCenter()";

	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    String errorMsg = null;
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = null;

	    try {
	        CostCenterVO costCenterVO = masterService.updateCreateCostCenter(costCenterDTO);
	        boolean isUpdate = costCenterDTO.getId() != null;
	        
	        if (costCenterVO != null) {
	            responseObjectsMap.put(CommonConstant.STRING_MESSAGE, isUpdate ? "CostCenter updated successfully" : "CostCenter created successfully");
	            responseObjectsMap.put("costCenterVO", costCenterVO);
	            responseDTO = createServiceResponse(responseObjectsMap);
	        } else {
	            errorMsg = isUpdate ? "CostCenter not found for ID: " + costCenterDTO.getId() : "CostCenter creation failed";
	            responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "CostCenter update failed" : "CostCenter creation failed", errorMsg);
	        }
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        boolean isUpdate = costCenterDTO.getId() != null;
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "CostCenter update failed" : "CostCenter creation failed", errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	// PartyMaster

		@GetMapping("/getPartyMasterById")
		public ResponseEntity<ResponseDTO> getPartyMasterById(@RequestParam Long id) {
			String methodName = "getPartyMasterById()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<PartyMasterVO> partyMasterVO = new ArrayList<>();
			try {
				partyMasterVO = masterService.getPartyMasterById(id);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "PartyMaster information get successfully By Id");
				responseObjectsMap.put("partyMasterVO", partyMasterVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "PartyMaster information receive failed By Id",
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@GetMapping("/getPartyMasterByOrgId")
		public ResponseEntity<ResponseDTO> getPartyMasterByOrgId(@RequestParam Long orgId) {
			String methodName = "getPartyMasterByOrgId()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<PartyMasterVO> partyMasterVO = new ArrayList<>();
			try {
				partyMasterVO = masterService.getPartyMasterByOrgId(orgId);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "PartyMaster information get successfully By OrgId");
				responseObjectsMap.put("partyMasterVO", partyMasterVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap,
						"PartyMaster information receive failed By OrgId", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@PutMapping("/updateCreatePartyMaster")
		public ResponseEntity<ResponseDTO> updateCreatePartyMaster(@Valid @RequestBody PartyMasterDTO partyMasterDTO) {
			String methodName = "updateCreatePartyMaster()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			try {
				PartyMasterVO partyMasterVO = masterService.updateCreatePartyMaster(partyMasterDTO);
				if (partyMasterVO != null) {
					boolean isUpdate = partyMasterDTO.getId() != null;
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
							isUpdate ? "PartyMaster updated successfully" : "PartyMaster created Successfully");
					responseObjectsMap.put("partyMasterVO", partyMasterVO);
					responseDTO = createServiceResponse(responseObjectsMap);
				} else {
					boolean isUpdate = partyMasterDTO.getId() != null;
					errorMsg = isUpdate ? "PartyMaster not found for ID: " + partyMasterDTO.getId()
							: "PartyMaster created failed";
					responseDTO = createServiceResponseError(responseObjectsMap,
							isUpdate ? "PartyMaster update failed" : "PartyMaster Created failed", errorMsg);
				}
			} catch (Exception e) {
				errorMsg = e.getMessage();
				boolean isUpdate = partyMasterDTO.getId() != null;
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				responseDTO = createServiceResponseError(responseObjectsMap,
						isUpdate ? "PartyMaster update failed" : "PartyMaster Created failed", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
		
		@GetMapping("/getPartyMasterDocId")
		public ResponseEntity<ResponseDTO> getPartyMasterDocId(@RequestParam Long orgId, @RequestParam String finYear,
				@RequestParam String branch, @RequestParam String branchCode) {

			String methodName = "getPartyMasterDocId()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			String mapp = "";

			try {
				mapp = masterService.getPartyMasterDocId(orgId, finYear, branch, branchCode);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}

			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "PartyMaster DocId information retrieved successfully");
				responseObjectsMap.put("partyMasterDocId", mapp);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap,
						"Failed to retrieve PartyMaster Docid information", errorMsg);
			}

			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		
		@PostMapping("/excelUploadForGroupLedger")
		public ResponseEntity<ResponseDTO> excelUploadForGroupLedger(@RequestParam MultipartFile[] files,
				@RequestParam(required = false) String createdBy,@RequestParam Long orgId) {
			String methodName = "excelUploadForGroupLedger()";
			int totalRows = 0;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			int successfulUploads = 0;
			ResponseDTO responseDTO = null;
			try {
				// Call service method to process Excel upload
				masterService.excelUploadForGroupLedger(files, createdBy, orgId);
				// Retrieve the counts after processing
				totalRows = masterService.getTotalRows(); // Get total rows processed
				successfulUploads = masterService.getSuccessfulUploads(); // Get successful uploads count
				responseObjectsMap.put("statusFlag", "Ok");
				responseObjectsMap.put("status", true);
				responseObjectsMap.put("totalRows", totalRows);
				responseObjectsMap.put("successfulUploads", successfulUploads);
				responseObjectsMap.put("message", "Excel Upload For Coa successful"); // Directly include the message here
				responseDTO = createServiceResponse(responseObjectsMap);
			} catch (Exception e) {
				String errorMsg = e.getMessage();
				LOGGER.error(CommonConstant.EXCEPTION, methodName, e);
				responseObjectsMap.put("statusFlag", "Error");
				responseObjectsMap.put("status", false);
				responseObjectsMap.put("errorMessage", errorMsg);
				responseDTO = createServiceResponseError(responseObjectsMap, "Excel Upload For Coa Failed", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
		
		@PostMapping("/excelUploadForChargeCode")
		public ResponseEntity<ResponseDTO> excelUploadForChargeCode(@RequestParam MultipartFile[] files,
				@RequestParam(required = false) String createdBy,@RequestParam Long orgId) {
			String methodName = "excelUploadForChargeCode()";
			int totalRows = 0;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			int successfulUploads = 0;
			ResponseDTO responseDTO = null;
			try {
				// Call service method to process Excel upload
				masterService.excelUploadForChargeCode(files, createdBy, orgId);
				// Retrieve the counts after processing
				totalRows = masterService.getTotalRows(); // Get total rows processed
				successfulUploads = masterService.getSuccessfulUploads(); // Get successful uploads count
				responseObjectsMap.put("statusFlag", "Ok");
				responseObjectsMap.put("status", true);
				responseObjectsMap.put("totalRows", totalRows);
				responseObjectsMap.put("successfulUploads", successfulUploads);
				responseObjectsMap.put("message", "Excel Upload For Charge Code successful"); // Directly include the message here
				responseDTO = createServiceResponse(responseObjectsMap);
			} catch (Exception e) {
				String errorMsg = e.getMessage();
				LOGGER.error(CommonConstant.EXCEPTION, methodName, e);
				responseObjectsMap.put("statusFlag", "Error");
				responseObjectsMap.put("status", false);
				responseObjectsMap.put("errorMessage", errorMsg);
				responseDTO = createServiceResponseError(responseObjectsMap, "Excel Upload For Charge Code Failed", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}


		@GetMapping("/getDepartmentNameForEmployee")
		public ResponseEntity<ResponseDTO> getDepartmentNameForEmployee(@RequestParam Long orgId) {

			String methodName = "getDepartmentNameForEmployee()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<Map<String, Object>> departmentName = new ArrayList<>();

			try {
				departmentName = masterService.getDepartmentNameForEmployee(orgId);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "DepartmentName retrieved successfully");
				responseObjectsMap.put("departmentName", departmentName);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve DepartmentName information",
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
		
		@GetMapping("/getDesignationNameForEmployee")
		public ResponseEntity<ResponseDTO> getDesignationNameForEmployee(@RequestParam Long orgId) {

			String methodName = "getDesignationNameForEmployee()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<Map<String, Object>> designationName = new ArrayList<>();

			try {
				designationName = masterService.getDesignationNameForEmployee(orgId);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "DesignationName retrieved successfully");
				responseObjectsMap.put("designationName", designationName);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve DesignationName information",
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
		
		@GetMapping("/getSalesPersonForCustomer")
		public ResponseEntity<ResponseDTO> getSalesPersonForCustomer(@RequestParam Long orgId) {

			String methodName = "getSalesPersonForCustomer()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<Map<String, Object>> salesPerson = new ArrayList<>();

			try {
				salesPerson = masterService.getSalesPersonForCustomer(orgId);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "SalesPerson retrieved successfully");
				responseObjectsMap.put("salesPerson", salesPerson);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve SalesPerson information",
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
}