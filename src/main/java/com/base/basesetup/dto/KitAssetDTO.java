package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KitAssetDTO {

	private long id;

	private String assetType;

	private String belongsTo;

	private String manufacturePartCode;

	private String assetCategory;

	private String categoryCode;

	private String assetCodeId;

	private String assetName;

	private int quantity;

}
