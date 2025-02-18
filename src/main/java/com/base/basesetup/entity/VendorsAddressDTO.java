package com.base.basesetup.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorsAddressDTO {

	private Long id;
	private String state;
	private String city;
	private String gstnIn;
	private String bussinesPlace;
	private String addressType;
	private String addressLane1;
	private String addressLane2;
	private String addressLane3;
	private Long pinCode;
	private String contact;
	private String vendorName;
//	private boolean sez;
//	private String contactPerson;
//	private Long contactNo;

}
