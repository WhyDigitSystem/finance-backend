package com.base.basesetup.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

	private Long id;

	private String employeeCode;
	private String employeeName;
	private String gender;
	private String branch;
	private String branchCode;
	private String department;
	private String email;
	private String designation;
	private LocalDate dateOfBirth;
	private LocalDate joiningdate;
	private String createdBy;
	private Long orgId;
	private boolean cancel;
	private String cancelRemark;
	private boolean active;
	private boolean salesFlag;

}
