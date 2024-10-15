package com.base.basesetup.dto;

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
	private float tdsWithHoldingPer;
	private String section;
	private int totTdsWhAmnt;

}
