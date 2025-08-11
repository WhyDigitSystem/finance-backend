package com.base.basesetup.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.base.basesetup.dto.EmailRequestDTO;
import com.base.basesetup.service.EmailServiceAuto;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/mail")
@Slf4j
public class MailController extends BaseController {

	private final EmailServiceAuto emailServiceAuto;

//	@Autowired
	public MailController(EmailServiceAuto emailServiceAuto) {
		this.emailServiceAuto = emailServiceAuto;
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
}
