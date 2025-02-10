package com.base.basesetup.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialTdsDTO {

	private Long id;
	private String section;
	private String whSection;
	private Long rateFrom;
	private Long rateTo;
	private BigDecimal whPercentage;
	private BigDecimal surPercentage;
	private BigDecimal edPercentage;
	private String tdsCertificateNo;
	
}
