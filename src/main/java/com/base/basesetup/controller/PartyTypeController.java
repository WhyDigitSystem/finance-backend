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
import com.base.basesetup.dto.PartyTypeDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.PartyTypeVO;
import com.base.basesetup.service.PartyTypeService;

@CrossOrigin
@RestController
@RequestMapping("/api/master")
public class PartyTypeController extends BaseController {
	
	@Autowired
	PartyTypeService partyTypeService;

	public static final Logger LOGGER = LoggerFactory.getLogger(MasterController.class);
	
	//PARTYTYPE
	
	
			@PutMapping("/createUpdatePartyType")
			public ResponseEntity<ResponseDTO> createUpdatePartyType(@Valid @RequestBody PartyTypeDTO partyTypeDTO) {
			    String methodName = "createUpdatePartyType()";

			    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			    String errorMsg = null;
			    Map<String, Object> responseObjectsMap = new HashMap<>();
			    ResponseDTO responseDTO = null;

			    try {
			        PartyTypeVO partyTypeVO = partyTypeService.createUpdatePartyType(partyTypeDTO);
			        boolean isUpdate = partyTypeDTO.getId() != null;
			        
			        if (partyTypeVO != null) {
			            responseObjectsMap.put(CommonConstant.STRING_MESSAGE, isUpdate ? "PartyType updated successfully" : "PartyType created successfully");
			            responseObjectsMap.put("partyTypeVO", partyTypeVO);
			            responseDTO = createServiceResponse(responseObjectsMap);
			        } else {
			            errorMsg = isUpdate ? "PartyType not found for ID: " + partyTypeDTO.getId() : "PartyType creation failed";
			            responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "PartyType update failed" : "PartyType creation failed", errorMsg);
			        }
			    } catch (Exception e) {
			        errorMsg = e.getMessage();
			        boolean isUpdate = partyTypeDTO.getId() != null;
			        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			        responseDTO = createServiceResponseError(responseObjectsMap, isUpdate ? "PartyType update failed" : "PartyType creation failed", errorMsg);
			    }
			    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			    return ResponseEntity.ok().body(responseDTO);
			}

			@GetMapping("/getPartyTypeById")
			public ResponseEntity<ResponseDTO> getPartyTypeById(@RequestParam Long id) {
				String methodName = "getPartyTypeById()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				List<PartyTypeVO> partyTypeVO = new ArrayList<>();
				try {
					partyTypeVO = partyTypeService.getPartyTypeById(id);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				}
				if (StringUtils.isBlank(errorMsg)) {
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "PartyType information get successfully By Id");
					responseObjectsMap.put("partyTypeVO", partyTypeVO);
					responseDTO = createServiceResponse(responseObjectsMap);
				} else {
					responseDTO = createServiceResponseError(responseObjectsMap, "PartyType information receive failed By Id",
							errorMsg);
				}
				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);
			}

			@GetMapping("/getAllPartyTypeByOrgId")
			public ResponseEntity<ResponseDTO> getAllPartyTypeByOrgId(@RequestParam Long orgid) {
				String methodName = "getAllPartyTypeByOrgId()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				List<PartyTypeVO> partyTypeVO = new ArrayList<>();
				try {
					partyTypeVO = partyTypeService.getAllPartyTypeByOrgId(orgid);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				}
				if (StringUtils.isBlank(errorMsg)) {
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "PartyType information get successfully By OrgId");
					responseObjectsMap.put("partyTypeVO", partyTypeVO);
					responseDTO = createServiceResponse(responseObjectsMap);
				} else {
					responseDTO = createServiceResponseError(responseObjectsMap,
							"PartyType information receive failed By OrgId", errorMsg);
				}
				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);
			}
			
//			@GetMapping("/getPartyCodeByOrgIdAndPartyType")
//			public ResponseEntity<ResponseDTO> getPartyCodeByOrgIdAndPartyType(@RequestParam(required = false) Long orgid,@RequestParam(required = false) String partytype) {
//				String methodName = "getPartyCodeByOrgIdAndPartyType()";
//				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
//				String errorMsg = null;
//				Map<String, Object> responseObjectsMap = new HashMap<>();
//				ResponseDTO responseDTO = null;
//				List<PartyTypeVO> partyTypeVO = new ArrayList<>();
//				try {
//					partyTypeVO = partyTypeService.getPartyCodeByOrgIdAndPartyType(orgid,partytype);
//				} catch (Exception e) {
//					errorMsg = e.getMessage();
//					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
//				}
//				if (StringUtils.isBlank(errorMsg)) {
//					responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "PartyType information get successfully By OrgId and PartyType");
//					responseObjectsMap.put("partyTypeVO", partyTypeVO);
//					responseDTO = createServiceResponse(responseObjectsMap);
//				} else {
//					responseDTO = createServiceResponseError(responseObjectsMap,
//							"PartyType information receive failed By OrgId and PartyType", errorMsg);
//				}
//				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
//				return ResponseEntity.ok().body(responseDTO);
//			}
//			
			
			@GetMapping("/getPartyCodeByOrgIdAndPartyType")
			public ResponseEntity<ResponseDTO> getPartyCodeByOrgIdAndPartyType(
					@RequestParam(required = false) Long orgid,@RequestParam(required = false) String partytype) {

				String methodName = "getPartyCodeByOrgIdAndPartyType()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				List<Map<String, Object>> mov = new ArrayList<>();
				try {
					mov = partyTypeService.getPartyCodeByOrgIdAndPartyType(orgid,partytype);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				}

				if (StringUtils.isBlank(errorMsg)) {
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
							"PartyType information get successfully By OrgId and PartyType");
					responseObjectsMap.put("partyTypeVO", mov);
					responseDTO = createServiceResponse(responseObjectsMap);
				} else {
					responseDTO = createServiceResponseError(responseObjectsMap,
							"PartyType information receive failed By OrgId and PartyType", errorMsg);
				}

				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);
			}
			
			
			

}
