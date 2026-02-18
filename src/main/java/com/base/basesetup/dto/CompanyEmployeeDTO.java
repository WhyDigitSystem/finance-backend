package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEmployeeDTO {

	private Long id;

	private String employeeCode;
	//private String companyCode;
	private String employeeName;
	private String phone;
	private String email;
//	private String webSite;
	private String createdBy;
	private Long orgId;
	private boolean active;

}
