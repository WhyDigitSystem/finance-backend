package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupLedgersDTO {
	
	
	private String parentCode;
	private String groupName;
	private String accountCode;
	private String accountName;
	private boolean active;
	private String displaySeq;

}
