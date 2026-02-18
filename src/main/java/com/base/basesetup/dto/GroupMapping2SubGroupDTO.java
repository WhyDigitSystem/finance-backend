package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMapping2SubGroupDTO {
	
	private Long id;

	private String displaySeq;
	
	private String subGroup;
	
	private String subGroupCode;
	
}
