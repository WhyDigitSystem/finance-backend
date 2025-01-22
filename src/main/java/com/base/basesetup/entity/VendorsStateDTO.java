package com.base.basesetup.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorsStateDTO {

	private Long id;
	private String state;
	private String stateCode;
	private Long stateNo;
	private String gstIn;
	private String contactPerson;
	private String phoneNo;
	private String eMail;
	
}
