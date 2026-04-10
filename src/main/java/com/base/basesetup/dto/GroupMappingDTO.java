package com.base.basesetup.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMappingDTO {
	
	private Long id;
	private String groupName;
	private String createdBy;
	private Long orgId;
	private List<SubGroupDetailsDTO>  subGroupDetailsVO;
	private List<GroupLedgersDTO>  groupLedgersDTO;

}
