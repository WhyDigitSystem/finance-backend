package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbDetailsDTO {
	
	private String coa;
	private String coaCode;
	private String clientAccountName;
	private String clientAccountCode;
	private BigDecimal openingBalanceDb;
	private BigDecimal openingBalanceCr;
	private BigDecimal transCredit;
	private BigDecimal transDebit;
	private BigDecimal closingBalanceDb;
	private BigDecimal closingBalanceCr;
	private String remarks;

}
