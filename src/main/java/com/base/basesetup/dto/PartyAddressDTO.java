package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyAddressDTO {

    private String state;
    private String businessPlace;
    private String stateGstIn;
    private String city;
    private String addressType;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private Long pincode;
	private String contactPerson;
	private Long contactNo;
    private boolean sez;
}