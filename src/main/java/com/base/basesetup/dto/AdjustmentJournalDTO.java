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
public class AdjustmentJournalDTO {

	private Long id;
	private String adjustmentType;
	private String currency;
	private BigDecimal exRate;
	private String refNo;
	private LocalDate refDate;
	private String suppRefNo;
	private LocalDate suppRefDate;
	private String remarks;
	private Long orgId;
	private String createdBy;
	private String branch;
	private String branchCode; 
	private String finYear;
	private String status;

	
	List<AccountParticularsDTO> accountParticularsDTO;

		
}
