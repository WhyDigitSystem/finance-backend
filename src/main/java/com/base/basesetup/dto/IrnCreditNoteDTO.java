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
public class IrnCreditNoteDTO {

	private Long id;
	private Long orgId;
	private String branch;
	private String branchCode;
	private String finYear;
	private String createdBy;
	private String bizType;
	private String bizMode;
	private String partyName;
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
	private String originBillNo;
	private LocalDate originBillDate;
	private String supplierRefNo;
	private LocalDate supplierRefDate;
	private String billCurr;
	private BigDecimal billCurrRate;
	private BigDecimal exAmount;
	private int creditDays;
	private String shipperRefNo;
	private String billMonth;
	private String salesType;
	private String creditRemarks;

	List<IrnCreditNoteDetailsDTO> irnCreditNoteDetailsDTO;

}
