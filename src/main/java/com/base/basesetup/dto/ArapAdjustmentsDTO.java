package com.base.basesetup.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArapAdjustmentsDTO {

	private Long id;
	private String branch;
	private String finYear;
	private String source;
	private String refNo;
	private String accountName;
	private String currency;
	private String accCurrency;
	private BigDecimal baseAmnt;
	private BigDecimal nativeAmt;
	private String offDocId;
	private String voucherType;
	private LocalDate refDate;
	private String subLedgerCode;
	private BigDecimal exRate;
	private String creditDays;
	private LocalDate dueDate;
	private Long orgId;
	private String createdBy;
	private String branchCode;
	private String ipNo;
	private String latitude;
	private String transId;
	private BigDecimal chargeableAmt;
	private BigDecimal tdsAmt;
	private String subLedgerName;
	private boolean gstFlag;
	private BigDecimal amount;
}
