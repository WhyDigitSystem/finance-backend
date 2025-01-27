package com.base.basesetup.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostInvoiceDTO {

	private Long id;
	private String mode;
	private String product;
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
	private String jobOrderNo;

	List<ChargerCostInvoiceDTO> chargerCostInvoiceDTO;

	List<TdsCostInvoiceDTO> tdsCostInvoiceDTO;

}
