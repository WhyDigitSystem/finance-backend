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
public class UrCostInvoiceGnaDTO {
	private Long id;
	private String supplierType;
	private String supplierCode;
	private String supplierBillNo;
	private LocalDate supplierBillDate;
	private String supplierName;
	private String supplierPlace;
	private int creditDays;
	private LocalDate dueDate;
	private String currency;
	private BigDecimal exRate;
	private String supplierGstIn;
	private String supplierGstInCode;
	private String remarks;
	private String address;
	private String otherInfo;
	private String shipperRefNo;
	private String gstType;
	private String vId;
	private LocalDate vDate;


	private Long orgId;
	private String createdBy;
	private String branch;
	private String branchCode;
	private String finYear;

	List<ChargesUrCostInvoiceGnaDTO> chargesUrCostInvoiceGnaDTO;

	List<TdsUrCostInvoiceGnaDTO> tdsUrCostInvoiceGnaDTO;
	

}
