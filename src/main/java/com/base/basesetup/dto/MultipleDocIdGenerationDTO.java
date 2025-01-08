package com.base.basesetup.dto;

import java.util.List;

import javax.persistence.Column;

import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.DocumentTypeMappingVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultipleDocIdGenerationDTO {

    private Long id;
	private String screenCode;
	private String screenName;
	private String docCode;
	private String branch;
	private String branchCode;
	private String finYear;
	private String finYearIdentifier;
	private Long orgId;
	private String createdBy;
	
	List<MultipleDocIdGenerationDetailsDTO> multipleDocIdGenerationDetailsDTO;
	
}
