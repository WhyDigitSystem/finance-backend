package com.base.basesetup.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeegen")
	@SequenceGenerator(name = "employeegen", sequenceName = "employeeseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "employeeid")
	private Long id;

	@Column(name = "employeecode",length = 30)
	private String employeeCode;
	@Column(name = "employee",length = 150)
	private String employeeName;
	@Column(name = "gender",length = 30)
	private String gender;
	@Column(name = "branch",length = 50)
	private String branch;
	@Column(name = "email",length = 150)
	private String email;
	@Column(name = "branchcode",length = 30)
	private String branchCode;
	@Column(name = "department",length = 30)
	private String department;
	@Column(name = "designation",length = 30)
	private String designation;
	@Column(name = "salesflag")
	private boolean salesFlag;
	@Column(name = "dateofbirth")
	private LocalDate dateOfBirth;
	@Column(name = "joiningdate")
	private LocalDate joiningDate;
	@Column(name = "createdby",length = 30)
	private String createdBy;
	@Column(name = "modifiedby",length = 30)
	private String updatedBy;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "cancelremarks",length = 150)
	private String cancelRemark;
	@Column(name = "active")
	private boolean active;

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	// Optionally, if you want to control serialization for 'cancel' field similarly
	@JsonGetter("cancel")
	public String getCancel() {
		return cancel ? "T" : "F";
	}

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
