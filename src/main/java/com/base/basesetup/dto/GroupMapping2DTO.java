package com.base.basesetup.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMapping2DTO {
	
	private Long id;

	private String segment;
	
	private String header;
	
	private String createdBy;  
	
	private boolean active;
	
	private Long orgId; 	
	
	//private String updatedBy;
	
	private List<GroupMapping2SubGroupDTO> groupMapping2SubGroupDTO;
	
}
