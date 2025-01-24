package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyStateDTO {
	private String state;
	private String gstIn;
	private Long stateNo;
	private String contactPerson;
	private String contactPhoneNo; 
	private String email;
	private String stateCode;
	

}