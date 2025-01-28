package com.base.basesetup.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DebitNoteDTO {
	private Long id;
	private String docNo;
	private String subType;
	private String product;
	private LocalDateTime docDate;
	private String partyType;
	private String partyCode;
	private String partyName;
	private String address;
	private String otherInfo;
	private String status;
	private String originBill;
	private String vchNo;
	private LocalDateTime vchDate;
	private String creditDays;
	private String supplierRefNo;
	private String date;
	private String dueDate;
	private String exRate;
	private boolean taxExempt;
	private String currency;
	private String remarks;
	private String shipperRefNo;
	private String gstType;
	private String mode;
	private Long orgId;
	private boolean active;
	private String createdBy;
	private String billCurrTotChargeAmount;
	private String billCurrTotGrossAmount;
	private String billCurrNetAmount;
	private String amountInWords;
	private String roundOff;
	private String lctotChargeAmount;
	private String lctotGrossAmount;
	private String lcNetAmount;


	List<ChargerDebitNoteDTO> chargerDebitNoteDTO;

	List<ParticularsDebitNoteDTO> particularsDebitNoteDTO;

	List<GstDebitNoteDTO> gstDebitNoteDTO;

}
