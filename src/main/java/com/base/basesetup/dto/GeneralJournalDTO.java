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
public class GeneralJournalDTO {
	private Long id;
	private String voucherSubType;
	private String currency;
	private BigDecimal exRate;
	private String refNo;
	private LocalDate refDate;
	private String status;
	
	private String remarks;
	private Long orgId;
	private String createdBy;
	private String branch;
	private String branchCode;
	private String finYear;

	List<ParticularsJournalDTO> particularsJournalDTO;

}
