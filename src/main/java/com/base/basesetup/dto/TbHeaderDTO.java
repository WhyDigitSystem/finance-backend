package com.base.basesetup.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TbHeaderDTO {

	private Long id;
	private String clientCode;
	private String client;
	private Long orgId;
	private String tbMonth;
	private String finYear;
	private String createdBy;
	
	private List<TbDetailsDTO>tbDetailsDTO;
	
}
