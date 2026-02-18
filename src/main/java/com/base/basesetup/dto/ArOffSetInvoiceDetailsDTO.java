package com.base.basesetup.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArOffSetInvoiceDetailsDTO {

	private String invoiceNo;
	private LocalDate invoiceDate;
	private String refNo;
	private LocalDate refDate;
	private String curr;
	private BigDecimal exRate;
	private BigDecimal invAmount;
	private BigDecimal outStanding;
	private BigDecimal settled;
	private BigDecimal setExRate;
	private BigDecimal tnxSettled;
	private BigDecimal gainOrLoss;
	private String remarks;
	private BigDecimal gstAmt;
	private BigDecimal chargeAmt;

}
