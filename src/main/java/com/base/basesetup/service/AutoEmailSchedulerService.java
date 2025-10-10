package com.base.basesetup.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.ScheduleRequestDTO;
import com.base.basesetup.entity.EmailSchedule;

@Service
public interface AutoEmailSchedulerService {

	List<EmailSchedule> autoSendEmails(List<ScheduleRequestDTO> request);

	EmailSchedule getFindBySchedule(Long id);

	List<EmailSchedule> getFindByScheduleOrgId(Long orgId);

//	Map<String, Object> autoSendEmails(ScheduleRequestDTO request);

}
