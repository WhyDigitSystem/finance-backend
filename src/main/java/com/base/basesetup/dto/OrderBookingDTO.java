package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookingDTO {
	
	private Long orgId;
	private String client;
	private String clientCode;
	private String year;
	private String month;
	private String type; 
	private BigDecimal amount;
	private String segment;
	private String customerName;
	private BigDecimal orderValue;
	private BigDecimal existingOrderPlan;
	private BigDecimal balanceOrderValue;
	private BigDecimal paymentReceived;
	private BigDecimal yetToReceive;
	private String classification;
	private String createdBy;
	private String yearType;

}
