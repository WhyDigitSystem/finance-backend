package com.base.basesetup.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class KitDTO {

	private Long id;

	private Long orgId;

	private boolean active;

	private String kitNo;

	private String kitDesc;
	private String partNo;

	private boolean cancel;

	private String finyr;

	private String createdBy;

	private String cancelRemarks;

	private int partQty;

	private boolean block;

	private boolean eflag;
	
	private List<KitAssetDTO> kitAssetDTO;

}
