package com.base.basesetup.dto;

import java.math.BigDecimal;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargerCostDebitNoteDTO {

	private String jobNo;
	private String chargeName;
	private String chargeCode;
	private String chargeLedger;
	private String sac;
	private String currency;
	private BigDecimal exRate;
	private String gst;
	private BigDecimal fcAmt;
	private BigDecimal lcAmt;
	private BigDecimal billAmt;
	private BigDecimal rate;
	private int qty;
	private Float GSTPercent;
	private BigDecimal gstAmount;
	private String ledger;
	private String govChargeCode;
	private String taxable;
	private String description;
//	private String exempted;

}
