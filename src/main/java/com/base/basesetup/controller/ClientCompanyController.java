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
import com.base.basesetup.dto.ClientCompanyDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.ClientCompanyVO;
import com.base.basesetup.service.ClientCompanyService;

@CrossOrigin
@RestController
@RequestMapping("/api/clientcompanycontroller")
public class ClientCompanyController  extends BaseController{

	
	public static final Logger LOGGER = LoggerFactory.getLogger(ClientCompanyController.class);

	@Autowired
	ClientCompanyService clientCompanyService;
	
	
	@PutMapping("/updateCreateClientCompany")
	public ResponseEntity<ResponseDTO> updateCreateClientCompany(@Valid @RequestBody ClientCompanyDTO clientCompanyDTO) {
		String methodName = "updateCreateClientCompany()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
	        Map<String, Object> clientCompanyVO = clientCompanyService.updateCreateClientCompany(clientCompanyDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, clientCompanyVO.get("message"));
	        responseObjectsMap.put("clientCompanyVO", clientCompanyVO.get("clientCompanyVO")); // Corrected key
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getClientCompanyById")
	public ResponseEntity<ResponseDTO> getClientCompanyById(@RequestParam(required = false) Long id) {
		String methodName = "getClientCompanyById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<ClientCompanyVO> clientCompanyVO = null;
		try {
			clientCompanyVO = clientCompanyService.getClientCompanyById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ClientCompany information get successfully By Id");
			responseObjectsMap.put("clientCompanyVO", clientCompanyVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ClientCompany information receive failed By Id", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	
	@GetMapping("/getClientCompanyByOrgId")
	public ResponseEntity<ResponseDTO> getClientCompanyByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getClientCompanyByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ClientCompanyVO> clientCompanyVO = new ArrayList<>();
		try {
			clientCompanyVO = clientCompanyService.getClientCompanyByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ClientCompany information get successfully By OrgId");
			responseObjectsMap.put("clientCompanyVO", clientCompanyVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ClientCompany information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
}
