package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotationDetailsDTO {
	private Long id;
	private String description;
    private BigDecimal quantity;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal tax;
//    private BigDecimal taxAmount;
//    private BigDecimal subTotal;
//    private BigDecimal baseAmount;

}
