package com.base.basesetup.dto;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReconcileBankDTO {
	private Long id;
	private LocalDate bankStmtDate;
	private String bankAccount;
	
	private Long orgId;
	private String branch;
	private String branchCode;
	private String createdBy;
	private String finYear;
	private boolean active;
	private String remarks;
	private String ipNo;
	private String latitude;
	
	List<ParticularsReconcileDTO> particularsReconcileDTO;
	
}
