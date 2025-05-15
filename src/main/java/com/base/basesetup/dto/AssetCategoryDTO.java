package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetCategoryDTO {

	private Long id;

	private String categoryCode;

	private Long orgId;

	private String category;

	private String assetType;

	private boolean active;

	private float length;

	private float breath;

	private float height;

	private String dimUnit;

	private boolean cancel;

	private String createdBy;

	private String cancelremarks;

}
