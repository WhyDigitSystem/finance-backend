package com.base.basesetup.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.EmailSchedule;


@Repository
public interface EmailScheduleRepo extends JpaRepository<EmailSchedule, Long> {
    List<EmailSchedule> findBySentFalse();

	Optional<EmailSchedule> findByEmployeeCodeAndEmail(String employeeCode, String email); 

}
