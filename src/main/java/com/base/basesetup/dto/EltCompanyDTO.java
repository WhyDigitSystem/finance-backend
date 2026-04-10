package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EltCompanyDTO {

private Long id;
	
	private String companyCode;
	private String companyName;
	private String phone;
	private String email;
	private String webSite;
	private String createdBy;
	private boolean active;
	
}
