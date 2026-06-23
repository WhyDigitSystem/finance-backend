package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxInvoiceProductLineDTO {

	private Long id;
	private String description;
	private BigDecimal quantity;
	private BigDecimal rate;
	private BigDecimal amount; 
}
