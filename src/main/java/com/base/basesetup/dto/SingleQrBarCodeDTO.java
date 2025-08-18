package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleQrBarCodeDTO {

	private Long id;
	private String qrBarCodeValue;
	private Long count;
	private String createdBy;
	private Long orgId;
	private boolean active ;
	private boolean cancel ;
	private String cancelRemarks;

}
