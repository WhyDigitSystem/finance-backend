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
import com.base.basesetup.dto.CompanyEmployeeDTO;
import com.base.basesetup.dto.EltCompanyDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.CompanyEmployeeVO;
import com.base.basesetup.entity.EltCompanyVO;
import com.base.basesetup.entity.UserVO;
import com.base.basesetup.service.CompanyService;
import com.base.basesetup.service.UserService;


@CrossOrigin
@RestController
@RequestMapping("/api/companycontroller")

 class CompanyController extends BaseController{
	
	public static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);

	@Autowired
	CompanyService companyService;
	
	@Autowired
	UserService userService;
	
	
	//ELT COMPANY
	
	@PutMapping("/updateCreateCompany")
	public ResponseEntity<ResponseDTO> updateCreateCompany(@Valid @RequestBody EltCompanyDTO eltCompanyDTO) {
		String methodName = "updateCreateCompany()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
	        Map<String, Object> eltCompanyVO = companyService.updateCreateCompany(eltCompanyDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, eltCompanyVO.get("message"));
	        responseObjectsMap.put("eltCompanyVO", eltCompanyVO.get("eltCompanyVO")); // Corrected key
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getEltCompanyById")
	public ResponseEntity<ResponseDTO> getEltCompanyById(@RequestParam(required = false) Long id) {
		String methodName = "getAllCostInvoiceById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<EltCompanyVO> eltCompanyVO = null;
		try {
			eltCompanyVO = companyService.getEltCompanyById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();   
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Elt CompanyVO information get successfully By id");
			responseObjectsMap.put("eltCompanyVO", eltCompanyVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Elt CompanyVO information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAllEltCompany")
	public ResponseEntity<ResponseDTO> getAllEltCompany() {
		String methodName = "getAllEltCompany()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<EltCompanyVO> eltCompanyVO = null;
		List<EltCompanyVO> eltCompanyVOs = new ArrayList<>();
		try {
			eltCompanyVOs = companyService.getAllEltCompany();
		} catch (Exception e) {
			errorMsg = e.getMessage();   
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Elt CompanyVO information get successfully By id");
			responseObjectsMap.put("eltCompanyVOs", eltCompanyVOs);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Elt CompanyVO information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	//Company Employee
	
	@PutMapping("/updateCreateCompanyEmployee")
	public ResponseEntity<ResponseDTO> updateCreateCompanyEmployee(@Valid @RequestBody CompanyEmployeeDTO companyEmployeeDTO) {
		String methodName = "updateCreateCompanyEmployee()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
	        Map<String, Object> companyEmployeeVO = companyService.updateCreateCompanyEmployee(companyEmployeeDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, companyEmployeeVO.get("message"));
	        responseObjectsMap.put("companyEmployeeVO", companyEmployeeVO.get("companyEmployeeVO")); // Corrected key
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getCompanyEmployeeById")
	public ResponseEntity<ResponseDTO> getCompanyEmployeeById(@RequestParam(required = false) Long id) {
		String methodName = "getCompanyEmployeeById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<CompanyEmployeeVO> companyEmployeeVO = null;
		try {
			companyEmployeeVO = companyService.getCompanyEmployeeById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();   
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Company Employee information get successfully By id");
			responseObjectsMap.put("companyEmployeeVO", companyEmployeeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Company Employee information receive failed By id", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@GetMapping("/getAllCompanyEmployeeByOrgId")
	public ResponseEntity<ResponseDTO> getAllCompanyEmployeeByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllCompanyEmployeeByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CompanyEmployeeVO> companyEmployeeVO = new ArrayList<>();
		try {
			companyEmployeeVO = companyService.getAllCompanyEmployeeByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();   
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Company Employee information get successfully By orgId");
			responseObjectsMap.put("companyEmployeeVO", companyEmployeeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Company Employee information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getBranchCodeByUser")
	public ResponseEntity<ResponseDTO> getBranchCodeByUser(@RequestParam(required =true) String userName) {
		String methodName = "getBranchCodeByUser()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<UserVO> userVO = new ArrayList<>();
		try {
			userVO = userService.getBranchCodeByUser(userName);
		} catch (Exception e) {
			errorMsg = e.getMessage();   
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "BranchCode information get successfully");
			responseObjectsMap.put("userVO", userVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"BranchCodeinformation receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
}
