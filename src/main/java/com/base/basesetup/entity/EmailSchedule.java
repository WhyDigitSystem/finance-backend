package com.base.basesetup.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "email_schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_schedulegen")
	@SequenceGenerator(name = "email_schedulegen", sequenceName = "email_scheduleseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "email_scheduleid")
	private Long id;

    private String employeeCode;
    private String email;
    private Long orgId;
    private LocalDateTime scheduledDateTime;
    private boolean sent = false;
    
    public EmailSchedule(String employeeCode, String email, LocalDateTime scheduledDateTime) {
        this.employeeCode = employeeCode;
        this.email = email;
        this.scheduledDateTime = scheduledDateTime;
        this.sent = false; 
    }


}
