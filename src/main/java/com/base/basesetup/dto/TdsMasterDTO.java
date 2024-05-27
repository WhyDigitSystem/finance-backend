package com.base.basesetup.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TdsMasterDTO {
	private Long Id;
	private Long orgId;
	private String section;
	private String sectionName;
	private boolean active;
	
	private List<TdsMaster2DTO>  tdsMaster2DTO;

}
