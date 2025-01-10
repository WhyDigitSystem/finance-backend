package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomersStateDTO {

	private Long id;
	private String state;
	private String stateCode;
	private Long stateNo;
	private String gstIn;
	private String contactPerson;
	private String phoneNo;
	private String eMail;
	private String customerCode;
	
}
