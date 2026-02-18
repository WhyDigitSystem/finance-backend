package com.base.basesetup.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.basesetup.common.CommonConstant;
import com.base.basesetup.dto.AllotmentDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.entity.AllotmentVO;
import com.base.basesetup.responseDTO.AllotmentResponseDTO;
import com.base.basesetup.service.AllotmentService;


@RestController
@RequestMapping("/api/allotment")
public class AllotmentController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(AllotmentController.class);

	@Autowired
	AllotmentService allotmentService;
	
	@PutMapping("/createUpdateAllotment")
	public ResponseEntity<ResponseDTO> createUpdateAllotment(@RequestBody AllotmentDTO allotmentDTO) {

		String methodName = "createUpdateAllotment()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseMap = new HashMap<>();

		try {
			Map<String, Object> allotment = allotmentService.createUpdateAllotment(allotmentDTO);
			responseMap.put("message", allotment.get("message"));
			responseMap.put("allotmentVO", allotment.get("allotmentVO"));

			ResponseDTO responseDTO = createServiceResponse(responseMap);
			return ResponseEntity.ok(responseDTO);
		} catch (Exception e) {

			LOGGER.error("{} - Unexpected Error: {}", methodName, e.getMessage(), e);

			ResponseDTO errorDTO = createServiceResponseError(responseMap, "Unexpected Error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
		}
	}
	
	@GetMapping("/getAllAllotmentByOrgId")
	public ResponseEntity<ResponseDTO> getAllAllotmentByOrgId(@RequestParam Long orgId) {
		String methodName = "getAllAllotmentByOrgId()";
		LOGGER.debug("Starting {}", methodName);

		Map<String, Object> responseMap = new HashMap<>();
		ResponseDTO responseDTO;
      List<AllotmentVO>  allotment = new ArrayList<>();
		try {
			allotment = allotmentService.getAllAllotmentByOrgId(orgId);
			responseMap.put("message", "Allotment Details retrieved successfully");
			responseMap.put("allotment", allotment);
			responseDTO = createServiceResponse(responseMap);
		} catch (Exception e) {
			LOGGER.error("Error in {}: {}", methodName, e.getMessage());
			responseDTO = createServiceResponseError(responseMap, "Error fetching users", e.getMessage());
		}

		LOGGER.debug("Ending {}", methodName);
		return ResponseEntity.ok(responseDTO);
	}
	
	@GetMapping("/getAllotmentById")
	public ResponseEntity<ResponseDTO> getAllotmentById(@RequestParam Long id) {
		String methodName = "getAllotmentById()";
		LOGGER.debug("Starting {}", methodName);

		Map<String, Object> responseMap = new HashMap<>();
		ResponseDTO responseDTO;

		try {
			AllotmentResponseDTO allotment = allotmentService.getAllotmentById(id);
			responseMap.put("message", "Allotment Details retrieved successfully");
			responseMap.put("allotment", allotment);
			responseDTO = createServiceResponse(responseMap);
		} catch (Exception e) {
			LOGGER.error("Error in {}: {}", methodName, e.getMessage());
			responseDTO = createServiceResponseError(responseMap, "Error fetching users", e.getMessage());
		}

		LOGGER.debug("Ending {}", methodName);
		return ResponseEntity.ok(responseDTO);
	}


}
