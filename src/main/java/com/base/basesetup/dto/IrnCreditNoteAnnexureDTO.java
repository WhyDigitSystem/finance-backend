package com.base.basesetup.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IrnCreditNoteAnnexureDTO {

	private Long id;
	private LocalDate transDate;
	private String transNo;
	private String kitId;
	private String dsec;
	private String skuType;
	private int qty;
	private double rate;
	private double amount;
	
}
