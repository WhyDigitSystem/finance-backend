package com.base.basesetup.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

	private Long id;
	private Long orgId;
	private String paymentType;
	private String bankChargeAcc;
	private BigDecimal bankCharges;
	private String bankInCurrency;
	private String type;
	private String partyCode;
	private BigDecimal serviceTaxAmt;
	private String sTaxInCurrency;
	private String partyName;
	private String chequeBank;
	private String gstState;
	private String gstIn;
	private String chequeNo;
	private LocalDate chequeDate;
	private String bankCashAcc;
	private String payTo;
	private BigDecimal paymentAmt;
	private String tdsAcc;
	private BigDecimal tdsAmt;
	private BigDecimal receiptAmt;
	private String currency;
	private BigDecimal currencyAmt;

	private String branch;
	private String branchCode;
	private String createdBy;

	private boolean active;
	private boolean cancel;
	private String cancelRemarks;
	private String finYear;
	

	private List<PaymentInvDtlsDTO> paymentInvDtlsDTO;

}
