package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrBarCodeDetailsDTO {
	private String partNo;
	private String partDescription;
	private String batchNo;
	private String barCodeValue;
	private String qrCodeValue;

}
