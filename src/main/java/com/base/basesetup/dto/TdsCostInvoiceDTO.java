package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TdsCostInvoiceDTO {
	private Long id;
	private String tdsWithHolding;
	private BigDecimal tdsWithHoldingPer;
	private String section;

}
