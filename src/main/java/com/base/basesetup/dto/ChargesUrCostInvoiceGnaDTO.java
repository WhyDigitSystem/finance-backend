package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargesUrCostInvoiceGnaDTO {
	private String chargeLedger;
	private String chargeAccount;
	private String currency;
	private BigDecimal exRate;
	private BigDecimal rate;
	private float GSTPercent;

}
