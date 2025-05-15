package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDTO {
	private Long id;
	private Long orgId;
	private String locationName;
	private String locationUnit;
	private String name;
	private String code;
	private String address;
	private String state;
	private Long pincode;
	private String city;
	private String country;
	private BigDecimal gst;
	private boolean active;
	private boolean cancel;
	private String createdBy;
	private String stockBranch;
}
