package com.base.basesetup.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TmsJobCardDTO {
	private Long id;
	//private String jobNo;
	private String customer;
	private boolean operationClosed;
	private String salesCategory;
	private boolean financeClosed;
	private String salesPerson;
	private BigDecimal income;
	private BigDecimal profit;
	private LocalDateTime closedOn;
	private BigDecimal expense;
	private String remarks;
	private boolean closed;

	private Long orgId;
	private String branch;
	private String branchCode;
	private String createdBy;
	private boolean active;
	private String finYear;
	private String cancelRemarks;
	

	List<CostCenterTmsJobCardDTO> costCenterTmsJobCardDTO;

}
