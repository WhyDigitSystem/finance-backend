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
public class CostDebitNoteDTO {

	private Long id;
	private String mode;
	private String product;
	private String invoiceNo;
	private LocalDate invoiceDate;
	private String supplierBillNo;
	private String supplierType;
	private String supplierCode;
	private int creditDays;
	private LocalDate dueDate;
	private String supplierName;
	private String supplierPlace;
	private String currency;
	private BigDecimal exRate;
	private String supplierGstIn;
	private String supplierGstInCode;
	private String remarks;
	private String address;
	private String otherInfo;
	private String shipperRefNo;
	private String gstType;
	private Long orgId;
	private String createdBy;
	private String cancelRemarks;
	private String branch;
	private String branchCode;
	private String customer;
	private String client;
	private String finYear;
	private String payment;
	private String accuralid;
	private String utrRef;
	private String costType;

	private String orginBill;
	private LocalDate originBillDate;
//	private String subType;
//	private String partyType;
//	private String suppRefNo;
//	private LocalDate suppDate;
//	private String partyName;
//	private String partyCode;
//	private String shipRefNo;
//	private String status;
//	private boolean taxExampt;
//	private LocalDate currentDate;
//	private BigDecimal currentDateValue;
//	private String partyAddType;

	private List<ChargerCostDebitNoteDTO> costDebitChargesDTO;

	private List<TdsCostDebitNoteDTO> costDebitNoteTaxPrtculDTO;

}
