package com.base.basesetup.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.EmailSchedule;


@Repository
public interface EmailScheduleRepo extends JpaRepository<EmailSchedule, Long> {
    List<EmailSchedule> findBySentFalse();

	Optional<EmailSchedule> findByEmployeeCodeAndEmail(String employeeCode, String email); 
	
	@Query(nativeQuery = true,value = "select * from email_schedule where email_scheduleid=?1 and sent=0")
	EmailSchedule getFindBySchedule(Long id);

	@Query(nativeQuery = true,value = "select * from email_schedule where org_id=?1 and sent=0")
	List<EmailSchedule> getFindByScheduleOrgId(Long orgId);
	
	@Query(nativeQuery = true, value = "select scheduled_date_time from email_schedule where employee_code = ?1 and email = ?2 and sent=0  order by scheduled_date_time desc limit 1")
		Optional<LocalDateTime> findScheduledDateTime(String employeeCode, String email);


}
