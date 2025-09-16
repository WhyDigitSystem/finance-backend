package com.base.basesetup.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.entity.EmailSchedule;
import com.base.basesetup.repo.EmailScheduleRepo;

@Service
public class AutoEmailSchedulerService {
	
//	 @Autowired
//	    private EmployeeRepo employeeRepository;

	 @Autowired
	    private EmailScheduleRepo scheduleRepo;

	    private final EmailServiceAuto mailService;
	    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4); 

	    public AutoEmailSchedulerService(EmailServiceAuto mailService) {
	        this.mailService = mailService;
	    }

	    /** 🔹 Load all pending schedules when app starts */
	    @PostConstruct
	    public void init() {
	        List<EmailSchedule> pending = scheduleRepo.findBySentFalse();
	        for (EmailSchedule schedule : pending) {
	            scheduleTaskSingle(schedule);
	        }
	    }

	    /** 🔹 Save new schedules or update existing ones */
	    public void autoSendEmails(List<Long> scheduleIds,
	                               List<String> employeeCodes,
	                               List<String> dateTimeList,
	                               List<String> bccEmails) {

	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");

	        for (int i = 0; i < employeeCodes.size(); i++) {
	            Long id = (scheduleIds != null && scheduleIds.size() > i) ? scheduleIds.get(i) : null;
	            String employeeCode = employeeCodes.get(i);
	            String dateTimeStr = dateTimeList.get(i);
	            String email = bccEmails.get(i);

	            try {
	                LocalDateTime scheduledDateTime = LocalDateTime.parse(dateTimeStr, formatter);

	                if (scheduledDateTime.isBefore(LocalDateTime.now())) {
	                    throw new IllegalArgumentException("❌ Scheduled time must be in the future!");
	                }

	                EmailSchedule schedule;

	                if (id != null) {
	                    // 🔹 Update existing record if ID exists
	                    schedule = scheduleRepo.findById(id)
	                            .map(existing -> {
	                                existing.setEmployeeCode(employeeCode);
	                                existing.setEmail(email);
	                                existing.setScheduledDateTime(scheduledDateTime);
	                                existing.setSent(false);
	                                return existing;
	                            })
	                            .orElseGet(() -> new EmailSchedule(employeeCode, email, scheduledDateTime));
	                } else {
	                    // 🔹 Otherwise check by employeeCode + email
	                    schedule = scheduleRepo.findByEmployeeCodeAndEmail(employeeCode, email)
	                            .map(existing -> {
	                                existing.setScheduledDateTime(scheduledDateTime);
	                                existing.setSent(false);
	                                return existing;
	                            })
	                            .orElseGet(() -> new EmailSchedule(employeeCode, email, scheduledDateTime));
	                }

	                // Save to DB
	                scheduleRepo.save(schedule);

	                // Register independent execution
	                scheduleTaskSingle(schedule);

	            } catch (Exception e) {
	                throw new RuntimeException("❌ Invalid date format or scheduling failed for employeeCode="
	                        + employeeCode + ", email=" + email, e);
	            }
	        }
	    }

	    private void scheduleTaskSingle(EmailSchedule schedule) {
	        long delay = Duration.between(LocalDateTime.now(), schedule.getScheduledDateTime()).toMillis();
	        if (delay < 0) return; // skip past times

	        scheduler.schedule(() -> {
	            try {
	                List<Map<String, String>> files = mailService.getAvailableFiles();

	                boolean hasFile = files.stream()
	                        .anyMatch(f -> f.get("employeeCode").equals(schedule.getEmployeeCode()));

	                if (hasFile) {
	                    try {
	                        mailService.sendSelectedAutoEmails(
	                                Collections.singletonList(schedule.getEmployeeCode()),
	                                Collections.singletonList(schedule.getEmail())
	                        );

	                        schedule.setSent(true);
	                        scheduleRepo.save(schedule);

	                        System.out.println("✅ Email sent to " + schedule.getEmail()
	                                + " for employee " + schedule.getEmployeeCode());
	                    } catch (Exception ex) {
	                        schedule.setSent(false);
	                        scheduleRepo.save(schedule);
	                        System.err.println("❌ Failed to send email to "
	                                + schedule.getEmail() + ": " + ex.getMessage());
	                    }
	                } else {
	                    System.err.println("⚠ No file found for employee " + schedule.getEmployeeCode());
	                }

	            } catch (Exception e) {
	                System.err.println("❌ Error while sending scheduled email for employee "
	                        + schedule.getEmployeeCode() + ": " + e.getMessage());
	            }
	        }, delay, TimeUnit.MILLISECONDS);
	    }
	}
