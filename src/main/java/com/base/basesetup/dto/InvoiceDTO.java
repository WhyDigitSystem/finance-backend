package com.base.basesetup.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {

	private Long id;
	private String finYear;
	private Long orgId;
	private String createdBy;
	private String poDate;
	private String poNumber;
	private String companyAddress;
	private String vendorAddress;
	private String deliveryAddress;
	private String termsAndConditions;
	private Long subtotal;
	private Integer sgst;
	private Integer cgst;
	private Long total;
	private String gstType;
	private Integer igst;
	private String modifiedBy;


	
	private List<InvoiceProductLinesDTO> items;

}
