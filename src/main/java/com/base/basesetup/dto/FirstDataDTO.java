package com.base.basesetup.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirstDataDTO {

	private Long id;
	
	private LocalDate docDate;

	private Long accountNumber;

	private String accountName;

	private String description;

	private String Category;

	private BigDecimal debit;

	private BigDecimal credit;

	private String currency;

	private BigDecimal balance;

	private String transactionType;

	private String createdBy;

}
