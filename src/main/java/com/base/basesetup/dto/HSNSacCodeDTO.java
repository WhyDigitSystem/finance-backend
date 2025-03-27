package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HSNSacCodeDTO {
	private Long id;
	private Long orgId;
	private String type;
	private String code;
	private String description;
	private String taxType;
	private BigDecimal igst;
	private BigDecimal cgst;
	private BigDecimal sgst;
	private String createdBy;
	private boolean active;

}
