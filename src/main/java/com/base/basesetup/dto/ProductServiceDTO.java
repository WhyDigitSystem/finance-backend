package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductServiceDTO {

	private Long id;
	private String type;
	private String code;
	private String name;
	private String description;
	private String dimension;
	private boolean active;
	private Long orgId;
	private String createdBy;

}
