package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartySpecialTDSDTO {
	
	private String section;
	private String tdsWithSec;
	private Long rateFrom;
	private Long rateTo;
	private BigDecimal tdsWithPer;
	private BigDecimal surchargePer;
	private BigDecimal edPercentage;
	private String tdsCertifiNo;

}
