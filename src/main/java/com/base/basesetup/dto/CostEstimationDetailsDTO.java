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
public class CostEstimationDetailsDTO {
	
	private String category;

	private String particulars;

	private String remarks;

	private BigDecimal amount;


}
