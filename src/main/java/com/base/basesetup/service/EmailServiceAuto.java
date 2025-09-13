package com.base.basesetup.service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.base.basesetup.repo.EmployeeRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceAuto {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private EmployeeRepo employeeRepository;

	private String watchDirectory;

	@Value("${email.bcc.address:}")
	private String bccAddress;


	@Value("${pdf.directory.path:C:/Users/Lenovo/Desktop/Email}")
	// 244 Server
//	@Value("${pdf.directory.path:C:/Users/Administrator/Desktop/Email}")
	public void setWatchDirectory(String path) {
		this.watchDirectory = path;
	}

	public String getWatchDirectory() {
		return this.watchDirectory;
	}

	public List<Map<String, String>> getAvailableFiles() {
	    List<Map<String, String>> fileList = new ArrayList<>();
	    Path directory = Paths.get(watchDirectory);

	    try {
	        if (!Files.exists(directory)) {
	            throw new RuntimeException("Directory not found: " + directory);
	        }

	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");

	        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.txt")) {
	            for (Path textFile : stream) {
	                String employeeCode = getBaseName(textFile.getFileName().toString());
	                Path pdfFile = directory.resolve(employeeCode + ".pdf");

	                if (Files.exists(pdfFile)) {
	                    Optional<String> emailOpt = employeeRepository.findEmailByCode(employeeCode);

	                    Map<String, String> fileInfo = new HashMap<>();
	                    fileInfo.put("employeeCode", employeeCode);

	                    String currentDateTime = LocalDateTime.now().format(formatter);
	                    fileInfo.put("dateTime", currentDateTime);

	                    fileInfo.put("textFileName", textFile.getFileName().toString());
	                    fileInfo.put("pdfFileName", pdfFile.getFileName().toString());
	                    emailOpt.ifPresent(email -> fileInfo.put("email", email));

	                    fileList.add(fileInfo);
	                }
	            }
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Error reading directory: " + e.getMessage(), e);
	    }

	    return fileList;
	}


//    public String getFileContent(String filename) throws IOException {
//        Path file = Paths.get(watchDirectory).resolve(filename);
//        return new String(Files.readAllBytes(file));
//    }

	public byte[] getFileContent(String filename) throws IOException {
		if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
			throw new IllegalArgumentException("Invalid filename");
		}

		Path file = Paths.get(watchDirectory).resolve(filename).normalize();
		return Files.readAllBytes(file);
	}

	//EmailAuto
	
	public void sendSelectedAutoEmails(List<String> employeeCodes) {
	    Path directory = Paths.get(watchDirectory);
	    Path backupDir = directory.resolve("backup");

	    try {
	        if (!Files.exists(backupDir)) {
	            Files.createDirectories(backupDir);
	        }

	        List<Path> processedFiles = new ArrayList<>();

	        for (String employeeCode : employeeCodes) {
	            Path textFile = directory.resolve(employeeCode + ".txt");
	            Path pdfFile = directory.resolve(employeeCode + ".pdf");

	            if (Files.exists(textFile) && Files.exists(pdfFile)) {
	                Optional<String> emailOpt = employeeRepository.findEmailByCode(employeeCode);

	                if (emailOpt.isPresent()) {
	                    String toEmail = emailOpt.get();

	                    if (isValidEmail(toEmail)) {
	                        if (sendEmailWithAttachmentsAuto(
	                                toEmail,
	                                "Documents for " + employeeCode,
	                                textFile,
	                                pdfFile)) {

	                            processedFiles.add(textFile);
	                            processedFiles.add(pdfFile);
	                        }
	                    } else {
	                        log.warn("Invalid email format for employee {}: {}", employeeCode, toEmail);
	                    }
	                }
	            }
	        }

	        moveFilesToBackupAuto(processedFiles, backupDir);
	    } catch (Exception e) {
	        throw new RuntimeException("Error sending selected emails", e);
	    }
	}

	private boolean sendEmailWithAttachmentsAuto(
	        String toEmail,
	        String subject,
	        Path textFile,
	        Path pdfFile) {

	    log.info("Preparing email to: {}", toEmail);

	    try {
	        String textContent = new String(Files.readAllBytes(textFile));
	        byte[] pdfContent = Files.readAllBytes(pdfFile);

	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);

	        helper.setFrom("mn0443585@gmail.com");   
	        helper.setTo(toEmail);                 
	        helper.setSubject(subject);

	        helper.setText(buildEmailBodyAuto(textContent, toEmail.split("@")[0]), true);
	        helper.addAttachment(pdfFile.getFileName().toString(), new ByteArrayResource(pdfContent));

	        mailSender.send(message);
	        log.info("✅ Successfully sent email to: {}", toEmail);
	        return true;
	    } catch (Exception e) {
	        log.error("❌ Failed to send email to {}. Exception: ", toEmail, e);
	        return false;
	    }
	}



	private void moveFilesToBackupAuto(List<Path> files, Path backupDir) throws IOException {
		if (files.isEmpty()) {
			log.info("No files to move to backup");
			return;
		}

		Map<String, List<Path>> filesByEmployee = files.stream()
				.collect(Collectors.groupingBy(file -> getBaseName(file.getFileName().toString())));

		for (Map.Entry<String, List<Path>> entry : filesByEmployee.entrySet()) {
			String employeeCode = entry.getKey();
			String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
			String backupFolderName = employeeCode + "_" + timestamp;

			Path employeeBackupDir = backupDir.resolve(backupFolderName);
			Files.createDirectories(employeeBackupDir);

			for (Path file : entry.getValue()) {
				try {
					Path target = employeeBackupDir.resolve(file.getFileName());
					Files.move(file, target, StandardCopyOption.REPLACE_EXISTING);
					log.info("Moved {} to backup: {}", file.getFileName(), target);
				} catch (IOException e) {
					log.error("Failed to move {} to backup: {}", file, e.getMessage());
				}
			}
		}
	}

	private String buildEmailBodyAuto(String content, String name) {
		return "<!DOCTYPE html>" + "<html><head><style>" + "body { font-family: Arial, sans-serif; }"
				+ ".content { background: #f5f5f5; padding: 15px; border-radius: 5px; }" + "</style></head>" + "<body>"
				+ "<h2>Hello, " + name + "!</h2>" + "<div class='content'>" + content + "</div>"
				+ "<p>Please find your document attached.</p>" + "</body></html>";
	}

	private String getBaseName(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf('.'));
	}

	private boolean isValidEmail(String email) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		return email != null && email.matches(emailRegex);
	}

	
	//SendEmail
	
	
	
	public void sendSelectedEmails(List<String> employeeCodes, String bccAddress) {
		Path directory = Paths.get(watchDirectory);
		Path backupDir = directory.resolve("backup");

		try {
			if (!Files.exists(backupDir)) {
				Files.createDirectories(backupDir);
			}

			List<Path> processedFiles = new ArrayList<>();

			for (String employeeCode : employeeCodes) {
				Path textFile = directory.resolve(employeeCode + ".txt");
				Path pdfFile = directory.resolve(employeeCode + ".pdf");

				if (Files.exists(textFile) && Files.exists(pdfFile)) {
					Optional<String> emailOpt = employeeRepository.findEmailByCode(employeeCode);

					if (emailOpt.isPresent()) {
						if (sendEmailWithAttachments(emailOpt.get(), "Documents for " + employeeCode, textFile, pdfFile,
								bccAddress)) {
							processedFiles.add(textFile);
							processedFiles.add(pdfFile);
						}
					}
				}
			}

			moveFilesToBackup(processedFiles, backupDir);
		} catch (Exception e) {
			throw new RuntimeException("Error sending selected emails", e);
		}
	}

	private boolean sendEmailWithAttachments(String toEmail, String subject, Path textFile, Path pdfFile,
			String bccAddress) throws MessagingException, IOException {
		log.info("Preparing email to: {}", toEmail);

		try {
			String textContent = new String(Files.readAllBytes(textFile));
			byte[] pdfContent = Files.readAllBytes(pdfFile);

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom("mn0443585@gmail.com");
			helper.setTo(toEmail);
			if (StringUtils.hasText(bccAddress)) {
				helper.setBcc(bccAddress);
			}
			helper.setSubject(subject);
			helper.setText(buildEmailBody(textContent, toEmail.split("@")[0]), true);
			helper.addAttachment(pdfFile.getFileName().toString(), new ByteArrayResource(pdfContent));

			mailSender.send(message);
			log.info("Successfully sent email to: {}", toEmail);
			return true;
		} catch (Exception e) {
			log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
			return false;
		}
	}

	private void moveFilesToBackup(List<Path> files, Path backupDir) throws IOException {
		if (files.isEmpty()) {
			log.info("No files to move to backup");
			return;
		}

		Map<String, List<Path>> filesByEmployee = files.stream()
				.collect(Collectors.groupingBy(file -> getBaseName(file.getFileName().toString())));

		for (Map.Entry<String, List<Path>> entry : filesByEmployee.entrySet()) {
			String employeeCode = entry.getKey();
			String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
			String backupFolderName = employeeCode + "_" + timestamp;

			Path employeeBackupDir = backupDir.resolve(backupFolderName);
			Files.createDirectories(employeeBackupDir);

			for (Path file : entry.getValue()) {
				try {
					Path target = employeeBackupDir.resolve(file.getFileName());
					Files.move(file, target, StandardCopyOption.REPLACE_EXISTING);
					log.info("Moved {} to backup: {}", file.getFileName(), target);
				} catch (IOException e) {
					log.error("Failed to move {} to backup: {}", file, e.getMessage());
				}
			}
		}
	}

	private String buildEmailBody(String content, String name) {
		return "<!DOCTYPE html>" + "<html><head><style>" + "body { font-family: Arial, sans-serif; }"
				+ ".content { background: #f5f5f5; padding: 15px; border-radius: 5px; }" + "</style></head>" + "<body>"
				+ "<h2>Hello, " + name + "!</h2>" + "<div class='content'>" + content + "</div>"
				+ "<p>Please find your document attached.</p>" + "</body></html>";
	}

//	private String getBaseName(String fileName) {
//		return fileName.substring(0, fileName.lastIndexOf('.'));
//	}

	public List<Map<String, Object>> getEmployeeEmail(Long orgId, String employeeCodeOrEmail) {
		Set<Object[]> chType = employeeRepository.getEmployeeEmail(orgId, employeeCodeOrEmail);
		return getEmployeeEmail(chType);
	}

	private List<Map<String, Object>> getEmployeeEmail(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
			map.put("email", ch[0] != null ? ch[0].toString() : "");
			List1.add(map);
		}
		return List1;
	}


}
