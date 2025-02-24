package com.base.basesetup.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RCostInvoiceGnaDTO {
	
	private Long id;
	private String partyType;
	private String partyCode;
	private String supplierBillNo;
	private LocalDate supplierBillDate;
	private String partyName;
	private int creditDays;
	private LocalDate dueDate;
	private String supplierGstIn;
	private String supplierGstInCode;
	private String currency;
	private BigDecimal exRate;
	private String place;
	private String address;
	private String remarks;
	private String gstType;
	
	//Default fields
	private Long orgId;
	private boolean active;
	private String createdBy;
	private String branch;
	private String branchCode;
	private String finYear;
	
	
	private List<ChargeRCostInvoiceGnaDTO> chargeRCostInvoiceGnaDTO;
	
	private List<TdsRCostInvoiceGnaDTO> tdsRCostInvoiceGnaDTO;


}
