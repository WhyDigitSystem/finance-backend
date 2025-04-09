package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargerDebitNoteDTO {
	private Long id;
	private String gChargeCode;
	private String gsac;
	private String chargeName;
	private String applyOn;
	private String tax;
	private String currency;
	private BigDecimal exRate;
	private BigDecimal rate;
	private String exempted;
	private String description;
	private BigDecimal fcAmount;
	private BigDecimal lcAmount;
	private String taxablePercentage;
	private BigDecimal tlcAmount;
	private BigDecimal billAmount;
	private BigDecimal gstPercentage;
	private String gst;
}
