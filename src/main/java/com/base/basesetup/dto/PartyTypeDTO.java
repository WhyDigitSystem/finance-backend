package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyTypeDTO {

	private Long id;
	private String partyType;
	private String partyTypeCode;
	private Long orgId;
	private String createdBy;
	private boolean active;
	private boolean cancel;
	private String cancelRemarks;
}
