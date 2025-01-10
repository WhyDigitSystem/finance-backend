package com.base.basesetup.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorsSalesPersonTaggingDTO {

	private Long id;
	private String salesPerson;
	private String empCode;
	private String salesBranch;
	private LocalDate effectiveFrom;
	private LocalDate effectiveTill;
	private String vendorCode;
	
}
