package com.base.basesetup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.basesetup.dto.ScheduleRequest;
import com.base.basesetup.service.AutoEmailSchedulerService;

@RestController
@RequestMapping("/api/autoemail")
public class AutoEmailSchedulerController {

    private final AutoEmailSchedulerService schedulerService;

    public AutoEmailSchedulerController(AutoEmailSchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @PostMapping("/schedule")
    public ResponseEntity<String> scheduleEmails(@RequestBody ScheduleRequest request) {
        schedulerService.autoSendEmails(request.getScheduleIds(),
                request.getEmployeeCodes(),
                request.getDateTime(),
                request.getBccEmails()
               
        );
        return ResponseEntity.ok("✅ Emails scheduled for employees: " + request.getEmployeeCodes());
    }

}

