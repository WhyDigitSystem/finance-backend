package com.base.basesetup.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceLevelDTO {

	private Long id;

	private String levelCode;

	private String levelName;

	private String createdBy;

	private Long orgId;

	private boolean active;
	
	private List<ServiceLevelDetailsDTO> serviceLevelDetailsDTO;

}
