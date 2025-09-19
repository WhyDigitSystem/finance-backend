package com.base.basesetup.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.dto.ScheduleRequestDTO;
import com.base.basesetup.entity.EmailSchedule;
import com.base.basesetup.repo.EmailScheduleRepo;

@Service
public class AutoEmailSchedulerServiceImpl implements AutoEmailSchedulerService{
	
//	 @Autowired
//	    private EmployeeRepo employeeRepository;

	 @Autowired
	    private EmailScheduleRepo scheduleRepo;

	 @Autowired
	  EmailServiceAutoImpl maAutoImpl;
	    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4); 

//	    public AutoEmailSchedulerService(EmailServiceAuto mailService) {
//	        this.mailService = mailService;
//	    }
	    
        public EmailSchedule getFindBySchedule(Long id) {
	    	
	    	return scheduleRepo.getFindBySchedule(id);
	    }

    public List<EmailSchedule> getFindByScheduleOrgId(Long orgId) {
	    	
	    	return scheduleRepo.getFindByScheduleOrgId(orgId);
	    }

	    /** 🔹 Load all pending schedules when app starts */
	    @PostConstruct
	    public void init() {
	        List<EmailSchedule> pending = scheduleRepo.findBySentFalse();
	        for (EmailSchedule schedule : pending) {
	            scheduleTaskSingle(schedule);
	        }
	    }


	    
	    
//	    @Override
//	    public List<EmailSchedule> autoSendEmails(List<ScheduleRequestDTO> request) {
//
//	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
//
//	        List<EmailSchedule> savedSchedules = new ArrayList<>();
//
////	        for (int i = 0; i < employeeCodes.size(); i++) {
////	            Long id = (scheduleIds != null && scheduleIds.size() > i) ? scheduleIds.get(i) : null;
////	            String employeeCode = employeeCodes.get(i);
////	            String dateTimeStr = dateTimeList.get(i);
////	            String email = bccEmails.get(i);
////
////	            try {
////	                LocalDateTime scheduledDateTime = LocalDateTime.parse(dateTimeStr, formatter);
////
////	                if (scheduledDateTime.isBefore(LocalDateTime.now())) {
////	                    throw new IllegalArgumentException("❌ Scheduled time must be in the future!");
////	                }
////
////	                EmailSchedule schedule;
//
//	                if (request.getScheduleIds() != null) {
//	                    // 🔹 Update existing record
//	                    schedule = scheduleRepo.findById(id)
//	                            .map(existing -> {
//	                                existing.setEmployeeCode(employeeCode);
//	                                existing.setEmail(email);
//	                                existing.setScheduledDateTime(scheduledDateTime);
//	                                existing.setSent(false);
//	                                return existing;
//	                            })
//	                            .orElseGet(() -> new EmailSchedule(employeeCode, email, scheduledDateTime));
//	                } else {
//	                    // 🔹 Check by employeeCode + email
//	                    schedule = scheduleRepo.findByEmployeeCodeAndEmail(employeeCode, email)
//	                            .map(existing -> {
//	                                existing.setScheduledDateTime(scheduledDateTime);
//	                                existing.setSent(false);
//	                                return existing;
//	                            })
//	                            .orElseGet(() -> new EmailSchedule(employeeCode, email, scheduledDateTime));
//	                }
//
//	                // Save to DB
//	                EmailSchedule saved = scheduleRepo.save(schedule);
//	                savedSchedules.add(saved);
//
//	                // Register independent execution
//	                scheduleTaskSingle(saved);
//
//	            } catch (Exception e) {
//	                throw new RuntimeException("❌ Invalid date format or scheduling failed for employeeCode="
//	                        + employeeCode + ", email=" + email, e);
//	            }
//	        }
//
//	        return savedSchedules;
//	    }

	    
	    
	    @Override
	    public List<EmailSchedule> autoSendEmails(List<ScheduleRequestDTO> requests) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");

	        List<EmailSchedule> savedSchedules = new ArrayList<>();

	        for (ScheduleRequestDTO dto : requests) {
	            try {
	                // 1. Parse datetime
	                LocalDateTime scheduledDateTime = LocalDateTime.parse(dto.getDateTime(), formatter);

	                if (scheduledDateTime.isBefore(LocalDateTime.now())) {
	                    throw new IllegalArgumentException("❌ Scheduled time must be in the future!");
	                }

	                // 2. Find existing by employeeCode + email
	                EmailSchedule schedule = scheduleRepo.findByEmployeeCodeAndEmail(dto.getEmployeeCodes(), dto.getBccEmails())
	                        .map(existing -> {
	                            // update existing
	                            existing.setScheduledDateTime(scheduledDateTime);
	                            existing.setSent(false);
	                            return existing;
	                        })
	                        .orElseGet(() -> {
	                            // create new
	                            EmailSchedule newSchedule = new EmailSchedule();
	                            newSchedule.setEmployeeCode(dto.getEmployeeCodes());
	                            newSchedule.setEmail(dto.getBccEmails());
	                            newSchedule.setScheduledDateTime(scheduledDateTime);
	                            newSchedule.setSent(false);
	                            return newSchedule;
	                        });

	                EmailSchedule saved = scheduleRepo.save(schedule);
	                savedSchedules.add(saved);

	                scheduleTaskSingle(saved);

	            } catch (Exception e) {
	                throw new RuntimeException("❌ Invalid date format or scheduling failed for employeeCode=" 
	                        + dto.getEmployeeCodes() + ", email=" + dto.getBccEmails(), e);
	            }
	        }

	        return savedSchedules;
	    }

	    

	    
	    
	    private void scheduleTaskSingle(EmailSchedule schedule) {
	        long delay = Duration.between(LocalDateTime.now(), schedule.getScheduledDateTime()).toMillis();
	        if (delay < 0) return; // skip past times

	        scheduler.schedule(() -> {
	            try {
	                List<Map<String, String>> files = maAutoImpl.getAvailableFiles();

	                boolean hasFile = files.stream()
	                        .anyMatch(f -> f.get("employeeCode").equals(schedule.getEmployeeCode()));

	                if (hasFile) {
	                    try {
	                    	maAutoImpl.sendSelectedAutoEmails(
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


