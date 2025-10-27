package com.base.basesetup.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SegmentMappingDTO {
	
	private Long id;
	private String clientCode;
	private String clientName;
	private Long orgId;
	private String createdBy;
	private boolean active;
	private String segmentType;	
	private List<SegmentMappingDetailsDTO> segmentMappingDetailsDTO;
}