package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesPurchaseItemDTO {
	
	private Long orgId;
	private String client;
	private String clientCode;
	private String year;
	private String month;
	private String description; 
	private BigDecimal value;
	private BigDecimal qty;
	private String createdBy;
	private String type;
	private String yearType; 	

}
