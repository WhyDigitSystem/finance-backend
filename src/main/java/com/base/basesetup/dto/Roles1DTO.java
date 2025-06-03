package com.base.basesetup.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Roles1DTO {
	
	private Long id;
	private String role;
	private String createdBy;
	private Long orgId;
	private boolean active;
	private List<RolesPermissionDTO> rolesPermissionDTO;
}