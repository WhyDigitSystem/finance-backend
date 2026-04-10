package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoaDTO {

	private Long id;

	private String type;

	private String groupName;

	private String accountGroupName;

	private String natureOfAccount;

	private String accountCode;
	
	private Long orgId;

	private String createdBy;

	private boolean interBranchAc;

	private boolean controllAc;

	private String currency;
	
	private boolean active;

	

}
