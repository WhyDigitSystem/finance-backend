package com.base.basesetup.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanOutstandingDTO {
	
	private Long orgId;
	private String client;
	private String clientCode;
	private String year;
	private String month;
	private String accountName; 
	private String accountCode;
	private BigDecimal amount;
	private String banckName;
	private BigDecimal sanctionAmount;
	private BigDecimal outstanding;
	private double interestRate;
	private int tenure;
	private BigDecimal interest;
	private BigDecimal principal;
	private BigDecimal emiTotal;
	private LocalDate tenureDate;
	private int balanceMonth;
	private String createdBy;
	private String yearType;

}
