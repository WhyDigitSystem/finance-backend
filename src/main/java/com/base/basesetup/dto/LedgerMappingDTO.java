package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class LedgerMappingDTO {

	private Long id;

	private String clientCoa;
	private String clientCoaCode;
	private String coaCode;
	private String clientName;
	private String coa;
	private Long orgId;
	private String createdBy;
	private boolean active;
	private String clientCode;
	
}
