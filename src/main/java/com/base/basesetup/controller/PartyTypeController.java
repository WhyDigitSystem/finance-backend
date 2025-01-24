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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.common.CommonConstant;
import com.base.basesetup.common.UserConstants;
import com.base.basesetup.dto.CustomersDTO;
import com.base.basesetup.dto.PartyTypeDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.PartyTypeVO;
import com.base.basesetup.entity.VendorDTO;
import com.base.basesetup.service.PartyTypeService;

@CrossOrigin
@RestController
@RequestMapping("/api/master")
public class PartyTypeController extends BaseController {

	@Autowired
	PartyTypeService partyTypeService;

	public static final Logger LOGGER = LoggerFactory.getLogger(MasterController.class);

	// PARTYTYPE

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
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
						isUpdate ? "PartyType updated successfully" : "PartyType created successfully");
				responseObjectsMap.put("partyTypeVO", partyTypeVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = isUpdate ? "PartyType not found for ID: " + partyTypeDTO.getId()
						: "PartyType creation failed";
				responseDTO = createServiceResponseError(responseObjectsMap,
						isUpdate ? "PartyType update failed" : "PartyType creation failed", errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			boolean isUpdate = partyTypeDTO.getId() != null;
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap,
					isUpdate ? "PartyType update failed" : "PartyType creation failed", errorMsg);
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
	public ResponseEntity<ResponseDTO> getPartyCodeByOrgIdAndPartyType(@RequestParam(required = false) Long orgid,
			@RequestParam(required = false) String partytype) {

		String methodName = "getPartyCodeByOrgIdAndPartyType()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mov = new ArrayList<>();
		try {
			mov = partyTypeService.getPartyCodeByOrgIdAndPartyType(orgid, partytype);
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

	@PostMapping("/customerUpload")
	public ResponseEntity<ResponseDTO> customerUpload(@RequestParam("files") MultipartFile files,
			@RequestParam("orgId") Long orgId, @RequestParam("createdBy") String createdBy) {
		String methodName = "customerUpload()";
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			// Call service method to process Excel upload
			partyTypeService.uploadCustomerData(files, orgId, createdBy);
			// Retrieve the counts after processing
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Customers Upload successfully");
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			String errorMsg = null;
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	//CUSTOMERS
	
	@PutMapping("/createUpdateCustomer")
	public ResponseEntity<ResponseDTO> createUpdateCustomer(@Valid @RequestBody CustomersDTO customersDTO) {
		String methodName = "createUpdateCustomer()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			Map<String, Object> customerVO = partyTypeService.createUpdateCustomer(customersDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, customerVO.get("message"));
			responseObjectsMap.put("partyMasterVO", customerVO.get("partyMasterVO")); // Corrected key
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getCustomersById")
	public ResponseEntity<ResponseDTO> getCustomersById(@RequestParam Long id) {
		String methodName = "getCustomersById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<PartyMasterVO> masterVOs = null;
		try {
			masterVOs = partyTypeService.getCustomersById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Customers information get successfully By Id");
			responseObjectsMap.put("customersVO", masterVOs);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Customers information receive failed By Id",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@GetMapping("/getAllCustomers")
	public ResponseEntity<ResponseDTO> getAllCustomers(@RequestParam Long orgId) {
		String methodName = "getAllCustomers()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<PartyMasterVO> masterVOs = new ArrayList<PartyMasterVO>();
		try {
			masterVOs = partyTypeService.getAllCustomers(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Customers information get successfully By orgId");
			responseObjectsMap.put("customersVO", masterVOs);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Customers information receive failed By orgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/createUpdateVendor")
	public ResponseEntity<ResponseDTO> createUpdateVendor(@Valid @RequestBody VendorDTO vendorDTO) {
		String methodName = "createUpdateVendor()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			Map<String, Object> vendorVO = partyTypeService.createUpdateVendor(vendorDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, vendorVO.get("message"));
			responseObjectsMap.put("partyMasterVO", vendorVO.get("partyMasterVO")); // Corrected key
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getVendorsById")
	public ResponseEntity<ResponseDTO> getVendorsById(@RequestParam Long id) {
		String methodName = "getVendorsById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<PartyMasterVO> masterVOs = null;
		try {
			masterVOs = partyTypeService.getVendorsById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Vendors information get successfully By Id");
			responseObjectsMap.put("customersVO", masterVOs);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Vendors information receive failed By Id",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAllVendors")
	public ResponseEntity<ResponseDTO> getAllVendors(@RequestParam Long orgId) {
		String methodName = "getAllVendors()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<PartyMasterVO> masterVOs = new ArrayList<PartyMasterVO>();
		try {
			masterVOs = partyTypeService.getAllVendors(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Vendors information get successfully By orgId");
			responseObjectsMap.put("customersVO", masterVOs);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Vendors information receive failed By orgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PostMapping("/vendorUpload")
	public ResponseEntity<ResponseDTO> vendorUpload(@RequestParam("files") MultipartFile files,
			@RequestParam("orgId") Long orgId, @RequestParam("createdBy") String createdBy) {
		String methodName = "customerUpload()";
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			// Call service method to process Excel upload
			partyTypeService.vendorUpload(files, orgId, createdBy);
			// Retrieve the counts after processing
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Vendors Upload successfully");
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			String errorMsg = null;
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
}
