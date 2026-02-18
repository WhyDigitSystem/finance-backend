package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO {

	private Long id;
	private String organizationName;
	private String createdBy;
	private String updatedBy;
	private String organizationMail;
	private String phoneNo;
	private String state;
	private String city;
	private String country;
	private String pinCode;
	private String address;
}
