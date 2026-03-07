package com.base.basesetup.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllotmentDTO {

	    private Long id;
	    private String supplier;
	    private String customer;
	   

	    private String mode;
	    private LocalDate startDate;
	    private LocalDate endDate;
	    private boolean active = true;
	    private String createdBy;
	    private String updatedBy;
	    private Long orgId;
	    private boolean cancel;
	    private String branchCode;
	    private String branchName;
	    
	    List<AllotmentDetailsDTO> allotmentDetailsDTO;

}