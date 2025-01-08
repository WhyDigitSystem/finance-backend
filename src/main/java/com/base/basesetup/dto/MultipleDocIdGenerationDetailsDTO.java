package com.base.basesetup.dto;

import com.base.basesetup.entity.MultipleDocIdGenerationDetailsVO;
import com.base.basesetup.entity.MultipleDocIdGenerationVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MultipleDocIdGenerationDetailsDTO {
	
	private String screenCode;
	private String screenName;
	private String docCode;
	private String branch;
	private String branchCode;
	private String prefixField;
	private String finYear;
	private String finYearIdentifier;

}
