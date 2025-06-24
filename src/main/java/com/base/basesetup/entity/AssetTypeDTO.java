package com.base.basesetup.entity;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetTypeDTO {

	private Long id;

	private Long orgId;

	private String assetType;

	private String typeCode;

	private boolean cancel;

	private String createdBy;

	private String cancelremarks;

	private boolean active;

}
