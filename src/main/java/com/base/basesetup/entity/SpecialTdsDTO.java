package com.base.basesetup.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialTdsDTO {

	private Long id;
	private String whSection;
	private Long rateFrom;
	private Long rateTo;
	private Long whPercentage;
	private Long surPercentage;
	private Long edPercentage;
	private String tdsCertificateNo;
	
}
