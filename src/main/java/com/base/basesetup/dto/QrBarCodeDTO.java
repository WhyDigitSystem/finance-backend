package com.base.basesetup.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrBarCodeDTO {

	private Long Id;

//	private String userName;

	private String entryNo;
	private Long count;
	private Long orgId;

	private String createdBy;
	private String finYear;
	private String branch;
	private String branchCode;
	private boolean active;
	private boolean cancel ;
	private String cancelRemarks;

	private List<QrBarCodeDetailsDTO> qrBarCodeDetailsDTO;

}
