package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorDTO {

	private Long id;
	private long orgId;
	private String vendorName;
	private String gstIn;
	private String panNo;
	private boolean active;
	private String createdBy;
	private Long creditDays;
	private BigDecimal creditLimit;
	private String creditTerms;
	private String taxRegistered;
	
	private List<VendorsStateDTO> vendorStateDTO;
	private List<VendorsAddressDTO> vendorAddressDTO;
	private List<SpecialTdsDTO> specialTdsDTO;
}
