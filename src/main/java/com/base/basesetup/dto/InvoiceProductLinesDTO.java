package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProductLinesDTO {
	
	private String description;
    private BigDecimal quantity;
    private BigDecimal rate;
    private BigDecimal tax;

	

}