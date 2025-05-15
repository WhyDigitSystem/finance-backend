package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetItemDTO {

	private Long id;

	private String skuId;

	private String assetName;

	private int status;

}
