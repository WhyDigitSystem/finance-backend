package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargeRCostInvoiceGnaDTO {

	private String chargeName;
	private boolean tdsApplicable;
	private String currency;
	private BigDecimal exRate;
	private BigDecimal rate;
	private float gstPer;
	private BigDecimal gtaamount;


	
	
}
