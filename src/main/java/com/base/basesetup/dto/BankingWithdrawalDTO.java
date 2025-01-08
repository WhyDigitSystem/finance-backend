package com.base.basesetup.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankingWithdrawalDTO {

	private Long id;
	private String withdrawalMode;
	private String payTo;
	private String chequeNo;
	private LocalDate chequeDate;
	private String chequeBank;
	private String bankAccount;
	private String currency;
	private BigDecimal exchangeRate;
	private BigDecimal withdrawalAmount;
	private String remarks;

	private Long orgId;
	private String createdBy;
	private String branch;
	private String branchCode;
	private String finYear;
	private String status;
	List<WithdrawalParticularsDTO> withdrawalParticularsDTO;

}
