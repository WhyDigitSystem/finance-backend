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
public class TaxInvoiceDTO {

	private Long id;
	private Long orgId;
	private String branch;
	private String branchCode;
	private String finYear;
	private String createdBy;
	private String bizType;
	private String bizMode;
	private String partyName;
	private Long partyId;
	private String partyCode;
	private String partyType;
	private String stateNo;
	private String stateCode;
	private String recipientGSTIN;
	private String placeOfSupply;
	private String addressType;
	private String address;
	private String pinCode;
	private String status;
	private String gstType;
	private String supplierBillNo;
	private LocalDate supplierBillDate;
	private String billCurr;
	private BigDecimal billCurrRate;
	private BigDecimal exAmount;
	private int creditDays;
	private String contactPerson;
	private String shipperInvoiceNo;
	private String billOfEntry;
	private String billMonth;
	private String invoiceNo;
	private LocalDate invoiceDate;
	private String salesType;
	
	List<TaxInvoiceDetailsDTO> taxInvoiceDetailsDTO;

	
	
	
}