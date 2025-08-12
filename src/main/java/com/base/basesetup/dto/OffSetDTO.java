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
public class OffSetDTO {
	
	private Long id;
	private String branch;
	private String branchCode;
	private String finYear;
	private String rpDocId;
	private LocalDate rpDocDate;
	private String subLedgerType;
	private String subLedgername;
	private String subLedgerCode;
	private String accountName;
	
	private String currency;
	private BigDecimal exRate;
	
	
	private Long orgId;
	private String createdBy;
	List<OffSetDetailsDTO> offSetDetailsDTO;


}
