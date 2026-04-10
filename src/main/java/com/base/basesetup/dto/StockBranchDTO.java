package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockBranchDTO {
	
	private Long id;
	private String branch;
	private String branchCode;
	private Long orgId;
	private boolean active;
	private String createdBy;

}
