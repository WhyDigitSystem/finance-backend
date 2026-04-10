package com.base.basesetup.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutomationDTO {

	private Long id;
	private String mainGroupName;
	private String createdBy;
	private Long orgId;
	private String subHeading;
	private List<AutomationDetailsDTO>  automationDetailsDTO;

}
