package com.base.basesetup.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolesDTO {
	
	private Long id;
	private String role;
	private String createdBy;
	private Long orgId;
	private boolean active;
	private List<RolesResponsibilityDTO> rolesResponsibilityDTO;
	private List<RolesPermissionDTO> rolesPermissionDTO;
}
