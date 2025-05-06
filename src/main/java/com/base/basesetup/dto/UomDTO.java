package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UomDTO {
	
	private Long id;
    private String uomCode;
    private String uomDesc;
    private Long orgId;
    private String createdBy;
    private boolean active;
}