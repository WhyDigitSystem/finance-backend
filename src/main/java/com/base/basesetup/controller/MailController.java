package com.base.basesetup.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.base.basesetup.common.CommonConstant;
import com.base.basesetup.common.UserConstants;
import com.base.basesetup.dto.EmailRequestDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.service.AutoEmailSchedulerService;
import com.base.basesetup.service.EmailServiceAuto;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/mail")
@Slf4j
public class MailController extends BaseController {

	private final EmailServiceAuto emailServiceAuto;
	
	private final AutoEmailSchedulerService autoEmailSchedulerService;

//	@Autowired
	public MailController(EmailServiceAuto emailServiceAuto, AutoEmailSchedulerService autoEmailSchedulerService) {
		this.emailServiceAuto = emailServiceAuto;
		this.autoEmailSchedulerService=autoEmailSchedulerService;
	}

	@GetMapping
	public ResponseEntity<?> getAvailableFiles() {
		try {
			log.info("Attempting to get available files from: {}", emailServiceAuto.getWatchDirectory());
			List<Map<String, String>> files = emailServiceAuto.getAvailableFiles();
			return ResponseEntity.ok(files);
		} catch (Exception e) {
			log.error("ERROR in getAvailableFiles", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Failed to get files: " + e.getMessage(), e);
		}
	}

//    @GetMapping("/content/{filename}")
//    public ResponseEntity<String> getFileContent(@PathVariable String filename) {
//        try {
//            String content = emailServiceAuto.getFileContent(filename);
//            return ResponseEntity.ok(content);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
//                    "Error reading file: " + e.getMessage(), e);
//        }
//    }

//	@GetMapping("/content/{filename}")
//	public ResponseEntity<byte[]> getFileContent(@PathVariable String filename) {
//		try {
//			byte[] content = emailServiceAuto.getFileContent(filename);
//
//			Path filePath = Paths.get(emailServiceAuto.getWatchDirectory()).resolve(filename).normalize();
//			String contentType = Files.probeContentType(filePath);
//			if (contentType == null) {
//				contentType = "application/octet-stream";
//			}
//
//			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(content);
//
//		} catch (IOException e) {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error reading file: " + e.getMessage(), e);
//		}
//	}

	
	@GetMapping("/content/{filename}")
	public ResponseEntity<byte[]> getFileContent(@PathVariable String filename) {
	    try {
	        byte[] content = emailServiceAuto.getFileContent(filename);

	        Path filePath = Paths.get(emailServiceAuto.getWatchDirectory()).resolve(filename).normalize();
	        String contentType = Files.probeContentType(filePath);
	        if (contentType == null) {
	            contentType = "application/octet-stream";
	        }

	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(contentType))
	                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
	                .body(content);

	    } catch (IOException e) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error reading file: " + e.getMessage(), e);
	    }
	}

	
	
	@GetMapping("/download/{filename}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
		try {
			// Resolve file path
			Path file = Paths.get(emailServiceAuto.getWatchDirectory()).resolve(filename).normalize();

			// Create Resource object
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() && resource.isReadable()) {
				String contentType = Files.probeContentType(file);
				if (contentType == null) {
					contentType = "application/octet-stream"; // fallback type
				}

				// Return the file with correct headers
				return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
						.body(resource);
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found or unreadable");
			}

		} catch (MalformedURLException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file path", e);
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error determining file type", e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error downloading file", e);
		}
	}

	@PostMapping("/send-emails")
	public ResponseEntity<String> sendSelectedEmails(@RequestBody EmailRequestDTO emailRequest) {

		try {
			if (emailRequest.getEmployeeCodes() == null || emailRequest.getEmployeeCodes().isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee codes list cannot be empty");
			}

			emailServiceAuto.sendSelectedEmails(emailRequest.getEmployeeCodes(), emailRequest.getBccAddress());
			return ResponseEntity.ok("Emails sent successfully");
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error sending emails: " + e.getMessage(), e);
		}
	}
	
	@GetMapping("/getEmployeeEmail")
	public ResponseEntity<ResponseDTO> getEmployeeEmail(@RequestParam Long orgId,  @RequestParam String employeeCodeOrEmail) {
		String methodName = "getEmployeeEmail()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();
		try {
			mapp = emailServiceAuto.getEmployeeEmail(orgId,employeeCodeOrEmail);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Email Details retrieved successfully");
			responseObjectsMap.put("mapp", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Email to retrieve tax invoice Details", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	
	
////	//
//	@PostMapping("/schedule")
//	public ResponseEntity<String> scheduleEmail(
//	        @RequestParam String employeeCode,
//	        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss a") LocalDateTime scheduledTime,
//	        @RequestParam(required = false) String bcc) {
//
//		autoEmailSchedulerService.autoSendEmails(employeeCode, scheduledTime, bcc);
//	    return ResponseEntity.ok("✅ Email scheduled for " + employeeCode + " at " + scheduledTime);
//	}

}
