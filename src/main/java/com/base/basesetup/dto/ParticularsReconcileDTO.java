package com.base.basesetup.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticularsReconcileDTO {
	private String voucherNo;
	private LocalDate voucherDate;
	private String chequeNo;
	private LocalDate chequeDate;
	private BigDecimal deposit;
	private BigDecimal withdrawal;
	private String bankRef;
}
