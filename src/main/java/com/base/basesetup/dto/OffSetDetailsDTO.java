package com.base.basesetup.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OffSetDetailsDTO {

	private String invoiceNo;
	private LocalDate invoiceDate;
	private String refNo;
	private String invCurrency;
	private BigDecimal invExRate;
	private BigDecimal arApAmount;
	private BigDecimal arApOutStanding;
	private BigDecimal arApSettled;
	private BigDecimal settledExRate;
	private BigDecimal gainOrLoss;
	private BigDecimal remarks;
}
