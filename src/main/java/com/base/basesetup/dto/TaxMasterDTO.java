package com.base.basesetup.dto;

import java.util.List;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxMasterDTO {
	
	private Long id;
	private Long orgId;
	private String finYear;
	private String serviceAccountCode;
	private String warehouse;
	private String gst;
	private Long gstSlab;
	private String createdBy;
	private String cancelRemarks;
	private boolean cancel;
	private boolean active;
	private String jobOrderNo;

	private List<TaxMasterDetailsDTO> taxMasterDetailsDTO;
}
