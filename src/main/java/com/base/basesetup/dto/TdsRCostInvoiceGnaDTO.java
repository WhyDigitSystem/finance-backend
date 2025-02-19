package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TdsRCostInvoiceGnaDTO {

	private String tds;
	private BigDecimal tdsPer;
	private String section;
//	private BigDecimal totalTdsAmt;

}
