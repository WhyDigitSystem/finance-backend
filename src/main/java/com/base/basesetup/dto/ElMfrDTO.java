package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElMfrDTO {

	private Long id;
	private Long orgId;
	private String description;
	private String elCode;
	private String createdBy;
	
}
