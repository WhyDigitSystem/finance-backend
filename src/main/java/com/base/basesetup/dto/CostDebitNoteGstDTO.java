package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CostDebitNoteGstDTO {

	private Long id;
	private String chargeAcc;
	private String subLodgerCode;
	private BigDecimal dBillAmt;
	private BigDecimal crBillAmt;
	private BigDecimal dBLCAmt;
	private BigDecimal crLCAmt;
	private String remarks;
}
