package com.base.basesetup.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.repo.EmployeeRepo;

@Service
public class AutoEmailSchedulerService {
	
	 @Autowired
	    private EmployeeRepo employeeRepository;

	    private final EmailServiceAuto mailService;
	    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	    public AutoEmailSchedulerService(EmailServiceAuto mailService) {
	        this.mailService = mailService;
	    }

	    public void autoSendEmails(List<String> employeeCodes, List<String> dateTimeList, List<String> bccEmails) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");

	        if (employeeCodes.size() != dateTimeList.size() || employeeCodes.size() != bccEmails.size()) {
	            throw new IllegalArgumentException("❌ Mismatch: employeeCodes, dateTimeList, and bccEmails must have same size!");
	        }

	        for (int i = 0; i < employeeCodes.size(); i++) {
	            String employeeCode = employeeCodes.get(i);
	            String dateTimeStr = dateTimeList.get(i);
	            String email = bccEmails.get(i);

	            try {
	                LocalDateTime scheduledDateTime;
	                try {
	                    scheduledDateTime = LocalDateTime.parse(dateTimeStr); // ISO
	                } catch (Exception e) {
	                    scheduledDateTime = LocalDateTime.parse(dateTimeStr, formatter); // fallback custom
	                }

	                long delay = Duration.between(LocalDateTime.now(), scheduledDateTime).toMillis();
	                if (delay < 0) {
	                    throw new IllegalArgumentException("Scheduled time must be in the future!");
	                }

	                // ✅ Check employee exists
	                boolean exists = employeeRepository.findByEmailAndEmployeeCode(email, employeeCode).isPresent();
	                if (!exists) {
	                    System.err.println("❌ Employee not found with code " + employeeCode + " and email " + email);
	                    continue;
	                }

	                scheduler.schedule(() -> {
	                    try {
	                        List<Map<String, String>> files = mailService.getAvailableFiles();
	                        Optional<Map<String, String>> fileOpt = files.stream()
	                                .filter(f -> f.get("employeeCode").equals(employeeCode))
	                                .findFirst();

	                        if (fileOpt.isPresent()) {
	                            mailService.sendSelectedAutoEmails(Collections.singletonList(employeeCode));
	                            System.out.println("✅ Email sent for employee: " + employeeCode +
	                                    " to " + email +
	                                    " at " + LocalDateTime.now());
	                        } else {
	                            System.err.println("❌ No files found for employee: " + employeeCode);
	                        }
	                    } catch (Exception e) {
	                        System.err.println("❌ Error while sending scheduled email for "
	                                + employeeCode + ": " + e.getMessage());
	                    }
	                }, delay, TimeUnit.MILLISECONDS);

	                System.out.println("📌 Email scheduled for employee " + employeeCode +
	                        " (email: " + email + ") at " + scheduledDateTime);

	            } catch (Exception e) {
	                throw new RuntimeException("Invalid date format! Use ISO `yyyy-MM-dd'T'HH:mm:ss` or `dd-MM-yyyy hh:mm:ss a`", e);
	            }
	        }
	    }
	}
