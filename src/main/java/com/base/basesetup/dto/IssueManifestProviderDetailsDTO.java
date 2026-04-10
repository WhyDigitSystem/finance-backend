package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueManifestProviderDetailsDTO {

	private Long id;

	private String kitId;

	private String kitName;

	private Long kitQty;

	private Long hsnCode;

	private String assetCode;

	private String asset;

	private Long assetQty;

	private Long actualQty;

}
