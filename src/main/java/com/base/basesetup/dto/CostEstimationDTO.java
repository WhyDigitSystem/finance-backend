package com.base.basesetup.dto;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostEstimationDTO {
	private Long id;

	private String employeeName;

	private String employeeCode;

	private String branch;

	private String branchCode;

	private String createdBy;

	private String finYear;

	private Long orgId;
	
	private boolean active;
	
	private String department;
	
	private String status;
	
	private LocalDate fromDate;
	
	private LocalDate toDate;

	List<CostEstimationDetailsDTO> costEstimationDetailsDTO;
}
