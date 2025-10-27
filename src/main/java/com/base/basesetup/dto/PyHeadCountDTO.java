package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PyHeadCountDTO {
	
	  private String year;
	    private Long orgId;
	    private String client;
	    private String clientCode;
	    private String department;
	    private String employmentType;
	    private String category;
	    private String month;
	    private Integer headcount;
	    private BigDecimal ctc;
	    private String createdBy;
	    private String yearType;

}
